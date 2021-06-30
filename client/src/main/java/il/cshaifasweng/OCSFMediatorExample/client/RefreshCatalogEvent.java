package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;

import java.io.IOException;
import java.util.List;

public class RefreshCatalogEvent {
    private List<Movie> movieList;

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public RefreshCatalogEvent(msgObject msg) {
        movieList=(List<Movie>) msg.getObject();
    }
    public  void sendRefreshRequest(){
        msgObject msg = new msgObject("#getAllMovies");
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message sent to server to get all movies");
    }
}
