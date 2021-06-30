package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.TheaterMovie;

public class BuyTicketEvent {
    private TheaterMovie theaterMovie;

    public BuyTicketEvent(TheaterMovie theaterMovie) {
        this.theaterMovie = theaterMovie;
    }

    public TheaterMovie getTheaterMovie() {
        return theaterMovie;
    }
}
