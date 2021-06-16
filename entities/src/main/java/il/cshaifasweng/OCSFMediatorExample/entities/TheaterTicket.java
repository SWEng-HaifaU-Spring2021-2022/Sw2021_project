package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Ticket")
public class TheaterTicket extends  Ticket implements Serializable {
    String branch;
    String hall;
    LocalTime startingTime;
    LocalTime endingTime;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket")
    List<Seat>reservedSeats;
    int movieShowid;
    public TheaterTicket(){}
    public TheaterTicket(String buyerEmail, String movieName, LocalDate screeningDate, String buyerName, String visaNumber, String cvv, String branch, String hall, LocalTime startingTime, LocalTime endingTime,int movieShowid) {
        super(buyerEmail, movieName, screeningDate, buyerName, visaNumber, cvv);
        this.branch = branch;
        this.hall = hall;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.reservedSeats = new ArrayList<>();
        this.movieShowid=movieShowid;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }

    public LocalTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalTime endingTime) {
        this.endingTime = endingTime;
    }

    public List<Seat> getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(List<Seat> reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

    public int getMovieShowid() {
        return movieShowid;
    }

    public void setMovieShowid(int movieShowid) {
        this.movieShowid = movieShowid;
    }
}
