package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="priceRequests")
public class PriceRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;
    private Integer oldPrice;
    private Integer newPrice;
    private String description;
    private int movieID;

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    // private Movie movie;
    public PriceRequest(){}

    public Integer getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Integer oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Integer getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Integer newPrice) {
        this.newPrice = newPrice;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PriceRequest(Integer oldPrice, Integer newPrice, String description){
        this.oldPrice=oldPrice;
        this.newPrice=newPrice;
        this.description=description;
    }

}
