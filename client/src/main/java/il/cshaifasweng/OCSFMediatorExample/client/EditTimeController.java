/**
 * Sample Skeleton for 'MovieTimeEdit.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent ;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.TheaterMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieShow;

public class EditTimeController implements Initializable {
    private final ObservableList<MovieShow> data = FXCollections.observableArrayList();
    private  static Movie cur_Movie=null;
    private static Theater temptheater=null;
    @FXML // fx:id="ShowTimeTable"
    private TableView<MovieShow> ShowTimeTable; // Value injected by FXMLLoader

    @FXML // fx:id="showidCol"
    private TableColumn<MovieShow, Number> showidCol; // Value injected by FXMLLoader
    @FXML // fx:id="DateCol"
    private TableColumn<MovieShow, LocalDateTime> DateCol; // Value injected by FXMLLoader

    @FXML // fx:id="showTimeCol"
    private TableColumn<MovieShow, String> showTimeCol; // Value injected by FXMLLoader

    @FXML // fx:id="endTimeCol"
    private TableColumn<MovieShow, String> endTimeCol; // Value injected by FXMLLoader

  /*  @FXML // fx:id="MovieidCol"
    private TableColumn<MovieShow, Movie> MovieidCol; // Value injected by FXMLLoader*/

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

    @FXML // fx:id="insDay"
    private TextField insDay; // Value injected by FXMLLoader

    @FXML // fx:id="instheaterid"
    private TextField instheaterid; // Value injected by FXMLLoader

    @FXML // fx:id="insertBtn"
    private Button insertBtn; // Value injected by FXMLLoader

    @FXML // fx:id="DeleteBtn"
    private Button DeleteBtn; // Value injected by FXMLLoader

    @FXML // fx:id="insMn"
    private TextField insMn; // Value injected by FXMLLoader

    @FXML // fx:id="insYear"
    private TextField insYear; // Value injected by FXMLLoader


    @FXML // fx:id="NameLabel"
    private Label NameLabel; // Value injected by FXMLLoader


    @FXML
    void DeleteShow(ActionEvent event) {
        int index = ShowTimeTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        MovieShow ms=ShowTimeTable.getSelectionModel().getSelectedItem();
        msgObject msg=new msgObject("#deleteMovieShow",ms);
        try {
            App.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message sent to server to remove the selcted moviesshows for a the DB");
    }
    @FXML
    void insertNewShow(ActionEvent event) {
        String new_begin_time=insbegintime.getText();
        String new_end_time=insendtime.getText();
        int day=Integer.parseInt(insDay.getText());
        int month=Integer.parseInt(insMn.getText());
        int year=Integer.parseInt(insYear.getText());
        System.out.println(year+"/"+month+"/"+day);
        Date  date=new Date(year,month,day);
        MovieShow newMS=new MovieShow(cur_Movie,date,temptheater,new_begin_time,new_end_time,40);
       msgObject msg=new msgObject("#addMovieShow",newMS);
        try {

           SimpleClient.getClient().sendToServer(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("adding new movie show request sent to the server");
        afterinserting();
    }
    public void afterinserting(){
        insbegintime.clear();
        insendtime.clear();
        insDay.clear();
        insMn.clear();
        insYear.clear();
    }
    @FXML
    void getSelected(MouseEvent event) {
        int index=ShowTimeTable.getSelectionModel().getSelectedIndex();
        if(index<=-1) {
            return;
        }

        beginTimeTbox.setText(showTimeCol.getCellData(index).toString());
        endTimeTbox.setText(endTimeCol.getCellData(index).toString());
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
        msgObject msg=new msgObject("#updateMovieShow",ms);
        try {
            App.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message sent to server to update the selcted moviesshows for a the DB");
        //Stage stage = (Stage) UpdateButton.getScene().getWindow();
        //stage.close();
    }

    public void inflatUI(Movie movie){//TODO: update it after getting the entities and a DB connection
        NameLabel.setText("Screening Table for "+ movie.getEngName()+" movie");
        cur_Movie=movie;
        initCol();
        if(SimpleClient.obj!=null){
            List<MovieShow> list=(List<MovieShow>)SimpleClient.obj;
            System.out.println(list.size()+"list length");
            temptheater=list.get(0).getTheater();
            for(MovieShow ms: list) {
                System.out.println(ms.getBeginTime());
                data.add(ms);
            }
            insertmovieid.setText(Integer.toString(movie.getMovieId()));
            ShowTimeTable.getItems().setAll(data);
            ShowTimeTable.autosize();
        }
        else{
            System.out.println("movie show list empty");
        }
    }

}
