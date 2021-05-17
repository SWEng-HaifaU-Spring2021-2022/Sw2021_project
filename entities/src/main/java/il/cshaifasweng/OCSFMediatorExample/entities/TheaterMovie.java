package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="theatermovies")
public class TheaterMovie extends Movie {
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
