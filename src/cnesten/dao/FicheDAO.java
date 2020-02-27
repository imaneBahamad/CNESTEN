package cnesten.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cnesten.entities.Action;
import cnesten.entities.Fiche;

@Stateless
public class FicheDAO {
    private static final String JPQL_SELECT_PAR_ACTION = "SELECT r FROM Fiche r WHERE r.action=:action";
    private static final String JPQL_UPDATE            = "UPDATE Fiche f SET f.action=:action,f.nom_fiche=:nom_fiche,f.file_fiche=:file_fiche WHERE f.id_fiche=:id_fiche";
    private static final String PARAM_ACTION           = "action";
    private static final String PARAM_NOM              = "nom_fiche";
    private static final String PARAM_FILE             = "file_fiche";
    private static final String PARAM_ID               = "id_fiche";

    // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "cnesten" )
    private EntityManager       em;

    public void creer( Fiche fiche ) throws DAOException {
        try {
            em.persist( fiche );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public byte[] get_fiche( Long id_fiche ) throws DAOException {
        try {
            return em.find( Fiche.class, id_fiche ).getFile_fiche();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public Fiche trouver_par_action( Action action ) throws DAOException {
        Fiche fiche = null;
        Query requete = em.createQuery( JPQL_SELECT_PAR_ACTION );
        requete.setParameter( PARAM_ACTION, action );
        try {
            fiche = (Fiche) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return fiche;
    }

    public List<Fiche> lister() throws DAOException {
        try {
            TypedQuery<Fiche> query = em.createQuery( "SELECT r FROM Fiche r",
                    Fiche.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public void supprimer( Fiche fiche ) throws DAOException {
        try {
            em.remove( em.merge( fiche ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public void modifier( Fiche fiche, Action action, String nom_fiche, byte[] file_fiche, Long id_fiche )
            throws DAOException {
        try {
            Query requete = em.createQuery( JPQL_UPDATE, Fiche.class );
            requete.setParameter( PARAM_ACTION, action );
            requete.setParameter( PARAM_NOM, nom_fiche );
            requete.setParameter( PARAM_FILE, file_fiche );
            requete.setParameter( PARAM_ID, id_fiche );

            requete.executeUpdate();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
