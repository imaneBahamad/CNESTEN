package cnesten.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cnesten.dao.UtilisateurDAO;
import cnesten.entities.Utilisateur;

public class RechercheUtilisateurForm {
    private static final String CHAMP_NOM = "nom";

    private Map<String, String> erreurs   = new HashMap<String, String>();

    private UtilisateurDAO      utilisateurDao;

    public RechercheUtilisateurForm( UtilisateurDAO utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Utilisateur rechercherUtilisateur( HttpServletRequest request ) {
        String nom = getValeurChamp( request, CHAMP_NOM );

        Utilisateur utilisateur = new Utilisateur();

        try {
            validationNom( nom );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOM, e.getMessage() );

        }

        if ( erreurs.isEmpty() ) {
            utilisateur = utilisateurDao.trouver_par_nom( nom );
        }

        return utilisateur;
    }

    /* La méthode de validation */

    private void validationNom( String nom ) throws FormValidationException {
        if ( nom != null ) {
            if ( nom.length() < 2 )
                throw new FormValidationException( "Au moins 2 caractères." );
        } else
            throw new FormValidationException( "Veuillez remplir le champs." );
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
