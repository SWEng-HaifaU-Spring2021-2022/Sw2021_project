package il.cshaifasweng.OCSFMediatorExample.client;

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
		//System.out.println("sadasdads");
		if(msg.getClass().equals(msgObject.class)) {
			System.out.print("msg arrived");
			msgObject tempmsg=(msgObject)msg;
			if(tempmsg.getMsg().equals("AllMovies")) {
				Platform.runLater(()->{
					CatalogController Catalog=new CatalogController();
					List<Movie>TML=(List<Movie>)tempmsg.getObject();
					Catalog.LoadData(TML);
				});
			}
		}
		System.out.println("sadasdads");

	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
