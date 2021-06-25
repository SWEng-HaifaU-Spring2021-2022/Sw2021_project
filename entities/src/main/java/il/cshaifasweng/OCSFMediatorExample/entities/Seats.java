package il.cshaifasweng.OCSFMediatorExample.entities;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.Serializable;

public class Seats implements Serializable {
    private static final long serialVersionUID = -2779473215117112413L;
    private Boolean[] seats;
    int Rowsnum;
    public Seats(){}
    public Seats (int capacity){
         Rowsnum= (int) Math.round(capacity/10.0);
        seats=new Boolean[capacity];
        for(int i=0;i<capacity;++i){
                seats[i]=false;
        }
    }
    public void ReserveSeat(int Row,int Col){
        int index=Row*10+Col;
        seats[index]=true;
    }
    public void unReserveSeat(int Row,int Col){
        int index=Row*10+Col;
        seats[index]=false;
    }
    public int getRowsnum() {
        return Rowsnum;
    }
    public boolean getSeatInfo(int Row,int Col){
        int index=Row*10+Col;
        return  seats[index];
    }
    public void setRowsnum(int rowsnum) {
        Rowsnum = rowsnum;
    }
}
