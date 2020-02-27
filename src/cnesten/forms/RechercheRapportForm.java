package cnesten.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cnesten.dao.ActionDAO;
import cnesten.dao.RapportDAO;
import cnesten.entities.Action;
import cnesten.entities.Rapport;

public class RechercheRapportForm {
    private static final String CHAMP_NOM_ACTION = "nom_action";

    private Map<String, String> erreurs          = new HashMap<String, String>();

    private RapportDAO          rapportDao;
    private ActionDAO           actionDao;

    public RechercheRapportForm( RapportDAO rapportDao, ActionDAO actionDao ) {
        this.rapportDao = rapportDao;
        this.actionDao = actionDao;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Rapport rechercherRapport( HttpServletRequest request ) {
        String nomaction = getValeurChamp( request, CHAMP_NOM_ACTION );

        Rapport rapport = new Rapport();
        Action action = new Action();

        try {
            validationNom( nomaction );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOM_ACTION, e.getMessage() );

        }

        if ( erreurs.isEmpty() ) {
            action = actionDao.trouver_par_nom( nomaction );
            rapport = rapportDao.trouver_par_action( action );
        }

        return rapport;
    }

    /* La méthode de validation */

    private void validationNom( String nom ) throws FormValidationException {
        if ( nom != null ) {
            if ( nom.length() < 3 )
                throw new FormValidationException( "Au moins 3 caractères." );
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
