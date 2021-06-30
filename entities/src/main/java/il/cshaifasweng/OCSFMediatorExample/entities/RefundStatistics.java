package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="RefundStatistics")
public class RefundStatistics implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int refund;
    LocalDate date;

    public RefundStatistics(){}

    public RefundStatistics(int refund, LocalDate date) {
        this.refund = refund;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
