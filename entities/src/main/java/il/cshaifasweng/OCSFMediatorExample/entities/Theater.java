package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.io.Serializable;
@Entity
@Table(name = "theaters")
public class Theater implements Serializable {
	private static final long serialVersionUID = -8224097662914849956L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int theaterId;
	private String location;
	@OneToMany(fetch=FetchType.LAZY, mappedBy="theater")
	 private List<Hall> halls;
	@OneToMany(fetch=FetchType.LAZY,mappedBy="theater")
	private List<MovieShow>movieShowList;
	/*machinesList*/
	public Theater()
	{
		
	}
	public Theater(String location)
	{
		this.location = location;
		this.halls = new ArrayList<>();
		this.movieShowList=new ArrayList<>();
	}
	public int getTheaterId() {
		return theaterId;
	}
	public void setTheaterId(int theaterId) {
		this.theaterId = theaterId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<Hall> getHalls() {
		return halls;
	}
	public void AddHalls(Hall hall) {
		halls.add(hall);
	}
	public void ADdMovieShow(MovieShow ms) {
		this.movieShowList.add(ms);
	}
	
}
