package cnesten.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cnesten.dao.ActionDAO;
import cnesten.entities.Action;
import cnesten.forms.ModificationActionForm;

@MultipartConfig( fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 )
@WebServlet( "/modificationAction" )
public class ModificationAction extends HttpServlet {
    public static final String  PARAM_ID         = "idAction";
    public static final String  ATT_ACTION       = "Action";
    public static final String  ATT_FORM         = "Form";
    public static final String  SESSION_ACTIONS  = "actions";
    public static final String  UPDATE_ACTION    = "action";
    private static final String VUE_FORM         = "/WEB-INF/modifierAction.jsp";
    public static final String  VUE_SUCCES       = "/listeActions";

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private ActionDAO           actionDao;

    private static final long   serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Récupération du paramètre */
        Long id = Long.parseLong( getValeurParametre( request, PARAM_ID ) );

        /*
         * Récupération de la Map des actions enregistrée en session
         */
        HttpSession session = request.getSession();
        Map<Long, Action> actions = (HashMap<Long, Action>) session.getAttribute( SESSION_ACTIONS );

        /* Si le code et la Map des actions ne sont pas vides */
        if ( id != null && actions != null ) {
            Action action = new Action();
            action.setId( id );
            action.setNom_action( actions.get( id ).getNom_action() );
            action.setProcédure( actions.get( id ).getProcédure() );
            action.setChecklist( actions.get( id ).getChecklist() );
            action.setAnnée( actions.get( id ).getAnnée() );

            session.setAttribute( UPDATE_ACTION, action );

            /* Affichage du formulaire */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /* Préparation de l'objet formulaire */
        ModificationActionForm form = new ModificationActionForm( actionDao );

        /* Traitement de la requête et récupération du bean en résultant */
        Action action = form.modifier( request );

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( ATT_ACTION, action );
        request.setAttribute( ATT_FORM, form );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {
            /*
             * Alors récupération de la map des Actions dans la session
             */
            HttpSession session = request.getSession();
            Map<Long, Action> actions = (HashMap<Long, Action>) session.getAttribute( SESSION_ACTIONS );

            /* Puis ajout de l'action courante dans la map */
            actions.put( action.getId(), action );

            /* Et enfin (ré)enregistrement de la map en session */
            session.setAttribute( SESSION_ACTIONS, actions );
        }

        if ( form.getErreurs().isEmpty() ) {

            /* Si aucune erreur, alors affichage de la liste des actions */
            response.sendRedirect( request.getContextPath() + VUE_SUCCES );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }

    /*
     * Méthode utilitaire qui retourne null si un paramètre est vide, et son
     * contenu sinon.
     */
    private static String getValeurParametre( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}
