package triathlon.persistence.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import triathlon.model.EventEntity;
import triathlon.model.ParticipantEntity;

public class HibernateUtils {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if ((sessionFactory==null)||(sessionFactory.isClosed()))
            sessionFactory=createNewSessionFactory();
        return sessionFactory;
    }

    private static SessionFactory createNewSessionFactory(){
        sessionFactory = new Configuration()
                .addAnnotatedClass(EventEntity.class)
                .addAnnotatedClass(ParticipantEntity.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static void closeSessionFactory(){
        if (sessionFactory!=null)
            sessionFactory.close();
    }
}