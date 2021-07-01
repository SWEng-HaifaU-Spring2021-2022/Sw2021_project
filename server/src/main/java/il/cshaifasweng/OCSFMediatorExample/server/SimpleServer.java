package il.cshaifasweng.OCSFMediatorExample.server;

import com.mysql.cj.xdevapi.Client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.sql.Update;

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
        if (msg.getClass().equals(msgObject.class)) {
            msgObject msgObj = (msgObject) msg;
            try {
                if (msgObj.getMsg().equals("TryLogIn")) {
                    client.sendToClient(tryLogIn((String[]) msgObj.getObject()));
                }
                if (msgObj.getMsg().equals("TryLogOut")) {
                    client.sendToClient(tryLogOut((User) msgObj.getObject()));
                }
                if (msgObj.getMsg().equals("BuyBundle")) {
                    System.out.println("BuyBundle");
                    buyBundle(msgObj, client);
                }
                if (msgObj.getMsg().equals("getBundles"))
                    getBundles(msgObj, client);
                if (msgObj.getMsg().equals("getAllInstructions"))
                    getAllInstructions(msgObj, client);
                if (msgObj.getMsg().equals("AddInstruction") ||
                        msgObj.getMsg().equals("UpdateInstruction") ||
                        msgObj.getMsg().equals("DeleteInstruction"))
                    EditInstruction(msgObj, client);
                if (msgObj.getMsg().startsWith("#get")) {
                    get(msgObj, client);
                }
                if (msgObj.getMsg().startsWith("#update")) update(msgObj, client);
                if (msgObj.getMsg().startsWith("#add")) {
                    change(msgObj, client);
                }
                if (msgObj.getMsg().startsWith("#delete")) change(msgObj, client);
                if(msgObj.getMsg().equals("#CheckPurpleCard")){
                    boolean flag=true;
                    List<PurpleCard> cardslist=getAllInstructions2();
                    MovieShow tempms=(MovieShow) msgObj.getObject();
                    if(tempms!=null){
                        for (PurpleCard pc:cardslist){
                            if (tempms.getShowDate().isAfter(pc.getStart())&&tempms.getShowDate().isBefore(pc.getEnd())&&pc.getProjAllowed()==false){
                                msgObject answer_msg=new msgObject("can't Pick seat");
                                client.sendToClient(answer_msg);
                                flag=false;
                            }
                        }
                        if(flag){
                            msgObject answer_msg=new msgObject("can Pick seat");
                            client.sendToClient(answer_msg);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("server received advanced message");
            AdvancedMsg advcmsg = (AdvancedMsg) msg;
            if (advcmsg.getMsg().equals("#addTicket")) {
                TheaterTicket theaterticket = (TheaterTicket) advcmsg.getObjectList().get(0);
                msgObject msg2 = new msgObject("#addTicket", theaterticket);
                try {
                    change(msg2, client);
                    msgObject answer_msg = new msgObject("purchased Successfully");
                    client.sendToClient(answer_msg);
                    MovieShow ms = getMovieShowbyid(theaterticket.getMovieShowid());
                    answer_msg.setObject((TheaterMovie) getMovie(ms.getMovie().getMovieId()));
                    answer_msg.setMsg("updateHallMap");
                    this.sendToAllClients(answer_msg);
                    System.out.println("sending confirmation");
                    sendRefreshcatlogevent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
               /* MovieShow movieShow=(MovieShow) advcmsg.getObjectList().get(1);
                msgObject msg1=new msgObject("#updateMovieShow",movieShow);
                update(msg1,client);
                */
            } else if (advcmsg.getMsg().equals("#deleteTheaterTicket")) {
                System.out.println("deleting theater ticket");
                DeleteTheaterTicket(advcmsg, client);
            }
        }

    }

    private void DeleteTheaterTicket(TheaterTicket theaterTicket, MovieShow ms) {
        try {
            RefundStatistics rs = new RefundStatistics(theaterTicket.getTotalCost(), LocalDate.now());
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            if (!theaterTicket.getScreeningDate().isBefore(LocalDate.now())) {
                EmailUtil.sendTheatetrTicketEmailMScancel(theaterTicket, 1.0);
            }
            Seats seats = ms.getSeats();
            List<Seat> seatList = theaterTicket.getReservedSeats();
            for (Seat st : seatList) {
                seats.unReserveSeat(st.getSeatCol(), st.getSeatRow());
                session.delete(st);
                session.flush();
            }
            session.delete(theaterTicket);
            session.flush();
            session.save(rs);
            session.flush();
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void DeleteTheaterTicket(AdvancedMsg msg, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            TheaterTicket theaterticket = (TheaterTicket) msg.getObjectList().get(0);
            EmailUtil.sendTheatetrTicketEmailCancelation(theaterticket, (double) msg.getObjectList().get(1));
            MovieShow ms = getMovieShowbyid(theaterticket.getMovieShowid());
            Seats seats = ms.getSeats();
            List<Seat> seatList = theaterticket.getReservedSeats();
            for (Seat st : seatList) {//ERROR:A different object with the same identifier value was already associated with the session
                seats.unReserveSeat(st.getSeatCol(), st.getSeatRow());
                session.delete(st);
                session.flush();

            }
            ms.setSeats(seats);
            session.update(ms);
            session.flush();
            session.delete(theaterticket);
            session.getTransaction().commit();
            try {
                client.sendToClient(getAllTickets(theaterticket.getBuyerEmail()));
                System.out.println("sending updated ticket list");
                sendRefreshcatlogevent();
                msgObject updateHallMapMsg = new msgObject("updateHallMap", (TheaterMovie) getMovie(ms.getMovie().getMovieId()));
                this.sendToAllClients(updateHallMapMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.format("Tickets have been sent to the client  %s\n", client.getInetAddress().getHostAddress());
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        }
        if (session != null) {
            session.close();
        }

    }

    private Seat getSeatById(int Seatid) {
        Seat st = (Seat) session.get(Seat.class, Seatid);
        return st;
    }

    private void get(msgObject msgobject, ConnectionToClient client) throws Exception {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        //session.beginTransaction();
        String msgString = msgobject.getMsg();
        System.out.println(msgString);
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

        } else if (msgString.equals("#getAllPriceRequests")) {
            client.sendToClient(getAllRequests());
            System.out.format("Sent all requests to client %s\n", client.getInetAddress().getHostAddress());
        } else if (msgString.equals("#getTickets")) {
            client.sendToClient(getAllTickets((String) msgobject.getObject()));
            System.out.format("Tickets have been sent to the client  %s\n", client.getInetAddress().getHostAddress());

        } else if (msgString.equals("#getSalesReport")) {
            int sales = getcinemarevenue((int) msgobject.getObject());
            msgObject answer_msg = new msgObject("branch revenue", sales);
            client.sendToClient(answer_msg);
            System.out.println("branch sales sent to client");
        } else if (msgString.equals("#getDataForReports")) {
            msgObject answer_msg = getAllTheatres();
            answer_msg.setMsg("openReportPage");
            client.sendToClient(answer_msg);
            System.out.format("Theaters sent to client to open report page client  %s\n", client.getInetAddress().getHostAddress());
        } else if (msgString.equals("#getAllComplaints")) {
            try {
                client.sendToClient(getAllComplaints());
                System.out.println("retrived Complaints");
                System.out.format("Sent Complaints to client %s\n", client.getInetAddress().getHostAddress());

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (msgString.equals("#getBundlesSalesReport")) {
            System.out.println("bla bla report check");
            int sales = getBundlesLinksSales();
            msgObject answer_msg = new msgObject("Bundles and links sales", sales);
            client.sendToClient(answer_msg);
            System.out.println("bundles and links sales value sent to client");
        } else if (msgString.equals("#getAllComplaintsReport")) {
            System.out.println("Complaint list ......");
            msgObject msg = getAllComplaints();
            msg.setMsg("Complaint List Reports");
            client.sendToClient(msg);
        }else if(msgString.equals("#getRefundValue")){
            msgObject msg=new msgObject("The Refund Value",getTicketsRefunds());
            client.sendToClient(msg);
        }
    }

    private void update(msgObject msgObj, ConnectionToClient client) {
        try {


            if (msgObj.getMsg().equals("#updateMovieShow") || msgObj.getMsg().equals("#updateMovieShowTicket")) {

                System.out.println(msgObj.getMsg());
                SessionFactory sessionFactory = getSessionFactory();
                session = sessionFactory.openSession();
                session.beginTransaction();
                change(msgObj, client);
                msgObj.setMsg("movie show updated");
                try {
                    if (msgObj.getMsg().equals("#updateMovieShow") && (msgObj.getMsg().equals("#updateMovieShowTicket") == false)) {//TODO:check it
                        System.out.println("sending updated movie show message");
                        client.sendToClient(msgObj);
                    }
                    sendRefreshcatlogevent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (msgObj.getMsg().equals("#updatePrice")) {
                SessionFactory sessionFactory = getSessionFactory();
                session = sessionFactory.openSession();
                session.beginTransaction();
                PriceRequest pr = (PriceRequest) msgObj.getObject();
                Movie movie = getMovie(pr.getMovieID());
                System.out.println(pr.getMovieID());
                System.out.println(movie.getEngName());
                if (movie.getClass().equals(TheaterMovie.class)) {
                    TheaterMovie Tm = (TheaterMovie) movie;
                    Tm.setEntryPrice(pr.getNewPrice());
                    movie = Tm;
                } else if (movie.getClass().equals(HomeMovie.class)) {
                    HomeMovie Hm = (HomeMovie) movie;
                    Hm.setEntryprice(pr.getNewPrice());
                    movie = Hm;

                }
                session.update(movie);
                session.delete(pr);
                session.flush();
                session.getTransaction().commit(); // Save everything.
                msgObj = getAllRequests();
                msgObj.setMsg("PriceRequestAccRej");
                try {
                    client.sendToClient(msgObj);
                    sendRefreshcatlogevent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (msgObj.getMsg().equals("#updateAnswerToComplaint")) {
                SessionFactory sessionFactory = getSessionFactory();
                session = sessionFactory.openSession();
                session.beginTransaction();
                System.out.println("a I'm in the server for answer");
                Complaint cmp = (Complaint) msgObj.getObject();
                System.out.println(cmp.getAnswer());
                session.update(((Complaint) msgObj.getObject()));
                session.flush();
                session.getTransaction().commit();
                EmailUtil.sendEmailComplaintAnswer(cmp);
                System.out.println("sending email to the client");
                msgObject answer_msg = getAllComplaints();
                answer_msg.setMsg("Complaint answered successfully");
                client.sendToClient(answer_msg);


            } else if (msgObj.getMsg().equals("#updateAnswerToComplaint")) {
                SessionFactory sessionFactory = getSessionFactory();
                session = sessionFactory.openSession();
                session.beginTransaction();
                System.out.println("a I'm in the server for answer");
                Complaint cmp = (Complaint) msgObj.getObject();
                System.out.println(cmp.getAnswer());
                session.update(((Complaint) msgObj.getObject()));
                session.flush();
                session.getTransaction().commit();
                msgObject answer_msg = new msgObject("Complaint answered successfully");
                client.sendToClient(answer_msg);
                EmailUtil.sendEmailComplaintAnswer(cmp);
                System.out.println("sending email to the client");

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
                sendRefreshcatlogevent();
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
                sendRefreshcatlogevent();
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

        } else if (msgObj.getMsg().equals("#deleteRequest")) {
            msgObject answer_msg = new msgObject();
            PriceRequest price = (PriceRequest) msgObj.getObject();
            session.delete((PriceRequest) msgObj.getObject());
            session.getTransaction().commit(); // Save everything
            answer_msg = getAllRequests();
            answer_msg.setMsg("PriceRequestAccRej");
            client.sendToClient(answer_msg);

        } else if (msgObj.getMsg().equals("#deleteTicket")) {
            msgObject answer_msg = new msgObject();
            Ticket ticket2 = (Ticket) msgObj.getObject();
            session.delete((Ticket) msgObj.getObject());
            session.getTransaction().commit(); // Save everything
            answer_msg.setMsg("Ticket deleted");
            client.sendToClient(answer_msg);
        }
        else if (msgObj.getMsg().equals("#addMovieShow")) {
            List<PurpleCard> cardslist=getAllInstructions2();
            MovieShow tempms=(MovieShow) msgObj.getObject();
            for (PurpleCard pc:cardslist){
                if (tempms.getShowDate().isAfter(pc.getStart())&&tempms.getShowDate().isBefore(pc.getEnd())&&!pc.getProjAllowed()){
                    msgObject msg=new msgObject("there is a purple instruction you cant add a movie screening");
                    client.sendToClient(msg);
                    return;
                }
            }
            session.save((MovieShow) msgObj.getObject());
            session.flush();
            session.getTransaction().commit();
            System.out.println("a new movie show added");
            AdvancedMsg tempmsg = new AdvancedMsg("newmovieShowadd");
            MovieShow ms = (MovieShow) msgObj.getObject();
            int movieid = ms.getMovie().getMovieId();
            tempmsg.addobject((List<Theater>) getAllTheatres().getObject());
            tempmsg.addobject(getMovie(movieid));
            try {

                client.sendToClient(tempmsg);
                sendRefreshcatlogevent();
                session.close();
            } catch (IOException e) {
                tempmsg.setMsg("failed");
                client.sendToClient(tempmsg);
                e.printStackTrace();
            }
        } else if (msgObj.getMsg().equals("#updateMovieShow")) {
            List<PurpleCard> cardslist=getAllInstructions2();
            MovieShow tempms=(MovieShow) msgObj.getObject();
            for (PurpleCard pc:cardslist){
                if (tempms.getShowDate().isAfter(pc.getStart())&&tempms.getShowDate().isBefore(pc.getEnd())&&pc.getProjAllowed()==false){
                    msgObject msg=new msgObject("there is a purple instruction you cant add a movie screening");
                    client.sendToClient(msg);
                    return;
                }
            }
            session.update(((MovieShow) msgObj.getObject()));
            System.out.println("test update1");
            session.flush();

            System.out.println("test update1");
            AdvancedMsg tempmsg = new AdvancedMsg("MovieShow Updated");
            MovieShow ms = (MovieShow) msgObj.getObject();
            int movieid = ms.getMovie().getMovieId();
            tempmsg.addobject((List<Theater>) getAllTheatres().getObject());
            tempmsg.addobject(getMovie(movieid));
            client.sendToClient(tempmsg);
        } else if (msgObj.getMsg().equals("#deleteMovieShow")) {
            System.out.println("deleting a movie show");
            try {
                List<TheaterTicket> theaterTicketList = gettheaterticketbymovieshowid(((MovieShow) msgObj.getObject()).getMovieShowId());
                for (TheaterTicket tt : theaterTicketList) {
                    DeleteTheaterTicket(tt, (MovieShow) msgObj.getObject());
                }
                session.delete(((MovieShow) msgObj.getObject()));

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
            try {
                System.out.println(tempmsg.getMsg());
                System.out.println(tempmsg.getClass().toString());
                client.sendToClient(tempmsg);
                System.out.println("message sent to reopen edit page");
                sendRefreshcatlogevent();
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (msgObj.getMsg().equals("#addPriceRequest")) {


            session.save(((PriceRequest) msgObj.getObject()));
            session.flush();
            session.getTransaction().commit();
            System.out.println("a new price change request have been added");
            msgObject answer_msg = getAllRequests();
            answer_msg.setMsg("a price request added");
            client.sendToClient(answer_msg);
            answer_msg.setMsg("refreshPriceRquestAccRej");
            this.sendToAllClients(answer_msg);
        } else if (msgObj.getMsg().equals("#addHomeTicket")) {
            try {
                HomeLinkTicket HLT = (HomeLinkTicket) msgObj.getObject();
                session.save((HomeLinkTicket) msgObj.getObject());
                session.flush();
                session.getTransaction().commit();
                System.out.println("home movie ticket have been bought");
                msgObject answer_msg = new msgObject("HomeMoviePurchasedSuccessfully", null);
                client.sendToClient(answer_msg);
                EmailUtil.sendEmailHomeTicket(HLT);
                System.out.println("Sending an email to the client");
            } catch (Exception e) {
                msgObject answer_msg = new msgObject("failed", null);
                client.sendToClient(answer_msg);
                e.printStackTrace();
            }
        } else if (msgObj.getMsg().equals("#addTicket")) {
            TheaterTicket theaterTicket = (TheaterTicket) msgObj.getObject();
            List<Seat> seatList = theaterTicket.getReservedSeats();
            MovieShow ms = getMovieShowbyid(theaterTicket.getMovieShowid());
            boolean isreservedflag = false;
            for (Seat st : seatList) {
                System.out.println("Row= " + st.getSeatRow() + "Col =" + (st.getSeatCol()));
                if (ms.getSeats().getSeatInfo(st.getSeatCol(), st.getSeatRow()) == true) {
                    isreservedflag = true;
                } else {
                    ms.getSeats().ReserveSeat(st.getSeatCol(), st.getSeatRow());
                }
            }
            if (isreservedflag == false) {
                session.save((TheaterTicket) msgObj.getObject());
                session.flush();

                for (Seat st : seatList) {
                    session.save(st);
                    session.flush();
                }
                session.getTransaction().commit();
                msgObject msg = new msgObject("#updateMovieShowTicket", ms);
                System.out.println(ms.getShowDate());
                update(msg, client);
                EmailUtil.sendTheatetrTicketEmail(theaterTicket);
                System.out.println("Theater ticket have been saved successfully to the data base");

            } else {

                msgObject msg = new msgObject("failed to reserve");
                Movie m = getMovie(ms.getMovie().getMovieId());
                msg.setObject(m);
                client.sendToClient(msg);
            }
        } else if (msgObj.getMsg().equals("#deleteHomeTicket")) {
            EmailUtil.sendHomeTicketEmailCancelation((HomeLinkTicket) msgObj.getObject());
            session.delete((HomeLinkTicket) msgObj.getObject());
            session.flush();
            HomeLinkTicket hlt = (HomeLinkTicket) msgObj.getObject();

            try {
                client.sendToClient(getAllTickets(hlt.getBuyerEmail()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.format("Tickets have been sent to the client  %s\n", client.getInetAddress().getHostAddress());
        } else if (msgObj.getMsg().equals("#addComplaint")) {
            try {
                session.save((Complaint) msgObj.getObject());
                session.flush();
                session.getTransaction().commit();
                System.out.println("a new complaint have been added");
                msgObject answer_msg = new msgObject("a Complaint added", null);
                client.sendToClient(answer_msg);
                answer_msg = getAllComplaints();
                answer_msg.setMsg("RefreshAnswerComplaint");
                this.sendToAllClients(answer_msg);

            } catch (IOException e) {
                msgObject answer_msg = new msgObject("failed", null);
                client.sendToClient(answer_msg);
            }

        } else if (msgObj.getMsg().equals("#addComplaint")) {
            try {
                session.save((Complaint) msgObj.getObject());
                session.flush();
                session.getTransaction().commit();
                System.out.println("a new complaint have been added");
                msgObject answer_msg = new msgObject("a Complaint added", null);
                client.sendToClient(answer_msg);
                answer_msg = getAllComplaints();
                answer_msg.setMsg("RefreshAnswerComplaint");
                this.sendToAllClients(answer_msg);
            } catch (IOException e) {
                msgObject answer_msg = new msgObject("failed", null);
                client.sendToClient(answer_msg);
            }

        }
        // session.getTransaction().commit();
    }

    private static msgObject getAllMovies() throws Exception {
        if (!session.isOpen()) {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
        }
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

    private void sendRefreshcatlogevent() {
        System.out.println("test test new function");
        try {
            msgObject msg = getAllMovies();
            msg.setMsg("RefreshCatalog");
            this.sendToAllClients(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static msgObject getAllTickets(String BuyerEmail) throws Exception {
        if (BuyerEmail.isEmpty()) {
            return new msgObject();
        }
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
        Root<Ticket> root = query.from(Ticket.class);
        query.select(root).where(builder.equal(root.get("buyerEmail"), BuyerEmail));
        ArrayList<Ticket> Data = (ArrayList<Ticket>) session.createQuery(query).getResultList();
        for (Ticket t : Data) {
            if (t.getClass().equals(TheaterTicket.class)) {
                TheaterTicket tk = (TheaterTicket) t;
                tk.getReservedSeats();
            }
        }
        msgObject msg_Answer = new msgObject("AllTickets", Data);
        return msg_Answer;
    }

    private List<TheaterTicket> gettheaterticketbymovieshowid(int movieshowid) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TheaterTicket> query = builder.createQuery(TheaterTicket.class);
        Root<TheaterTicket> root = query.from(TheaterTicket.class);
        query.select(root).where(builder.equal(root.get("movieShowid"), movieshowid));
        ArrayList<TheaterTicket> Data = (ArrayList<TheaterTicket>) session.createQuery(query).getResultList();
        return Data;
    }


    private static msgObject getAllRequests() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PriceRequest> query = builder.createQuery(PriceRequest.class);
        query.from(PriceRequest.class);
        List<PriceRequest> list = session.createQuery(query).getResultList();
        msgObject msg = new msgObject("AllRequests", list);
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
            } else if (m.getMovieId() == id) {
                if (m.getClass().equals(TheaterMovie.class)) {
                    TheaterMovie TH = (TheaterMovie) m;
                    List<MovieShow> msl = TH.getMSList();
                    for (MovieShow ms : msl) {
                        ms.getMovie();
                        ms.getTheater();
                    }
                }
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

    private static msgObject getAllComplaints() throws Exception {
        System.out.println("getting All Complaints");
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Complaint> query = builder.createQuery(Complaint.class);
        query.from(Complaint.class);
        ArrayList<Complaint> data = (ArrayList<Complaint>) session.createQuery(query).getResultList();
        msgObject msg = new msgObject("Complaints", data);
        return msg;
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

    private int getcinemarevenue(int branchid) {
        int sum = 0;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<TheaterTicket> query = builder.createQuery(TheaterTicket.class);
        Root<TheaterTicket> root = query.from(TheaterTicket.class);
        query.select(root).where(builder.equal(root.get("branchid"), branchid));
        ArrayList<TheaterTicket> Data = (ArrayList<TheaterTicket>) session.createQuery(query).getResultList();
        for (TheaterTicket tt : Data) {
            if (tt.getBuyingDate().getMonth().equals(LocalDate.now().getMonth()))
                sum += tt.getTotalCost();
        }
        return sum;
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
        configuration.addAnnotatedClass(Ticket.class);
        configuration.addAnnotatedClass(HomeLinkTicket.class);
        configuration.addAnnotatedClass(TheaterTicket.class);
        configuration.addAnnotatedClass(Seat.class);
        configuration.addAnnotatedClass(Bundle.class);
        configuration.addAnnotatedClass(PurpleCard.class);
        configuration.addAnnotatedClass(CinemaManager.class);
        configuration.addAnnotatedClass(Complaint.class);
        configuration.addAnnotatedClass(RefundStatistics.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private MovieShow getMovieShowbyid(int id) {
        if (!session.isOpen()) {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
        }
        String sqlQ = "FROM MovieShow U WHERE U.id = :u_id";
        Query query = session.createQuery(sqlQ);
        query.setParameter("u_id", id);
        List<MovieShow> list = query.list();
        MovieShow ms = list.get(0);
        return ms;

    }

    private int getBundlesLinksSales() {
        int sum = 0;
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<HomeLinkTicket> query = builder.createQuery(HomeLinkTicket.class);
        Root<HomeLinkTicket> root = query.from(HomeLinkTicket.class);
        ArrayList<HomeLinkTicket> Data = (ArrayList<HomeLinkTicket>) session.createQuery(query).getResultList();
        for (HomeLinkTicket tt : Data) {
            if (tt.getBuyingDate().getMonth().getValue() == LocalDate.now().getMonth().getValue())
                sum += tt.getTotalCost();
        }
        CriteriaQuery<Bundle> query2 = builder.createQuery(Bundle.class);
        Root<Bundle> root2 = query2.from(Bundle.class);
        ArrayList<Bundle> Data2 = (ArrayList<Bundle>) session.createQuery(query2).getResultList();
        for (Bundle b : Data2) {
            if (b.getDate().getMonth().equals(LocalDate.now().getMonth()))
                sum += 20;
        }
        return sum;
    }

    int getTicketsRefunds() {
        int sum = 0;

        LocalDate st = LocalDate.of(LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                1);

        LocalDate ed = LocalDate.of(LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                LocalDate.now().getMonth().length(LocalDate.now().isLeapYear()));

        System.out.println(st + "\n" + ed + ".");

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            String sqlQ = "FROM RefundStatistics R WHERE R.date BETWEEN :st AND :ed";
            Query query = session.createQuery(sqlQ);
            query.setParameter("st", st);
            query.setParameter("ed", ed);
            List<RefundStatistics> list = query.list();

            for (RefundStatistics rs : list) {
                sum += rs.getRefund();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
        return sum;
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

    public static void addCinemaManager() {
        try {
            CinemaManager CM = new CinemaManager("Wajeeh", "9e1f0bda8", "wajeeh", "atrash", 4, 1);
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(CM);
            session.getTransaction().commit(); // Save everything.
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }


    public static void addserviceWorker() {
        try {
            User serviceWorker = new User("Yaseen", "9e1f0bda8", "Yaseen", "AbedElhalim", 2);
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(serviceWorker);
            session.getTransaction().commit(); // Save everything.
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public static void addcontentworker() {
        try {
            User serviceWorker = new User("Yamen", "9e1f0bda8", "yamen", "masallha", 3);
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(serviceWorker);
            session.getTransaction().commit(); // Save everything.
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }


    void getBundles(msgObject msg, ConnectionToClient client) {
        String Email = msg.getObject().toString();
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        String sqlQ = "FROM Bundle U WHERE U.email = :u_email";
        Query query = session.createQuery(sqlQ);
        query.setParameter("u_email", Email);
        List<Bundle> list = query.list();
        msg.setMsg("SentYourBundles");
        msg.setObject(list);
        try {
            client.sendToClient(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }

    void buyBundle(msgObject msg, ConnectionToClient client) {
        Bundle bundle = (Bundle) msg.getObject();
        msgObject m = new msgObject("BundleBought");

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(bundle);
            session.flush();
            session.getTransaction().commit(); // Save everything.
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("BuyingBundleError");
            m.setMsg("BuyingBundleError");
            exception.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
        try {
            client.sendToClient(m);
        } catch (IOException e) {
            e.printStackTrace();
        }

        EmailUtil.sendEmail(bundle.getEmail(), "Cinema Bundle", "Your bundle has been bought successfully, thanks for using our services!");

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
            MovieShow ms = new MovieShow(m, localD1, th, "20:00", "22:00", String.valueOf(hall.getHallNumber()), hall.getCapacity());
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
            Hall hall2 = new Hall(50, th2, 1);
            session.save(hall2);
            session.flush();
            Date d2 = new Date(2021 - 1900, 10, 5);
            instant = d2.toInstant();
            LocalDate localD2 = instant.atZone(defaultZoneId).toLocalDate();
            MovieShow ms2 = new MovieShow(m2, localD2, th2, "19:00", "21:00", String.valueOf(hall2.getHallNumber()), hall2.getCapacity());
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
            Hall hall3 = new Hall(20, th3, 2);
            session.save(hall3);
            session.flush();
            Date d3 = new Date(2021 - 1900, 2, 12);
            instant = d3.toInstant();
            LocalDate localD3 = instant.atZone(defaultZoneId).toLocalDate();
            MovieShow ms3 = new MovieShow(m3, localD3, th3, "12:00", "14:00", String.valueOf(hall3.getHallNumber()), hall3.getCapacity());
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


    private void DeleteMovieShow(MovieShow ms) {

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            List<TheaterTicket> theaterTicketList = gettheaterticketbymovieshowid(ms.getMovieShowId());
            for (TheaterTicket tt : theaterTicketList) {
                DeleteTheaterTicket(tt, ms);
            }
            session.delete(ms);
            session.flush();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null)
                session.close();
        }
        System.out.println("MovieShow Deleted");


    }


    void EditInstruction(msgObject msg, ConnectionToClient client) {
        PurpleCard card = (PurpleCard) msg.getObject();
        PurpleCard temp = card;
        String s = msg.getMsg();
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            if (s.equals("AddInstruction")) {
                session.save(card);

            } else if (s.equals("UpdateInstruction")) {
                session.update(card);
            } else if (s.equals("DeleteInstruction"))
                session.delete(card);
            session.flush();
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
        msg.setObject(null);
        if (s.equals("AddInstruction"))
            msg.setMsg("AddedInstruction");
        else if (s.equals("UpdateInstruction"))
            msg.setMsg("UpdatedInstruction");
        else if (s.equals("DeleteInstruction"))
            msg.setMsg("DeletedInstruction");

        try {
            client.sendToClient(msg);
            System.out.println(msg.getMsg());
            System.out.println("problem problem");

            if (s.equals("AddInstruction") || s.equals("UpdateInstruction")) {
                InstructionEmailReturn(temp);
                sendRefreshcatlogevent();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void getAllInstructions(msgObject msg, ConnectionToClient client) {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        String sqlQ = "FROM PurpleCard";
        Query query = session.createQuery(sqlQ);
        List<PurpleCard> list = query.list();
        msg.setMsg("SentPurpleCards");
        msg.setObject(list);
        try {
            client.sendToClient(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }

    List<PurpleCard> getAllInstructions2() {
        if (!session.isOpen()) {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
        }
        String sqlQ = "FROM PurpleCard";
        Query query = session.createQuery(sqlQ);
        List<PurpleCard> list = query.list();

        return list;
    }

    void InstructionEmailReturn(PurpleCard card) {

        boolean proj = card.getProjAllowed();
        LocalDate start = card.getStart();
        LocalDate end = card.getEnd();

        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<MovieShow> query1 = builder.createQuery(MovieShow.class);
        query1.from(MovieShow.class);
        ArrayList<MovieShow> movieShows = (ArrayList<MovieShow>) session.createQuery(query1).getResultList();

        CriteriaQuery<TheaterTicket> query2 = builder.createQuery(TheaterTicket.class);
        query2.from(TheaterTicket.class);
        ArrayList<TheaterTicket> tickets = (ArrayList<TheaterTicket>) session.createQuery(query2).getResultList();

        session.close();

        if (proj) {
            InstructionShowsProcess(card, tickets, movieShows);

        } else {
            for (TheaterTicket tt : tickets) {
                if ((tt.getScreeningDate().isAfter(start) && tt.getScreeningDate().isBefore(end))
                        || tt.getScreeningDate().isEqual(start) || tt.getScreeningDate().isEqual(end)) {
                    EmailUtil.sendEmail(tt.getBuyerEmail(), "Show Cancellation", "The show on "
                            + tt.getScreeningDate() + " for the movie " + tt.getMovieName() +
                            " has been cancelled, you'll be refunded with " + tt.getTotalCost() + "₪ soon.");
                    DeleteTheaterTicket(tt, getMovieShowbyid(tt.getMovieShowid()));
                }
            }

            for (MovieShow ms : movieShows) {
                if ((ms.getShowDate().isAfter(start) && ms.getShowDate().isBefore(end))
                        || ms.getShowDate().isEqual(start) || ms.getShowDate().isEqual(end)) {
                    DeleteMovieShow(ms);
                }
            }

        }
       // System.out.println(getTicketsRefunds());
    }

    void InstructionShowsProcess(PurpleCard card, ArrayList<TheaterTicket> tickets, ArrayList<MovieShow> movieShows) {
        LocalDate start = card.getStart();
        LocalDate end = card.getEnd();
        int maxCap = card.getMaxCap();

        SessionFactory sessionFactory;
        MovieShow temp = null;

        for (MovieShow ms : movieShows) {
            if ((ms.getShowDate().isAfter(start) && ms.getShowDate().isBefore(end))
                    || ms.getShowDate().isEqual(start)
                    || ms.getShowDate().isEqual(end)) {
                if (ms.getSeats().getNumofSeats() > allowedCap(ms.getCapacity(), maxCap)
                        && ms.getSeats().getResSeats() <= allowedCap(ms.getCapacity(), maxCap))
                    ms.setCapacity(allowedCap(ms.getCapacity(), maxCap));
                else if (ms.getSeats().getNumofSeats() > allowedCap(ms.getCapacity(), maxCap)) {
                    for (TheaterTicket tt : tickets) {
                        if (tt.getMovieShowid() == ms.getMovieShowId()) {
                            if ((tt.getScreeningDate().isAfter(start) && tt.getScreeningDate().isBefore(end))
                                    || tt.getScreeningDate().isEqual(start)
                                    || tt.getScreeningDate().isEqual(end)) {

                                EmailUtil.sendEmail(tt.getBuyerEmail(), "Show Cancellation", "Due to new instructions, the show on "
                                        + tt.getScreeningDate() + "for the movie " + tt.getMovieName() +
                                        " is being restricted to a lower number of customers , you'll be refunded with " + tt.getTotalCost() + "₪ soon.");

                                sessionFactory = getSessionFactory();
                                session = sessionFactory.openSession();

                                temp = getMovieShowbyid(tt.getMovieShowid());

                                if (session != null)
                                    session.close();

                                DeleteTheaterTicket(tt, temp);

                                if (ms.getSeats().getNumofSeats() <= allowedCap(ms.getCapacity(), maxCap)) break;
                                else if (ms.getSeats().getResSeats() <= allowedCap(ms.getCapacity(), maxCap)) {
                                    ms.setCapacity(allowedCap(ms.getCapacity(), maxCap));
                                    break;
                                }
                            }
                        }
                    }
                    ms.setCapacity(allowedCap(ms.getCapacity(), maxCap));
                    updateMovieShow(ms);

                }

            }

        }

    }

    int allowedCap(int hallCap, int maxCap) {

        if (1.2 * maxCap <= hallCap) return maxCap;
        if (hallCap > 0.8 * maxCap) return (int) (0.8 * maxCap);
        return hallCap / 2;

    }


    void updateMovieShow(MovieShow ms) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(ms);
            session.flush();
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