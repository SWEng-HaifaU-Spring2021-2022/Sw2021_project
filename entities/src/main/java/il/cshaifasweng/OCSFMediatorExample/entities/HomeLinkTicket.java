package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Table(name="HomeTickets")
public class HomeLinkTicket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int homeTicketID;
    private String buyerName;
    private String buyerEmail;
    private LocalDate linkDate;
    private LocalTime startingTime;
    private LocalTime expirationTime;
    private String visaNumber;
    private String cvv;
    private String movieName;
    private String link;
    private boolean isSent;
    public HomeLinkTicket(){}

    public HomeLinkTicket(String buyerName, String buyerEmail, LocalDate linkDate, LocalTime startingTime, LocalTime expirationTime, String visaNumber, String cvv, String movieName, String link) {
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        this.linkDate = linkDate;
        this.startingTime = startingTime;
        this.expirationTime = expirationTime;
        this.visaNumber = visaNumber;
        this.cvv = cvv;
        this.movieName=movieName;
        this.link=link;
        this.isSent=false;
    }

    public int getHomeTicketID() {
        return homeTicketID;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public LocalDate getLinkDate() {
        return linkDate;
    }

    public void setLinkDate(LocalDate linkDate) {
        this.linkDate = linkDate;
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

    public String getVisaNumber() {
        return visaNumber;
    }

    public void setVisaNumber(String visaNumber) {
        this.visaNumber = visaNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
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
    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

}
