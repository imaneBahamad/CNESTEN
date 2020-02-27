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
    private static final String CHAMP_PROCEDURE  = "procédure";
    private static final String CHAMP_CHECKLIST  = "checklist";
    private static final String CHAMP_ANNEE      = "année";
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
                /* Récupération du contenu du fichier */
                contenuproc = partproc.getInputStream();
            }

        } catch ( IllegalStateException e ) {
            e.printStackTrace();
            setErreur( CHAMP_PROCEDURE, "Les données envoyées sont trop volumineuses." );
        } catch ( IOException e ) {
            e.printStackTrace();
            setErreur( CHAMP_PROCEDURE, "Erreur de configuration du serveur." );
        } catch ( ServletException e ) {
            e.printStackTrace();
            setErreur( CHAMP_PROCEDURE,
                    "Ce type de requête n'est pas supporté, merci d'utiliser le formulaire prévu pour envoyer votre fichier." );
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
                /* Récupération du contenu du fichier */
                contenuchlist = partchlist.getInputStream();
            }
        } catch ( IllegalStateException e ) {
            e.printStackTrace();
            setErreur( CHAMP_CHECKLIST, "Les données envoyées sont trop volumineuses." );
        } catch ( IOException e ) {
            e.printStackTrace();
            setErreur( CHAMP_CHECKLIST, "Erreur de configuration du serveur." );
        } catch ( ServletException e ) {
            e.printStackTrace();
            setErreur( CHAMP_CHECKLIST,
                    "Ce type de requête n'est pas supporté, merci d'utiliser le formulaire prévu pour envoyer votre fichier." );
        }

        /* Si aucune erreur n'est survenue jusqu'à présent */
        if ( erreurs.isEmpty() ) {
            traiterNomac( nomaction, action );
            traiterProcédure( nomproc, contenuproc, sizeproc, action );
            traiterChecklist( nomchlist, contenuchlist, sizechlist, action );
            traiterAnnee( annee, action );
            traiterLce( lce, action );
        }

        /* Si aucune erreur n'est survenue jusqu'à présent */
        if ( erreurs.isEmpty() ) {
            /* Ajout du fichier dans la base */
            try {
                actionDao.creer( action );
            } catch ( Exception e ) {
                setErreur( CHAMP_PROCEDURE, "Erreur lors de l'écriture du fichier sur le disque." );
            }
        }

        /* Initialisation du résultat global de la validation. */
        if ( erreurs.isEmpty() ) {
            resultat = "Succès de l'envoi du document.";
        } else {
            resultat = "Échec de l'envoi du document.";
        }
        return action;
    }

    /*
     * Appel à la validation du nom reçu et initialisation de la propriété
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
     * Appel à la validation du fichier reçu et initialisation de la propriété
     * procédure du bean
     */
    private void traiterProcédure( String nomproc, InputStream contenuproc, int sizeproc, Action action ) {
        try {
            validationProcédure( nomproc, contenuproc );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PROCEDURE, e.getMessage() );
        }
        action.setNom_proc( nomproc );
        try {
            action.setProcédure( getArrayByteFromStream( contenuproc, sizeproc ) );
        } catch ( IOException e ) {
            setErreur( CHAMP_PROCEDURE, e.getMessage() );
        }
    }

    /*
     * Appel à la validation du fichier reçu et initialisation de la propriété
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
     * Appel à la validation du nombre reçu et initialisation de la propriété
     * année du bean
     */
    private void traiterAnnee( String annee, Action action ) {
        int valeurAnnee = 0;
        try {
            valeurAnnee = validationAnnee( annee );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_ANNEE, e.getMessage() );
        }
        action.setAnnée( valeurAnnee );
    }

    /*
     * Appel à la validation du texte reçu et initialisation de la propriété lce
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
         * On crée un tableau de byte avec une longueur égal à la taille du
         * fichier (logique non :mrgreen: ?)
         */
        byte[] data = new byte[size];
        /*
         * On lit la totalité du fichier. Le premier paramètre est le tableau de
         * byte qui va recevoir les données, le deuxième est la position dans le
         * fichier où on commence à lire les données (donc zéro puisqu'on
         * commence au début du fichier), enfin le dernier paramètre est le
         * nombre d'octet que l'on veut lire, ici on donne length puisqu'on veut
         * lire la totalité du fichier d'un coup.
         */
        try {
            int result = stream.read( data, 0, size );
        } catch ( Exception e ) {
        }

        return data;
    }

    /* La méthode de validation */

    private void validationNomac( String nomaction ) throws FormValidationException {
        if ( nomaction != null ) {
            if ( nomaction.length() < 3 )
                throw new FormValidationException( "Le nom de l'action doit contenir au moins 3 caractères." );
            if ( actionDao.trouver_par_nom( nomaction ) != null )
                throw new FormValidationException( "Cette action existe déjà." );
        } else
            throw new FormValidationException( "Merci de saisir le nom de l'action." );
    }

    private void validationProcédure( String nomproc, InputStream contenuproc ) throws FormValidationException {
        if ( nomproc == null || contenuproc == null ) {
            throw new FormValidationException( "Merci de sélectionner un fichier à envoyer." );
        }
    }

    private int validationAnnee( String annee ) throws FormValidationException {
        int temp;
        if ( annee != null ) {
            try {
                temp = Integer.parseInt( annee );
                if ( temp < 0 )
                    throw new FormValidationException( "L'année doit être un nombre positif." );
            } catch ( NumberFormatException e ) {
                throw new FormValidationException( "L'année doit être un nombre." );
            }

        } else {
            throw new FormValidationException( "Merci de saisir une année." );
        }

        return temp;
    }

    private void validationLce( String lce ) throws FormValidationException {
        if ( lce != null && lce.length() < 10 ) {
            throw new FormValidationException( "Le LCE doit contenir au moins 10 caractères." );
        }
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */

    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    /*
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
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
         * Boucle sur chacun des paramètres de l'en-tête "contentdisposition".
         */
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            /*
             * Recherche de l'éventuelle présence du paramètre "filename".
             */
            if ( contentDisposition.trim().startsWith( "filename" ) ) {
                /*
                 * Si "filename" est présent, alors renvoi de sa valeur,
                 * c'est-à-dire du nom de fichier sans guillemets.
                 */
                return contentDisposition.substring(
                        contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        } /* Et pour terminer, si rien n'a été trouvé... */
        return null;
    }

}
