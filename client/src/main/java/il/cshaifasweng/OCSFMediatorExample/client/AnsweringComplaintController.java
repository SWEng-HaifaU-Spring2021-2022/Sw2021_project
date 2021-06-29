package il.cshaifasweng.OCSFMediatorExample.client;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent ;
import javafx.scene.input.MouseEvent;
//import com.sun.glass.events.MouseEvent;

//import antlr.collections.List;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieShow;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableView;
import java.time.LocalDate;
import java.util.Date;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class AnsweringComplaintController implements Initializable{
	ObservableList<Complaint> list = FXCollections.observableArrayList();
	 @FXML // fx:id="table_Comp"
	    private TableView<Complaint> table_Comp; // Value injected by FXMLLoader

	    @FXML // fx:id="guest_email"
	    private TableColumn<Complaint, String> guest_email; // Value injected by FXMLLoader

	    @FXML // fx:id="complaint_content"
	    private TableColumn<Complaint, String> complaint_content; // Value injected by FXMLLoader

	    @FXML // fx:id="status_col"
	    private TableColumn<Complaint, String> status_col; // Value injected by FXMLLoader

	    @FXML // fx:id="answer_col"
	    private TableColumn<Complaint, String> answer_col; // Value injected by FXMLLoader

	    @FXML // fx:id="complaint_date"
	    private TableColumn<Complaint, LocalDate> complaint_date; // Value injected by FXMLLoader

	    @FXML // fx:id="Complaint_text"
	    private TextArea Complaint_text; // Value injected by FXMLLoader

	    @FXML // fx:id="Answer_text"
	    private TextArea Answer_text; // Value injected by FXMLLoader

	    @FXML // fx:id="Answer_btn"
	    private Button Answer_btn; // Value injected by FXMLLoader

	    @FXML
	    void getSelected(MouseEvent event) {
	    	int index=table_Comp.getSelectionModel().getSelectedIndex();
	        if(index<=-1) {
	            return;
	        }
	        Complaint_text.setText(table_Comp.getSelectionModel().getSelectedItem().getContent());
	        if(status_col.getCellData(index).toString()=="answered")
	        {
	        	if(!table_Comp.getSelectionModel().getSelectedItem().getAnswer().isEmpty()) {
	        		Answer_text.setText(table_Comp.getSelectionModel().getSelectedItem().getAnswer());
	        		Answer_text.setEditable(false);
	        	}
	        }
	    }

	    @FXML
	    void sendAnswerToComplaint(ActionEvent event) {
	    	int index = table_Comp.getSelectionModel().getSelectedIndex();
	        if (index <= -1) {
	            return;
	        }
	        Complaint tb=table_Comp.getSelectionModel().getSelectedItem();
	        tb.setAnswer( Answer_text.getText());
	        tb.setStatus("Answered");
	        msgObject msg=new msgObject("#updateAnswerToComplaint",tb);
	        try {
	            SimpleClient.getClient().sendToServer(msg);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        System.out.println("message sent to server to update the answer field to the selected complaint for a the DB");
	    }

		@Override
		public void initialize(URL location, ResourceBundle resources) {
	        initCol();
			loadData();
			EventBus.getDefault().register(this);
		}
	    private void initCol() {//TODO: update it to match the final class attributes
	    	guest_email.setCellValueFactory(new PropertyValueFactory<>("email"));
	    	complaint_content.setCellValueFactory(new PropertyValueFactory<>("content"));
	    	status_col.setCellValueFactory(new PropertyValueFactory<>("status"));
	    	answer_col.setCellValueFactory(new PropertyValueFactory<>("answer"));
	    	complaint_date.setCellValueFactory(new PropertyValueFactory<>("date"));
	        
	    }
	    public void loadData() {
	    	msgObject data1 = new msgObject("");
	    	List<Complaint> complaintList = (List<Complaint>) SimpleClient.obj;
			try {
				list.clear();
				for(Complaint m: complaintList) {
					list.add(m);
				}
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			table_Comp.setItems(list);
			autoResizeColumns(table_Comp);
		}
		public static void autoResizeColumns( TableView<?> table ){
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
	void goCatalog(ActionEvent event) {
		msgObject msg = new msgObject("#getAllMovies");
		try {
			SimpleClient.getClient().sendToServer(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("message sent to server to get all movies");
	}
	@Subscribe
	public void onComplaintEvent(ComplaintEvent event){
		Platform.runLater(()->{
			list.clear();
			list.addAll(event.getComplaintList());
			table_Comp.setItems(list);
			autoResizeColumns(table_Comp);
		});
	}
}
