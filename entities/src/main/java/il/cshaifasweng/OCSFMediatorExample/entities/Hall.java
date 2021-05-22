package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.Serializable;

@Entity
@Table(name = "hall")
public class Hall implements Serializable {

	private static final long serialVersionUID = -8224097662914849956L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Hallid;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id")
	private Theater theater;
	private int hallId;
	private int capacity;
	private int hallNumber;
	//private boolean[][] seatsMatrix;
	
	public Hall()
	{
		
	}
	public Hall(int capacity,Theater theater, int hallNumber)
	{
		this.capacity = capacity;
		this.hallNumber = hallNumber;
		setTheater(theater);
		//this.seatsMatrix=new boolean[5][5];
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