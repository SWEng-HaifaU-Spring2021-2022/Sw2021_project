package il.cshaifasweng.OCSFMediatorExample.entities;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
@Entity
@Table(name = "movieShow")
public class MovieShow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int movieShowId;
	 private Movie movie;
	 private Date showDate;
	 private Theater theater;
	 private Time beginTime;
	 private Time endTime;
	 private int maxNumber; 
	 
	 public MovieShow()
	 {
		 
	 }
	 public MovieShow(Movie movie, Date showDate, Theater theater,Time beginTime, Time endTime,int maxNumber)
	 {
		 this.movie = movie;
		 this.showDate = showDate;
		 this.theater = theater;
		 this.beginTime = beginTime;
		 this.endTime = endTime;
		 this.maxNumber = maxNumber;
	 }
	public int getMovieShowId() {
		return movieShowId;
	}
	public void setMovieShowId(int movieShowId) {
		this.movieShowId = movieShowId;
	}
	public Movie getMovieId() {
		return movie;
	}
	public void setMovieId(Movie movie) {
		this.movie = movie;
	}
	public Date getShowDate() {
		return showDate;
	}
	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}
	public Theater getTheater() {
		return theater;
	}
	public void setTheater(Theater theater) {
		this.theater = theater;
	}
	public Time getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Time beginTime) {
		this.beginTime = beginTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	public int getMaxNumber() {
		return maxNumber;
	}
	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}
	 
}