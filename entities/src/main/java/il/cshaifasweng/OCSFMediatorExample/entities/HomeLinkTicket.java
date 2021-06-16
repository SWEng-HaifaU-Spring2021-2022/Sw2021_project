package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Table(name="Ticket")
public class HomeLinkTicket  extends Ticket  implements  Serializable{

    private LocalTime startingTime;
    private LocalTime expirationTime;
    private String link;
    private boolean isSent;
    public HomeLinkTicket(){}

    public HomeLinkTicket(String buyerEmail, String movieName, LocalDate screeningDate, String buyerName, String visaNumber, String cvv, LocalTime startingTime, LocalTime expirationTime, String link) {
        super(buyerEmail, movieName, screeningDate, buyerName, visaNumber, cvv);
        this.startingTime = startingTime;
        this.expirationTime = expirationTime;
        this.link = link;
        this.isSent = false;
    }

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }

    public LocalTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }
}
