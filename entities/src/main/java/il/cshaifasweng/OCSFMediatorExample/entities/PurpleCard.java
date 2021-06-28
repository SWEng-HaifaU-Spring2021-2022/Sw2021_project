package il.cshaifasweng.OCSFMediatorExample.entities;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "PurpleCard")
public class PurpleCard implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;

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

    public Boolean isProjAllowed() {
        return projAllowed;
    }

    public Boolean getProjAllowed() {
        return projAllowed;
    }

    public ReadOnlyProperty<Boolean> ProjAllowProperty() {
        return new ReadOnlyObjectWrapper<Boolean>(projAllowed);
    }

    public Boolean getprojAllowed() {
        return projAllowed;
    }

    /*public String isProjAllowed() {
            return Boolean.toString(projAllowed);
        }

        public String getProjAllowed() {
            return Boolean.toString(projAllowed);
        }

        public String getprojAllowed() {
            return Boolean.toString(projAllowed);
        }
        public String ProjAllowedProperty(){
            return Boolean.toString(projAllowed);
        }*/
    public void setProjAllowed(boolean projAllowed) {
        this.projAllowed = projAllowed;
    }
}
