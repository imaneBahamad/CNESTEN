package cnesten.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Fiche implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long              id_fiche;
    private String            nom_fiche;
    private byte[]            file_fiche;
    @OneToOne
    @JoinColumn( name = "id" )
    private Action            action;

    public Long getId_fiche() {
        return id_fiche;
    }

    public void setId_fiche( Long id_fiche ) {
        this.id_fiche = id_fiche;
    }

    public String getNom_fiche() {
        return nom_fiche;
    }

    public void setNom_fiche( String nom_fiche ) {
        this.nom_fiche = nom_fiche;
    }

    public byte[] getFile_fiche() {
        return file_fiche;
    }

    public void setFile_fiche( byte[] file_fiche ) {
        this.file_fiche = file_fiche;
    }

    public Action getAction() {
        return action;
    }

    public void setAction( Action action ) {
        this.action = action;
    }

}
