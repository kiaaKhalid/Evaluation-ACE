package org.learn.beans;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Mariage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin; // Null si le mariage est en cours

    private int nbrEnfants;

    @ManyToOne
    private Homme homme;

    @ManyToOne
    private Femme femme;

    // Constructeurs
    public Mariage() {
    }

    // Constructeur pour un mariage (sans date de fin au début)
    public Mariage(Date dateDebut, int nbrEnfants, Homme homme, Femme femme) {
        this.dateDebut = dateDebut;
        this.nbrEnfants = nbrEnfants;
        this.homme = homme;
        this.femme = femme;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; } // Pour marquer un divorce
    public int getNbrEnfants() { return nbrEnfants; }
    public void setNbrEnfants(int nbrEnfants) { this.nbrEnfants = nbrEnfants; }
    public Homme getHomme() { return homme; }
    public void setHomme(Homme homme) { this.homme = homme; }
    public Femme getFemme() { return femme; }
    public void setFemme(Femme femme) { this.femme = femme; }
}
