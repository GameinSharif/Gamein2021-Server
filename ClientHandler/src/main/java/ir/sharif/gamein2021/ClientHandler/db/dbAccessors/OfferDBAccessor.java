package ir.sharif.gamein2021.ClientHandler.db.dbAccessors;

import ir.sharif.gamein2021.ClientHandler.db.DBSet;
import ir.sharif.gamein2021.core.domain.entity.Offer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class OfferDBAccessor implements DBSet<Offer> {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    @Override
    public Offer get(long id) {

        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
        Root<Offer> root = criteriaQuery.from(Offer.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();

        if (!results.isEmpty()) {
            return (Offer) results.get(0);
        }
        return null;

    }

    public ArrayList<Offer> getOffersOf(long teamId) {

        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
        Root <Offer> root = criteriaQuery.from(Offer.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("team_id"), teamId));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();

        if (!results.isEmpty()){
            return (ArrayList<Offer>) results;
        }
        return null;

    }

    @Override
    public ArrayList<Offer> all() {

        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
        Root <Offer> root = criteriaQuery.from(Offer.class);
        criteriaQuery.select(root);
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();

        if (!results.isEmpty()){
            return (ArrayList<Offer>) results;
        }
        return null;

    }

    @Override
    public void add(Offer offer) {

        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(offer);
        session.getTransaction().commit();

    }

    @Override
    public void update(Offer offer) {

        Session session = this.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.update(offer);
        session.getTransaction().commit();

    }

    public OfferDBAccessor() {}

}
