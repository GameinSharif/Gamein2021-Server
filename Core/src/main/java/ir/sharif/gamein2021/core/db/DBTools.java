package ir.sharif.gamein2021.core.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBTools {

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable exception) {
            System.err.println(exception);
            throw new ExceptionInInitializerError(exception);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}