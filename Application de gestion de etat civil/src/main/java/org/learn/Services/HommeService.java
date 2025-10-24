package org.learn.Services;


import org.learn.beans.Femme;
import org.learn.beans.Homme;
import org.learn.beans.Mariage;
import org.learn.dao.IDao;
import org.learn.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HommeService implements IDao<Homme> {

    // ... Implémentation du CRUD (create, delete, update, findById, findAll) ...
    // (Similaire à l'exercice précédent)

    @Override
    public boolean create(Homme o) {
        Session session = null; Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(o);
            tx.commit(); return true;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override public boolean delete(Homme o) { /* ... */ return false; }
    @Override public boolean update(Homme o) { /* ... */ return false; }
    @Override public Homme findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Homme.class, id);
        }
    }
    @Override public List<Homme> findAll() { /* ... */ return null; }


    // =========================================================================
    // MÉTHODES SPÉCIFIQUES
    // =========================================================================

    /**
     * 1. Affiche les épouses d'un homme (mariages actifs) entre deux dates.
     */
    public List<Femme> findEpousesEntreDates(Homme homme, Date dateDebut, Date dateFin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT m.femme FROM Mariage m " +
                    "WHERE m.homme = :homme " +
                    "AND m.dateDebut <= :dateFin " + // Le mariage a commencé avant la fin de la période
                    "AND (m.dateFin IS NULL OR m.dateFin >= :dateDebut)"; // Le mariage est en cours OU s'est terminé après le début de la période

            Query<Femme> query = session.createQuery(hql, Femme.class);
            query.setParameter("homme", homme);
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            return query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 2. Affiche les mariages d'un homme (formaté).
     */
    public void afficherMariages(Homme homme) {
        // Re-charger l'homme dans une session pour éviter LazyInitializationException
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Homme h = session.get(Homme.class, homme.getId());
            if (h == null) return;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("-------------------------------------------------");
            System.out.println("Nom : " + h.getNom().toUpperCase() + " " + h.getPrenom().toUpperCase());

            System.out.println("\nMariages En Cours :");
            int countEnCours = 1;
            int countEchoues = 1;

            for (Mariage m : h.getMariages()) {
                if (m.getDateFin() == null) {
                    Femme f = m.getFemme();
                    System.out.println(countEnCours + ". Femme : " + f.getNom().toUpperCase() + " " + f.getPrenom() +
                            "\t Date Début : " + sdf.format(m.getDateDebut()) +
                            "\t Nbr Enfants : " + m.getNbrEnfants());
                    countEnCours++;
                }
            }
            if (countEnCours == 1) System.out.println(" (Aucun)");

            System.out.println("\nMariages échoués :");
            for (Mariage m : h.getMariages()) {
                if (m.getDateFin() != null) {
                    Femme f = m.getFemme();
                    System.out.println(countEchoues + ". Femme : " + f.getNom().toUpperCase() + " " + f.getPrenom() +
                            "\t Date Début : " + sdf.format(m.getDateDebut()) +
                            "\t Date Fin : " + sdf.format(m.getDateFin()) +
                            "\t Nbr Enfants : " + m.getNbrEnfants());
                    countEchoues++;
                }
            }
            if (countEchoues == 1) System.out.println(" (Aucun)");
            System.out.println("-------------------------------------------------");

        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
