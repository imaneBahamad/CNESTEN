package cnesten.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Rapport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long              id_rap;
    private String            nom_rap;
    private byte[]            file_rap;
    @OneToOne
    @JoinColumn( name = "id" )
    private Action            action;

    public Long getId_rap() {
        return id_rap;
    }

    public void setId_rap( Long id_rap ) {
        this.id_rap = id_rap;
    }

    public String getNom_rap() {
        return nom_rap;
    }

    public void setNom_rap( String nom_rap ) {
        this.nom_rap = nom_rap;
    }

    public byte[] getFile_rap() {
        return file_rap;
    }

    public void setFile_rap( byte[] file_rap ) {
        this.file_rap = file_rap;
    }

    public Action getAction() {
        return action;
    }

    public void setAction( Action action ) {
        this.action = action;
    }

}
