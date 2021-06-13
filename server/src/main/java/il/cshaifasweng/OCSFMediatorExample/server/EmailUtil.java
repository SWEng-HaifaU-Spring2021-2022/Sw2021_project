package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.HomeLinkTicket;

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

            msg.setFrom(new InternetAddress("sowmiyanagarajan98@gmail.com", "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse("sowmiyanagarajan98@gmail.com", false));

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
            String body="Thanks for choosing us you bought a link for,"+ HLT.getMovieName()+" at Date:"+ HLT.getLinkDate()+" the link will be valid at: "+ HLT.getStartingTime()+ " until : "+ HLT.getExpirationTime()+"at the same day you will recive a reminder before an hour from the movie enjoy \n"+HLT.getLink();
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

            msg.setFrom(new InternetAddress("sowmiyanagarajan98@gmail.com", "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse("sowmiyanagarajan98@gmail.com", false));

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
}
