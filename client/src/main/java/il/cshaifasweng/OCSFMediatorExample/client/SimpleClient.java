package il.cshaifasweng.OCSFMediatorExample.client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import javafx.application.Platform;

import javax.xml.catalog.Catalog;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;
	public static Object obj=null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		//System.out.println("message arrived");
		msgObject temp=(msgObject)msg;
		//System.out.println(temp.getMsg());
		if(msg.getClass().equals(msgObject.class)) {
			System.out.print("msg arrived");
			msgObject tempmsg=(msgObject)msg;
			if(tempmsg.getMsg().equals("AllMovies")) {
				Platform.runLater(()->{
					try {
						obj=tempmsg.getObject();
						App.setRoot("Catalog");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
			else if(tempmsg.getMsg().equals("Theaters Retrived")) {

				Platform.runLater(()->{
					CatalogController Catalog=new CatalogController();
					ArrayList<Theater>TheatersList=(ArrayList<Theater>)tempmsg.getObject();
					try {
						System.out.println("Openning the edit Page");
						Catalog.openEditPage(TheatersList);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});

			}else if(tempmsg.getMsg().equals("newmovieShowadd")){
				System.out.println("an object have been added");
				Platform.runLater(()->{
				});
			}else if(tempmsg.getMsg().equals("MovieShow Deleted")){
				System.out.println("an object have been deleted");
				Platform.runLater(()->{
					List<MovieShow> MSL=(List<MovieShow>) tempmsg.getObject();
					EditTimeController EditController=new EditTimeController();
					EditController.reloadTable(MSL);
				});
			}else if(tempmsg.getMsg().equals("movie show updated")){
				System.out.println("an object have been updated");
				Platform.runLater(()->{
				});
			}
		}


	}

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}