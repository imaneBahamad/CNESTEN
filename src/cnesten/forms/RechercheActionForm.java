package cnesten.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cnesten.dao.ActionDAO;
import cnesten.entities.Action;

public class RechercheActionForm {
    private static final String CHAMP_NOM_ACTION = "nom_action";
    private static final String CHAMP_ANNEE      = "année";

    private Map<String, String> erreurs          = new HashMap<String, String>();

    private ActionDAO           actionDao;

    public RechercheActionForm( ActionDAO actionDao ) {
        this.actionDao = actionDao;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Action rechercherAction( HttpServletRequest request ) {
        String nomaction = getValeurChamp( request, CHAMP_NOM_ACTION );
        String annee = getValeurChamp( request, CHAMP_ANNEE );

        Action action = new Action();

        try {
            validationNom( nomaction );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOM_ACTION, e.getMessage() );

        }

        int valeurannee = 0;
        try {
            valeurannee = validationAnnee( annee );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_ANNEE, e.getMessage() );
        }

        if ( erreurs.isEmpty() ) {
            action = actionDao.trouver( nomaction, valeurannee );
        }
        return action;
    }

    /* Les méthodes de validations */

    private void validationNom( String nom ) throws FormValidationException {
        if ( nom != null ) {
            if ( nom.length() < 3 )
                throw new FormValidationException( "Au moins 3 caractères." );
        } else
            throw new FormValidationException( "Veuillez remplir le champs." );
    }

    private int validationAnnee( String annee ) throws FormValidationException {
        int temp;
        if ( annee != null ) {
            try {
                temp = Integer.parseInt( annee );
                if ( temp < 0 )
                    throw new FormValidationException( "Veuillez saisir un nombre positif." );
            } catch ( NumberFormatException e ) {
                throw new FormValidationException( "L'année doit être un nombre." );
            }

        } else {
            throw new FormValidationException( "Veuillez remplir le champs." );
        }

        return temp;
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
