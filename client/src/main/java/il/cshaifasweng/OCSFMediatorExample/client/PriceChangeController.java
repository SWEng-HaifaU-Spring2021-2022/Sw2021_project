/**
 * Sample Skeleton for 'contentmanagerPrices.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.PriceRequest;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PriceChangeController implements Initializable {
	
	@Override
	 public void initialize(URL url, ResourceBundle rb){
		initCol();
		loadData();
		
	}

	ObservableList<PriceRequest> list = FXCollections.observableArrayList();
	
    @FXML // fx:id="priceTable"
    private TableView<PriceRequest> priceTable; // Value injected by FXMLLoader
    
    @FXML // fx:id="oldPriceCol"
    private TableColumn<PriceRequest, String> oldPriceCol; // Value injected by FXMLLoader

    @FXML // fx:id="newPriceCol"
    private TableColumn<PriceRequest, String> newPriceCol; // Value injected by FXMLLoader

    @FXML // fx:id="descCol"
    private TableColumn<PriceRequest, String> descCol; // Value injected by FXMLLoader


    @FXML // fx:id="AcceptBtn"
    private Button AcceptBtn; // Value injected by FXMLLoader

    @FXML // fx:id="RejectBtn"
    private Button RejectBtn; // Value injected by FXMLLoader
    
    @FXML
    private Button Home;

    @FXML
    void accept(ActionEvent event) {
    	int index = priceTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
    	PriceRequest priceSelected = priceTable.getSelectionModel().getSelectedItem();
    	msgObject msg = new msgObject("#updatePrice",priceSelected);
        try {
			SimpleClient.getClient().sendToServer(msg);
			System.out.println("update price Messege Sent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void reject(ActionEvent event) {
    	int index = priceTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
    	PriceRequest priceSelected = priceTable.getSelectionModel().getSelectedItem();
    	msgObject msg = new msgObject("#deleteRequest",priceSelected);
        try {
			SimpleClient.getClient().sendToServer(msg);
			System.out.println("Delete Messege Sent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    public void initCol() {
    	oldPriceCol.setCellValueFactory(new PropertyValueFactory<>("oldPrice"));
    	newPriceCol.setCellValueFactory(new PropertyValueFactory<>("newPrice"));
    	descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    	
    }
    @FXML
    void goHome(ActionEvent event) {
    	try {
			App.setRoot("primary");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void loadData() {
    	System.out.println("load data");
    	List<PriceRequest> priceList=(List<PriceRequest>)SimpleClient.obj;
		try {
			list.clear();
			for(PriceRequest m: priceList) {
				list.add(m);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		priceTable.setItems(list);
		//autoResizeColumns(MoviesTable);
	}
    

}
