package org.learn.Services;

import org.learn.beans.Mariage;
import org.learn.dao.IDao;
import org.learn.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class MariageService implements IDao<Mariage> {

    // ... Implémentation du CRUD (create, delete, update, findById, findAll) ...
    @Override
    public boolean create(Mariage o) {
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

    @Override public boolean delete(Mariage o) { /* ... */ return false; }

    @Override
    public boolean update(Mariage o) {
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

    @Override public Mariage findById(int id) { /* ... */ return null; }
    @Override public List<Mariage> findAll() { /* ... */ return null; }
}
