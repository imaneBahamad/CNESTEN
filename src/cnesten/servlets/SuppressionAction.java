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
import cnesten.dao.DAOException;
import cnesten.entities.Action;

@WebServlet( "/suppressionAction" )
public class SuppressionAction extends HttpServlet {
    public static final String PARAM_ID         = "idAction";
    public static final String SESSION_ACTIONS  = "actions";
    public static final String VUE              = "/listeActions";

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private ActionDAO          actionDao;

    private static final long  serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Récupération du paramètre */
        Long id = Long.parseLong( getValeurParametre( request, PARAM_ID ) );

        /*
         * Récupération de la Map des Actions enregistrés en session
         */
        HttpSession session = request.getSession();
        Map<Long, Action> actions = (HashMap<Long, Action>) session.getAttribute( SESSION_ACTIONS );

        /* Si le code et la Map des Actions ne sont pas vides */
        if ( id != null && actions != null ) {
            try {
                /* Alors suppression du Action de la BDD */
                actionDao.supprimer( actions.get( id ) );
                /* Puis suppression du Action de la Map */
                actions.remove( id );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
            /* Et remplacement de l'ancienne Map en session par la nouvelle */
            session.setAttribute( SESSION_ACTIONS, actions );
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
