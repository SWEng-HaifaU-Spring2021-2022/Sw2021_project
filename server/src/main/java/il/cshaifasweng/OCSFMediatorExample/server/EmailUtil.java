package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.entities.HomeLinkTicket;
import il.cshaifasweng.OCSFMediatorExample.entities.Seat;
import il.cshaifasweng.OCSFMediatorExample.entities.TheaterTicket;
import java.util.Date;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class EmailUtil {

    /**
     * Utility method to send simple HTML email
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */

    public static void sendEmail(String toEmail, String subject, String body){
        try
        {

            final String fromEmail = "sirtyacinema@gmail.com"; //requires valid gmail id
            final String password = "9e1f0bda8"; // correct password for gmail id

            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("sirtyacinema@gmail.com", "Sirtya G8"));

            msg.setReplyTo(InternetAddress.parse("sirtyacinema@gmail.com", false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sendEmailHomeTicket(HomeLinkTicket HLT){
        try
        {

            final String fromEmail = "sirtyacinema@gmail.com"; //requires valid gmail id
            final String password = "9e1f0bda8"; // correct password for gmail id

            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("sowmiyanagarajan98@gmail.com", "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse("sowmiyanagarajan98@gmail.com", false));

            msg.setSubject("Payment Confirmation", "UTF-8");
            String body="Thanks for choosing us you bought a link for,"+ HLT.getMovieName()+" at :"+ HLT.getScreeningDate()+" the link will be valid at: "+ HLT.getStartingTime()+ " until : "+ HLT.getExpirationTime()+"at the same day you will recive a reminder before an hour from the movie enjoy the total cost= "+HLT.getTotalCost()+"\n"+HLT.getLink();
            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(HLT.getBuyerEmail(), false));
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sendEmailRemainder(HomeLinkTicket HLT){
        try
        {

            final String fromEmail = "sirtyacinema@gmail.com"; //requires valid gmail id
            final String password = "9e1f0bda8"; // correct password for gmail id

            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("sirtyacinema@gmail.com", "Sirtya G8"));

            msg.setReplyTo(InternetAddress.parse("sirtyacinema@gmail.com", false));

            msg.setSubject("Movie Remainder", "UTF-8");
            String body="this a remainder for you the link that you bought for "+HLT.getMovieName()+"will be available after one hour from now ( at"+HLT.getStartingTime() +")";
            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(HLT.getBuyerEmail(), false));
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static  void sendTheatetrTicketEmail(TheaterTicket TH){
        try
        {

            final String fromEmail = "sirtyacinema@gmail.com"; //requires valid gmail id
            final String password = "9e1f0bda8"; // correct password for gmail id

            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("sirtyacinema@gmail.com", "Sirtya G8"));

            msg.setReplyTo(InternetAddress.parse("sirtyacinema@gmail.com", false));

            msg.setSubject("Payment Confirmation", "UTF-8");
            String body="Thanks for choosing us you bought a Ticket for,"+ TH.getMovieName()+" Branch: "+TH.getBranch()+" Hall: "+TH.getHall()+" "+TH.getScreeningDate()+"Your chosen seats:\n";
            for(Seat st: TH.getReservedSeats()){
                body=body+"Row: "+st.getSeatCol()+" Col"+st.getSeatRow()+"\n";
            }
            body+=" the Total Cost= "+TH.getTotalCost();
            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TH.getBuyerEmail(), false));
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static  void sendTheatetrTicketEmailCancelation(TheaterTicket TH,double refundPer){
        try
        {

            final String fromEmail = "sirtyacinema@gmail.com"; //requires valid gmail id
            final String password = "9e1f0bda8"; // correct password for gmail id

            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("sirtyacinema@gmail.com", "Sirtya G8"));

            msg.setReplyTo(InternetAddress.parse("sirtyacinema@gmail.com", false));

            msg.setSubject("Reservation Cancelation", "UTF-8");
            String body="Your Reservation for ,"+ TH.getMovieName()+" at Branch: "+TH.getBranch()+" Hall: "+TH.getHall()+" "+TH.getScreeningDate()+"for the following seats:\n";
            for(Seat st: TH.getReservedSeats()){
                body=body+"Row: "+st.getSeatCol()+" Col"+st.getSeatRow()+"\n";
            }
            body+="has been canceled as you wish\n";
            body+=" the Total refund value is= "+TH.getTotalCost()*refundPer+"NIS";
            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TH.getBuyerEmail(), false));
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static  void sendHomeTicketEmailCancelation(HomeLinkTicket hlt){
        try
        {

            final String fromEmail = "sirtyacinema@gmail.com"; //requires valid gmail id
            final String password = "9e1f0bda8"; // correct password for gmail id

            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("sirtyacinema@gmail.com", "Sirtya G8"));

            msg.setReplyTo(InternetAddress.parse("sirtyacinema@gmail.com", false));

            msg.setSubject("Reservation Cancelation", "UTF-8");
            String body="Your Link for ,"+ hlt.getMovieName()+" That starts at "+hlt.getStartingTime().toString()+" and ends at: "+hlt.getExpirationTime().toString()+" at: "+hlt.getScreeningDate();

            body+=" has been canceled as you wish\n";
            body+=" the Total refund value is= "+hlt.getTotalCost()*0.5+"NIS";
            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(hlt.getBuyerEmail(), false));
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
