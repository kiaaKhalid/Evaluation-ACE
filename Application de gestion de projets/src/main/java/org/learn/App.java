package org.learn;

import java.text.SimpleDateFormat;

import org.learn.classes.*;
import org.learn.service.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class App 
{
    public static void main( String[] args )
    {

        // Formatteur de date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Initialisation des services
            EmployeService es = new EmployeService();
            ProjetService ps = new ProjetService();
            TacheService ts = new TacheService();
            EmployeTacheService ets = new EmployeTacheService();

            // =========================================================
            // 1. Création des données
            // =========================================================

            // Employés (dont 1 chef de projet)
            Employe chefProjet = new Employe("El Alaoui", "Ahmed", "0611111111");
            Employe dev1 = new Employe("Bennani", "Fatima", "0622222222");
            es.create(chefProjet);
            es.create(dev1);

            // Projet (géré par Ahmed)
            Projet projet1 = new Projet("Gestion de stock", sdf.parse("2013-01-14"), chefProjet);
            ps.create(projet1);

            // Tâches pour ce projet
            Tache t1 = new Tache("Analyse", sdf.parse("2013-02-01"), sdf.parse("2013-02-28"), 1500.0, projet1); // > 1000
            Tache t2 = new Tache("Conception", sdf.parse("2013-03-01"), sdf.parse("2013-03-31"), 800.0, projet1);  // < 1000
            Tache t3 = new Tache("Développement", sdf.parse("2013-04-01"), sdf.parse("2013-04-30"), 2500.0, projet1); // > 1000

            ts.create(t1);
            ts.create(t2);
            ts.create(t3);

            // Assigner des employés aux tâches
            ets.create(new EmployeTache(dev1, t1));
            ets.create(new EmployeTache(dev1, t2));
            ets.create(new EmployeTache(chefProjet, t3)); // Le chef de projet participe aussi

            // Mettre à jour les tâches avec les dates réelles (pour simuler la fin)
            t1.setDateDebutReelle(sdf.parse("2013-02-10"));
            t1.setDateFinReelle(sdf.parse("2013-02-20")); // Tâche finie
            ts.update(t1);

            t2.setDateDebutReelle(sdf.parse("2013-03-10"));
            // Pas de date de fin réelle (tâche en cours)
            ts.update(t2);

            t3.setDateDebutReelle(sdf.parse("2013-04-10"));
            t3.setDateFinReelle(sdf.parse("2013-04-25")); // Tâche finie
            ts.update(t3);


            // =========================================================
            // 2. Tests des méthodes
            // =========================================================

            System.out.println("\n#### Test ProjetService : Affichage des tâches terminées ####");
            // Devrait afficher T1 (Analyse) et T3 (Développement)
            ps.afficherTachesReelles(projet1);

            System.out.println("\n#### Test EmployeService : Projets gérés par Ahmed (Chef) ####");
            List<Projet> projetsAhmed = es.findProjetsByEmploye(chefProjet);
            for(Projet p : projetsAhmed) {
                System.out.println(" - Projet: " + p.getNom());
            }

            System.out.println("\n#### Test EmployeService : Tâches réalisées par Fatima (Dev1) ####");
            List<Tache> tachesFatima = es.findTachesByEmploye(dev1);
            for(Tache t : tachesFatima) {
                System.out.println(" - Tâche: " + t.getNom());
            }

            System.out.println("\n#### Test TacheService : Tâches > 1000 DH (Requête Nommée) ####");
            List<Tache> tachesCheres = ts.findTachesPrixSuperieur(1000.0);
            for(Tache t : tachesCheres) {
                System.out.println(" - " + t.getNom() + " | Prix: " + t.getPrix());
            }

            System.out.println("\n#### Test TacheService : Tâches terminées en Février 2013 ####");
            Date debut = sdf.parse("2013-02-01");
            Date fin = sdf.parse("2013-02-28");
            List<Tache> tachesFevrier = ts.findTachesEntreDeuxDates(debut, fin);
            for(Tache t : tachesFevrier) {
                System.out.println(" - " + t.getNom() + " | Date Fin: " + sdf.format(t.getDateFinReelle()));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
