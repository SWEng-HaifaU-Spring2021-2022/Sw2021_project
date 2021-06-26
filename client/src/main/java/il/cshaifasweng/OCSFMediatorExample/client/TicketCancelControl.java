package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

public class TicketCancelControl implements Initializable {
	
	
	public static ObservableList<Ticket> list = FXCollections.observableArrayList();

	
	@Override
	 public void initialize(URL url, ResourceBundle rb){
		initCol();
		if(SimpleClient.ticketlist!=null){
			List<Ticket> ticketslist=SimpleClient.ticketlist;
			loadData(ticketslist);
			System.out.println("done initialize");
			System.out.println("Bla Bla");
		}
		else{
			loadData(null);
			System.out.println("done initialize");
		}
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
	private TableColumn<Ticket, Integer> costCol;

	@FXML
	private TableColumn<Ticket, String> VisaCol;

    @FXML
    private TextField EmailText;

    @FXML
    private Button CancelButton;

    @FXML
    private Button ShowMovies;
    
    @FXML
    private Button goHome;

	@FXML
	private Label ticketDetailsLabel;

    @FXML
    void CancelBtn(ActionEvent event) {
		int index = CancelTable.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		} else {
			Ticket selctedticket = CancelTable.getSelectionModel().getSelectedItem();
			AdvancedMsg msg=new AdvancedMsg();
			if (selctedticket.getClass().equals(TheaterTicket.class)) {
				msg.addobject((TheaterTicket)selctedticket);
				msg.setMsg("#deleteTheaterTicket");
				LocalTime lt=LocalTime.now();
				if(LocalDate.now().isBefore(selctedticket.getScreeningDate())){
					Warning newwarning = new Warning("You will get a full refund("+selctedticket.getTotalCost()+") an email will be sent");
					EventBus.getDefault().post(new WarningEvent((Warning) newwarning));
					msg.addobject(1.0);
				}
				else if(LocalDate.now().equals(selctedticket.getScreeningDate())){
					long remaininghours=lt.until(((TheaterTicket) selctedticket).getStartingTime(), ChronoUnit.HOURS);
					if(remaininghours>=1&&remaininghours<=3){
						Warning newwarning = new Warning("You will get a %50 refund ("+selctedticket.getTotalCost()/2+") an email will be sent");
						EventBus.getDefault().post(new WarningEvent((Warning) newwarning));
						msg.addobject(0.5);
					}
					else{
						Warning newwarning = new Warning("You won't get a refund an email will be sent");
						EventBus.getDefault().post(new WarningEvent((Warning) newwarning));
						msg.addobject(0.0);
					}
				}
				try {
					SimpleClient.getClient().sendToServer(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				HomeLinkTicket hlt=(HomeLinkTicket)selctedticket;
				LocalTime lt=LocalTime.now();
				Long remainingTime=lt.until(hlt.getStartingTime(),ChronoUnit.HOURS);
				System.out.println(hlt.getScreeningDate().equals(LocalDate.now()));
				if(!(hlt.getScreeningDate().equals(LocalDate.now()))||(hlt.getScreeningDate().equals(LocalDate.now()))&&remainingTime>=1){
					msgObject msgobj=new msgObject("#deleteHomeTicket",hlt);
					try {
						Warning newwarning = new Warning("You will get a %50 refund ("+selctedticket.getTotalCost()/2+") an email will be sent");
						EventBus.getDefault().post(new WarningEvent((Warning) newwarning));
						SimpleClient.getClient().sendToServer(msgobj);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					Warning newwarning = new Warning("Sorry You can't return the link you bought now ");
					EventBus.getDefault().post(new WarningEvent((Warning) newwarning));
				}
			}

		}
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
		if (EmailText.getText().isEmpty()){
			return;
		}
		String BuyerEmail=EmailText.getText();
		msgObject msgObject=new msgObject("#getTickets",BuyerEmail);
		try {
			SimpleClient.getClient().sendToServer(msgObject);
			System.out.println("getting the user tickets");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    void loadData(List<Ticket>TicketList) {
		try {
			if(TicketList!=null){
				list.clear();
				for(Ticket t: TicketList) {
					list.add(t);
				}
			}else{
				list.clear();
				CancelTable.getItems().clear();
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		CancelTable.setItems(list);
	}
	
    public void initCol() {
    	MovieCol.setCellValueFactory(new PropertyValueFactory<>("buyerName"));
		DateCol.setCellValueFactory(new PropertyValueFactory<>("screeningDate"));
		costCol.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
		VisaCol.setCellValueFactory(new PropertyValueFactory<>("visaNumber"));
    	
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
    		if (SimpleClient.ticketlist!=null){
				SimpleClient.ticketlist.clear();
				list.clear();
				CancelTable.getItems().clear();
			}
			App.setRoot("primary");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@FXML
	void ShowDetails(MouseEvent event){
		String str="";
	int index=CancelTable.getSelectionModel().getSelectedIndex();
		if(index<=-1) {
			return;
		}
		else{
			Ticket selctedticket=CancelTable.getSelectionModel().getSelectedItem();
			if(selctedticket.getClass().equals(TheaterTicket.class)){
				TheaterTicket tkit=(TheaterTicket) selctedticket;
				str= tkit.toString();
				ticketDetailsLabel.setText(str);
			}else{
				HomeLinkTicket hlt=(HomeLinkTicket) selctedticket;
				str=hlt.toString();
				ticketDetailsLabel.setText(str);
			}
		}
	}
}
