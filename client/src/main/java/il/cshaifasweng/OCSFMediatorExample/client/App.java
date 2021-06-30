package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/*import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;*/
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class App extends Application {

    private static Scene scene;
    private static Stage STAGE;
    private static SimpleClient client;

    //private static Stage stage=new Stage();
    public static void setScene(Scene newscene) {
        scene = newscene;

    }

    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getDefault().register(this);
        client = SimpleClient.getClient();
        client.openConnection();
        scene = new Scene(loadFXML("primary"));
        //scene.getStylesheets().add(Main.class.getResource("/il/cshaifasweng/OCSFMediatorExample/CSSFiles/bootstrap3.css").toExternalForm());
        /*JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(scene);*/
       // scene.setFill(Color.WHITE);
        STAGE = stage;
        STAGE.setScene(scene);
        STAGE.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
       if (SimpleClient.getUser() != null) SimpleClient.RequestLogOut();
        while (SimpleClient.getUser() != null) {
            System.out.println("Logging out");
        }
        EventBus.getDefault().unregister(this);
        super.stop();
    }

    @Subscribe
    public void onWarningEvent(WarningEvent event) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION,
                    String.format("Message: %s\nTimestamp: %s\n",
                            event.getWarning().getMessage(),
                            event.getWarning().getTime().toString())
            );
            alert.show();
        });

    }
   /* @Subscribe
    public  void onReportinfoEvent(ReportinfoEvent event){

        System.out.println("test event bus");
        System.out.println(((msgObject)event.getReceivedData()).getMsg());
    }*/
    public static void main(String[] args) {
        launch();
    }


}