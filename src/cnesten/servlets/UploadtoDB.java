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
import cnesten.forms.UploadtoDBForm;

@MultipartConfig( fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 )
@WebServlet( "/uploadtoDB" )
public class UploadtoDB extends HttpServlet {
    public static final String ATT_ACTION       = "Action";
    public static final String ATT_FORM         = "Form";
    public static final String SESSION_ACTIONS  = "actions";
    public static final String VUE_FORM         = "/WEB-INF/uploadtoDB.jsp";
    public static final String VUE_SUCCES       = "/listeActions";

    // Injection de nos EJBs (Session Bean Stateless)
    @EJB
    private ActionDAO          actionDao;

    private static final long  serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Affichage du formulaire */
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        UploadtoDBForm form = new UploadtoDBForm( actionDao );

        Action action = form.upload( request );

        request.setAttribute( ATT_ACTION, action );
        request.setAttribute( ATT_FORM, form );

        if ( form.getErreurs().isEmpty() ) {
            HttpSession session = request.getSession();
            Map<Long, Action> actions = (HashMap<Long, Action>) session
                    .getAttribute( SESSION_ACTIONS );

            if ( actions == null ) {
                actions = new HashMap<Long, Action>();
            }

            actions.put( action.getId(), action );
            session.setAttribute( SESSION_ACTIONS, actions );
        }

        if ( form.getErreurs().isEmpty() ) {

            /* Si aucune erreur, alors affichage de la fiche des exécutions */
            response.sendRedirect( request.getContextPath() + VUE_SUCCES );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }

    }

}
