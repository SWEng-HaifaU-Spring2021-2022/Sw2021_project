package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class PrimaryController {

    @FXML
    void sendWarning(ActionEvent event)  throws IOException {
    	try {
			//SimpleClient.getClient().sendToServer("#warning");
			msgObject msg=new msgObject("#getAllMovies");
        	SimpleClient.getClient().sendToServer(msg);
        	System.out.println("message sent to server to get all movies");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}

