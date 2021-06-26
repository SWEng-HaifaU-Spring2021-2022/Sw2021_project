package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Bundle")
public class Bundle implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int remainingEntries;
    String email;
    String fName;
    String lName;

    public Bundle( String email, String fName, String lName) {
        this.remainingEntries = 20;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
    }

    public Bundle() {

    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public int getRemainingEntries() {
        return remainingEntries;
    }

    public void setRemainingEntries(int remainingEntries) {
        this.remainingEntries = remainingEntries;
    }
}
