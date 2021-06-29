package il.cshaifasweng.OCSFMediatorExample.client;

public class PriceChangeEvent {
    private Object priceRequestList;

    public PriceChangeEvent(Object priceRequestList) {
        this.priceRequestList = priceRequestList;
    }

    public Object getPriceRequestList() {
        return priceRequestList;
    }

    public void setPriceRequestList(Object priceRequestList) {
        this.priceRequestList = priceRequestList;
    }
}
