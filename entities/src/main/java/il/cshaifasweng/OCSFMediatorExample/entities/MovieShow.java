package il.cshaifasweng.OCSFMediatorExample.entities;
import java.util.List;
import javax.persistence.*;
import java.io.Serializable;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
@Entity
@Table(name = "movieShow")
public class MovieShow implements Serializable  {
	private static final long serialVersionUID = -8224097662914849956L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int movieShowId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id")
	 private Movie movie;
	@Basic
	 private Date showDate;
	 @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id")
	 private Theater theater;
	 private String beginTime;
	 private String endTime;
	 private int maxNumber; 
	 
	 public MovieShow()
	 {
		 
	 }
	 public MovieShow(Movie movie, Date showDate, Theater theater,String beginTime, String endTime,int maxNumber)
	 {
		setMovie(movie);
		 this.showDate = showDate;
		 setTheater(theater);
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
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
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
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getMaxNumber() {
		return maxNumber;
	}
	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}

}