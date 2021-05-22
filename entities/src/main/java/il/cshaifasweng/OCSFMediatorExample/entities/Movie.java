package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="movie")

public class Movie implements Serializable   {
	private static final long serialVersionUID = -8224097662914849956L;

	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 protected int movieId;
	protected String engName;
	protected String hebName;
	protected String actors;
	protected String genere;
	protected String description;
	protected String producer;
	 @Column(columnDefinition="LONGBLOB")
	 protected byte[] image;
	public Movie()
	{
		
	}
	public Movie(String engName, String hebName, String actors, String genre, String description, String producer, byte[] image )
	{
		this.engName = engName;
		this.hebName = hebName;
		this.actors = actors;
		this.genere = genre;
		this.description = description;
		this.image = image;
		this.producer = producer;
	}
	public int getMovieId() {
		return movieId;
	}
	public String getEngName() {
		return engName;
	}
	public void setEngName(String engName) {
		this.engName = engName;
	}
	public String getHebName() {
		return hebName;
	}
	public void setHebName(String hebName) {
		this.hebName = hebName;
	}
	public String getActors() {
		return actors;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	public String getGenere() { return genere; }
	public void setGenere(String genere) { this.genere = genere; }

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}


}
