package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.print.DocFlavor;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.Serializable;
@Entity
@Table(name = "hall")
public class Hall implements Serializable {
	private static final long serialVersionUID = -8224097662914849956L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int hallId;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "theater_id")
	private Theater theater;
	private int capacity;
	private int hallNumber;
	//private Seats seatsPlaces;

	public Hall()
	{

	}

	public Hall(int capacity,Theater theater, int hallNumber)
	{
		this.capacity = capacity;
		this.hallNumber = hallNumber;
		setTheater(theater);
		int RowsNum= (int) Math.round (capacity/10.0);
		//this.seatsPlaces= new Seats(capacity);
	}

	public int gethallId() {
		return hallId;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
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
	@Override
	public String toString() {
		return String.valueOf(this.hallNumber);
	}

	/*public Seats getSeatsPlaces() {
		return seatsPlaces;
	}

	public void setSeatsPlaces(Seats seatsPlaces) {
		this.seatsPlaces = seatsPlaces;
	}*/
}