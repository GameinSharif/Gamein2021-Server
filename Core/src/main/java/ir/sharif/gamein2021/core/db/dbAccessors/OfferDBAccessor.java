package ir.sharif.gamein2021.core.db.dbAccessors;

import ir.sharif.gamein2021.core.db.DBSet;
import ir.sharif.gamein2021.core.db.DBTools;
import ir.sharif.gamein2021.core.entity.Offer;
import ir.sharif.gamein2021.core.entity.Team;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class OfferDBAccessor implements DBSet<Offer> {

    @Override
    public Offer get(long id) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
        Root<Offer> root = criteriaQuery.from(Offer.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()) {
            return (Offer) results.get(0);
        }
        return null;

    }

    @Override
    public ArrayList<Offer> all() {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
        Root <Offer> root = criteriaQuery.from(Offer.class);
        criteriaQuery.select(root);
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()){
            return (ArrayList<Offer>) results;
        }
        return null;

    }

    @Override
    public void add(Offer offer) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(offer);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(Offer offer) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(offer);
        session.getTransaction().commit();
        session.close();

    }

    public OfferDBAccessor() {}

}
