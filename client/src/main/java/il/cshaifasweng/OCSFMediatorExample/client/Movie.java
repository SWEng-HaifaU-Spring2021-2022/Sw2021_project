package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDateTime;

public class Movie {
    private int movieid;
    private ImageView img;
    private String EngName;
    private String HebNmae;
    private String Actors;
    private String Producer;
    private String Description;
    private int rating;
    private LocalDateTime ShowTime;
    private Boolean showInTheater;
    private String Genere;
    public Movie(int id, ImageView img, String engName, String hebName, String Actors, String Producer, String description, int rating, LocalDateTime showTime, Boolean showInTheater, String genere){
        this.movieid=id;
        this.img=img;
        this.EngName=engName;
        this.HebNmae=hebName;
        this.Actors=Actors;
        this.Producer=Producer;
        this.Description=description;
        this.rating = rating;
        this.ShowTime = showTime;
        this.showInTheater = showInTheater;
        Genere = genere;
        //System.out.println("12313 const");
    }

    public int getMovieid() {
        return movieid;
    }

   public ImageView getImg() {
        return img;
    }

    public String getEngName() {
        return EngName;
    }

    public String getHebNmae() {
        return HebNmae;
    }

    public String getActors() {
        return Actors;
    }

    public String getProducer() {
        return Producer;
    }

    public String getDescription() {
        return Description;
    }

    public int getRating() {
        return rating;
    }

    public LocalDateTime getShowTime() {
        return ShowTime;
    }

    public Boolean getShowInTheater() {
        return showInTheater;
    }

    public String getGenere() {
        return Genere;
    }
}
