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
import cnesten.dao.FicheDAO;
import cnesten.entities.Fiche;
import cnesten.forms.UploadFicheForm;

@MultipartConfig( fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 )
@WebServlet( "/uploadFiche" )
public class UploadFiche extends HttpServlet {
    public static final String ATT_FICHE        = "Fiche";
    public static final String ATT_FORM         = "Form";
    public static final String SESSION_FICHES   = "fiches";
    public static final String VUE_FORM         = "/WEB-INF/uploadFiche.jsp";
    public static final String VUE_SUCCES       = "/listeFiches";

    // Injection de nos EJBs (Session Bean Stateless)
    @EJB
    private FicheDAO           ficheDao;
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
        UploadFicheForm form = new UploadFicheForm( ficheDao, actionDao );

        Fiche fiche = form.upload( request );

        request.setAttribute( ATT_FICHE, fiche );
        request.setAttribute( ATT_FORM, form );

        if ( form.getErreurs().isEmpty() ) {
            HttpSession session = request.getSession();
            Map<Long, Fiche> fiches = (HashMap<Long, Fiche>) session
                    .getAttribute( SESSION_FICHES );

            if ( fiches == null ) {
                fiches = new HashMap<Long, Fiche>();
            }

            fiches.put( fiche.getId_fiche(), fiche );
            session.setAttribute( SESSION_FICHES, fiches );
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
