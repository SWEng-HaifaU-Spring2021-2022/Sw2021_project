package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.util.List;

import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.TheaterMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.entities.msgObject;
import javafx.application.Platform;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;
	public static Object obj=null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		/*if (msg.getClass().equals(List.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
			obj=(List<Movie>)msg;
		}*/
		System.out.println("get over here");
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
			else if(tempmsg.getMsg().equals("movieShowsForMovie")) {
				System.out.println("movie shows arrived1");
				Platform.runLater(()->{
					obj=tempmsg.getObject();
					CatalogController Catalog=new CatalogController();
					System.out.println("movie shows arrived2");
					try {
						System.out.println("movie shows arrived3");
						Catalog.openEditPage();
						System.out.println("movie shows arrived4");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				
			}else if(tempmsg.getMsg().equals("newmovieShowadd")){
				System.out.println("an object have been added");
				Platform.runLater(()->{
					EditTimeController ETC=new EditTimeController();
					ETC.afterinserting();
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

