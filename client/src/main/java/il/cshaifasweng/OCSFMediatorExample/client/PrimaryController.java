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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;

public class PrimaryController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button ShowCatalog;

	@FXML
	void ShowCatalog(ActionEvent event) {
		try {
			App.setRoot("Catalog");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	void initialize() {
		//assert ShowCatalog != null : "fx:id=\"ShowCatalog\" was not injected: check your FXML file 'primary.fxml'.";

	}
}

