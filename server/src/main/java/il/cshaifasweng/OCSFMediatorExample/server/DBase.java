package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
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
        configuration.addAnnotatedClass(Movie.class);
        configuration.addAnnotatedClass(Hall.class);
        configuration.addAnnotatedClass(Theater.class);
        configuration.addAnnotatedClass(MovieShow.class);
        configuration.addAnnotatedClass(TheaterMovie.class);
        configuration.addAnnotatedClass(HomeMovie.class);
        configuration.addAnnotatedClass(PriceRequest.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Ticket.class);
        configuration.addAnnotatedClass(HomeLinkTicket.class);
        configuration.addAnnotatedClass(TheaterTicket.class);
        configuration.addAnnotatedClass(Seat.class);
        configuration.addAnnotatedClass(Bundle.class);
        configuration.addAnnotatedClass(PurpleCard.class);
        configuration.addAnnotatedClass(CinemaManager.class);
        configuration.addAnnotatedClass(Complaint.class);
        ServiceRegistry serviceRegistry = new
                StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static ArrayList<HomeLinkTicket> getHomeLinkTicket() {
        System.out.println("getting all home movies ticket for this day");
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<HomeLinkTicket> query = builder.createQuery(HomeLinkTicket.class);
        query.from(HomeLinkTicket.class);
        System.out.println(LocalDate.now());
        ArrayList<HomeLinkTicket> data = (ArrayList<HomeLinkTicket>) session.createQuery(query).getResultList();
        ArrayList<HomeLinkTicket> wantedData = new ArrayList<>();
        for (HomeLinkTicket hlt : data) {
            if (hlt.getScreeningDate().equals(LocalDate.now()))
                wantedData.add(hlt);
        }
        System.out.println(wantedData.size());
        if (session != null) {
            session.close();
        }
        return wantedData;

    }

    public static ArrayList<Complaint> getComplaint() {
        System.out.println("getting all the complaints");
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Complaint> query = builder.createQuery(Complaint.class);
        query.from(Complaint.class);
        System.out.println(LocalDate.now());
        ArrayList<Complaint> data = (ArrayList<Complaint>) session.createQuery(query).getResultList();
        ArrayList<Complaint> wantedData = new ArrayList<>();
        for (Complaint cm : data) {
            if (cm.getDate().equals(LocalDate.now().minusDays(1)))
                wantedData.add(cm);
        }
        System.out.println(wantedData.size());
        if (session != null) {
            session.close();
        }
        return wantedData;

    }

    public static void UpdateHometicket(HomeLinkTicket hlt) {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(hlt);
        session.flush();
        session.getTransaction().commit();
        System.out.println("updating the ticket is sent attribute");
        if (session != null) {
            session.close();
        }
    }

    public static void UpdateComplaint(Complaint cm) {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(cm);
        session.flush();
        session.getTransaction().commit();
        System.out.println("updating the complaint is sent attribute");
        if (session != null) {
            session.close();
        }
    }

        public static msgObject getAllMovies () {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
            query.from(Movie.class);
            List<Movie> list = session.createQuery(query).getResultList();
            for (Movie m : list) {
                if (m.getClass().equals(TheaterMovie.class)) {
                    TheaterMovie TM = (TheaterMovie) m;
                    List<MovieShow> temp = TM.getMSList();
                    for (MovieShow ms : temp) {
                        ms.getTheater();
                        //System.out.println();
                    }
                }

            }
            msgObject msg = new msgObject("RefreshCatalog", list);
            return msg;
        }

}

