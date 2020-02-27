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
import cnesten.forms.ModificationFicheForm;

@MultipartConfig( fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 )
@WebServlet( "/modificationFiche" )
public class ModificationFiche extends HttpServlet {
    public static final String  PARAM_ID         = "idFiche";
    public static final String  ATT_FICHE        = "Fiche";
    public static final String  ATT_FORM         = "Form";
    public static final String  SESSION_FICHES   = "fiches";
    public static final String  UPDATE_FICHE     = "fiche";
    private static final String VUE_FORM         = "/WEB-INF/modifierFiche.jsp";
    public static final String  VUE_SUCCES       = "/listeFiches";

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private FicheDAO            ficheDao;
    @EJB
    private ActionDAO           actionDao;

    private static final long   serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* R�cup�ration du param�tre */
        Long id = Long.parseLong( getValeurParametre( request, PARAM_ID ) );

        /*
         * R�cup�ration de la Map des fiches enregistr�e en session
         */
        HttpSession session = request.getSession();
        Map<Long, Fiche> fiches = (HashMap<Long, Fiche>) session.getAttribute( SESSION_FICHES );

        /* Si le code et la Map des fiches ne sont pas vides */
        if ( id != null && fiches != null ) {
            Fiche fiche = new Fiche();
            fiche.setId_fiche( id );
            fiche.setAction( fiches.get( id ).getAction() );
            fiche.setNom_fiche( fiches.get( id ).getNom_fiche() );
            fiche.setFile_fiche( fiches.get( id ).getFile_fiche() );

            session.setAttribute( UPDATE_FICHE, fiche );

            /* Affichage du formulaire */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /* Pr�paration de l'objet formulaire */
        ModificationFicheForm form = new ModificationFicheForm( ficheDao, actionDao );

        /* Traitement de la requ�te et r�cup�ration du bean en r�sultant */
        Fiche fiche = form.modifierFiche( request );

        /* Ajout du bean et de l'objet m�tier � l'objet requ�te */
        request.setAttribute( ATT_FICHE, fiche );
        request.setAttribute( ATT_FORM, form );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {
            /*
             * Alors r�cup�ration de la map des fiches dans la session
             */
            HttpSession session = request.getSession();
            Map<Long, Fiche> fiches = (HashMap<Long, Fiche>) session.getAttribute( SESSION_FICHES );

            /* Puis ajout de la fiche courante dans la map */
            fiches.put( fiche.getId_fiche(), fiche );

            /* Et enfin (r�)enregistrement de la map en session */
            session.setAttribute( SESSION_FICHES, fiches );
        }

        if ( form.getErreurs().isEmpty() ) {

            /* Si aucune erreur, alors affichage de la liste des fiches */
            response.sendRedirect( request.getContextPath() + VUE_SUCCES );
        } else {
            /* Sinon, r�-affichage du formulaire de cr�ation avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }

    /*
     * M�thode utilitaire qui retourne null si un param�tre est vide, et son
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
