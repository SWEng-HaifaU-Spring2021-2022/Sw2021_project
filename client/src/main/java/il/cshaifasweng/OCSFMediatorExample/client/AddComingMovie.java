/**
 * Sample Skeleton for 'AddComingMovie.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.HomeMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.TheaterMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddComingMovie implements Initializable {

    @FXML // fx:id="MovieEng"
    private TextField MovieEng; // Value injected by FXMLLoader

    @FXML // fx:id="MovieHeb"
    private TextField MovieHeb; // Value injected by FXMLLoader

    @FXML // fx:id="Actors"
    private TextField Actors; // Value injected by FXMLLoader

    @FXML // fx:id="Genre"
    private TextField Genre; // Value injected by FXMLLoader

    @FXML // fx:id="Description"
    private TextField Description; // Value injected by FXMLLoader

    @FXML // fx:id="Producer"
    private TextField Producer; // Value injected by FXMLLoader

    @FXML // fx:id="ImageURL"
    private TextField ImageURL; // Value injected by FXMLLoader

    @FXML // fx:id="AddBtn"
    private Button AddBtn; // Value injected by FXMLLoader
    @FXML // fx:id="message"
    private Label message; // Value injected by FXMLLoader

    @FXML // fx:id="entryPrice"
    private TextField entryPrice; // Value injected by FXMLLoader

    @FXML // fx:id="priceLabel"
    private Label priceLabel; // Value injected by FXMLLoader

    @FXML // fx:id="linkLabel"
    private Label linkLabel; // Value injected by FXMLLoader

    @FXML // fx:id="MovieLink"
    private TextField MovieLink; // Value injected by FXMLLoader

    @FXML // fx:id="MovieType"
    private ChoiceBox<String> MovieType; // Value injected by FXMLLoader

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MovieType.getItems().add("Coming Soon");
        MovieType.getItems().add("Home Movie");
        MovieType.getItems().add("Theater Movie");
        MovieType.setValue("Coming Soon");
        MovieType.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue)-> {
            entryPrice.setVisible(false);
            priceLabel.setVisible(false);
            linkLabel.setVisible(false);
            MovieLink.setVisible(false);
            if (newValue.equals("Theater Movie")){
                entryPrice.setVisible(true);
                priceLabel.setVisible(true);
            }
            if (newValue.equals("Home Movie")){
                entryPrice.setVisible(true);
                priceLabel.setVisible(true);
                linkLabel.setVisible(true);
                MovieLink.setVisible(true);
            }
        });
    }

    @FXML
    void addMovie(ActionEvent event) throws IOException {
        Movie m=new Movie();
        if (MovieEng.getText().isEmpty()||MovieHeb.getText().isEmpty()||Actors.getText().isEmpty()||Genre.getText().isEmpty()||Description.getText().isEmpty()||Producer.getText().isEmpty()||ImageURL.getText().isEmpty()){
            message.setText("there's an empty field");
            return;
        }
        String selctedChoice=MovieType.getSelectionModel().getSelectedItem();
        if(selctedChoice.equals("Coming Soon")){
            m.setEngName(MovieEng.getText());
            m.setHebName(MovieHeb.getText());
            m.setActors(Actors.getText());
            m.setDescription(Description.getText());
            m.setGenere(Genre.getText());
            m.setProducer(Producer.getText());
            m.setImgURL(ImageURL.getText());
            msgObject msg=new msgObject();
            msg.setObject(m);
            msg.setMsg("#addMovie");
            SimpleClient.getClient().sendToServer(msg);
        }else if(selctedChoice.equals("Theater Movie")){
            if (entryPrice.getText().isEmpty()){
                message.setText("there's an empty field");
                return;
            }
            TheaterMovie TM=new TheaterMovie(MovieEng.getText(),MovieHeb.getText(),Actors.getText(),Genre.getText(),Description.getText(),Producer.getText(),ImageURL.getText(),Integer.parseInt(entryPrice.getText()));
            msgObject msg=new msgObject();
            msg.setObject(TM);
            msg.setMsg("#addMovie");
            SimpleClient.getClient().sendToServer(msg);
        }else if(selctedChoice.equals("Home Movie")){
            if (entryPrice.getText().isEmpty()||MovieLink.getText().isEmpty()){
                message.setText("there's an empty field");
                return;
            }
            HomeMovie HM=new HomeMovie(MovieEng.getText(),MovieHeb.getText(),Actors.getText(),Genre.getText(),Description.getText(),Producer.getText(),ImageURL.getText(),MovieLink.getText(),Integer.parseInt(entryPrice.getText()));
            msgObject msg=new msgObject();
            msg.setObject(HM);
            msg.setMsg("#addMovie");
            SimpleClient.getClient().sendToServer(msg);
        }
        Stage stage=(Stage)AddBtn.getScene().getWindow();
        stage.close();
    }
    @FXML
    void goHome(ActionEvent event) {
        msgObject msg=new msgObject("#getAllMovies");
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message sent to server to get all movies");
    }

}
