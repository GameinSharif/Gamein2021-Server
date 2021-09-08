package ir.sharif.gamein2021.ClientHandler.db;

import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

public class DBTools {

    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    @Autowired
    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable e) {
            logger.debug(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}