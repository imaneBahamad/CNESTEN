package cnesten.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Action implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long              id;
    private String            nom_action;
    private String            nom_proc;
    private byte[]            procédure;
    private String            nom_chlist;
    private byte[]            checklist;
    private int               année;
    private String            lce;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getNom_action() {
        return nom_action;
    }

    public void setNom_action( String nom_action ) {
        this.nom_action = nom_action;
    }

    public String getNom_proc() {
        return nom_proc;
    }

    public void setNom_proc( String nom_proc ) {
        this.nom_proc = nom_proc;
    }

    public byte[] getProcédure() {
        return procédure;
    }

    public void setProcédure( byte[] procédure ) {
        this.procédure = procédure;
    }

    public String getNom_chlist() {
        return nom_chlist;
    }

    public void setNom_chlist( String nom_chlist ) {
        this.nom_chlist = nom_chlist;
    }

    public byte[] getChecklist() {
        return checklist;
    }

    public void setChecklist( byte[] checklist ) {
        this.checklist = checklist;
    }

    public int getAnnée() {
        return année;
    }

    public void setAnnée( int année ) {
        this.année = année;
    }

    public String getLce() {
        return lce;
    }

    public void setLce( String lce ) {
        this.lce = lce;
    }

}
