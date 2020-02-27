package cnesten.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cnesten.dao.ActionDAO;
import cnesten.dao.FicheDAO;
import cnesten.dao.RapportDAO;
import cnesten.dao.UtilisateurDAO;
import cnesten.entities.Action;
import cnesten.entities.Fiche;
import cnesten.entities.Rapport;
import cnesten.entities.Utilisateur;

@WebFilter( urlPatterns = "/*" )
public class PrechargementFilter implements Filter {
    public static final String ATT_SESSION_ACTIONS      = "actions";
    public static final String ATT_SESSION_RAPPORTS     = "rapports";
    public static final String ATT_SESSION_FICHES       = "fiches";
    public static final String ATT_SESSION_UTILISATEURS = "utilisateurs";

    @EJB
    private ActionDAO          actionDao;
    @EJB
    private RapportDAO         rapportDao;
    @EJB
    private FicheDAO           ficheDao;
    @EJB
    private UtilisateurDAO     utilisateurDao;

    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain )
            throws IOException, ServletException {

        /* Cast de l'objet request */
        HttpServletRequest request = (HttpServletRequest) req;

        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();
        /*
         * Si les map n'existent pas en session, alors l'utilisateur se connecte
         * pour la première fois et nous devons précharger en session les infos
         * contenues dans la BDD.
         */
        if ( session.getAttribute( ATT_SESSION_ACTIONS ) == null ) {
            /*
             * Récupération de la liste des actions existantes, et
             * enregistrement en session
             */
            List<Action> listeActions = actionDao.lister();
            Map<Long, Action> mapActions = new HashMap<Long, Action>();
            for ( Action action : listeActions ) {
                mapActions.put( action.getId(), action );
            }
            session.setAttribute( ATT_SESSION_ACTIONS, mapActions );
        }

        if ( session.getAttribute( ATT_SESSION_RAPPORTS ) == null ) {
            /*
             * Récupération de la liste des rapports existants, et
             * enregistrement en session
             */
            List<Rapport> listeRapports = rapportDao.lister();
            Map<Long, Rapport> mapRapports = new HashMap<Long, Rapport>();
            for ( Rapport rapport : listeRapports ) {
                mapRapports.put( rapport.getId_rap(), rapport );
            }
            session.setAttribute( ATT_SESSION_RAPPORTS, mapRapports );
        }

        if ( session.getAttribute( ATT_SESSION_FICHES ) == null ) {
            /*
             * Récupération de la liste des fiches existantes, et enregistrement
             * en session
             */
            List<Fiche> listeFiches = ficheDao.lister();
            Map<Long, Fiche> mapFiches = new HashMap<Long, Fiche>();
            for ( Fiche fiche : listeFiches ) {
                mapFiches.put( fiche.getId_fiche(), fiche );
            }
            session.setAttribute( ATT_SESSION_FICHES, mapFiches );
        }

        if ( session.getAttribute( ATT_SESSION_UTILISATEURS ) == null ) {
            /*
             * Récupération de la liste des utilisateurs existants, et
             * enregistrement en session
             */
            List<Utilisateur> listeUtilisateurs = utilisateurDao.lister();
            Map<Long, Utilisateur> mapUtilisateurs = new HashMap<Long, Utilisateur>();
            for ( Utilisateur utilisateur : listeUtilisateurs ) {
                mapUtilisateurs.put( utilisateur.getCode_utilisateur(), utilisateur );
            }
            session.setAttribute( ATT_SESSION_UTILISATEURS, mapUtilisateurs );
        }

        /* Pour terminer, poursuite de la requête en cours */
        chain.doFilter( request, res );

    }

    public void destroy() {
    }

}
