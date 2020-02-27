package cnesten.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cnesten.entities.Utilisateur;;

@Stateless
public class UtilisateurDAO {
    private static final String JPQL_SELECT_PAR_EMAIL = "SELECT u FROM Utilisateur u WHERE u.email=:email";
    private static final String JPQL_SELECT_PAR_NOM   = "SELECT u FROM Utilisateur u WHERE u.nom=:nom";
    private static final String JPQL_UPDATE           = "UPDATE Utilisateur u SET u.nom=:nom,u.prenom=:prenom,u.email=:email,u.privilège=:privilège WHERE u.code_utilisateur=:code_utilisateur";
    private static final String PARAM_NOM             = "nom";
    private static final String PARAM_PRENOM          = "prenom";
    private static final String PARAM_EMAIL           = "email";
    private static final String PARAM_PRIVILEGE       = "privilège";
    private static final String PARAM_CODE            = "code_utilisateur";

    // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "cnesten" )
    private EntityManager       em;

    public void creer( Utilisateur utilisateur ) throws DAOException {
        try {
            em.persist( utilisateur );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public Utilisateur trouver_par_email( String email ) throws DAOException {
        Utilisateur utilisateur = null;
        Query requete = em.createQuery( JPQL_SELECT_PAR_EMAIL );
        requete.setParameter( PARAM_EMAIL, email );
        try {
            utilisateur = (Utilisateur) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return utilisateur;
    }

    public Utilisateur trouver_par_nom( String nom ) throws DAOException {
        Utilisateur utilisateur = null;
        Query requete = em.createQuery( JPQL_SELECT_PAR_NOM );
        requete.setParameter( PARAM_NOM, nom );
        try {
            utilisateur = (Utilisateur) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return utilisateur;
    }

    public Utilisateur trouver_par_code( Long code_utilisateur ) throws DAOException {
        try {
            return em.find( Utilisateur.class, code_utilisateur );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public List<Utilisateur> lister() throws DAOException {
        try {
            TypedQuery<Utilisateur> query = em.createQuery( "SELECT u FROM Utilisateur u ORDER BY u.code_utilisateur",
                    Utilisateur.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public void supprimer( Utilisateur utilisateur ) throws DAOException {
        try {
            em.remove( em.merge( utilisateur ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    public void modifier( Utilisateur utilisateur, String nom, String prenom, String email, String privilège,
            Long code )
            throws DAOException {
        try {
            Query requete = em.createQuery( JPQL_UPDATE, Utilisateur.class );
            requete.setParameter( PARAM_NOM, nom );
            requete.setParameter( PARAM_PRENOM, prenom );
            requete.setParameter( PARAM_EMAIL, email );
            requete.setParameter( PARAM_PRIVILEGE, privilège );
            requete.setParameter( PARAM_CODE, code );
            requete.executeUpdate();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

}
