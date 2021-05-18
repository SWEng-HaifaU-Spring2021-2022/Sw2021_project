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
            if (msgObj.getMsg().equals("#getAllMovies")) {
            	/*List<TheaterMovie> list=getAllMovies();
            	List<TheaterMovie> temp=new ArrayList();
            	TheaterMovie [] arr=new TheaterMovie[list.size()];
            	list.toArray(arr);
            	//int [] arr= {1,2,3,4};
            	TheaterMovie tm=list.get(0);*/
            	msgObject msgobj2=new msgObject("AllMovies",getAllMovies());
         
            	client.sendToClient(msgobj2);
            	//get(msgObj.getMsg(), client);
            	System.out.format("Sent movies to client %s\n", client.getInetAddress().getHostAddress());
            }
            if (msgObj.getMsg().startsWith("#update")) update(msgObj, client);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void get(String msgString, ConnectionToClient client) throws Exception {
    	System.out.println("get func");
    	System.out.println(msgString);
    	SessionFactory sessionFactory = getSessionFactory();
    	session = sessionFactory.openSession();
    	//session.beginTransaction();

        if (msgString.equals("#getAllMovies")) {
            try {
            	System.out.println("before sending to client");
            	 client.sendToClient(getAllMovies());
            	client.sendToClient("bla bla");
                // System.out.format("Sent movies to client %s\n", client.getInetAddress().getHostAddress());
            	
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (msgString.equals( "#getAllHalls")) {
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
        } else if (msgString.equals( "#getAllMovieShows")) {
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
        }
        else if (msgObj.getMsg().equals("#updateMovieShow"))
        {
            session.update(((MovieShow)msgObj.getObject()));
            session.flush();
        }
        else if (msgObj.getMsg().equals("#deleteMovieShow"))
        {
            session.delete(((MovieShow)msgObj.getObject()));
            session.flush();
        }
    }

    private static List<Movie> getAllMovies() throws Exception {
    	SessionFactory sessionFactory = getSessionFactory();
    	session = sessionFactory.openSession();
    	CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
        query.from(Movie.class);
        List<Movie> list= session.createQuery(query).getResultList();
        for(Movie m : list) {
        	System.out.println(m.getDescription());
        }
        return list;
    	/*SessionFactory sessionFactory = getSessionFactory();
    	session = sessionFactory.openSession();
    	
    	
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
        query.from(Movie.class);
        return session.createQuery(query).getResultList();*/
    }

    private static List<Hall> getAllHalls() throws Exception {
    	SessionFactory sessionFactory = getSessionFactory();
    	session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Hall> query = builder.createQuery(Hall.class);
        query.from(Movie.class);
        List<Hall> data = session.createQuery(query).getResultList();
        return data;
    }

    private static List<Theater> getAllTheatres() throws Exception {

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Theater> query = builder.createQuery(Theater.class);
        query.from(Theater.class);
        List<Theater> data = session.createQuery(query).getResultList();
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
    	String [] actors= {" Lewis Tan","Jessica McNamee"," Josh Lawson"};
    	String str="MMA fighter Cole Young seeks out Earth's greatest champions in order to stand against the enemies of Outworld in a high stakes battle for the universe.";
    	File imagfile1 = new File(System.getProperty("user.dir") + "/MK.jpg");
    	byte[] pixelsArray1 = Files.readAllBytes(Paths.get("C:\\Users\\USER1\\eclipse-workspace\\Sw2021_project\\server\\src\\main\\java\\il\\cshaifasweng\\OCSFMediatorExample\\server\\MK.jpg"));
    	Movie m=new Movie("Mortal Kombat","מורטל קומבט",actors,"Action",str,"wb",pixelsArray1);
    	Theater th=new Theater("Haifa");
    	Hall hall=new Hall(40,th,1);
    	th.AddHalls(hall);
    	Date d=new Date(10000000);
    	MovieShow ms=new MovieShow(m,d,th,"15:00","17:00",40);
    	session.save(th);
    	session.save(hall);
    	session.save(m);
    	session.save(ms);
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
