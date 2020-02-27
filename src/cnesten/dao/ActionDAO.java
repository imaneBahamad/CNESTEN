package cnesten.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cnesten.entities.Action;

@Stateless
public class ActionDAO {
    private static final String JPQL_SELECT         = "SELECT a FROM Action a WHERE a.nom_action=:nom_action and a.année=:année";
    private static final String JPQL_SELECT_PAR_NOM = "SELECT a FROM Action a WHERE a.nom_action=:nom_action";
    private static final String JPQL_UPDATE         = "UPDATE Action a SET a.nom_action=:nom_action,a.nom_proc=:nom_proc,a.procédure=:procédure,a.nom_chlist=:nom_chlist,a.checklist=:checklist,a.année=:année,a.lce=:lce WHERE a.id=:id";
    private static final String PARAM_NOM           = "nom_action";
    private static final String PARAM_NOM_PROC      = "nom_proc";
    private static final String PARAM_PROCEDURE     = "procédure";
    private static final String PARAM_NOM_CHLIST    = "nom_chlist";
    private static final String PARAM_CHECKLIST     = "checklist";
    private static final String PARAM_ANNEE         = "année";
    private static final String PARAM_LCE           = "lce";
    private static final String PARAM_ID            = "id";

    // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "cnesten" )
    private EntityManager       em;

    public void creer( Action action ) throws DAOException {
        try {
            em.persist( action );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public byte[] get_proc( Long id ) throws DAOException {
        try {
            return em.find( Action.class, id ).getProcédure();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public byte[] get_check( Long id ) throws DAOException {
        try {
            return em.find( Action.class, id ).getChecklist();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public Action trouver( String nom, int année ) throws DAOException {
        Action action = null;
        Query requete = em.createQuery( JPQL_SELECT );
        requete.setParameter( PARAM_NOM, nom );
        requete.setParameter( PARAM_ANNEE, année );

        try {
            action = (Action) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return action;
    }

    public Action trouver_par_nom( String nom ) throws DAOException {
        Action action = null;
        Query requete = em.createQuery( JPQL_SELECT_PAR_NOM );
        requete.setParameter( PARAM_NOM, nom );
        try {
            action = (Action) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return action;
    }

    public List<Action> lister() throws DAOException {
        try {
            TypedQuery<Action> query = em.createQuery( "SELECT a FROM Action a",
                    Action.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public void supprimer( Action action ) throws DAOException {
        try {
            em.remove( em.merge( action ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public void modifier( Action action, String nom_action, String nom_proc, byte[] procédure, String nom_chlist,
            byte[] checklist, int année, String lce, Long id )
            throws DAOException {
        try {
            Query requete = em.createQuery( JPQL_UPDATE, Action.class );
            requete.setParameter( PARAM_NOM, nom_action );
            requete.setParameter( PARAM_NOM_PROC, nom_proc );
            requete.setParameter( PARAM_PROCEDURE, procédure );
            requete.setParameter( PARAM_NOM_CHLIST, nom_chlist );
            requete.setParameter( PARAM_CHECKLIST, checklist );
            requete.setParameter( PARAM_ANNEE, année );
            requete.setParameter( PARAM_LCE, lce );
            requete.setParameter( PARAM_ID, id );

            requete.executeUpdate();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

}
