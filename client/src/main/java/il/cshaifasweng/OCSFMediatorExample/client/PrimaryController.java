package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PrimaryController {
	@FXML // fx:id="grid"
	private Button grid; // Value injected by FXMLLoader



	@FXML
	void open_grid(ActionEvent event) throws IOException {
		msgObject msg = new msgObject("#getAllMovies");
		SimpleClient.getClient().sendToServer(msg);
		System.out.println("message sent to server to get all movies");
		// App.setRoot("GridCatalog");
	}


	@FXML
	void sendWarning(ActionEvent event) throws IOException {
		 Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION,
                    String.format("Message: %s\nTimestamp: %s\n",
                            event.getWarning().getMessage(),
                            event.getWarning().getTime().toString())
            );
            alert.show();
        });
	}
	@FXML
	void buyBundle(ActionEvent event) {
		try {
			App.setRoot("BuyBundle");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@FXML
	void PCard(ActionEvent event) {
		try {
			App.setRoot("PurpleCard");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@FXML
	void showBundles(ActionEvent event) {
		try {
			App.setRoot("ViewBundles");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@FXML
	void open_Complaint(ActionEvent event) throws IOException {
		App.setRoot("Complaint");
	}

	@FXML
	void openanswerComp(ActionEvent event) throws IOException {
		msgObject msg = new msgObject("#getAllComplaints");
		SimpleClient.getClient().sendToServer(msg);
		System.out.println("message sent to server to get all Complaints");

		//App.setRoot("AnswerComplaints");

	}


}