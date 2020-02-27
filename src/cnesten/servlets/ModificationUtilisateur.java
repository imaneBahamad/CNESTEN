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
import cnesten.forms.ModificationUtilisateurForm;

@WebServlet( "/modificationUtilisateur" )
public class ModificationUtilisateur extends HttpServlet {
    public static final String PARAM_CODE_UTILISATEUR = "codeUtilisateur";
    public static final String ATT_USER               = "Utilisateur";
    public static final String ATT_FORM               = "Form";
    public static final String SESSION_UTILISATEURS   = "utilisateurs";
    public static final String UPDATE_UTILISATEUR     = "utilisateur";
    public static final String VUE_FORM               = "/WEB-INF/modifierUtilisateur.jsp";
    public static final String VUE_SUCCES             = "/listeUtilisateurs";

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private UtilisateurDAO     utilisateurDao;

    private static final long  serialVersionUID       = 1L;

    @SuppressWarnings( "unchecked" )
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* R�cup�ration du param�tre */
        Long codeUtilisateur = Long.parseLong( getValeurParametre( request, PARAM_CODE_UTILISATEUR ) );

        /*
         * R�cup�ration de la Map des utilisateurs enregistr�s en session
         */
        HttpSession session = request.getSession();
        Map<Long, Utilisateur> utilisateurs = (HashMap<Long, Utilisateur>) session
                .getAttribute( SESSION_UTILISATEURS );

        /* Si le code et la Map des utilisateurs ne sont pas vides */
        if ( codeUtilisateur != null && utilisateurs != null ) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setCode_utilisateur( codeUtilisateur );
            utilisateur.setNom( utilisateurs.get( codeUtilisateur ).getNom() );
            utilisateur.setPrenom( utilisateurs.get( codeUtilisateur ).getPrenom() );
            utilisateur.setEmail( utilisateurs.get( codeUtilisateur ).getEmail() );
            utilisateur.setPrivil�ge( utilisateurs.get( codeUtilisateur ).getPrivil�ge() );
            session.setAttribute( UPDATE_UTILISATEUR, utilisateur );

            /* Affichage du formulaire */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }

    @SuppressWarnings( "unchecked" )
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /* Pr�paration de l'objet formulaire */
        ModificationUtilisateurForm form = new ModificationUtilisateurForm( utilisateurDao );

        /* Traitement de la requ�te et r�cup�ration du bean en r�sultant */
        Utilisateur utilisateur = form.modifierUtilisateur( request );

        /*
         * Stockage du formulaire et du bean dans l'objet request
         */
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, utilisateur );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {

            /*
             * Alors r�cup�ration de la map des utilisateurs dans la session
             */
            HttpSession session = request.getSession();
            Map<Long, Utilisateur> utilisateurs = (HashMap<Long, Utilisateur>) session
                    .getAttribute( SESSION_UTILISATEURS );

            /* Puis ajout de l'utilisateur courant dans la map */
            utilisateurs.put( utilisateur.getCode_utilisateur(), utilisateur );

            /* Et enfin (r�)enregistrement de la map en session */
            session.setAttribute( SESSION_UTILISATEURS, utilisateurs );
        }

        if ( form.getErreurs().isEmpty() ) {

            /* Si aucune erreur, alors affichage de la liste des utilisateurs */
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
