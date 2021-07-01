package il.cshaifasweng.OCSFMediatorExample.entities;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="Ticket")
public abstract class Ticket implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int ticketID;
	String buyerEmail;
	String movieName;
	@Column( columnDefinition = "DATE")
	LocalDate screeningDate;
	String buyerName;
	String visaNumber;
	String cvv;
	int totalCost;
	LocalDate BuyingDate;
	public Ticket(){}

	public Ticket(String buyerEmail, String movieName, LocalDate screeningDate, String buyerName, String visaNumber, String cvv) {
		this.buyerEmail = buyerEmail;
		this.movieName = movieName;
		this.screeningDate = screeningDate;
		this.buyerName = buyerName;
		this.visaNumber = visaNumber;
		this.cvv = cvv;
		this.BuyingDate=LocalDate.now();
	}


	public int getTicketID() {
		return ticketID;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public LocalDate getScreeningDate() {
		return screeningDate;
	}

	public void setScreeningDate(LocalDate screeningDate) {
		this.screeningDate = screeningDate;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getVisaNumber() {
		return visaNumber;
	}

	public void setVisaNumber(String visaNumber) {
		this.visaNumber = visaNumber;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}
	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

	public LocalDate getBuyingDate() {
		return BuyingDate;
	}

	public void setBuyingDate(LocalDate buyingDate) {
		BuyingDate = buyingDate;
	}
}
