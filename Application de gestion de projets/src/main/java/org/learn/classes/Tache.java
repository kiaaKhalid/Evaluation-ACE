package org.learn.classes;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@NamedQuery(name = "Tache.findAbovePrice", query = "FROM Tache t WHERE t.prix > :prixMin")
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;

    @Temporal(TemporalType.DATE)
    private Date dateDebutPlanifiee;

    @Temporal(TemporalType.DATE)
    private Date dateFinPlanifiee;

    @Temporal(TemporalType.DATE)
    private Date dateDebutReelle; // Pour l'affichage de ProjetService

    @Temporal(TemporalType.DATE)
    private Date dateFinReelle; // Pour l'affichage de ProjetService

    private double prix;

    // Relation : Une tâche appartient à un projet
    @ManyToOne
    private Projet projet;

    // Relation : Plusieurs employés peuvent être assignés à une tâche
    @OneToMany(mappedBy = "tache", fetch = FetchType.LAZY)
    private List<EmployeTache> employesParticipants;

    // Constructeurs
    public Tache() {
    }

    public Tache(String nom, Date dateDebutPlanifiee, Date dateFinPlanifiee, double prix, Projet projet) {
        this.nom = nom;
        this.dateDebutPlanifiee = dateDebutPlanifiee;
        this.dateFinPlanifiee = dateFinPlanifiee;
        this.prix = prix;
        this.projet = projet;
    }

    // Getters et Setters (dateDebutReelle et dateFinReelle sont importants)
    // ...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Date getDateDebutPlanifiee() { return dateDebutPlanifiee; }
    public void setDateDebutPlanifiee(Date dateDebutPlanifiee) { this.dateDebutPlanifiee = dateDebutPlanifiee; }
    public Date getDateFinPlanifiee() { return dateFinPlanifiee; }
    public void setDateFinPlanifiee(Date dateFinPlanifiee) { this.dateFinPlanifiee = dateFinPlanifiee; }
    public Date getDateDebutReelle() { return dateDebutReelle; }
    public void setDateDebutReelle(Date dateDebutReelle) { this.dateDebutReelle = dateDebutReelle; }
    public Date getDateFinReelle() { return dateFinReelle; }
    public void setDateFinReelle(Date dateFinReelle) { this.dateFinReelle = dateFinReelle; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public Projet getProjet() { return projet; }
    public void setProjet(Projet projet) { this.projet = projet; }
    public List<EmployeTache> getEmployesParticipants() { return employesParticipants; }
    public void setEmployesParticipants(List<EmployeTache> employesParticipants) { this.employesParticipants = employesParticipants; }
}
