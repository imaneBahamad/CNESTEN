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
import cnesten.dao.RapportDAO;
import cnesten.entities.Rapport;

@WebServlet( "/suppressionRap" )
public class SuppressionRap extends HttpServlet {
    public static final String PARAM_ID         = "idRap";
    public static final String SESSION_RAPPORTS = "rapports";
    public static final String VUE              = "/listeRapports";

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private RapportDAO         rapportDao;

    private static final long  serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Récupération du paramètre */
        Long idrap = Long.parseLong( getValeurParametre( request, PARAM_ID ) );

        /*
         * Récupération de la Map des rapports enregistrée en session
         */
        HttpSession session = request.getSession();
        Map<Long, Rapport> rapports = (HashMap<Long, Rapport>) session.getAttribute( SESSION_RAPPORTS );

        /* Si le code et la Map des rapports ne sont pas vides */
        if ( idrap != null && rapports != null ) {
            try {
                /* Alors suppression du rapport de la BDD */
                rapportDao.supprimer( rapports.get( idrap ) );
                /* Puis suppression du rapport de la Map */
                rapports.remove( idrap );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
            /* Et remplacement de l'ancienne Map en session par la nouvelle */
            session.setAttribute( SESSION_RAPPORTS, rapports );
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
