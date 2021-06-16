package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.HomeLinkTicket;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.Instant;
import java.time.ZoneId;

public class DBase {
    private static Session session;

    private static SessionFactory getSessionFactory() throws HibernateException {
        org.hibernate.cfg.Configuration configuration = new Configuration();

        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(HomeLinkTicket.class);

        ServiceRegistry serviceRegistry = new
                StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }
    public static ArrayList<HomeLinkTicket> getHomeLinkTicket(){
        System.out.println("getting all home movies ticket for this day");
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        CriteriaBuilder builder=session.getCriteriaBuilder();
        CriteriaQuery<HomeLinkTicket> query=builder.createQuery(HomeLinkTicket.class);
        query.from(HomeLinkTicket.class);
        System.out.println(LocalDate.now());
        ArrayList<HomeLinkTicket> data=(ArrayList<HomeLinkTicket>) session.createQuery(query).getResultList();
        ArrayList<HomeLinkTicket>wantedData=new ArrayList<>();
        for(HomeLinkTicket hlt:data){
            if(hlt.getScreeningDate().equals(LocalDate.now()))
                wantedData.add(hlt);
        }
        System.out.println(wantedData.size());
        if(session!=null){
            session.close();
        }
        return  wantedData;

    }
    public static  void UpdateHometicket(HomeLinkTicket hlt){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(hlt);
        session.flush();
        session.getTransaction().commit();
        System.out.println("updating the ticket is sent attribute");
        if(session!=null){
            session.close();
        }
    }

}
