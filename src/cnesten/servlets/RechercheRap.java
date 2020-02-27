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
import cnesten.dao.RapportDAO;
import cnesten.entities.Rapport;
import cnesten.forms.RechercheRapportForm;

@WebServlet( "/rechercheRap" )
public class RechercheRap extends HttpServlet {
    public static final String ATT_FORM          = "Form";
    public static final String SESSION_RECHERCHE = "recherche";
    public static final String VUE               = "/WEB-INF/rapport.jsp";
    public static final String VUE_SUCCES        = "/WEB-INF/rechercherRap.jsp";

    @EJB
    private RapportDAO         rapportDao;
    @EJB
    private ActionDAO          actionDao;

    private static final long  serialVersionUID  = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /*
         * Récupération de la map des rapports enregistrée en session
         */
        HttpSession session = request.getSession();
        Map<Long, Rapport> rechRapport = new HashMap<Long, Rapport>();

        RechercheRapportForm form = new RechercheRapportForm( rapportDao, actionDao );

        Rapport rapport = form.rechercherRapport( request );

        request.setAttribute( ATT_FORM, form );

        if ( form.getErreurs().isEmpty() ) {

            if ( rapport != null ) {
                /* Ajout du rapport recherché dans la map */
                rechRapport.put( rapport.getId_rap(), rapport );
            }
            /* Et enfin enregistrement de la map en session */
            session.setAttribute( SESSION_RECHERCHE, rechRapport );

            /* Direction vers la fiche du résultat */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire les erreurs */
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }

}
