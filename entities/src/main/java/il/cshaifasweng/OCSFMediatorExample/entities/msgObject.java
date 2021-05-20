package il.cshaifasweng.OCSFMediatorExample.entities;
import java.io.Serializable;
public class msgObject implements Serializable {
	private static final long serialVersionUID = -8224097662914849956L;
    String msg;
    Object object;
    public msgObject(){}
    public msgObject(String msg, Object object) {
        this.msg = msg;
        this.object = object;
    }
    public msgObject(String msg) {
        this.msg = msg;
        this.object = null;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
