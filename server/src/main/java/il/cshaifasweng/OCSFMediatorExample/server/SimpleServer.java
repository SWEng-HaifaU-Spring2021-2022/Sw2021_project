package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import java.io.IOException;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

public class SimpleServer extends AbstractServer {
    private static Session session;

    public SimpleServer(int port) {
        super(port);

    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        msgObject msgObj = (msgObject) msg;
        try {
            if (msgObj.getMsg().startsWith("#getAll")) get(msgObj.getMsg(), client);
            if (msgObj.getMsg().startsWith("#update")) update(msgObj, client);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void get(String msgString, ConnectionToClient client) throws Exception {
        if (msgString == "#getAllMovies") {
            try {
                client.sendToClient(getAllMovies());
                System.out.format("Sent movies to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (msgString == "#getAllHalls") {
            try {
                client.sendToClient(getAllHalls());
                System.out.format("Sent movies to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (msgString == "#getAllTheatres") {
            try {
                client.sendToClient(getAllTheatres());
                System.out.format("Sent movies to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (msgString == "#getAllMovieShows") {
            try {
                client.sendToClient(getAllMovieShows());
                System.out.format("Sent movies to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void update(msgObject msgObj, ConnectionToClient client)
    {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            change(msgObj, client);
            session.getTransaction().commit(); // Save everything.
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }


    private void change(msgObject msgObj, ConnectionToClient client)
    {
        if(msgObj.getMsg()=="#updateMovie"){
            session.save((Movie)msgObj.getObject());
            session.flush();
        }
        else if(msgObj.getMsg()=="#addMovie"){
            session.update((Movie)msgObj.getObject());
            session.flush();
        }
        else if (msgObj.getMsg()=="#removeMovie")
        {
            session.delete(((Movie)msgObj.getObject()));
            session.flush();
        }
        else if (msgObj.getMsg()=="#addMovieShow")
        {
            session.save(((MovieShow)msgObj.getObject()));
            session.flush();
        }
        else if (msgObj.getMsg()=="#updateMovieShow")
        {
            session.update(((MovieShow)msgObj.getObject()));
            session.flush();
        }
        else if (msgObj.getMsg()=="#deleteMovieShow")
        {
            session.delete(((MovieShow)msgObj.getObject()));
            session.flush();
        }
    }

    private static List<Movie> getAllMovies() throws Exception {

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
        query.from(Movie.class);
        List<Movie> data = session.createQuery(query).getResultList();
        return data;
    }

    private static List<Movies> getAllHalls() throws Exception {

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Hall> query = builder.createQuery(Hall.class);
        query.from(Movies.class);
        List<Hall> data = session.createQuery(query).getResultList();
        return data;
    }

    private static List<Theatre> getAllTheatres() throws Exception {

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Theatre> query = builder.createQuery(Theatre.class);
        query.from(Theatre.class);
        List<Theatre> data = session.createQuery(query).getResultList();
        return data;
    }

    private static List<MovieShow> getAllMovieShows() throws Exception {

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MovieShow> query = builder.createQuery(MovieShow.class);
        query.from(MovieShow.class);
        List<MovieShow> data = session.createQuery(query).getResultList();
        return data;
    }

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Movie.class);
        configuration.addAnnotatedClass(Hall.class);
        configuration.addAnnotatedClass(Theatre.class);
        configuration.addAnnotatedClass(MovieShow.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

}
