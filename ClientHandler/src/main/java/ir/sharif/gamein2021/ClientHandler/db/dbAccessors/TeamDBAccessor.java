package ir.sharif.gamein2021.ClientHandler.db.dbAccessors;

import ir.sharif.gamein2021.ClientHandler.db.DBSet;
import ir.sharif.gamein2021.ClientHandler.db.DBTools;
import ir.sharif.gamein2021.core.entity.Team;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class TeamDBAccessor implements DBSet<Team> {

    @Override
    public Team get(long id) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Team> criteriaQuery = criteriaBuilder.createQuery(Team.class);
        Root<Team> root = criteriaQuery.from(Team.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()) {
            return (Team) results.get(0);
        }
        return null;

    }

    @Override
    public ArrayList<Team> all() {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Team> criteriaQuery = criteriaBuilder.createQuery(Team.class);
        Root <Team> root = criteriaQuery.from(Team.class);
        criteriaQuery.select(root);
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()){
            return (ArrayList<Team>) results;
        }
        return null;

    }

    @Override
    public void add(Team team) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(team);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(Team team) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(team);
        session.getTransaction().commit();
        session.close();

    }

    public TeamDBAccessor() {}

}
