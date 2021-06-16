/**
 * Sample Skeleton for 'MovieTimeEdit.fxml' Controller Class
 */
package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent ;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.time.Instant;
import java.time.ZoneId;

public class EditTimeController implements Initializable {
    private static final ObservableList<MovieShow> data = FXCollections.observableArrayList();
    private final ObservableList<Theater> theaterslist = FXCollections.observableArrayList();
    private final ObservableList<Hall> hallsList = FXCollections.observableArrayList();
    private  static Movie cur_Movie=null;

    @FXML // fx:id="ShowTimeTable"
    private TableView<MovieShow> ShowTimeTable; // Value injected by FXMLLoader
    @FXML // fx:id="showidCol"
    private TableColumn<MovieShow, Number> showidCol; // Value injected by FXMLLoader
    @FXML // fx:id="DateCol"
    private TableColumn<MovieShow, LocalDate> DateCol; // Value injected by FXMLLoader
    @FXML // fx:id="showTimeCol"
    private TableColumn<MovieShow, String> showTimeCol; // Value injected by FXMLLoader
    @FXML // fx:id="endTimeCol"
    private TableColumn<MovieShow, String> endTimeCol; // Value injected by FXMLLoader
    @FXML // fx:id="Theater_col"
    private TableColumn<MovieShow, String> Theater_col; // Value injected by FXMLLoader
   /* @FXML // fx:id="Hall_col"
    private TableColumn<MovieShow, String> Hall_col; // Value injected by FXMLLoader*/
    @FXML // fx:id="beginTimeTbox"
    private TextField beginTimeTbox; // Value injected by FXMLLoader
    @FXML // fx:id="UpdateButton"
    private Button UpdateButton; // Value injected by FXMLLoader
    @FXML // fx:id="endTimeTbox"
    private TextField endTimeTbox; // Value injected by FXMLLoader
    @FXML // fx:id="insertmovieid"
    private TextField insertmovieid; // Value injected by FXMLLoader
    @FXML // fx:id="insbegintime"
    private TextField insbegintime; // Value injected by FXMLLoader
    @FXML // fx:id="insendtime"
    private TextField insendtime; // Value injected by FXMLLoader
    @FXML // fx:id="insertBtn"
    private Button insertBtn; // Value injected by FXMLLoader
    @FXML // fx:id="DeleteBtn"
    private Button DeleteBtn; // Value injected by FXMLLoader
    @FXML // fx:id="NameLabel"
    private Label NameLabel; // Value injected by FXMLLoader
    @FXML // fx:id="Theaters_List"
    private ChoiceBox<Theater> Theaters_List; // Value injected by FXMLLoader
    @FXML // fx:id="DatePicker_Update"
    private DatePicker DatePicker_Update; // Value injected by FXMLLoader
    @FXML // fx:id="Theater_List_Update"
    private ChoiceBox<Theater> Theater_List_Update; // Value injected by FXMLLoader
    @FXML // fx:id="Update_Hall"
    private ChoiceBox<Hall> Update_Hall; // Value injected by FXMLLoader
    @FXML // fx:id="Hall_Insert"
    private ChoiceBox<Hall> Hall_Insert; // Value injected by FXMLLoader
    @FXML // fx:id="NewScreening_Date"
    private DatePicker NewScreening_Date; // Value injected by FXMLLoader
    @FXML
    void DeleteShow(ActionEvent event) {
        int index = ShowTimeTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        MovieShow ms=ShowTimeTable.getSelectionModel().getSelectedItem();
        msgObject msg=new msgObject("#deleteMovieShow",ms);
        try {
            SimpleClient.getClient().sendToServer(msg);
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message sent to server to remove the selcted moviesshows for a the DB");

    }
    @FXML
    void insertNewShow(ActionEvent event) {
        String new_begin_time=insbegintime.getText();
        String new_end_time=insendtime.getText();
        Theater th=Theaters_List.getSelectionModel().getSelectedItem();
        LocalDate newLocalDate=NewScreening_Date.getValue();
        Hall hall=Hall_Insert.getValue();
        MovieShow newMS=new MovieShow(cur_Movie,newLocalDate,th,new_begin_time,new_end_time,String.valueOf(hall.gethallId()),hall.getCapacity());
        msgObject msg=new msgObject("#addMovieShow",newMS);
        try {
            SimpleClient.getClient().sendToServer(msg);
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("adding new movie show request sent to the server");
        afterinserting();
    }
    public void afterinserting(){
        insbegintime.clear();
        insendtime.clear();
    }
    @FXML
    void getSelected(MouseEvent event) {
        int index=ShowTimeTable.getSelectionModel().getSelectedIndex();
        if(index<=-1) {
            return;
        }
        //MovieShow ms= ShowTimeTable.getSelectionModel().getSelectedItem();
        beginTimeTbox.setText(showTimeCol.getCellData(index).toString());
        endTimeTbox.setText(endTimeCol.getCellData(index).toString());
        DatePicker_Update.setValue(DateCol.getCellData(index));
/*
        ms.getTheater();
        Theater_List_Update.setValue(ms.getTheater());
*/

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
    }
    private void initCol() {//TODO: update it to match the final class attributes
        showidCol.setCellValueFactory(new PropertyValueFactory<>("movieShowId"));
        DateCol.setCellValueFactory(new PropertyValueFactory<>("showDate"));
        showTimeCol.setCellValueFactory(new PropertyValueFactory<>("beginTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        Theater_col.setCellValueFactory(new PropertyValueFactory<>("theater"));
        //Hall_col.setCellValueFactory(new PropertyValueFactory<>("theater"));
        //  MovieidCol.setCellValueFactory(new PropertyValueFactory<>("movie"));
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
    void updateSelectedShowTime(ActionEvent event) {//TODO:send a request to the server to update the show time
        int index = ShowTimeTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        MovieShow ms=ShowTimeTable.getSelectionModel().getSelectedItem();
        ms.setBeginTime( beginTimeTbox.getText());
        ms.setEndTime(endTimeTbox.getText());
        ms.setShowDate(DatePicker_Update.getValue());
        if(Theater_List_Update.getSelectionModel().getSelectedItem()!=null){
            ms.setTheater(Theater_List_Update.getSelectionModel().getSelectedItem());
        }
        msgObject msg=new msgObject("#updateMovieShow",ms);
        try {
            SimpleClient.getClient().sendToServer(msg);
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message sent to server to update the selcted moviesshows for a the DB");
        //Stage stage = (Stage) UpdateButton.getScene().getWindow();
        //stage.close();
    }
    public void inflatUI(Movie movie,List<Theater> TheaterList){
        NameLabel.setText("Screening Table for "+ movie.getEngName()+" movie");
        cur_Movie=movie;
        theaterslist.removeAll();
        theaterslist.addAll(TheaterList);
        Theaters_List.getItems().addAll(theaterslist);//choice box
        Theaters_List.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue)-> {
            Theater th=newValue;
            if(th!=null){
                List<Hall>halls=th.getHalls();
                if(halls==null){
                    System.out.println("halls list is empty");
                }
                else{
                    System.out.println("halls list isn't empty");
                    hallsList.clear();
                    hallsList.addAll(halls);
                    Hall_Insert.getItems().removeAll(Hall_Insert.getItems());
                    Hall_Insert.getItems().addAll(hallsList);
                }

            }else{
                System.out.println("theater is null");
            }
        });//adding listeners to the choicebox elements "insert"
        Theater_List_Update.getItems().addAll(theaterslist);//choice box
        Theater_List_Update.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue)-> {
            hallsList.clear();
            Theater th=newValue;
            if(th!=null){
                List<Hall>halls=th.getHalls();
                if(halls==null){
                    System.out.println("halls list is empty");
                }
                else{
                    System.out.println("halls list isn't empty");
                    hallsList.clear();
                    hallsList.removeAll();
                    hallsList.addAll(halls);
                    Update_Hall.getItems().removeAll(Update_Hall.getItems());
                    Update_Hall.getItems().addAll(hallsList);
                }

            }else{
                System.out.println("theater is null");
            }
        });//adding listener to the choicebox elements "update"
        initCol();
        TheaterMovie thMovie=(TheaterMovie) movie;
        if(thMovie.getMSList()!=null){
            List<MovieShow> list=thMovie.getMSList();
            data.clear();
            for(MovieShow ms: list) {
                data.add(ms);
            }
            insertmovieid.setText(Integer.toString(movie.getMovieId()));
            ShowTimeTable.getItems().setAll(data);
            autoResizeColumns(ShowTimeTable);
        }
        else{
            System.out.println("movie show list empty");
        }
    }


}