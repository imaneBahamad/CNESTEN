package cnesten.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Utilisateur implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long              code_utilisateur;
    private String            nom;
    private String            prenom;
    private String            email;
    private String            motdepasse;
    private String            privilège;

    public Long getCode_utilisateur() {
        return code_utilisateur;
    }

    public void setCode_utilisateur( Long code_utilisateur ) {
        this.code_utilisateur = code_utilisateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom( String prenom ) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse( String motdepasse ) {
        this.motdepasse = motdepasse;
    }

    public String getPrivilège() {
        return privilège;
    }

    public void setPrivilège( String privilège ) {
        this.privilège = privilège;
    }

}
