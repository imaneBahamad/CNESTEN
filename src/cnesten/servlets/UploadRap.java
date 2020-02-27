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
import cnesten.dao.RapportDAO;
import cnesten.entities.Rapport;
import cnesten.forms.UploadRapForm;

@MultipartConfig( fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 )
@WebServlet( "/uploadRap" )
public class UploadRap extends HttpServlet {
    public static final String ATT_RAPPORT      = "Rapport";
    public static final String ATT_FORM         = "Form";
    public static final String SESSION_RAPPORTS = "rapports";
    public static final String VUE_FORM         = "/WEB-INF/uploadRap.jsp";
    public static final String VUE_SUCCES       = "/listeRapports";

    // Injection de nos EJBs (Session Bean Stateless)
    @EJB
    private RapportDAO         rapportDao;
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
        UploadRapForm form = new UploadRapForm( rapportDao, actionDao );

        Rapport rapport = form.upload( request );

        request.setAttribute( ATT_RAPPORT, rapport );
        request.setAttribute( ATT_FORM, form );

        if ( form.getErreurs().isEmpty() ) {
            HttpSession session = request.getSession();
            Map<Long, Rapport> rapports = (HashMap<Long, Rapport>) session
                    .getAttribute( SESSION_RAPPORTS );

            if ( rapports == null ) {
                rapports = new HashMap<Long, Rapport>();
            }

            rapports.put( rapport.getId_rap(), rapport );
            session.setAttribute( SESSION_RAPPORTS, rapports );
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
