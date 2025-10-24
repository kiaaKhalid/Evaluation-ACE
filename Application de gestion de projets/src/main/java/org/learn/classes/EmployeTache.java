package org.learn.classes;

import javax.persistence.*;

@Entity
public class EmployeTache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Employe employe;

    @ManyToOne
    private Tache tache;

    // Constructeurs
    public EmployeTache() {
    }

    public EmployeTache(Employe employe, Tache tache) {
        this.employe = employe;
        this.tache = tache;
    }

    // Getters et Setters
    // ...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }
    public Tache getTache() { return tache; }
    public void setTache(Tache tache) { this.tache = tache; }
}