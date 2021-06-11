package il.cshaifasweng.OCSFMediatorExample.client;

import java.net.URL;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.HomeMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieShow;
import il.cshaifasweng.OCSFMediatorExample.entities.TheaterMovie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class HomeMovieBuyWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label MovieNameLabel;

    @FXML
    private ImageView MovieImage;

    @FXML
    private TextField BuyerNameText;

    @FXML
    private TextField EmailTExt;

    @FXML
    private Label TicketPriceLabel;

    @FXML
    private TextField VisaText;

    @FXML
    private TextField CvvText;

    @FXML
    void initialize() {
        assert MovieNameLabel != null : "fx:id=\"MovieNameLabel\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";
        assert MovieImage != null : "fx:id=\"MovieImage\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";
        assert BuyerNameText != null : "fx:id=\"BuyerNameText\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";
        assert EmailTExt != null : "fx:id=\"EmailTExt\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";
        assert TicketPriceLabel != null : "fx:id=\"TicketPriceLabel\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";
        assert VisaText != null : "fx:id=\"VisaText\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";
        assert CvvText != null : "fx:id=\"CvvText\" was not injected: check your FXML file 'HomeMovieBuyWindow.fxml'.";

    }
    public void setDetails(HomeMovie homeMovie){
        // curtheaterMovie=theatermovie;
        MovieNameLabel.setText(homeMovie.getEngName()+" / "+ homeMovie.getHebName());
        MovieImage.setImage(homeMovie.getImageProperty());
        TicketPriceLabel.setText(TicketPriceLabel.getText()+" :"+homeMovie.getEntryPrice());

    }
}
