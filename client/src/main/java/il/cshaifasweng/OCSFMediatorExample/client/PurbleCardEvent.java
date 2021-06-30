package il.cshaifasweng.OCSFMediatorExample.client;

public class PurbleCardEvent {
    private boolean openmap;

    public PurbleCardEvent(boolean openmap) {
        this.openmap = openmap;
    }

    public boolean isOpenmap() {
        return openmap;
    }
}
