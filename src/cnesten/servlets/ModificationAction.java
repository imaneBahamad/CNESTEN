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
        /* R�cup�ration du param�tre */
        Long id = Long.parseLong( getValeurParametre( request, PARAM_ID ) );

        /*
         * R�cup�ration de la Map des actions enregistr�e en session
         */
        HttpSession session = request.getSession();
        Map<Long, Action> actions = (HashMap<Long, Action>) session.getAttribute( SESSION_ACTIONS );

        /* Si le code et la Map des actions ne sont pas vides */
        if ( id != null && actions != null ) {
            Action action = new Action();
            action.setId( id );
            action.setNom_action( actions.get( id ).getNom_action() );
            action.setProc�dure( actions.get( id ).getProc�dure() );
            action.setChecklist( actions.get( id ).getChecklist() );
            action.setAnn�e( actions.get( id ).getAnn�e() );

            session.setAttribute( UPDATE_ACTION, action );

            /* Affichage du formulaire */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /* Pr�paration de l'objet formulaire */
        ModificationActionForm form = new ModificationActionForm( actionDao );

        /* Traitement de la requ�te et r�cup�ration du bean en r�sultant */
        Action action = form.modifier( request );

        /* Ajout du bean et de l'objet m�tier � l'objet requ�te */
        request.setAttribute( ATT_ACTION, action );
        request.setAttribute( ATT_FORM, form );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {
            /*
             * Alors r�cup�ration de la map des Actions dans la session
             */
            HttpSession session = request.getSession();
            Map<Long, Action> actions = (HashMap<Long, Action>) session.getAttribute( SESSION_ACTIONS );

            /* Puis ajout de l'action courante dans la map */
            actions.put( action.getId(), action );

            /* Et enfin (r�)enregistrement de la map en session */
            session.setAttribute( SESSION_ACTIONS, actions );
        }

        if ( form.getErreurs().isEmpty() ) {

            /* Si aucune erreur, alors affichage de la liste des actions */
            response.sendRedirect( request.getContextPath() + VUE_SUCCES );
        } else {
            /* Sinon, r�-affichage du formulaire de cr�ation avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }

    /*
     * M�thode utilitaire qui retourne null si un param�tre est vide, et son
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
