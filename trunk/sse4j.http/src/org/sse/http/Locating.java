package org.sse.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sse.ws.base.WSFilterGeoc;
import org.sse.ws.base.WSPointF;
import org.sse.ws.base.WSResult;

public class Locating extends HttpServlet {
	private static final long serialVersionUID = 1L;
	org.sse.ws.Locating locating = new org.sse.ws.Locating();

	public Locating() {
		super();
	}
	
	//	<ws:geocoding>
	//	  <arg0>
	//	    <address></address>
	//	    <key></key>
	//	  </arg0>
	//	</ws:geocoding>
	//
	//	<ws:reverseGeocoding>
	//	  <arg0>
	//	    <x></x>
	//	    <y></y>
	//	  </arg0>
	//	</ws:reverseGeocoding>
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO
		// 1 get parameters
		boolean isgeoc = false;		

		// 2 execute
		WSResult result = null;
		if (isgeoc) {
			WSFilterGeoc geoc = new WSFilterGeoc();
			
			result = locating.geocoding(geoc);
		} else {
			WSPointF point = new WSPointF();
			
			result = locating.reverseGeocoding(point);
		}

		// 3 write
		GZipWriter.write(result, response);
	}
}
