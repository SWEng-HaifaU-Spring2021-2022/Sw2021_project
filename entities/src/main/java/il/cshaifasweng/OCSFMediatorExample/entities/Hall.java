package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.Serializable;

@Entity
@Table(name = "hall")
public class Hall implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hallId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;
    private int capacity;
    /*private int theaterId;*/
    private int hallNumber;
    @Column(columnDefinition = "LONGBLOB")
    private byte[] seatsMatrix;

    public Hall() {

    }

    public Hall(int capacity, Theater theater, int hallNumber) {
        this.capacity = capacity;
        this.hallNumber = hallNumber;
        this.theater = theater;
        this.seatsMatrix = new byte[25];

    }

    public boolean getSeat(int row, int col) throws Exception {
        if (row * col < seatsMatrix.length) return (seatsMatrix[row * col - 1] == 0);
        else throw new Exception("Out of range");
    }

    public void setSeat(int row, int col, boolean val) throws Exception {
        if (row * col < seatsMatrix.length) {
            if (val) seatsMatrix[row * col - 1] = 1;
            else seatsMatrix[row * col - 1] = 0;
        } else throw new Exception("Out of range");
    }

    public byte[] getSeatsMatrix() {
        return seatsMatrix;
    }

    public void setSeatsMatrix(byte[] seatsMatrix) {
        this.seatsMatrix = seatsMatrix;
    }

    public int getHallId() {
        return hallId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /*public int getTheaterId() {
        return theaterId;
    }
    public void setTheaterId(int theaterId) {
        this.theaterId = theaterId;
    }*/
    public int getHallNumber() {
        return hallNumber;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public void setHallNumber(int hallNumber) {
        this.hallNumber = hallNumber;
    }


}