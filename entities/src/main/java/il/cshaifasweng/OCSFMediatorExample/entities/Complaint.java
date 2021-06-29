package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.time.LocalTime;
@Entity
@Table(name="Complaint")
public class Complaint implements Serializable {
	private static final long serialVersionUID = -8224097662914849956L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ComplaintID;
	private String email;
	private String content;
	private String answer;
	private String status;
	private LocalDate date;
	private LocalTime sendTime;
	public Complaint()
	{
		
	}
	public Complaint(int ComplaintID, String email,String content, String answer, String status, LocalDate date, LocalTime sendTime)
	{
		this.ComplaintID = ComplaintID;
		this.email = email;
		this.content = content;
		this.answer = answer;
		this.status = status;
		this.date = date;
		this.sendTime = sendTime;
	}
	public LocalTime getSendTime() {
		return sendTime;
	}
	public void setSendTime(LocalTime sendTime) {
		this.sendTime = sendTime;
	}
	public int getComplaintID() {
		return ComplaintID;
	}
	public void setComplaintID(int complaintID) {
		ComplaintID = complaintID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String isStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	
}

