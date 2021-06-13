package il.cshaifasweng.OCSFMediatorExample.entities;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Ticket")
public class Ticket implements Serializable {
	private static final long serialVersionUID = -8224097662914849956L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	int TicketNumber;
	String OwnerName;
	String Email;
	int MovieID;
	String HallNumber;
	String SeatNumber;
	String MovieDate;
	int BuyerID;
	
	
	public Ticket() {}

	public Ticket(int TicketNumber, String OwnerName, String Email, int MovieID, String HallNumber,
			String SeatNumber, String MovieDate, int BuyerID) {
		this.TicketNumber = TicketNumber;
		this.OwnerName = OwnerName;
		this.Email = Email;
		this.MovieID = MovieID;
		this.HallNumber = HallNumber;
		this.SeatNumber = SeatNumber;
		this.MovieDate = MovieDate;
		this.BuyerID = BuyerID;
	}
	
	public int getTicketNumber() {
		return this.TicketNumber;
	}
	
	public String getOwnerName() {
		return this.OwnerName;
	}
	
	public String getEmail() {
		return this.Email;
	}
	
	public int getMovieID() {
		return this.MovieID;
	}
	
	public String getHallNumber() {
		return this.HallNumber;
	}
	
	public String getSeatNumber() {
		return this.SeatNumber;
	}
	
	public String getMovieDate() {
		return this.MovieDate;
	}
	
	public int getBuyerID() {
		return this.BuyerID;
	}
	
	
	
	
	public void setTicketNumber(int TicketNumber) {
		this.TicketNumber = TicketNumber;
	}
	
	public void setOwnerName(String OwnerName) {
		this.OwnerName = OwnerName;
	}
	
	public void setEmail(String Email) {
		this.Email = Email;
	}
	
	public void setMovieID(int MovieID) {
		this.MovieID = MovieID;
	}
	
	public void setHallNumber(String HallNumber) {
		this.HallNumber = HallNumber;
	}
	
	public void setSeatNumber(String SeatNumber) {
		this.SeatNumber = SeatNumber;
	}
	
	public void setMovieDate(String MovieDate) {
		this.MovieDate = MovieDate;
	}
	
	public void setBuyerID(int BuyerID) {
		this.BuyerID = BuyerID;
	}
	
}
