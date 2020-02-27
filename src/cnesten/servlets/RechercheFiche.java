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
import cnesten.dao.FicheDAO;
import cnesten.entities.Fiche;
import cnesten.forms.RechercheFicheForm;

@WebServlet( "/rechercheFiche" )
public class RechercheFiche extends HttpServlet {
    public static final String ATT_FORM          = "Form";
    public static final String SESSION_RECHERCHE = "recherche";
    public static final String VUE               = "/WEB-INF/fiche.jsp";
    public static final String VUE_SUCCES        = "/WEB-INF/rechercherFiche.jsp";

    @EJB
    private FicheDAO           ficheDao;
    @EJB
    private ActionDAO          actionDao;

    private static final long  serialVersionUID  = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /*
         * Récupération de la map des fiches enregistrée en session
         */
        HttpSession session = request.getSession();
        Map<Long, Fiche> rechFiche = new HashMap<Long, Fiche>();

        RechercheFicheForm form = new RechercheFicheForm( ficheDao, actionDao );

        Fiche fiche = form.rechercherFiche( request );

        request.setAttribute( ATT_FORM, form );

        if ( form.getErreurs().isEmpty() ) {
            if ( fiche != null ) {
                /* Ajout de la fiche recherchée dans la map */
                rechFiche.put( fiche.getId_fiche(), fiche );
            }
            /* Et enfin enregistrement de la map en session */
            session.setAttribute( SESSION_RECHERCHE, rechFiche );

            /* Direction vers la fiche du résultat */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire les erreurs */
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }

}
