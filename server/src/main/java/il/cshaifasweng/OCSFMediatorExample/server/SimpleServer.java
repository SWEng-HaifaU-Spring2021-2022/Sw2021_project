package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import il.cshaifasweng.OCSFMediatorExample.entities.Hall;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieShow;
import il.cshaifasweng.OCSFMediatorExample.entities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.TheaterMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;

public class SimpleServer extends AbstractServer {

    private static Session session;
    public SimpleServer(int port) {
        super(port);
    }
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        msgObject msgObj = (msgObject) msg;
        try {
            if (msgObj.getMsg().startsWith("#get")) {
                get(msgObj, client);
            }
            if (msgObj.getMsg().startsWith("#update")) update(msgObj, client);
            if(msgObj.getMsg().startsWith("#add")){
                change(msgObj,client);
            }
            if (msgObj.getMsg().startsWith("#delete")) change(msgObj, client);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void get(msgObject msgobject, ConnectionToClient client) throws Exception {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        //session.beginTransaction();
        String msgString=msgobject.getMsg();
        if (msgString.equals("#getAllMovies")) {
            try {
                client.sendToClient(getAllMovies());
                System.out.println("retrived Movies");
                System.out.format("Sent movies to client %s\n", client.getInetAddress().getHostAddress());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }/* else if (msgString.equals( "#getAllHalls")) {
            try {
                client.sendToClient(getAllHalls());
                System.out.format("Sent movies to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (msgString.equals( "#getAllTheatres")) {
            try {
                client.sendToClient(getAllTheatres());
                System.out.format("Sent movies to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/else if(msgString.equals("#getshows")||msgString.equals("#getshowsdisplay"))  {
            int id=(int)msgobject.getObject();
            try{
                msgObject tempmsg=getMovieShowsbyid(id);
                if (msgString.equals("#getshowsdisplay")){
                    // System.out.println("bla bla");
                    tempmsg.setMsg("getshowsdisplay");
                }
                client.sendToClient(tempmsg);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            System.out.format("Sent movies Show of movies id "+id+" to client %s\n", client.getInetAddress().getHostAddress());

        }
    }

    private void update(msgObject msgObj, ConnectionToClient client)
    {
        try {

            if(msgObj.getMsg().equals("#updateMovieShow")){
                SessionFactory sessionFactory = getSessionFactory();
                session = sessionFactory.openSession();
                session.beginTransaction();
                change(msgObj, client);
                session.getTransaction().commit(); // Save everything.
                msgObj.setMsg("movie show updated");
                try {
                    client.sendToClient(msgObj);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        if(msgObj.getMsg().equals("#updateMovie")){
            session.save((Movie)msgObj.getObject());
            session.flush();
        }
        else if(msgObj.getMsg().equals("#addMovie")){
            session.update((Movie)msgObj.getObject());
            session.flush();
        }
        else if (msgObj.getMsg().equals("#removeMovie"))
        {
            session.delete(((Movie)msgObj.getObject()));
            session.flush();
        }
        else if (msgObj.getMsg().equals("#addMovieShow"))
        {
            session.save(((MovieShow)msgObj.getObject()));
            session.flush();
            session.getTransaction().commit();
            System.out.println("a new movie show added");
            msgObject tempmsg=new msgObject("newmovieShowadd",null);
            try {
                client.sendToClient(tempmsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (msgObj.getMsg().equals("#updateMovieShow"))
        {
            session.update(((MovieShow)msgObj.getObject()));
            session.flush();
        }
        else if (msgObj.getMsg().equals("#deleteMovieShow"))
        {
            try{
                session.delete(((MovieShow)msgObj.getObject()));
                session.getTransaction().commit();
            }
            catch (HibernateException e){
                e.printStackTrace();
                session.getTransaction().rollback();
            }
            System.out.println("MovieShow Deleted");
            msgObject tempmsg=new msgObject("movieshowdeleted",null);
            try {
                // session.getTransaction().commit();
                client.sendToClient(tempmsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            session.close();
        }
    }

    private static msgObject getAllMovies() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TheaterMovie> query = builder.createQuery(TheaterMovie.class);
        query.from(TheaterMovie.class);
        List<TheaterMovie> list= session.createQuery(query).getResultList();
        for(TheaterMovie m:list){
            m.getMSList();
        }
        msgObject msg=new msgObject("AllMovies",list);
        return msg;
    }

    /*private static List<Hall> getAllHalls() throws Exception {
    	SessionFactory sessionFactory = getSessionFactory();
    	session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Hall> query = builder.createQuery(Hall.class);
        query.from(Movie.class);
        List<Hall> data = session.createQuery(query).getResultList();
        return data;
    }*/

  /*  private static List<Theater> getAllTheatres() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Theater> query = builder.createQuery(Theater.class);
        query.from(Theater.class);
        List<Theater> data = session.createQuery(query).getResultList();
        return data;
    }*/

    private static msgObject getMovieShowsbyid(int id) throws Exception {
        System.out.println("getting movie shows");
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MovieShow> query = builder.createQuery(MovieShow.class);
        query.from(MovieShow.class);
        ArrayList<MovieShow> data = (ArrayList<MovieShow>) session.createQuery(query).getResultList();
        List<MovieShow> wantedlist = new ArrayList();
        for(MovieShow ms:data) {
            if(ms.getMovie().getMovieId()==id){
                wantedlist.add(ms);
            }
        }
        msgObject msg=new msgObject("movieShowsForMovie",wantedlist);
        return msg;

    }
    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Movie.class);
        configuration.addAnnotatedClass(Hall.class);
        configuration.addAnnotatedClass(Theater.class);
        configuration.addAnnotatedClass(MovieShow.class);
        configuration.addAnnotatedClass(TheaterMovie.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static void AddToDB() {
        try {
            String  actors= " Lewis Tan,Jessica McNamee, Josh Lawson";
            String str="MMA fighter Cole Young seeks out Earth's greatest champions in order to stand against the enemies of Outworld in a high stakes battle for the universe.";
            String imgURL  = "https://m.media-amazon.com/images/M/MV5BY2ZlNWIxODMtN2YwZi00ZjNmLWIyN2UtZTFkYmZkNDQyNTAyXkEyXkFqcGdeQXVyODkzNTgxMDg@._V1_.jpg";
            TheaterMovie m=new TheaterMovie("Mortal Kombat","מורטל קומבט",actors,"Action",str,"wb",imgURL,40);
            session.save(m);
            session.flush();
            Theater th=new Theater("Haifa");
            session.save(th);
            session.flush();
            Date d=new Date(10000000);
            MovieShow ms=new MovieShow(m,d,th,"15:00","17:00",40);
            m.AddMovieShow(ms);
            session.save(ms);
            session.flush();
            Hall hall=new Hall(40,th,1);
            session.save(hall);
            session.flush();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void test() {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            AddToDB();
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

}