package cnesten.forms;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import cnesten.dao.ActionDAO;
import cnesten.entities.Action;

public class UploadtoDBForm {
    private static final String CHAMP_NOM_ACTION = "nom_action";
    private static final String CHAMP_PROCEDURE  = "proc�dure";
    private static final String CHAMP_CHECKLIST  = "checklist";
    private static final String CHAMP_ANNEE      = "ann�e";
    private static final String CHAMP_LCE        = "lce";

    private String              resultat;
    private Map<String, String> erreurs          = new HashMap<String, String>();

    private ActionDAO           actionDao;

    public UploadtoDBForm( ActionDAO actionDao ) {
        this.actionDao = actionDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Action upload( HttpServletRequest request ) {
        String nomaction = getValeurChamp( request, CHAMP_NOM_ACTION );
        String annee = getValeurChamp( request, CHAMP_ANNEE );
        String lce = getValeurChamp( request, CHAMP_LCE );

        Action action = new Action();

        String nomproc = null, nomchlist = null;
        InputStream contenuproc = null, contenuchlist = null;
        int sizeproc = 0, sizechlist = 0;

        try {
            Part partproc = request.getPart( CHAMP_PROCEDURE );
            sizeproc = (int) partproc.getSize();
            nomproc = getNomFichier( partproc );

            if ( nomproc != null && !nomproc.isEmpty() ) {
                nomproc = nomproc.substring(
                        nomproc.lastIndexOf( '/' ) + 1 )
                        .substring( nomproc.lastIndexOf( '\\' ) +
                                1 );
                /* R�cup�ration du contenu du fichier */
                contenuproc = partproc.getInputStream();
            }

        } catch ( IllegalStateException e ) {
            e.printStackTrace();
            setErreur( CHAMP_PROCEDURE, "Les donn�es envoy�es sont trop volumineuses." );
        } catch ( IOException e ) {
            e.printStackTrace();
            setErreur( CHAMP_PROCEDURE, "Erreur de configuration du serveur." );
        } catch ( ServletException e ) {
            e.printStackTrace();
            setErreur( CHAMP_PROCEDURE,
                    "Ce type de requ�te n'est pas support�, merci d'utiliser le formulaire pr�vu pour envoyer votre fichier." );
        }

        try {
            Part partchlist = request.getPart( CHAMP_CHECKLIST );
            sizechlist = (int) partchlist.getSize();
            nomchlist = getNomFichier( partchlist );

            if ( nomchlist != null && !nomchlist.isEmpty() ) {
                nomchlist = nomchlist.substring(
                        nomchlist.lastIndexOf( '/' ) + 1 )
                        .substring( nomchlist.lastIndexOf( '\\' ) +
                                1 );
                /* R�cup�ration du contenu du fichier */
                contenuchlist = partchlist.getInputStream();
            }
        } catch ( IllegalStateException e ) {
            e.printStackTrace();
            setErreur( CHAMP_CHECKLIST, "Les donn�es envoy�es sont trop volumineuses." );
        } catch ( IOException e ) {
            e.printStackTrace();
            setErreur( CHAMP_CHECKLIST, "Erreur de configuration du serveur." );
        } catch ( ServletException e ) {
            e.printStackTrace();
            setErreur( CHAMP_CHECKLIST,
                    "Ce type de requ�te n'est pas support�, merci d'utiliser le formulaire pr�vu pour envoyer votre fichier." );
        }

        /* Si aucune erreur n'est survenue jusqu'� pr�sent */
        if ( erreurs.isEmpty() ) {
            traiterNomac( nomaction, action );
            traiterProc�dure( nomproc, contenuproc, sizeproc, action );
            traiterChecklist( nomchlist, contenuchlist, sizechlist, action );
            traiterAnnee( annee, action );
            traiterLce( lce, action );
        }

        /* Si aucune erreur n'est survenue jusqu'� pr�sent */
        if ( erreurs.isEmpty() ) {
            /* Ajout du fichier dans la base */
            try {
                actionDao.creer( action );
            } catch ( Exception e ) {
                setErreur( CHAMP_PROCEDURE, "Erreur lors de l'�criture du fichier sur le disque." );
            }
        }

        /* Initialisation du r�sultat global de la validation. */
        if ( erreurs.isEmpty() ) {
            resultat = "Succ�s de l'envoi du document.";
        } else {
            resultat = "�chec de l'envoi du document.";
        }
        return action;
    }

    /*
     * Appel � la validation du nom re�u et initialisation de la propri�t�
     * action du bean
     */
    private void traiterNomac( String nomaction, Action action ) {
        try {
            validationNomac( nomaction );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOM_ACTION, e.getMessage() );
        }
        action.setNom_action( nomaction );
    }

