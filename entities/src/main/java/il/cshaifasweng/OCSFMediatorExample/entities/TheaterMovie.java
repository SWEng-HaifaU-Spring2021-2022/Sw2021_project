package il.cshaifasweng.OCSFMediatorExample.entities;
import java.awt.image.ImageObserver;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Observable;

@Entity
@Table(name="movie")
public class TheaterMovie extends Movie implements Serializable  {
	private static final long serialVersionUID = -8224097662914849956L;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
	private List<MovieShow> MSList;
	private int entryPrice;
	public TheaterMovie() {}
	public TheaterMovie(String engName, String hebName, String actors, String genere, String description, String producer, String imgURL,int entryPrice) {
		super(engName,hebName,actors,genere,description,producer,imgURL);
		this.entryPrice=entryPrice;
		MSList=new ArrayList<>();
	}
	public int getEntryPrice() {
		return entryPrice;
	}
	public void setEntryPrice(int entryPrice) {
		this.entryPrice = entryPrice;
	}
	public List<MovieShow> getMSList() {
		String str="";
		for (MovieShow ms:this.MSList){
			str+=ms.toString()+"\n";
		}
		return MSList;
	}
	public void AddMovieShow(MovieShow Ms) {
		this.MSList.add(Ms);
	}
}