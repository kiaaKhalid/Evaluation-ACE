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

import javax.persistence.criteria.*; // Pour Criteria API
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FemmeService implements IDao<Femme> {

    // ... Implémentation du CRUD (create, delete, update, findById) ...
    @Override
    public boolean create(Femme o) {
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

    @Override public boolean delete(Femme o) { /* ... */ return false; }
    @Override public boolean update(Femme o) { /* ... */ return false; }
    @Override public Femme findById(int id) { /* ... */ return null; }

    @Override
    public List<Femme> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Femme", Femme.class).list();
        }
    }

    // =========================================================================
    // MÉTHODES SPÉCIFIQUES
    // =========================================================================

    /**
     * 1. (Test) Femme la plus âgée.
     */
    public Femme findFemmePlusAgee() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // ASC = Ascendant, la date la plus ancienne (donc la personne la plus âgée)
            Query<Femme> query = session.createQuery("FROM Femme ORDER BY dateNaissance ASC", Femme.class);
            query.setMaxResults(1); // On ne veut que la première
            return query.uniqueResult();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 2. (Native Query) Nombre d'enfants d'une femme pour les mariages commencés entre 2 dates.
     */
    public long countEnfants(Femme femme, Date dateDebut, Date dateFin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.getNamedNativeQuery("Femme.countEnfants");
            query.setParameter("femmeId", femme.getId());
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);

            // Les requêtes SUM() en SQL natif retournent souvent BigDecimal ou Double
            Object result = query.uniqueResult();
            if (result == null) {
                return 0;
            } else if (result instanceof BigDecimal) {
                return ((BigDecimal) result).longValue();
            } else if (result instanceof Number) {
                return ((Number) result).longValue();
            }
            return 0;
        } catch (HibernateException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 3. (HQL Named Query) Femmes mariées au moins deux fois.
     */
    public List<Femme> findFemmesMarieesDeuxFois() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Femme> query = session.createNamedQuery("Femme.marriedMultipleTimes", Femme.class);
            return query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 4. (Criteria API) Nombre d'hommes mariés à 4 femmes (ou plus)
     * pendant une période donnée (mariages actifs).
     */
    public long countHommesMarriesQuatreFemmes(Date dateDebut, Date dateFin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);

            Root<Homme> homme = cq.from(Homme.class);
            Join<Homme, Mariage> mariage = homme.join("mariages", JoinType.INNER);

            // Prédicat 1: Le mariage a commencé AVANT la fin de la période
            Predicate pDateDebut = cb.lessThanOrEqualTo(mariage.get("dateDebut"), dateFin);

            // Prédicat 2: Le mariage est en cours (dateFin IS NULL)
            Predicate pDateFinNull = cb.isNull(mariage.get("dateFin"));

            // Prédicat 3: OU Le mariage s'est terminé APRES le début de la période
            Predicate pDateFinAfter = cb.greaterThanOrEqualTo(mariage.get("dateFin"), dateDebut);

            // Combinaison des filtres de date (P1 AND (P2 OR P3))
            Predicate pDateFilter = cb.and(pDateDebut, cb.or(pDateFinNull, pDateFinAfter));

            // Appliquer le filtre de date
            cq.where(pDateFilter);

            // Grouper par Homme
            cq.groupBy(homme.get("id"));

            // Condition HAVING: Compter les mariages par homme >= 4
            cq.having(cb.greaterThanOrEqualTo(cb.count(mariage), 4L));

            // On veut compter le nombre d'hommes qui remplissent ces conditions
            cq.select(cb.countDistinct(homme));

            Query<Long> query = session.createQuery(cq);
            return query.getSingleResult();

        } catch (HibernateException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
