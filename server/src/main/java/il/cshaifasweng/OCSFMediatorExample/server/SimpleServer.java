package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

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

public class SimpleServer extends AbstractServer {

    private static Session session;

    public SimpleServer(int port) {
        super(port);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        msgObject msgObj = (msgObject) msg;
        try {
            if (msgObj.getMsg().equals("TryLogIn")) {
                client.sendToClient(tryLogIn((String[]) msgObj.getObject()));
            }
            if (msgObj.getMsg().equals("TryLogOut")) {
                client.sendToClient(tryLogOut((User) msgObj.getObject()));
            }

            if (msgObj.getMsg().startsWith("#get")) {
                get(msgObj, client);
            }
            if (msgObj.getMsg().startsWith("#update")) update(msgObj, client);
            if (msgObj.getMsg().startsWith("#add")) {
                change(msgObj, client);
            }
            if (msgObj.getMsg().startsWith("#delete")) change(msgObj, client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void get(msgObject msgobject, ConnectionToClient client) throws Exception {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        //session.beginTransaction();
        String msgString = msgobject.getMsg();
        if (msgString.equals("#getAllMovies")) {
            try {
                client.sendToClient(getAllMovies());
                System.out.println("retrived Movies");
                System.out.format("Sent movies to client %s\n", client.getInetAddress().getHostAddress());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (msgString.equals("#getAllHalls")) {
            try {
                client.sendToClient(getAllHalls());
                System.out.format("Sent movies to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (msgString.equals("#getAllTheatres")) {
            try {
                client.sendToClient(getAllTheatres());
                System.out.format("Sent theaters to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (msgString.equals("#getshows") || msgString.equals("#getshowsdisplay")) {
            int id = (int) msgobject.getObject();
            try {
                msgObject tempmsg = getMovieShowsbyid(id);
                if (msgString.equals("#getshowsdisplay")) {
                    // System.out.println("bla bla");
                    tempmsg.setMsg("getshowsdisplay");
                }
                client.sendToClient(tempmsg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.format("Sent movies Show of movies id " + id + " to client %s\n", client.getInetAddress().getHostAddress());

        }
        else if(msgString.equals("#getAllPriceRequests")) {
        	client.sendToClient(getAllRequests());
            System.out.format("Sent all requests to client %s\n", client.getInetAddress().getHostAddress());
        }
    }

    private void update(msgObject msgObj, ConnectionToClient client) {
        try {

            if (msgObj.getMsg().equals("#updateMovieShow")) {
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
            
            if(msgObj.getMsg().equals("#updatePrice")){
                SessionFactory sessionFactory = getSessionFactory();
                session = sessionFactory.openSession();
                session.beginTransaction();
                PriceRequest pr=(PriceRequest)msgObj.getObject();
                Movie movie=getMovie(pr.getMovieID());
                System.out.println(pr.getMovieID());
                System.out.println(movie.getEngName());
                if(movie.getClass().equals(TheaterMovie.class)) {
                	TheaterMovie Tm=(TheaterMovie)movie;
                	Tm.setEntryPrice(pr.getNewPrice());
                	movie = Tm;
                }else if(movie.getClass().equals(HomeMovie.class)){
                	HomeMovie Hm = (HomeMovie)movie;
                	Hm.setEntryprice(pr.getNewPrice());
                	movie = Hm;
                	
                }
                session.update(movie);
                session.delete(pr);
                session.flush();
                session.getTransaction().commit(); // Save everything.
                msgObj.setMsg("Price updated");
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


    private void change(msgObject msgObj, ConnectionToClient client) throws Exception {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        if (msgObj.getMsg().equals("#updateMovie")) {
            session.update((Movie) msgObj.getObject());
            session.flush();
        } else if (msgObj.getMsg().equals("#addMovie")) {
            session.save((Movie) msgObj.getObject());
            session.flush();
            session.getTransaction().commit();
            System.out.println("a coming soon movie have been added");
            msgObject answer_msg = new msgObject("movie added successfully");
            try {
                client.sendToClient(answer_msg);
            } catch (IOException e) {
                answer_msg.setMsg("failed");
                client.sendToClient(answer_msg);
                e.printStackTrace();
            }
        } else if (msgObj.getMsg().equals("#deleteMovie")) {
            msgObject answer_msg = new msgObject();
            try {
                //TODO:check if it a theater movie
                Movie m = (Movie) msgObj.getObject();
                if (m.getClass().equals(TheaterMovie.class)) {
                    TheaterMovie TM = (TheaterMovie) m;
                    for (MovieShow ms : TM.getMSList()) {
                        session.delete((ms));
                    }
                }
                session.delete(((Movie) msgObj.getObject()));
                session.flush();
                session.getTransaction().commit(); // Save everything
                answer_msg.setMsg("Movie deleted");
                client.sendToClient(answer_msg);
            } catch (Exception ex) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
                System.err.println("An error occurred, changes have been rolled back.");
                answer_msg.setMsg("failed");
                try {
                    client.sendToClient(answer_msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ex.printStackTrace();

            }

        }
        else if (msgObj.getMsg().equals("#deleteRequest")) {
        	msgObject answer_msg = new msgObject();
        	PriceRequest price = (PriceRequest)msgObj.getObject();
        	session.delete((PriceRequest)msgObj.getObject());
        	session.getTransaction().commit(); // Save everything
            answer_msg.setMsg("PriceRequest deleted");
            client.sendToClient(answer_msg);
        	
        }
        
        else if (msgObj.getMsg().equals("#addMovieShow"))
        {
            session.save((MovieShow)msgObj.getObject());
            session.flush();
            session.getTransaction().commit();
            System.out.println("a new movie show added");
            AdvancedMsg tempmsg=new AdvancedMsg("newmovieShowadd");
            MovieShow ms=(MovieShow)msgObj.getObject();
            int movieid=ms.getMovie().getMovieId();
            tempmsg.addobject((List<Theater>)getAllTheatres().getObject());
            tempmsg.addobject(getMovie(movieid));
            session.close();

            try {

                client.sendToClient(tempmsg);
            } catch (IOException e) {
                tempmsg.setMsg("failed");
                client.sendToClient(tempmsg);
                e.printStackTrace();
            }
        } else if (msgObj.getMsg().equals("#updateMovieShow")) {
            session.update(((MovieShow) msgObj.getObject()));
            session.flush();
            AdvancedMsg tempmsg= new AdvancedMsg("MovieShow Updated");
            MovieShow ms=(MovieShow)msgObj.getObject();
            int movieid=ms.getMovie().getMovieId();
            tempmsg.addobject((List<Theater>)getAllTheatres().getObject());
            tempmsg.addobject(getMovie(movieid));
            client.sendToClient(tempmsg);
        }
        else if (msgObj.getMsg().equals("#deleteMovieShow"))
        {
            System.out.println("deleting a movie show");
            try{
                session.delete(((MovieShow)msgObj.getObject()));
                session.getTransaction().commit();
            } catch (HibernateException e) {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
            System.out.println("MovieShow Deleted");
            AdvancedMsg tempmsg = new AdvancedMsg("MovieShow Deleted");
            try {
                MovieShow ms = (MovieShow) msgObj.getObject();
                int movieid = ms.getMovie().getMovieId();
                tempmsg.addobject((List<Theater>) getAllTheatres().getObject());
                tempmsg.addobject(getMovie(movieid));
            } catch (Exception e) {
                e.printStackTrace();
            }
            tempmsg.setMsg("MovieShow Deleted");
            session.close();
            try {
                System.out.println(tempmsg.getMsg());
                System.out.println(tempmsg.getClass().toString());
                client.sendToClient(tempmsg);
                System.out.println("message sent to reopen edit page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (msgObj.getMsg().equals("#addPriceRequest")) {
            session.save(((PriceRequest) msgObj.getObject()));
            session.flush();
            session.getTransaction().commit();
            System.out.println("a new price change request have been added");
            msgObject answer_msg = new msgObject("a price request added", null);
            client.sendToClient(answer_msg);
        }else if(msgObj.getMsg().equals("#addHomeTicket")){
            try {
                HomeLinkTicket HLT=(HomeLinkTicket)msgObj.getObject();
                session.save((HomeLinkTicket)msgObj.getObject());
                session.flush();
                session.getTransaction().commit();
                System.out.println("home movie ticket have been buyed");
                msgObject answer_msg=new msgObject("HomeMoviePurchasedSuccessfully",null);
                client.sendToClient(answer_msg);
                EmailUtil.sendEmailHomeTicket(HLT );
                System.out.println("Sending an email to the client");
            }
            catch (Exception e){
                msgObject answer_msg=new msgObject("failed",null);
                client.sendToClient(answer_msg);
                e.printStackTrace();
            }
        }

    }

    private static msgObject getAllMovies() throws Exception {
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
        msgObject msg = new msgObject("AllMovies", list);
        return msg;
    }
    private static msgObject getAllRequests() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PriceRequest> query = builder.createQuery(PriceRequest.class);
        query.from(PriceRequest.class);
        List<PriceRequest> list= session.createQuery(query).getResultList();
        msgObject msg=new msgObject("AllRequests",list);
        return msg;
    }
    private static Movie getMovie(int id) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
        query.from(Movie.class);
        List<Movie> list = session.createQuery(query).getResultList();
        for (Movie m : list) {
            if (m.getClass().equals(TheaterMovie.class)) {
                if (m.getMovieId() == id) {
                    TheaterMovie TM = (TheaterMovie) m;
                    List<MovieShow> temp = TM.getMSList();
                    for (MovieShow ms : temp) {
                        ms.getTheater();
                    }
                    return m;
                }
            }
            else if(m.getMovieId() == id) {
            	return m;
            }

        }
        return null;
    }

    private static List<Hall> getAllHalls() throws Exception {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Hall> query = builder.createQuery(Hall.class);
        query.from(Hall.class);
        List<Hall> data = session.createQuery(query).getResultList();
        return data;
    }

    private static msgObject getAllTheatres() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Theater> query = builder.createQuery(Theater.class);
        query.from(Theater.class);
        List<Theater> data = session.createQuery(query).getResultList();
        for (Theater th : data) {
            List<Hall> halls = th.getHalls();
            for (Hall h : halls) {
                h.getHallNumber();
                // System.out.println();
            }

        }
        msgObject newmsg = new msgObject("Theaters Retrived", data);
        return newmsg;
    }

    private static msgObject tryLogIn(String[] data) {
        String userName = data[0];
        String password = data[1];
        msgObject msg = new msgObject("");
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        String sqlQ = "FROM User U WHERE U.userName = :user_name";
        Query query = session.createQuery(sqlQ);
        query.setParameter("user_name", userName);
        List<User> list = query.list();
        if (!list.isEmpty()) {
            User user = list.get(0);
            if (user.checkPassword(password)) {
                if (user.getConnected()) msg.setMsg("LINAlreadyConnected");
                else {
                    user.setConnected(true);
                    System.out.println(user.getFirstName() + " " + user.getLastName());
                    try {
                        session.beginTransaction();
                        session.update(user);
                        session.getTransaction().commit(); // Save everything.
                        msg.setObject(user);
                        msg.setMsg("LINUserFound");
                        System.out.println("LINUserFound");
                    } catch (HibernateException e) {
                        if (session != null)
                            session.getTransaction().rollback();
                        e.printStackTrace();
                        msg.setMsg("LINUnknownLogInError");
                    }
                }
            } else msg.setMsg("LINUserNotFound");

        } else msg.setMsg("LINUserNotFound");


        return msg;
    }

    private static msgObject tryLogOut(User user) {
        msgObject msg = new msgObject("");
            try {
                user.setConnected(false);
                SessionFactory sessionFactory = getSessionFactory();
                session = sessionFactory.openSession();
                session.beginTransaction();
                session.update(user);
                session.getTransaction().commit(); // Save everything.
                msg.setMsg("LOUTLoggedOut");
            } catch (HibernateException e) {
                e.printStackTrace();
                msg.setMsg("LOUTUnknownLogOutError");
            }

        return msg;
    }


    private static msgObject getMovieShowsbyid(int id) throws Exception {
        System.out.println("getting movie shows");
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MovieShow> query = builder.createQuery(MovieShow.class);
        query.from(MovieShow.class);
        ArrayList<MovieShow> data = (ArrayList<MovieShow>) session.createQuery(query).getResultList();
        List<MovieShow> wantedlist = new ArrayList();
        for (MovieShow ms : data) {
            if (ms.getMovie().getMovieId() == id) {
                wantedlist.add(ms);
            }
        }
        msgObject msg = new msgObject("movieShowsForMovie", wantedlist);
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
        configuration.addAnnotatedClass(HomeMovie.class);
        configuration.addAnnotatedClass(PriceRequest.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(HomeLinkTicket.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static void AddUsers() {
        try {
            User user = new User("Admin", "admin", "The", "Admin", 5);
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit(); // Save everything.
        } catch (HibernateException e) {
            e.printStackTrace();

        }
    }

    private static void AddToDB() {
        try {
            System.out.println("Theater movie1");
            //Getting the default zone id
            ZoneId defaultZoneId = ZoneId.systemDefault();

            //Converting the date to Instant
            Instant instant;
            String actors = " Alexander Skarsgård,Millie Bobby Brown, Rebecca Hall";
            String str = "The epic next chapter in the cinematic Monsterverse pits two of the greatest icons in motion picture history against one another - the fearsome Godzilla and the mighty Kong - with humanity caught in the balance.";
            String imgURL = "https://upload.wikimedia.org/wikipedia/he/f/f5/Godzilla_vs.Kong.png";
            TheaterMovie m = new TheaterMovie("Godzilla vs. Kong\n", "גודזילה נגד קונג", actors, "Action,Sci-Fi,Thriller", str, "wb", imgURL, 70);
            session.save(m);
            session.flush();
            Theater th = new Theater("Haifa");
            session.save(th);
            session.flush();
            Hall hall = new Hall(40, th, 1);
            session.save(hall);
            session.flush();
            Date d = new Date(2021 - 1900, 7, 11);
            instant = d.toInstant();
            LocalDate localD1 = instant.atZone(defaultZoneId).toLocalDate();
            MovieShow ms = new MovieShow(m, localD1, th, "20:00", "22:00", 40);
            m.AddMovieShow(ms);
            session.save(ms);
            session.flush();
            //___________________________________________________________________________________________
            System.out.println("Theater movie2");
            String actors2 = " Lewis Tan,Jessica McNamee, Josh Lawson";
            String str2 = "MMA fighter Cole Young seeks out Earth's greatest champions in order to stand against the enemies of Outworld in a high stakes battle for the universe.";
            String imgURL2 = "https://m.media-amazon.com/images/M/MV5BY2ZlNWIxODMtN2YwZi00ZjNmLWIyN2UtZTFkYmZkNDQyNTAyXkEyXkFqcGdeQXVyODkzNTgxMDg@._V1_.jpg";
            TheaterMovie m2 = new TheaterMovie("Mortal Kombat", "מורטל קומבט", actors2, "Action", str2, "wb", imgURL2, 40);
            session.save(m2);
            session.flush();
            Theater th2 = new Theater("Herzilya");
            session.save(th2);
            session.flush();
            Hall hall2 = new Hall(40, th2, 1);
            session.save(hall2);
            session.flush();
            Date d2 = new Date(2021 - 1900, 10, 5);
            instant = d2.toInstant();
            LocalDate localD2 = instant.atZone(defaultZoneId).toLocalDate();
            MovieShow ms2 = new MovieShow(m2, localD2, th2, "19:00", "21:00", 60);
            m.AddMovieShow(ms2);
            session.save(ms2);
            session.flush();
            //________________________________________________________________________________________________________________
            System.out.println("Theater movie3");
            String actors3 = " Chris Pratt,Yvonne Strahovski, J.K. Simmons";
            String str3 = "A man is drafted to fight in a future war where the fate of humanity relies on his ability to confront his past.";
            String imgURL3 = "https://images-na.ssl-images-amazon.com/images/I/81qDMksX4PS._RI_.jpg";
            TheaterMovie m3 = new TheaterMovie("The Tomorrow War", "מלחמת המחר", actors3, "Action,Adventure,Sci-Fi,Thriller", str3, "Amazon", imgURL3, 50);
            session.save(m3);
            session.flush();
            Theater th3 = new Theater("Tel-Aviv");
            session.save(th3);
            session.flush();
            Hall hall3 = new Hall(40, th3, 2);
            session.save(hall3);
            session.flush();
            Date d3 = new Date(2021 - 1900, 2, 12);
            instant = d3.toInstant();
            LocalDate localD3 = instant.atZone(defaultZoneId).toLocalDate();
            MovieShow ms3 = new MovieShow(m3, localD3, th3, "12:00", "14:00", 40);
            m.AddMovieShow(ms3);
            session.save(ms3);
            session.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void addHomeMovie() {
        System.out.println("home movie1");
        String actors = " Edward Asner,Jordan Nagai,John Ratzenberger";
        String str = "78-year-old Carl Fredricksen travels to Paradise Falls in his house equipped with balloons, inadvertently taking a young stowaway.";
        String imgURL = "https://upload.wikimedia.org/wikipedia/he/8/82/Up_Poster_Israel.jpg";
        HomeMovie hm = new HomeMovie("Up", "למעלה", actors, "Animation,Adventure,Comedy,Thriller", str, "Amazon", imgURL, "https://www.youtube.com/watch?v=ORFWdXl_zJ4", 50);
        session.save(hm);
        System.out.println("home movie2");
    }

    public static void test() {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            AddToDB();
            addHomeMovie();
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

    public static msgObject getAllMovies2() throws Exception {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
        query.from(Movie.class);
        List<Movie> list = session.createQuery(query).getResultList();
        for (Movie m : list) {
            if (m.getClass().equals(TheaterMovie.class)) {
                System.out.println("it's a theater movie ");
            }
            /*List<MovieShow> temp= m.getMSList();
            for (MovieShow ms:temp){
                System.out.println(ms.getTheater());

            }*/
        }
        msgObject msg = new msgObject("AllMovies", list);
        return msg;
    }

}