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
import java.io.Serializable;
import java.util.ArrayList;
import java.io.Serializable;
@Entity
@Table(name = "hall")
public class Hall implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Hallid;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id")
	private Theater theater;
	private int hallId;
	private int capacity;
	/*private int theaterId;*/
	private int hallNumber;
	private boolean[][] seatsMatrix;
	
	public Hall()
	{
		
	}
	public Hall(int capacity,Theater theater, int hallNumber)
	{
		this.capacity = capacity;
		this.hallNumber = hallNumber;
		this.theater=theater;
		this.seatsMatrix=new boolean[5][5];
	}
	public int getHallId() {
		return hallId;
	}
	public void setHallId(int hallId) {
		this.hallId = hallId;
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