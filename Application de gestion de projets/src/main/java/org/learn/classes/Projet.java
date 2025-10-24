package org.learn.classes;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    // Relation : Un projet a un seul chef de projet
    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe; // Chef de projet

    // Relation : Un projet est décomposé en plusieurs tâches
    @OneToMany(mappedBy = "projet", fetch = FetchType.LAZY)
    private List<Tache> taches;

    // Constructeurs
    public Projet() {
    }

    public Projet(String nom, Date dateDebut, Employe employe) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.employe = employe;
    }

    // Getters et Setters
    // ...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }
    public List<Tache> getTaches() { return taches; }
    public void setTaches(List<Tache> taches) { this.taches = taches; }
}
