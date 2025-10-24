package org.learn.service;


import org.learn.classes.EmployeTache;
import org.learn.dao.IDao;
import org.learn.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class EmployeTacheService implements IDao<EmployeTache> {

    // ... CRUD (create, delete, update, findById, findAll) ...
    @Override public boolean create(EmployeTache o) {
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
    @Override public boolean delete(EmployeTache o) { /* ... */ return false; }
    @Override public boolean update(EmployeTache o) { /* ... */ return false; }
    @Override public EmployeTache findById(int id) { /* ... */ return null; }
    @Override public List<EmployeTache> findAll() { /* ... */ return null; }
}
