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
import cnesten.dao.FicheDAO;
import cnesten.entities.Fiche;

@WebServlet( "/suppressionFiche" )
public class SuppressionFiche extends HttpServlet {
    public static final String PARAM_ID         = "idFiche";
    public static final String SESSION_FICHES   = "fiches";
    public static final String VUE              = "/listeFiches";

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private FicheDAO           ficheDao;

    private static final long  serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Récupération du paramètre */
        Long idfiche = Long.parseLong( getValeurParametre( request, PARAM_ID ) );

        /*
         * Récupération de la Map des fiches enregistrée en session
         */
        HttpSession session = request.getSession();
        Map<Long, Fiche> fiches = (HashMap<Long, Fiche>) session.getAttribute( SESSION_FICHES );

        /* Si le code et la Map des fiches ne sont pas vides */
        if ( idfiche != null && fiches != null ) {
            try {
                /* Alors suppression de la fiche de la BDD */
                ficheDao.supprimer( fiches.get( idfiche ) );
                /* Puis suppression de la fiche de la Map */
                fiches.remove( idfiche );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
            /* Et remplacement de l'ancienne Map en session par la nouvelle */
            session.setAttribute( SESSION_FICHES, fiches );
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
