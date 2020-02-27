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

import cnesten.dao.UtilisateurDAO;
import cnesten.entities.Utilisateur;
import cnesten.forms.RechercheUtilisateurForm;

@WebServlet( "/rechercheUtilisateur" )
public class RechercheUtilisateur extends HttpServlet {
    public static final String ATT_FORM          = "Form";
    public static final String SESSION_RECHERCHE = "recherche";
    public static final String VUE               = "/WEB-INF/utilisateur.jsp";
    public static final String VUE_SUCCES        = "/WEB-INF/rechercherUtilisateur.jsp";

    @EJB
    private UtilisateurDAO     UtilisateurDao;

    private static final long  serialVersionUID  = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /*
         * Récupération de la map des utilisateurs enregistrée en session
         */
        HttpSession session = request.getSession();
        Map<Long, Utilisateur> rechUtilisateur = new HashMap<Long, Utilisateur>();

        RechercheUtilisateurForm form = new RechercheUtilisateurForm( UtilisateurDao );

        Utilisateur utilisateur = form.rechercherUtilisateur( request );

        request.setAttribute( ATT_FORM, form );

        if ( form.getErreurs().isEmpty() ) {
            if ( utilisateur != null ) {
                /* Ajout du Utilisateur recherché dans la map */
                rechUtilisateur.put( utilisateur.getCode_utilisateur(), utilisateur );
            }
            /* Et enfin enregistrement de la map en session */
            session.setAttribute( SESSION_RECHERCHE, rechUtilisateur );

            /* Direction vers la fiche du résultat */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire les erreurs */
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }

}
