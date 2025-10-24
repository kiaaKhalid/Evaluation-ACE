package org.learn.service;


import org.learn.classes.Tache;
import org.learn.dao.IDao;
import org.learn.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.Date;
import java.util.List;

public class TacheService implements IDao<Tache> {

    // ... CRUD (create, delete, update, findById, findAll) ...
    // (Similaire à EmployeService)
    @Override public boolean create(Tache o) {
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
    @Override public boolean delete(Tache o) { /* ... */ return false; }
    @Override public boolean update(Tache o) {
        Session session = null; Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(o);
            tx.commit(); return true;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); return false;
        } finally {
            if (session != null) session.close();
        }
    }
    @Override public Tache findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Tache.class, id);
        }
    }
    @Override public List<Tache> findAll() { /* ... */ return null; }

    // =========================================================================
    // MÉTHODES SPÉCIFIQUES
    // =========================================================================

    /**
     * Afficher les tâches dont le prix est supérieur à 1000 DH (requête nommée).
     */
    public List<Tache> findTachesPrixSuperieur(double prixMin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Tache> query = session.createNamedQuery("Tache.findAbovePrice", Tache.class);
            query.setParameter("prixMin", prixMin);
            return query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Afficher les tâches réalisées (ayant une dateFinReelle) entre deux dates.
     */
    public List<Tache> findTachesEntreDeuxDates(Date dateDebut, Date dateFin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Tache t WHERE t.dateFinReelle BETWEEN :dateDebut AND :dateFin";
            Query<Tache> query = session.createQuery(hql, Tache.class);
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            return query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }
}
