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

import cnesten.dao.DAOException;
import cnesten.dao.UtilisateurDAO;
import cnesten.entities.Utilisateur;

@WebServlet( "/suppressionUtilisateur" )
public class SuppressionUtilisateur extends HttpServlet {
    public static final String PARAM_CODE_UTILISATEUR = "codeUtilisateur";
    public static final String SESSION_UTILISATEURS   = "utilisateurs";
    public static final String VUE                    = "/listeUtilisateurs";

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    UtilisateurDAO             utilisateurDao;

    private static final long  serialVersionUID       = 1L;

    @SuppressWarnings( "unchecked" )
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Récupération du paramètre */
        Long codeUtilisateur = Long.parseLong( getValeurParametre( request, PARAM_CODE_UTILISATEUR ) );

        /*
         * Récupération de la Map des utilisateurs enregistrés en session
         */
        HttpSession session = request.getSession();
        Map<Long, Utilisateur> utilisateurs = (HashMap<Long, Utilisateur>) session
                .getAttribute( SESSION_UTILISATEURS );
        /*
         * Si le code et la Map des utilisateurs ne sont pas vides
         */
        if ( codeUtilisateur != null && utilisateurs != null ) {
            try {
                /* Alors suppression de l'utilisateur de la BDD */
                utilisateurDao.supprimer( utilisateurs.get( codeUtilisateur ) );
                /* Puis suppression de l'utilisateur de la Map */
                utilisateurs.remove( codeUtilisateur );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
            /* Et remplacement de l'ancienne Map en session par la nouvelle */
            session.setAttribute( SESSION_UTILISATEURS, utilisateurs );
        }
        /* Redirection vers la fiche récapitulative */
        response.sendRedirect( request.getContextPath() + VUE );
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

    }

    /*
     * Méthode utilitaire qui retourne null si un paramètre est vide, et son
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
