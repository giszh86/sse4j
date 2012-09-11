package org.sse.test.ws;

import org.sse.ws.Locating;
import org.sse.ws.Routing;
import org.sse.ws.base.WSFilterGeoc;
import org.sse.ws.base.WSPointF;
import org.sse.ws.base.WSResult;

public class WSLocatingTest {

	public static void main(String[] args) throws Exception {
		new Routing();
		Thread.sleep(10000);

		WSResult result = new Locating().reverseGeocoding(new WSPointF(116.4f, 39.9f));
		System.out.println(result.getJsonString());

		WSFilterGeoc geoc = new WSFilterGeoc();
		geoc.setKey("110000");
		geoc.setAddress("学院路25号");
		result = new Locating().geocoding(geoc);
		System.out.println(result.getJsonString());

	}

}
