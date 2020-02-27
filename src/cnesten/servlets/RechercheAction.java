package cnesten.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cnesten.dao.ActionDAO;
import cnesten.entities.Action;
import cnesten.forms.RechercheActionForm;

@WebServlet( "/rechercheAction" )
public class RechercheAction extends HttpServlet {
    public static final String ATT_FORM          = "Form";
    public static final String SESSION_RECHERCHE = "recherche";
    public static final String VUE               = "/WEB-INF/action.jsp";
    public static final String VUE_SUCCES        = "/WEB-INF/rechercherAction.jsp";

    @EJB
    private ActionDAO          actionDao;

    private static final long  serialVersionUID  = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /*
         * Récupération de la map des actions enregistrée en session
         */
        HttpSession session = request.getSession();
        Map<Long, Action> rechAction = new HashMap<Long, Action>();

        RechercheActionForm form = new RechercheActionForm( actionDao );

        Action action = form.rechercherAction( request );

        request.setAttribute( ATT_FORM, form );

        if ( form.getErreurs().isEmpty() ) {
            if ( action != null ) {
                /* Ajout de l'action recherchée dans la map */
                rechAction.put( action.getId(), action );
            }
            /* Et enfin enregistrement de la map en session */
            session.setAttribute( SESSION_RECHERCHE, rechAction );

            /* Direction vers la fiche du résultat */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire les erreurs */
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }

}
