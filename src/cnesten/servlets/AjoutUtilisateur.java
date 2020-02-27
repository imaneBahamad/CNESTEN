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
import cnesten.forms.AjoutUtilisateurForm;

@WebServlet( "/ajoutUtilisateur" )
public class AjoutUtilisateur extends HttpServlet {
    public static final String ATT_USER             = "Utilisateur";
    public static final String ATT_FORM             = "Form";
    public static final String SESSION_UTILISATEURS = "utilisateurs";
    public static final String VUE_FORM             = "/WEB-INF/ajouterUtilisateur.jsp";
    public static final String VUE_SUCCES           = "/listeUtilisateurs";

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private UtilisateurDAO     utilisateurDao;

    private static final long  serialVersionUID     = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Affichage du formulaire */
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    @SuppressWarnings( "unchecked" )
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /* Préparation de l'objet formulaire */
        AjoutUtilisateurForm form = new AjoutUtilisateurForm( utilisateurDao );
        /*
         * Appel au traitement et à la validation de la requête, et récupération
         * du bean en résultant
         */
        Utilisateur utilisateur = form.ajouterUtilisateur( request );
        /*
         * Stockage du formulaire et du bean dans l'objet request
         */
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, utilisateur );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {

            /*
             * Alors récupération de la map des utilisateurs dans la session
             */
            HttpSession session = request.getSession();
            Map<Long, Utilisateur> utilisateurs = (HashMap<Long, Utilisateur>) session
                    .getAttribute( SESSION_UTILISATEURS );

            /*
             * Si aucune map n'existe, alors initialisation d'une nouvelle map
             */
            if ( utilisateurs == null ) {
                utilisateurs = new HashMap<Long, Utilisateur>();
            }

            /* Puis ajout de l'utilisateur courant dans la map */
            utilisateurs.put( utilisateur.getCode_utilisateur(), utilisateur );
            /* Et enfin (ré)enregistrement de la map en session */
            session.setAttribute( SESSION_UTILISATEURS, utilisateurs );
        }

        if ( form.getErreurs().isEmpty() ) {
            /* Si aucune erreur, alors affichage de la liste des utilisateurs */
            response.sendRedirect( request.getContextPath() + VUE_SUCCES );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}
