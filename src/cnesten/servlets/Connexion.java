package cnesten.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cnesten.dao.UtilisateurDAO;
import cnesten.entities.Utilisateur;
import cnesten.forms.ConnexionForm;

@WebServlet( "/connexion" )
public class Connexion extends HttpServlet {
    public static final String ATT_USER         = "Utilisateur";
    public static final String ATT_FORM         = "Form";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String VUE_FORM         = "/WEB-INF/connexion.jsp";
    public static final String VUE_FICHE        = "/listeActions";

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private UtilisateurDAO     utilisateurDao;

    private static final long  serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Affichage de la page de connexion */
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /* Préparation de l'objet formulaire */
        ConnexionForm form = new ConnexionForm( utilisateurDao );
        /*
         * Appel au traitement et à la validation de la requête, et récupération
         * du bean en résultant
         */
        Utilisateur utilisateur = form.connecterUtilisateur( request );

        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();

        /*
         * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
         * Utilisateur à la session, sinon suppression du bean de la session.
         */
        if ( form.getErreurs().isEmpty() ) {
            session.setAttribute( ATT_SESSION_USER, utilisateur );
        } else {
            session.setAttribute( ATT_SESSION_USER, null );
        }

        /*
         * Stockage du formulaire et du bean dans l'objet request
         */
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, utilisateur );

        if ( form.getErreurs().isEmpty() ) {
            /* Si aucune erreur, alors affichage de la fiche d'exécution */
            response.sendRedirect( request.getContextPath() + VUE_FICHE );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }

}
