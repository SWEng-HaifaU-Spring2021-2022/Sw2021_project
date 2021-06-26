package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="PurpleCard")
public class PurpleCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    LocalDate start;
    LocalDate end;
    int maxCap;
    boolean projAllowed;

    public PurpleCard(LocalDate start, LocalDate end, int maxCap, boolean projAllowed) {
        this.start = start;
        this.end = end;
        this.maxCap = maxCap;
        this.projAllowed = projAllowed;
    }

    public PurpleCard() {

    }

    public int getId() {
        return id;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public int getMaxCap() {
        return maxCap;
    }

    public void setMaxCap(int maxCap) {
        this.maxCap = maxCap;
    }

    public boolean isProjAllowed() {
        return projAllowed;
    }

    public void setProjAllowed(boolean projAllowed) {
        this.projAllowed = projAllowed;
    }
}
