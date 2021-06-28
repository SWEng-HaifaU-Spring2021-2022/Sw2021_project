package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="seats")
public class Seat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatdId;
    @Column(name = "SeatCol")
    private int seatRow;
    @Column(name = "SeatRow")
    private int seatCol;
    private int state;//0-not reserved  1-reserved   2 -choosen*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private TheaterTicket ticket;
   public  Seat(){};
   public Seat(int Row,int Col,int state){
       this.seatRow=Col;
       this.seatCol=Row;
       this.state=state;
   }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }

    public int getSeatCol() {
        return seatCol;
    }

    public void setSeatCol(int seatCol) {
        this.seatCol = seatCol;
    }

    @Override
    public String toString(){
        String str="Row: "+seatCol + "Col: "+seatRow+"\n";
        return  str;
    }
   public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public TheaterTicket getTicket() {
        return ticket;
    }

    public void setTicket(TheaterTicket ticket) {
        this.ticket = ticket;
    }
}
