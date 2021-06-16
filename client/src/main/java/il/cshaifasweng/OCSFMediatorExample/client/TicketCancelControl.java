package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.PriceRequest;
import il.cshaifasweng.OCSFMediatorExample.entities.Ticket;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class TicketCancelControl implements Initializable {
	
	
	ObservableList<Ticket> list = FXCollections.observableArrayList();

	
	@Override
	 public void initialize(URL url, ResourceBundle rb){
		initCol();
		
	}
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Ticket> CancelTable;

    @FXML
    private TableColumn<Ticket, String> MovieCol;

    @FXML
    private TableColumn<Ticket, String> DateCol;

    @FXML
    private TextField EmailText;

    @FXML
    private Button CancelButton;

    @FXML
    private Button ShowMovies;
    
    @FXML
    private Button goHome;

    @FXML
    void CancelBtn(ActionEvent event) {
    	/*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    	Ticket ticketSelected = CancelTable.getSelectionModel().getSelectedItem();
    	String ticketDate = ticketSelected.getMovieDate();
    	LocalDateTime date1 = LocalDateTime.parse(ticketDate, dtf);
    	LocalDateTime now = LocalDateTime.now();   
		long daysBetween = Duration.between(now, date1).toHours();
		String toBuyer;
		if(daysBetween >= 3) {
			toBuyer = "You have refunded with 100% from the price";
		}
		else if(daysBetween >= 1) {
			toBuyer = "You have refunded with 50% from the price";
		}
		else {
			toBuyer = "You have not refunded, Ticket Canceled!";
		}
		
    	msgObject msg = new msgObject("#deleteTicket",ticketSelected);
        try {
			SimpleClient.getClient().sendToServer(msg);
			System.out.println("Delete Ticket Sent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		showStage(toBuyer);*/
    }
    
    public static void showStage(String Text1){
    	Stage newStage = new Stage();
    	VBox comp = new VBox();
    	TextField nameField = new TextField(Text1);
    	comp.getChildren().add(nameField);

    	Scene stageScene = new Scene(comp, 300, 300);
    	newStage.setScene(stageScene);
    	newStage.show();
    	}

    @FXML
    void ShowBtn(ActionEvent event) {
    	loadData();
    }
    void loadData() {
	   /* System.out.println("load data");
		List<Ticket> ticketList=(List<Ticket>)SimpleClient.obj;
		String text = EmailText.getText();
		try {
			list.clear();
			for(Ticket m: ticketList) {
				if(m.getEmail().toString().equals(text))
					list.add(m);
					
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		CancelTable.setItems(list);
		autoResizeColumns(CancelTable);*/
    }
	
    public void initCol() {
    	MovieCol.setCellValueFactory(new PropertyValueFactory<>("OwnerName"));
    	DateCol.setCellValueFactory(new PropertyValueFactory<>("MovieDate"));
    	
    }
    public static void autoResizeColumns( TableView<?> table )//method to reszie columns taken from StackOverFlow
	{
		//Set the right policy
		table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach( (column) ->
		{
			//Minimal width = columnheader
			Text t = new Text( column.getText() );
			double max = t.getLayoutBounds().getWidth();
			for ( int i = 0; i < table.getItems().size(); i++ )
			{
				//cell must not be empty
				if ( column.getCellData( i ) != null )
				{
					t = new Text( column.getCellData( i ).toString() );
					double calcwidth = t.getLayoutBounds().getWidth();
					//remember new max-width
					if ( calcwidth > max )
					{
						max = calcwidth;
					}
				}
			}
			//set the new max-widht with some extra space
			column.setPrefWidth( max + 10.0d );
		} );
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
    

}
