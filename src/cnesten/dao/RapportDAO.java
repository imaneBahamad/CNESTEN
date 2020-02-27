package cnesten.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cnesten.entities.Action;
import cnesten.entities.Rapport;

@Stateless
public class RapportDAO {
    private static final String JPQL_SELECT_PAR_ACTION = "SELECT r FROM Rapport r WHERE r.action=:action";
    private static final String JPQL_UPDATE            = "UPDATE Rapport r SET r.action=:action,r.nom_rap=:nom_rap,r.file_rap=:file_rap WHERE r.id_rap=:id_rap";
    private static final String PARAM_ACTION           = "action";
    private static final String PARAM_NOM              = "nom_rap";
    private static final String PARAM_FILE             = "file_rap";
    private static final String PARAM_ID               = "id_rap";

    // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "cnesten" )
    private EntityManager       em;

    public void creer( Rapport rapport ) throws DAOException {
        try {
            em.persist( rapport );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public byte[] get_rap( Long id_rap ) throws DAOException {
        try {
            return em.find( Rapport.class, id_rap ).getFile_rap();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public Rapport trouver_par_action( Action action ) throws DAOException {
        Rapport rapport = null;
        Query requete = em.createQuery( JPQL_SELECT_PAR_ACTION );
        requete.setParameter( PARAM_ACTION, action );
        try {
            rapport = (Rapport) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return rapport;
    }

    public List<Rapport> lister() throws DAOException {
        try {
            TypedQuery<Rapport> query = em.createQuery( "SELECT r FROM Rapport r",
                    Rapport.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public void supprimer( Rapport rapport ) throws DAOException {
        try {
            em.remove( em.merge( rapport ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public void modifier( Rapport rapport, Action action, String nom_rap, byte[] file_rap, Long id_rap )
            throws DAOException {
        try {
            Query requete = em.createQuery( JPQL_UPDATE, Rapport.class );
            requete.setParameter( PARAM_ACTION, action );
            requete.setParameter( PARAM_NOM, nom_rap );
            requete.setParameter( PARAM_FILE, file_rap );
            requete.setParameter( PARAM_ID, id_rap );

            requete.executeUpdate();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

}
