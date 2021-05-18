package il.cshaifasweng.OCSFMediatorExample.client;

import java.util.List;

import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.TheaterMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;
	public static Object obj=null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		obj=msg;
		TheaterMovie tm=(TheaterMovie)obj;
		System.out.println(tm.getDescription());
		/*if (msg.getClass().equals(List.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
			obj=(List<Movie>)msg;
		}*/

	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
