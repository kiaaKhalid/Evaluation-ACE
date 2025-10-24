package org.learn.service;


import org.learn.classes.Employe;
import org.learn.classes.Projet;
import org.learn.classes.Tache;
import org.learn.dao.IDao;
import org.learn.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class EmployeService implements IDao<Employe> {

    @Override
    public boolean create(Employe o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean delete(Employe o) {
        // ... (Même logique que create, mais avec session.delete(o)) ...
        return false;
    }

    @Override
    public boolean update(Employe o) {
        // ... (Même logique que create, mais avec session.update(o)) ...
        return false;
    }

    @Override
    public Employe findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Employe.class, id);
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Employe> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Employe> query = session.createQuery("FROM Employe", Employe.class);
            return query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    // =========================================================================
    // MÉTHODES SPÉCIFIQUES
    // =========================================================================

    /**
     * Afficher la liste des tâches réalisées par un employé.
     */
    public List<Tache> findTachesByEmploye(Employe e) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // On utilise HQL pour joindre EmployeTache et Tache
            String hql = "SELECT et.tache FROM EmployeTache et WHERE et.employe = :employe";
            Query<Tache> query = session.createQuery(hql, Tache.class);
            query.setParameter("employe", e);
            return query.list();
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Afficher la liste des projets gérés par un employé.
     */
    public List<Projet> findProjetsByEmploye(Employe e) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // On utilise HQL pour récupérer les projets où l'employé est le manager
            String hql = "FROM Projet p WHERE p.employe = :employe";
            Query<Projet> query = session.createQuery(hql, Projet.class);
            query.setParameter("employe", e);
            return query.list();

            // Alternative (si la session reste ouverte et le FetchType est EAGER):
            // return e.getProjetsGeres();
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
