package il.cshaifasweng.OCSFMediatorExample.entities;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;
import java.io.Serializable;
import java.io.Serializable;
import java.util.Date;
import java.sql.Time;
import java.util.ArrayList;

@Entity
@Table(name = "movieShow")
public class MovieShow implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieShowId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id")
    private Movie movie;
    private LocalDate showDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theater_id")
    private Theater theater;
    private String beginTime;
    private String endTime;
    private String hallnumber;
    @Lob
    @Column(name = "seats", columnDefinition = "BLOB")
    private Seats seats;
    //private boolean[][]seats;
    private int Capacity;


    public MovieShow() {

    }

    public MovieShow(Movie movie, LocalDate showDate, Theater theater, String beginTime, String endTime, String hallnumber, int Capacity) {
        setMovie(movie);
        this.showDate = showDate;
        setTheater(theater);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.Capacity = Capacity;
        this.seats = new Seats(Capacity);
        this.hallnumber = hallnumber;
    }

    public int getMovieShowId() {
        return movieShowId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = showDate.toString(); //formatter.format(showDate);
        String str = "" + strDate + "-begins at:" + beginTime + "-end at:" + endTime + " at theater :" + theater.getLocation() + "\n";
        //System.out.println(theater.getLocation());
        return str;
    }

    public Seats getSeats() {
        return seats;
    }

    public void setSeats(Seats seats) {
        this.seats = seats;
    }

    public String getHallnumber() {
        return hallnumber;
    }

    public void setHallnumber(String hallnumber) {
        this.hallnumber = hallnumber;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {

        this.Capacity = capacity;
        Boolean[] ns = new Boolean[capacity];
        Boolean[] os = this.seats.getSeats();

        for (int i = 0; i < capacity; i++) {
            if (i < seats.getResSeats())
                ns[i] = true;
            else ns[i] = false;
        }

        this.seats.setSeats(ns);

    }
}