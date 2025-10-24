package org.learn.classes;

import javax.persistence.*;
import java.util.List;

@Entity
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private String prenom;
    private String telephone;

    // Relation : Un employé peut être chef de projet de plusieurs projets
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<Projet> projetsGeres;

    // Relation : Un employé participe à plusieurs tâches (via la table EmployeTache)
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<EmployeTache> tachesRealisees;

    // Constructeurs
    public Employe() {
    }

    public Employe(String nom, String prenom, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    // Getters et Setters (vous pouvez les générer automatiquement)
    // ...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public List<Projet> getProjetsGeres() { return projetsGeres; }
    public void setProjetsGeres(List<Projet> projetsGeres) { this.projetsGeres = projetsGeres; }
    public List<EmployeTache> getTachesRealisees() { return tachesRealisees; }
    public void setTachesRealisees(List<EmployeTache> tachesRealisees) { this.tachesRealisees = tachesRealisees; }
}
