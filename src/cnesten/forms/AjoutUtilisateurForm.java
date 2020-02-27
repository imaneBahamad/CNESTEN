package cnesten.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import cnesten.dao.DAOException;
import cnesten.dao.UtilisateurDAO;
import cnesten.entities.Utilisateur;

public class AjoutUtilisateurForm {
    private static final String CHAMP_NOM        = "nom";
    private static final String CHAMP_PRENOM     = "prenom";
    private static final String CHAMP_EMAIL      = "email";
    private static final String CHAMP_PASS       = "motdepasse";
    private static final String CHAMP_CONF       = "confirmation";
    private static final String CHAMP_PRIVILEGE  = "privil�ge";
    private static final String ALGO_CHIFFREMENT = "SHA-256";

    private String              resultat;
    private Map<String, String> erreurs          = new HashMap<String, String>();

    private UtilisateurDAO      utilisateurDao;

    public AjoutUtilisateurForm( UtilisateurDAO utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Utilisateur ajouterUtilisateur( HttpServletRequest request ) {
        /* R�cup�rer les valeurs saisies par l'utilisateur */
        String nom = getValeurChamp( request, CHAMP_NOM );
        String prenom = getValeurChamp( request, CHAMP_PRENOM );
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String motdepasse = getValeurChamp( request, CHAMP_PASS );
        String confirmation = getValeurChamp( request, CHAMP_CONF );
        String privilege = getValeurChamp( request, CHAMP_PRIVILEGE );

        Utilisateur utilisateur = new Utilisateur();

        try {
            traiterNom( nom, utilisateur );
            traiterPrenom( prenom, utilisateur );
            traiterEmail( email, utilisateur );
            traiterMotsDePasse( motdepasse, confirmation, utilisateur );
            utilisateur.setPrivil�ge( privilege );

            if ( erreurs.isEmpty() ) {
                utilisateurDao.creer( utilisateur );
                resultat = "Succ�s de l'ajout.";
            } else {
                resultat = "�chec de l'ajout.";
            }
        } catch ( DAOException e ) {
            resultat = "�chec de l'ajout : une erreur impr�vue est survenue, merci de r�essayer dans quelques instants.";
            e.printStackTrace();
        }

        return utilisateur;
    }

    /*
     * Appel � la validation du nom re�u et initialisation de la propri�t� nom
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
     * Appel � la validation du prenom re�u et initialisation de la propri�t�
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
     * Appel � la validation de l'adresse email re�ue et initialisation de la
     * propri�t� email du bean
     */
    private void traiterEmail( String email, Utilisateur utilisateur ) {
        try {
            validationEmail( email );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }
        utilisateur.setEmail( email );
    }

    /*
     * Appel � la validation des mots de passe re�us, chiffrement du mot de
     * passe et initialisation de la propri�t� motdepasse du bean
     */
    private void traiterMotsDePasse( String motdepasse, String confirmation, Utilisateur utilisateur ) {
        try {
            validationMotsDePasse( motdepasse, confirmation );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
            setErreur( CHAMP_CONF, null );
        }
        /*
         * Utilisation de la biblioth�que Jasypt pour chiffrer le mot de passe
         * efficacement.L'algorithme SHA-256 est ici utilis�, avec par d�faut un
         * salage al�atoire et un grand nombre d'it�rations de la fonction de
         * hashage. La String retourn�e est de longueur 56 et contient le hash
         * en Base64.
         */
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm( ALGO_CHIFFREMENT );
        passwordEncryptor.setPlainDigest( false );
        String motDePasseChiffre = passwordEncryptor.encryptPassword( motdepasse );

        utilisateur.setMotdepasse( motDePasseChiffre );
    }

    /* Les m�thodes de validation */

    private void validationNom( String nom ) throws FormValidationException {
        if ( nom != null ) {
            if ( nom.length() < 2 )
                throw new FormValidationException( "Le nom d'utilisateur doit contenir au moins 2 caract�res." );
            else if ( utilisateurDao.trouver_par_nom( nom ) != null ) {
                throw new FormValidationException( "Ce nom existe d�j�." );
            }
        } else
            throw new FormValidationException( "Merci de saisir le nom de l'utilisateur." );

    }

    private void validationPrenom( String prenom ) throws FormValidationException {
        if ( prenom != null && prenom.length() < 3 ) {
            throw new FormValidationException( "Le prenom doit contenir au moins 3 caract�res." );
        }
    }

    private void validationEmail( String email ) throws FormValidationException {
        if ( email != null ) {
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
                throw new FormValidationException( "Merci de saisir une adresse mail valide." );
            } else if ( utilisateurDao.trouver_par_email( email ) != null ) {
                throw new FormValidationException(
                        "Cette adresse email est d�j� utilis�e, merci d'en choisir une autre." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir une adresse mail." );
        }
    }

    private void validationMotsDePasse( String motDePasse, String confirmation ) throws FormValidationException {
        if ( motDePasse != null && confirmation != null ) {
            if ( !motDePasse.equals( confirmation ) ) {
                throw new FormValidationException(
                        "Les mots de passe entr�s sont diff�rents, merci de les saisir � nouveau." );
            } else if ( motDePasse.length() < 3 ) {
                throw new FormValidationException( "Les mots de passe doivent contenir au moins 3 caract�res." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir et confirmer le mot de passe." );
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

}
