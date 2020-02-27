package cnesten.forms;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import cnesten.dao.ActionDAO;
import cnesten.dao.FicheDAO;
import cnesten.entities.Action;
import cnesten.entities.Fiche;

public class ModificationFicheForm {
    private static final String CHAMP_ID         = "id_fiche";
    private static final String CHAMP_NOM_ACTION = "nom_action";
    private static final String CHAMP_FILE       = "file_fiche";

    private String              resultat;
    private Map<String, String> erreurs          = new HashMap<String, String>();

    private FicheDAO            ficheDao;
    private ActionDAO           actionDao;

    public ModificationFicheForm( FicheDAO ficheDao, ActionDAO actionDao ) {
        this.actionDao = actionDao;
        this.ficheDao = ficheDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Fiche modifierFiche( HttpServletRequest request ) {
        Long id = Long.parseLong( getValeurChamp( request, CHAMP_ID ) );
        String nomaction = getValeurChamp( request, CHAMP_NOM_ACTION );

        Fiche fiche = new Fiche();
        Action action = new Action();

        String nomfiche = null;
        InputStream contenufiche = null;
        int sizefiche = 0;

        try {
            Part partfiche = request.getPart( CHAMP_FILE );
            sizefiche = (int) partfiche.getSize();
            nomfiche = getNomFichier( partfiche );

            if ( nomfiche != null && !nomfiche.isEmpty() ) {
                nomfiche = nomfiche.substring(
                        nomfiche.lastIndexOf( '/' ) + 1 )
                        .substring( nomfiche.lastIndexOf( '\\' ) +
                                1 );
                /* R�cup�ration du contenu du fichier */
                contenufiche = partfiche.getInputStream();
            }

        } catch ( IllegalStateException e ) {
            e.printStackTrace();
            setErreur( CHAMP_FILE, "Les donn�es envoy�es sont trop volumineuses." );
        } catch ( IOException e ) {
            e.printStackTrace();
            setErreur( CHAMP_FILE, "Erreur de configuration du serveur." );
        } catch ( ServletException e ) {
            e.printStackTrace();
            setErreur( CHAMP_FILE,
                    "Ce type de requ�te n'est pas support�, merci d'utiliser le formulaire pr�vu pour envoyer votre fichier." );
        }

        /* Si aucune erreur n'est survenue jusqu'� pr�sent */
        if ( erreurs.isEmpty() ) {
            fiche.setId_fiche( id );
            traiterNomac( nomaction, action, fiche );
            traiterFiche( nomfiche, contenufiche, sizefiche, fiche );
        }

        /* Si aucune erreur n'est survenue jusqu'� pr�sent */
        if ( erreurs.isEmpty() ) {
            /* Ajout du fichier dans la base */
            try {
                ficheDao.modifier( fiche, fiche.getAction(), fiche.getNom_fiche(), fiche.getFile_fiche(),
                        fiche.getId_fiche() );
            } catch ( Exception e ) {
                setErreur( CHAMP_FILE, "Erreur lors de l'�criture du fichier sur le disque." );
            }
        }

        /* Initialisation du r�sultat global de la validation. */
        if ( erreurs.isEmpty() ) {
            resultat = "Succ�s de l'envoi du document.";
        } else {
            resultat = "�chec de l'envoi du document.";
        }
        return fiche;
    }

    /*
     * Appel � la validation du nom re�u et initialisation de la propri�t�
     * action du bean
     */
    private void traiterNomac( String nomaction, Action action, Fiche fiche ) {
        try {
            validationNomac( nomaction, action );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOM_ACTION, e.getMessage() );
        }
        fiche.setAction( action );
    }

    /*
     * Appel � la validation du fichier re�u et initialisation de la propri�t�
     * file_fiche du bean
     */
    private void traiterFiche( String nomfiche, InputStream contenufiche, int sizefiche, Fiche fiche ) {
        try {
            validationFiche( nomfiche, contenufiche );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_FILE, e.getMessage() );
        }
        fiche.setNom_fiche( nomfiche );
        try {
            fiche.setFile_fiche( getArrayByteFromStream( contenufiche, sizefiche ) );
        } catch ( IOException e ) {
            setErreur( CHAMP_FILE, e.getMessage() );
        }
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

    private void validationNomac( String nomaction, Action action ) throws FormValidationException {
        if ( nomaction != null ) {
            if ( nomaction.length() < 3 )
                throw new FormValidationException( "Le nom de l'action doit contenir au moins 3 caract�res." );
            if ( actionDao.trouver_par_nom( nomaction ) == null )
                throw new FormValidationException( "Cette action n'existe pas dans notre base de donn�es." );
        } else
            throw new FormValidationException( "Merci de saisir le nom de l'action." );
        action.setId( actionDao.trouver_par_nom( nomaction ).getId() );
    }

    private void validationFiche( String nomfiche, InputStream contenufiche ) throws FormValidationException {
        if ( nomfiche == null || contenufiche == null ) {
            throw new FormValidationException( "Merci de s�lectionner un fichier � envoyer." );
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
