package org.learn.service;

import org.learn.classes.Projet;
import org.learn.classes.Tache;
import org.learn.dao.IDao;
import org.learn.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.text.SimpleDateFormat;
import java.util.List;

public class ProjetService implements IDao<Projet> {

    // ... CRUD (create, delete, update, findById, findAll) ...
    // (Similaire à EmployeService)
    @Override public boolean create(Projet o) { /* ... */ return false; }
    @Override public boolean delete(Projet o) { /* ... */ return false; }
    @Override public boolean update(Projet o) { /* ... */ return false; }
    @Override public Projet findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Projet.class, id);
        }
    }
    @Override public List<Projet> findAll() { /* ... */ return null; }

    // =========================================================================
    // MÉTHODES SPÉCIFIQUES
    // =========================================================================

    /**
     * Afficher la liste des tâches planifiées pour un projet.
     */
    public List<Tache> findTachesByProjet(Projet p) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Tache t WHERE t.projet = :projet";
            Query<Tache> query = session.createQuery(hql, Tache.class);
            query.setParameter("projet", p);
            return query.list();

            // Alternative (si la session reste ouverte et le FetchType est EAGER):
            // return p.getTaches();
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Afficher la liste des tâches réalisées avec les dates réelles (Format Spécifique).
     */
    public void afficherTachesReelles(Projet p) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Re-charger le projet pour s'assurer que les données sont fraîches
            Projet projet = session.get(Projet.class, p.getId());
            if (projet == null) {
                System.out.println("Projet non trouvé.");
                return;
            }

            // Formatage des dates
            SimpleDateFormat sdfProjet = new SimpleDateFormat("dd MMMM yyyy");
            SimpleDateFormat sdfTache = new SimpleDateFormat("dd/MM/yyyy");

            // Affichage En-tête
            System.out.println("------------------------------------------------------------------");
            System.out.println("Projet : " + projet.getId() + "\t Nom : " + projet.getNom() +
                    "\t Date début : " + sdfProjet.format(projet.getDateDebut()));
            System.out.println("Liste des tâches:");
            System.out.println("Num\t Nom\t\t Date Début Réelle\t Date Fin Réelle");
            System.out.println("------------------------------------------------------------------");

            // Récupérer les tâches du projet
            List<Tache> taches = projet.getTaches();

            if(taches.isEmpty()) {
                System.out.println(" (Aucune tâche pour ce projet)");
            } else {
                for(Tache t : taches) {
                    // On affiche seulement les tâches *réalisées* (celles avec une date de fin réelle)
                    if(t.getDateFinReelle() != null) {
                        String dateDebut = sdfTache.format(t.getDateDebutReelle());
                        String dateFin = sdfTache.format(t.getDateFinReelle());
                        System.out.println(t.getId() + "\t " + t.getNom() + "\t\t " + dateDebut + "\t\t " + dateFin);
                    }
                }
            }
            System.out.println("------------------------------------------------------------------");

        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
