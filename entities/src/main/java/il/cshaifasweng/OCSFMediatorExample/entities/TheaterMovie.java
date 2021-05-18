package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="movie")
public class TheaterMovie extends Movie implements Serializable  {
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
	private List<MovieShow> MSList;
	private int entryPrice;
	public TheaterMovie() {}
	public TheaterMovie(String engName, String hebName, String[] actors, String genere, String description, String producer, byte[] image,int entryPrice) {
		super(engName,hebName,actors,genere,description,producer,image);
		this.entryPrice=entryPrice;
		MSList=new  ArrayList<MovieShow>();
	}
	public int getEntryPrice() {
		return entryPrice;
	}
	public void setEntryPrice(int entryPrice) {
		this.entryPrice = entryPrice;
	}
	public List<MovieShow> getMSList() {
		return MSList;
	}
	public void AddMovieShow(MovieShow Ms) {
		this.MSList.add(Ms);
	}
}
