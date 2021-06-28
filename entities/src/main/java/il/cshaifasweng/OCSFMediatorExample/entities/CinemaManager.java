package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="User")
public class CinemaManager extends User implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    int branchid;

    public CinemaManager(){}
    public CinemaManager(String userName, String password, String firstName, String lastName, int permission, int branchid) {
        super(userName, password, firstName, lastName, permission);
        this.branchid = branchid;
    }

    public int getBranchid() {
        return branchid;
    }

    public void setBranchid(int branchid) {
        this.branchid = branchid;
    }
}
