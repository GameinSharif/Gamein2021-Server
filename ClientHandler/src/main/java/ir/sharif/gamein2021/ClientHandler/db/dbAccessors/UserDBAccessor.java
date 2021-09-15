package ir.sharif.gamein2021.ClientHandler.db.dbAccessors;

import ir.sharif.gamein2021.ClientHandler.db.DBSet;
import ir.sharif.gamein2021.ClientHandler.db.DBTools;
import ir.sharif.gamein2021.core.domain.entity.User;
import org.hibernate.Session;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserDBAccessor implements DBSet<User> {

    @Override
    public User get(long id) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()) {
            return (User) results.get(0);
        }
        return null;

    }

    public User getByUsername(String username){

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("username"),username));
        Query query = session.createQuery(criteriaQuery);
        List<User> results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()){
            return (User) results.get(0);
        }
        return null;
    }

    @Override
    public ArrayList<User> all() {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()){
            return (ArrayList<User>) results;
        }
        return null;

    }

    @Override
    public void add(User user) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(User user) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();
        session.close();

    }

    public UserDBAccessor() {}

}