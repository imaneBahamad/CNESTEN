package cnesten.forms;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import cnesten.dao.ActionDAO;
import cnesten.dao.RapportDAO;
import cnesten.entities.Action;
import cnesten.entities.Rapport;

public class UploadRapForm {
    private static final String CHAMP_NOM_ACTION = "nom_action";
    private static final String CHAMP_FILE       = "file_rap";

    private String              resultat;
    private Map<String, String> erreurs          = new HashMap<String, String>();

    private RapportDAO          rapportDao;
    private ActionDAO           actionDao;

    public UploadRapForm( RapportDAO rapportDao, ActionDAO actionDao ) {
        this.actionDao = actionDao;
        this.rapportDao = rapportDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Rapport upload( HttpServletRequest request ) {
        String nomaction = getValeurChamp( request, CHAMP_NOM_ACTION );

        Rapport rapport = new Rapport();
        Action action = new Action();

        String nomrap = null;
        InputStream contenurap = null;
        int sizerap = 0;

        try {
            Part partrap = request.getPart( CHAMP_FILE );
            sizerap = (int) partrap.getSize();
            nomrap = getNomFichier( partrap );

            if ( nomrap != null && !nomrap.isEmpty() ) {
                nomrap = nomrap.substring(
                        nomrap.lastIndexOf( '/' ) + 1 )
                        .substring( nomrap.lastIndexOf( '\\' ) +
                                1 );
                /* Récupération du contenu du fichier */
                contenurap = partrap.getInputStream();
            }

        } catch ( IllegalStateException e ) {
            e.printStackTrace();
            setErreur( CHAMP_FILE, "Les données envoyées sont trop volumineuses." );
        } catch ( IOException e ) {
            e.printStackTrace();
            setErreur( CHAMP_FILE, "Erreur de configuration du serveur." );
        } catch ( ServletException e ) {
            e.printStackTrace();
            setErreur( CHAMP_FILE,
                    "Ce type de requête n'est pas supporté, merci d'utiliser le formulaire prévu pour envoyer votre fichier." );
        }

        /* Si aucune erreur n'est survenue jusqu'à présent */
        if ( erreurs.isEmpty() ) {
            traiterNomac( nomaction, action, rapport );
            traiterRapport( nomrap, contenurap, sizerap, rapport );
        }

        /* Si aucune erreur n'est survenue jusqu'à présent */
        if ( erreurs.isEmpty() ) {
            /* Ajout du fichier dans la base */
            try {
                rapportDao.creer( rapport );
            } catch ( Exception e ) {
                setErreur( CHAMP_FILE, "Erreur lors de l'écriture du fichier sur le disque." );
            }
        }

        /* Initialisation du résultat global de la validation. */
        if ( erreurs.isEmpty() ) {
            resultat = "Succès de l'envoi du document.";
        } else {
            resultat = "Échec de l'envoi du document.";
        }
        return rapport;
    }

    /*
     * Appel à la validation du nom reçu et initialisation de la propriété
     * action du bean
     */
    private void traiterNomac( String nomaction, Action action, Rapport rapport ) {
        try {
            validationNomac( nomaction, action );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOM_ACTION, e.getMessage() );
        }
        rapport.setAction( action );
    }

    /*
     * Appel à la validation du fichier reçu et initialisation de la propriété
     * file_rap du bean
     */
    private void traiterRapport( String nomrap, InputStream contenurap, int sizerap, Rapport rapport ) {
        try {
            validationRapport( nomrap, contenurap );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_FILE, e.getMessage() );
        }
        rapport.setNom_rap( nomrap );
        try {
            rapport.setFile_rap( getArrayByteFromStream( contenurap, sizerap ) );
        } catch ( IOException e ) {
            setErreur( CHAMP_FILE, e.getMessage() );
        }
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

    /* Les méthodes de validation */

    private void validationNomac( String nomaction, Action action ) throws FormValidationException {
        if ( nomaction != null ) {
            if ( nomaction.length() < 3 )
                throw new FormValidationException( "Le nom de l'action doit contenir au moins 3 caractères." );
            if ( actionDao.trouver_par_nom( nomaction ) == null )
                throw new FormValidationException( "Cette action n'existe pas dans notre base de données." );
        } else
            throw new FormValidationException( "Merci de saisir le nom de l'action." );
        action.setId( actionDao.trouver_par_nom( nomaction ).getId() );
    }

    private void validationRapport( String nomrap, InputStream contenurap ) throws FormValidationException {
        if ( nomrap == null || contenurap == null ) {
            throw new FormValidationException( "Merci de sélectionner un fichier à envoyer." );
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
