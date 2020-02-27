package cnesten.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import cnesten.dao.DAOException;
import cnesten.dao.UtilisateurDAO;
import cnesten.entities.Utilisateur;

public class ConnexionForm {
    private static final String CHAMP_NOM        = "nom";
    private static final String CHAMP_PASS       = "motdepasse";
    private static final String GENERAL          = "general";

    private static final String ALGO_CHIFFREMENT = "SHA-256";

    private String              resultat;
    private Map<String, String> erreurs          = new HashMap<String, String>();

    private UtilisateurDAO      utilisateurDao;

    public ConnexionForm( UtilisateurDAO utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Utilisateur connecterUtilisateur( HttpServletRequest request ) {
        /* R�cup�rer les valeurs saisies par l'utilisateur */
        String nom = getValeurChamp( request, CHAMP_NOM );
        String motdepasse = getValeurChamp( request, CHAMP_PASS );

        Utilisateur utilisateur = new Utilisateur();

        try {
            traiterNom( nom, utilisateur );
            traiterMotsDePasse( motdepasse, utilisateur );
            checkPass( motdepasse, nom, utilisateur );

            if ( erreurs.isEmpty() ) {
                utilisateur.setPrenom( utilisateurDao.trouver_par_nom( nom ).getPrenom() );
                utilisateur.setPrivil�ge( utilisateurDao.trouver_par_nom( nom ).getPrivil�ge() );

                resultat = "Succ�s de la connexion.";
            } else {
                resultat = "�chec de la connexion.";
            }
        } catch ( DAOException e ) {
            resultat = "�chec de la connexion: une erreur impr�vue est survenue, merci de r�essayer dans quelques instants.";
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
     * Appel � la validation du mot de passe re�u et initialisation de la
     * propri�t� motdepasse du bean
     */
    private void traiterMotsDePasse( String motdepasse, Utilisateur utilisateur ) {
        try {
            validationMotDePasse( motdepasse );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
        }
        utilisateur.setMotdepasse( motdepasse );
    }

    /* V�rifier si le mot de passe est correct */
    private void checkPass( String motdepasse, String nom, Utilisateur utilisateur ) {
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm( ALGO_CHIFFREMENT );
        passwordEncryptor.setPlainDigest( false );
        try {
            boolean test = passwordEncryptor.checkPassword( motdepasse,
                    utilisateurDao.trouver_par_nom( nom ).getMotdepasse() );
            if ( test == false ) {
                setErreur( GENERAL, "Utilisateur/Mot de passe incorrect" );
            }
        } catch ( NullPointerException e ) {
            // setErreur( GENERAL, "Veuillez remplir tous les champs" );
        }
    }

    /* Les m�thodes de validation */

    private void validationNom( String nom ) throws FormValidationException {
        if ( nom != null ) {
            if ( nom.length() < 2 )
                throw new FormValidationException( "Le nom d'utilisateur doit contenir au moins 2 caract�res." );
            else if ( utilisateurDao.trouver_par_nom( nom ) == null ) {
                throw new FormValidationException( "Ce nom n'existe pas dans notre base de donn�es." );
            }
        } else
            throw new FormValidationException( "Merci de saisir le nom de l'utilisateur." );

    }

    private void validationMotDePasse( String motdepasse ) throws FormValidationException {
        if ( motdepasse != null ) {
            if ( motdepasse.length() < 3 )
                throw new FormValidationException( "Les mots de passe doivent contenir au moins 3 caract�res." );
        } else
            throw new FormValidationException( "Merci de saisir votre mot de passe." );
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
