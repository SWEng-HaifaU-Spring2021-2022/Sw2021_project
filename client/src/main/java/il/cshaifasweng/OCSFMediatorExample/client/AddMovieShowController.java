/**
 * Sample Skeleton for 'AddMovieShow.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import java.net.URL;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.MovieShow;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddMovieShowController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="ShowDateDP"
    private DatePicker ShowDateDP; // Value injected by FXMLLoader

    @FXML // fx:id="startTimeTB"
    private TextField startTimeTB; // Value injected by FXMLLoader

    @FXML // fx:id="endTimeTB"
    private TextField endTimeTB; // Value injected by FXMLLoader

    @FXML // fx:id="TheatreCB"
    private ChoiceBox<?> TheatreCB; // Value injected by FXMLLoader

    @FXML // fx:id="HallCB"
    private ChoiceBox<?> HallCB; // Value injected by FXMLLoader

    @FXML // fx:id="AddBtn"
    private Button AddBtn; // Value injected by FXMLLoader

    @FXML // fx:id="msgLB"
    private Label msgLB; // Value injected by FXMLLoader

    @FXML
    void addMS(ActionEvent event) {

        MovieShow ms = new MovieShow();
        msgObject msg = new msgObject("AddMovieShow");


    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert ShowDateDP != null : "fx:id=\"ShowDateDP\" was not injected: check your FXML file 'AddMovieShow.fxml'.";
        assert startTimeTB != null : "fx:id=\"startTimeTB\" was not injected: check your FXML file 'AddMovieShow.fxml'.";
        assert endTimeTB != null : "fx:id=\"endTimeTB\" was not injected: check your FXML file 'AddMovieShow.fxml'.";
        assert TheatreCB != null : "fx:id=\"TheatreCB\" was not injected: check your FXML file 'AddMovieShow.fxml'.";
        assert HallCB != null : "fx:id=\"HallCB\" was not injected: check your FXML file 'AddMovieShow.fxml'.";
        assert AddBtn != null : "fx:id=\"AddBtn\" was not injected: check your FXML file 'AddMovieShow.fxml'.";
        assert msgLB != null : "fx:id=\"msgLB\" was not injected: check your FXML file 'AddMovieShow.fxml'.";

    }
}
