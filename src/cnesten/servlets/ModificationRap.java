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
import cnesten.forms.ModificationRapForm;

@MultipartConfig( fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 )
@WebServlet( "/modificationRap" )
public class ModificationRap extends HttpServlet {
    public static final String  PARAM_ID         = "idRap";
    public static final String  ATT_RAPPORT      = "Rapport";
    public static final String  ATT_FORM         = "Form";
    public static final String  SESSION_RAPPORTS = "rapports";
    public static final String  UPDATE_RAPPORT   = "rapport";
    private static final String VUE_FORM         = "/WEB-INF/modifierRap.jsp";
    public static final String  VUE_SUCCES       = "/listeRapports";

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private RapportDAO          rapportDao;
    @EJB
    private ActionDAO           actionDao;

    private static final long   serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Récupération du paramètre */
        Long id = Long.parseLong( getValeurParametre( request, PARAM_ID ) );

        /*
         * Récupération de la Map des rapports enregistrée en session
         */
        HttpSession session = request.getSession();
        Map<Long, Rapport> rapports = (HashMap<Long, Rapport>) session.getAttribute( SESSION_RAPPORTS );

        /* Si le code et la Map des rapports ne sont pas vides */
        if ( id != null && rapports != null ) {
            Rapport rapport = new Rapport();
            rapport.setId_rap( id );
            rapport.setAction( rapports.get( id ).getAction() );
            rapport.setNom_rap( rapports.get( id ).getNom_rap() );
            rapport.setFile_rap( rapports.get( id ).getFile_rap() );

            session.setAttribute( UPDATE_RAPPORT, rapport );

            /* Affichage du formulaire */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /* Préparation de l'objet formulaire */
        ModificationRapForm form = new ModificationRapForm( rapportDao, actionDao );

        /* Traitement de la requête et récupération du bean en résultant */
        Rapport rapport = form.modifierRap( request );

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( ATT_RAPPORT, rapport );
        request.setAttribute( ATT_FORM, form );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {
            /*
             * Alors récupération de la map des rapports dans la session
             */
            HttpSession session = request.getSession();
            Map<Long, Rapport> rapports = (HashMap<Long, Rapport>) session.getAttribute( SESSION_RAPPORTS );

            /* Puis ajout du rapport courant dans la map */
            rapports.put( rapport.getId_rap(), rapport );

            /* Et enfin (ré)enregistrement de la map en session */
            session.setAttribute( SESSION_RAPPORTS, rapports );
        }

        if ( form.getErreurs().isEmpty() ) {

            /* Si aucune erreur, alors affichage de la liste des Raps */
            response.sendRedirect( request.getContextPath() + VUE_SUCCES );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
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
