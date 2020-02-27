package cnesten.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cnesten.dao.DAOException;
import cnesten.dao.UtilisateurDAO;
import cnesten.entities.Utilisateur;

public class ModificationUtilisateurForm {
    private static final String CHAMP_CODE_UTILISATEUR = "code_utilisateur";
    private static final String CHAMP_NOM              = "nom";
    private static final String CHAMP_PRENOM           = "prenom";
    private static final String CHAMP_EMAIL            = "email";
    private static final String CHAMP_PASS             = "motdepasse";
    private static final String CHAMP_CONF             = "confirmation";
    private static final String CHAMP_PRIVILEGE        = "privilège";

    private String              resultat;
    private Map<String, String> erreurs                = new HashMap<String, String>();

    private UtilisateurDAO      utilisateurDao;

    public ModificationUtilisateurForm( UtilisateurDAO utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Utilisateur modifierUtilisateur( HttpServletRequest request ) {
        /* Récupérer les valeurs saisies par l'utilisateur */
        Long codeutilisateur = Long.parseLong( getValeurChamp( request, CHAMP_CODE_UTILISATEUR ) );
        String nom = getValeurChamp( request, CHAMP_NOM );
        String prenom = getValeurChamp( request, CHAMP_PRENOM );
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String motdepasse = getValeurChamp( request, CHAMP_PASS );
        String confirmation = getValeurChamp( request, CHAMP_CONF );
        String privilege = getValeurChamp( request, CHAMP_PRIVILEGE );

        Utilisateur utilisateur = new Utilisateur();

        try {
            utilisateur.setCode_utilisateur( codeutilisateur );
            traiterNom( nom, utilisateur );
            traiterPrenom( prenom, utilisateur );
            traiterEmail( email, utilisateur );
            utilisateur.setPrivilège( privilege );

            if ( erreurs.isEmpty() ) {
                utilisateurDao.modifier( utilisateur, utilisateur.getNom(), utilisateur.getPrenom(),
                        utilisateur.getEmail(), utilisateur.getPrivilège(), utilisateur.getCode_utilisateur() );
                resultat = "Succès de la modification.";
            } else {
                resultat = "Échec de la modification.";
            }
        } catch ( DAOException e ) {
            resultat = "Échec de la modification : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }

        return utilisateur;
    }

    /*
     * Appel à la validation du nom reçu et initialisation de la propriété nom
     * du bean
     */
    private void traiterNom( String nom, Utilisateur utilisateur ) {
        try {
            validationNom( nom );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOM, e.getMessage() );
        }
        utilisateur.setNom( nom );
    }

    /*
     * Appel à la validation du prenom reçu et initialisation de la propriété
     * prenom du bean
     */
    private void traiterPrenom( String prenom, Utilisateur utilisateur ) {
        try {
            validationPrenom( prenom );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PRENOM, e.getMessage() );
        }
        utilisateur.setPrenom( prenom );
    }

    /*
     * Appel à la validation de l'adresse email reçue et initialisation de la
     * propriété email du bean
     */
    private void traiterEmail( String email, Utilisateur utilisateur ) {
        try {
            validationEmail( email );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }
        utilisateur.setEmail( email );
    }

    /* Les méthodes de validation */

    private void validationNom( String nom ) throws FormValidationException {
        if ( nom != null ) {
            if ( nom.length() < 2 )
                throw new FormValidationException( "Le nom d'utilisateur doit contenir au moins 2 caractères." );
        } else
            throw new FormValidationException( "Merci de saisir le nom de l'utilisateur." );

    }

    private void validationPrenom( String prenom ) throws FormValidationException {
        if ( prenom != null && prenom.length() < 3 ) {
            throw new FormValidationException( "Le prenom doit contenir au moins 3 caractères." );
        }
    }

    private void validationEmail( String email ) throws FormValidationException {
        if ( email != null ) {
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
                throw new FormValidationException( "Merci de saisir une adresse mail valide." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir une adresse mail." );
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

}
