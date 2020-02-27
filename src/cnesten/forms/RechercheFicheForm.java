package cnesten.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cnesten.dao.ActionDAO;
import cnesten.dao.FicheDAO;
import cnesten.entities.Action;
import cnesten.entities.Fiche;

public class RechercheFicheForm {
    private static final String CHAMP_NOM_ACTION = "nom_action";

    private Map<String, String> erreurs          = new HashMap<String, String>();

    private FicheDAO            ficheDao;
    private ActionDAO           actionDao;

    public RechercheFicheForm( FicheDAO ficheDao, ActionDAO actionDao ) {
        this.ficheDao = ficheDao;
        this.actionDao = actionDao;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Fiche rechercherFiche( HttpServletRequest request ) {
        String nomaction = getValeurChamp( request, CHAMP_NOM_ACTION );

        Fiche fiche = new Fiche();
        Action action = new Action();

        try {
            validationNom( nomaction );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOM_ACTION, e.getMessage() );

        }

        if ( erreurs.isEmpty() ) {
            action = actionDao.trouver_par_nom( nomaction );
            fiche = ficheDao.trouver_par_action( action );

        }

        return fiche;
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
