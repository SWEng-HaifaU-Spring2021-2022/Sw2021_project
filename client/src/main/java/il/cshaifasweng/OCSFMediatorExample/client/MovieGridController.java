/**
 * Sample Skeleton for 'MovieGrid.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MovieGridController {
    private  Movie movie;
    private static Movie clickedMovie;
    @FXML // fx:id="MovieImage"
    private ImageView MovieImage; // Value injected by FXMLLoader

    @FXML // fx:id="EditBtn"
    private Button EditBtn; // Value injected by FXMLLoader

    @FXML // fx:id="DeleteBtn"
    private Button DeleteBtn; // Value injected by FXMLLoader

    @FXML // fx:id="sendBtn"
    private Button sendBtn; // Value injected by FXMLLoader

    @FXML // fx:id="PriceField"
    private TextField PriceField; // Value injected by FXMLLoader

    @FXML // fx:id="MovieTitle"
    private Label MovieTitle; // Value injected by FXMLLoader

    @FXML // fx:id="ActorsLabel"
    private Label ActorsLabel; // Value injected by FXMLLoader

    @FXML // fx:id="GenerLabel"
    private Label GenerLabel; // Value injected by FXMLLoader

    @FXML // fx:id="PriceLabel"
    private Label PriceLabel; // Value injected by FXMLLoader

    @FXML // fx:id="ProducaerLabel"
    private Label ProducaerLabel; // Value injected by FXMLLoader

    @FXML // fx:id="ScreeningLabel"
    private Label ScreeningLabel; // Value injected by FXMLLoader

    @FXML // fx:id="descriptionLabel"
    private Label descriptionLabel; // Value injected by FXMLLoader

    @FXML // fx:id="screenPane"
    private ScrollPane screenPane; // Value injected by FXMLLoader

    @FXML
    private Label priceRequestLabel;

    @FXML
    private Button BuyBtn;

    public void setMovieGrid(Movie movie){
        this.movie=movie;
    }
    public Movie getMovieGrid(){
        return this.movie;
    }
    public void setDisplay(){
        if(SimpleClient.getUser()==null || (SimpleClient.getUser()!=null && SimpleClient.getUser().getPermission()<3))
        {
            EditBtn.setVisible(false);
            DeleteBtn.setVisible(false);
            sendBtn.setVisible(false);
            PriceField.setVisible(false);
            priceRequestLabel.setVisible(false);
        }
        MovieTitle.setText(movie.getEngName()+'\\'+movie.getHebName());
        ActorsLabel.setText("Actors: "+movie.getActors());
        ProducaerLabel.setText("Producer "+ movie.getProducer());
        if(movie.getClass().equals(HomeMovie.class)){
            HomeMovie HM=(HomeMovie) movie;
            PriceLabel.setText("Price: "+ HM.getEntryPrice());
        }else if(movie.getClass().equals(TheaterMovie.class)){
            TheaterMovie TM=(TheaterMovie) movie;
            PriceLabel.setText("Price: "+ TM.getEntryPrice());
        }else{
            PriceLabel.setText("there is no price it's a coming soon movie ...");
        }
        GenerLabel.setText("Genre:"+movie.getGenere());
        MovieImage.setImage(movie.getImageProperty());
        descriptionLabel.setText("description:\n"+movie.getDescription());
        if (movie.getClass().equals(TheaterMovie.class)){
            TheaterMovie TM=(TheaterMovie)movie;
            ScreeningLabel.setText("screening Times: \n "+TM.getMSList());
        }else if(movie.getClass().equals(HomeMovie.class)){
            ScreeningLabel.setText("HOME MOVIE");
            screenPane.setVisible(false);
        }else{
            priceRequestLabel.setVisible(false);
            sendBtn.setVisible(false);
            PriceField.setVisible(false);
            ScreeningLabel.setText("COMING SOON...");
            BuyBtn.setVisible(false);
        }
    }
    @FXML
    void DeleteMovie(ActionEvent event) {
        msgObject msg=new msgObject("#deleteMovie");
        msg.setObject(movie);
        try {
			/*Stage stage=(Stage)Delete_Btn.getScene().getWindow();
			stage.close();;*/
            SimpleClient.getClient().sendToServer(msg);
			/*stage.setOnHiding((e) -> {
				handleRefresh(new ActionEvent());
			});*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void EditBtn(ActionEvent event) throws IOException {
        if(movie.getClass().equals(TheaterMovie.class)){
            clickedMovie=movie;
            GridCatalogController.clickedMovie=movie;
            msgObject msg=new msgObject("#getAllTheatres");
            SimpleClient.getClient().sendToServer(msg);
            System.out.println("message sent to server to get all theaters ");
        }else{
            System.out.println("the movie isn't for screening");
        }
    }

    public void openEditpage(List<Theater> TheatersList) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieTimeEdit.fxml"));
        Parent parent = loader.load();
        EditTimeController controller = (EditTimeController) loader.getController();
        if(clickedMovie==null){
            System.out.println(" null 129");
        }else{
            System.out.println("not  null");
        }
        controller.inflatUI(clickedMovie,TheatersList);
        Stage stage = new Stage();
        stage.setTitle("Edit Movie");
        stage.setScene(new Scene(parent));
        stage.show();
       /* stage.setOnHiding((e) -> {
            handleRefresh(new ActionEvent());
        });*/
    }
    
    @FXML
    void sendRequest(ActionEvent event) throws IOException {
        if(movie.getClass().equals(TheaterMovie.class)||movie.getClass().equals(HomeMovie.class)){
            PriceRequest pr=new PriceRequest();
            String description="change price request for '"+movie.getEngName()+"' movie";
            if(PriceField.getText().isEmpty()){
               /* message.setText("the field is empty");*/
                return;
            }
            Integer newprice=Integer.parseInt(PriceField.getText());
            if (movie.getClass().equals(TheaterMovie.class)){
                TheaterMovie TM=(TheaterMovie) movie;
                pr.setOldPrice(TM.getEntryPrice());
                pr.setNewPrice(newprice);
                pr.setDescription(description);
            }
            if (movie.getClass().equals(HomeMovie.class)){
                HomeMovie HM=(HomeMovie) movie;
                pr.setOldPrice(HM.getEntryPrice());
                pr.setNewPrice(newprice);
                pr.setDescription(description);
            }
            pr.setMovieID(movie.getMovieId());
            msgObject msg=new msgObject("#addPriceRequest",pr);
            SimpleClient.getClient().sendToServer(msg);
            PriceField.clear();
        }else{
           /* message.setText("this is a coming soon movie there is no price now");*/
            return;
        }
    }
    
    
    private void handleRefresh(ActionEvent actionEvent) {
        try {
            //SimpleClient.getClient().sendToServer("#warning");
            msgObject msg=new msgObject("#getAllMovies");
            SimpleClient.getClient().sendToServer(msg);
            System.out.println("message sent to server to get all movies");
            System.out.println(MovieGridController.clickedMovie.getEngName());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void reopenEditpage(List<Theater> TheatersList,Movie movie) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieTimeEdit.fxml"));
        Parent parent = loader.load();
        EditTimeController controller = (EditTimeController) loader.getController();
        if(movie==null){
            System.out.println(" null 129");
        }else{
            System.out.println("not  null");
        }
        controller.inflatUI(movie,TheatersList);
        Stage stage = new Stage();
        stage.setTitle("Edit Movie");
        stage.setScene(new Scene(parent));
        stage.show();
        /*stage.setOnHiding((e) -> {
            handleRefresh(new ActionEvent());
        });*/
    }
    @FXML
    void OpenBuyWindow(ActionEvent event) throws IOException {
        if(movie.getClass().equals(TheaterMovie.class)){
            FXMLLoader loader=new FXMLLoader(getClass().getResource(("BuyTicketWindow.fxml")));
            Parent parent=loader.load();
            BuyTicketWindowController controller=(BuyTicketWindowController) loader.getController();
            controller.setDetails((TheaterMovie) movie);
            Stage stage=new Stage();
            stage.setTitle("Buy Ticket "+movie.getEngName());
            stage.setScene(new Scene(parent));
            stage.show();
        }
        else if (movie.getClass().equals(HomeMovie.class)){
            FXMLLoader loader=new FXMLLoader(getClass().getResource(("HomeMovieBuyWindow.fxml")));
            Parent parent=loader.load();
            HomeMovieBuyWindowController controller=(HomeMovieBuyWindowController)loader.getController();
            controller.setDetails((HomeMovie) movie);
           /* BuyTicketWindowController controller=(BuyTicketWindowController) loader.getController();
            controller.setDetails((TheaterMovie) movie);*/
            Stage stage=new Stage();
            stage.setTitle("Buy Ticket "+movie.getEngName());
            stage.setScene(new Scene(parent));
            stage.show();
        }

    }
}
