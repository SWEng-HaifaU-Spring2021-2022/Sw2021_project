package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class LogInScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button LogInBtn;

    @FXML
    private Label instructions;

    @FXML
    private PasswordField PasswordTF;

    @FXML
    private TextField UsernameTF;

    private static int retVal = 10;

    @FXML
    private Hyperlink goHomeBtn;

    public int getRetVal() {
        return retVal;
    }

    public static void setRetVal(int val) {
        retVal = val;
    }

    @FXML
    void onEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) LogIn(null);
    }

    @FXML
    void goHome(ActionEvent event) {
        try {
            App.setRoot("Catalog");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void LogIn(ActionEvent event) {
        try {
            if (UsernameTF.getText().equals("") || PasswordTF.getText().equals("")) {
                instructions.setTextFill(Color.color(0.7, 0, 0));
                instructions.setText("A field or two are empty! please enter your credentials again.");
            } else {

                SimpleClient.RequestLogIn(UsernameTF.getText(), PasswordTF.getText());

                while (getRetVal()==10)
                {
                    instructions.setTextFill(Color.color(0, 0, 0.7));
                    instructions.setText("Checking data.");
                    TimeUnit.SECONDS.sleep(1);
                    instructions.setText("Checking data..");
                    TimeUnit.SECONDS.sleep(1);
                    instructions.setText("Checking data...");
                    TimeUnit.SECONDS.sleep(1);
                }

                if (retVal==1){

                    msgObject msg=new msgObject("#getAllMovies");
                    SimpleClient.getClient().sendToServer(msg);
                    System.out.println("message sent to server to get all movies");
                } else{
                    instructions.setTextFill(Color.color(0.7, 0, 0));
                    if(retVal==0)instructions.setText("Wrong username or password! please enter your credentials again.");
                    if(retVal==-1)instructions.setText("Server connection error! please try again later.");
                    if(retVal==-2)instructions.setText("You are already connected! Log out to connect again.");
                    if(retVal==-3)instructions.setText("An unknown error has occurred! please try again later.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        retVal=10;
    }


    @FXML
    void initialize() {
        assert LogInBtn != null : "fx:id=\"LogInBtn\" was not injected: check your FXML file 'LogInScreen.fxml'.";
        assert PasswordTF != null : "fx:id=\"PasswordTF\" was not injected: check your FXML file 'LogInScreen.fxml'.";
        assert UsernameTF != null : "fx:id=\"UsernameTF\" was not injected: check your FXML file 'LogInScreen.fxml'.";

    }
}