    /*
     * Appel � la validation du fichier re�u et initialisation de la propri�t�
     * proc�dure du bean
     */
    private void traiterProc�dure( String nomproc, InputStream contenuproc, int sizeproc, Action action ) {
        try {
            validationProc�dure( nomproc, contenuproc );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PROCEDURE, e.getMessage() );
        }
        action.setNom_proc( nomproc );
        try {
            action.setProc�dure( getArrayByteFromStream( contenuproc, sizeproc ) );
        } catch ( IOException e ) {
            setErreur( CHAMP_PROCEDURE, e.getMessage() );
        }
    }

    /*
     * Appel � la validation du fichier re�u et initialisation de la propri�t�
     * checklist du bean
     */
    private void traiterChecklist( String nomchlist, InputStream contenuchlist, int sizechlist, Action action ) {
        action.setNom_chlist( nomchlist );
        try {
            action.setChecklist( getArrayByteFromStream( contenuchlist, sizechlist ) );
        } catch ( IOException e ) {
            setErreur( CHAMP_CHECKLIST, e.getMessage() );
        }
    }

    /*
     * Appel � la validation du nombre re�u et initialisation de la propri�t�
     * ann�e du bean
     */
    private void traiterAnnee( String annee, Action action ) {
        int valeurAnnee = 0;
        try {
            valeurAnnee = validationAnnee( annee );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_ANNEE, e.getMessage() );
        }
        action.setAnn�e( valeurAnnee );
    }

    /*
     * Appel � la validation du texte re�u et initialisation de la propri�t� lce
     * du bean
     */
    private void traiterLce( String lce, Action action ) {
        try {
            validationLce( lce );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_LCE, e.getMessage() );
        }
        action.setLce( lce );
    }

    public byte[] getArrayByteFromStream( InputStream stream, int size ) throws IOException {
        /*
         * On cr�e un tableau de byte avec une longueur �gal � la taille du
         * fichier (logique non :mrgreen: ?)
         */
        byte[] data = new byte[size];
        /*
         * On lit la totalit� du fichier. Le premier param�tre est le tableau de
         * byte qui va recevoir les donn�es, le deuxi�me est la position dans le
         * fichier o� on commence � lire les donn�es (donc z�ro puisqu'on
         * commence au d�but du fichier), enfin le dernier param�tre est le
         * nombre d'octet que l'on veut lire, ici on donne length puisqu'on veut
         * lire la totalit� du fichier d'un coup.
         */
        try {
            int result = stream.read( data, 0, size );
        } catch ( Exception e ) {
        }

        return data;
    }

    /* La m�thode de validation */

    private void validationNomac( String nomaction ) throws FormValidationException {
        if ( nomaction != null ) {
            if ( nomaction.length() < 3 )
                throw new FormValidationException( "Le nom de l'action doit contenir au moins 3 caract�res." );
            if ( actionDao.trouver_par_nom( nomaction ) != null )
                throw new FormValidationException( "Cette action existe d�j�." );
        } else
            throw new FormValidationException( "Merci de saisir le nom de l'action." );
    }

    private void validationProc�dure( String nomproc, InputStream contenuproc ) throws FormValidationException {
        if ( nomproc == null || contenuproc == null ) {
            throw new FormValidationException( "Merci de s�lectionner un fichier � envoyer." );
        }
    }

    private int validationAnnee( String annee ) throws FormValidationException {
        int temp;
        if ( annee != null ) {
            try {
                temp = Integer.parseInt( annee );
                if ( temp < 0 )
                    throw new FormValidationException( "L'ann�e doit �tre un nombre positif." );
            } catch ( NumberFormatException e ) {
                throw new FormValidationException( "L'ann�e doit �tre un nombre." );
            }

        } else {
            throw new FormValidationException( "Merci de saisir une ann�e." );
        }

        return temp;
    }

    private void validationLce( String lce ) throws FormValidationException {
        if ( lce != null && lce.length() < 10 ) {
            throw new FormValidationException( "Le LCE doit contenir au moins 10 caract�res." );
        }
    }

    /*
     * Ajoute un message correspondant au champ sp�cifi� � la map des erreurs.
     */

    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    /*
     * M�thode utilitaire qui retourne null si un champ est vide, et son contenu
     * sinon.
     */

    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }

    private static String getNomFichier( Part part ) {
        /*
         * Boucle sur chacun des param�tres de l'en-t�te "contentdisposition".
         */
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            /*
             * Recherche de l'�ventuelle pr�sence du param�tre "filename".
             */
            if ( contentDisposition.trim().startsWith( "filename" ) ) {
                /*
                 * Si "filename" est pr�sent, alors renvoi de sa valeur,
                 * c'est-�-dire du nom de fichier sans guillemets.
                 */
                return contentDisposition.substring(
                        contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        } /* Et pour terminer, si rien n'a �t� trouv�... */
        return null;
    }

}
