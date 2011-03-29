package org.sse.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sse.ws.base.WSResult;

public class Searching extends HttpServlet {
	private static final long serialVersionUID = 1L;
	org.sse.ws.Searching searching = new org.sse.ws.Searching();

	public Searching() {
		super();
	}

	//	<ws:poiInfo>
	//	  <arg0>
	//	    <id></id>
	//	    <key></key>
	//	  </arg0>
	//	</ws:poiInfo>
	//
	//	<ws:search>
	//	  <arg0>
	//	    <count></count>
	//	    <!--distance></distance>
	//	    <geometryWKT></geometryWKT-->
	//	    <key></key>
	//	    <keyword></keyword>
	//	    <preference></preference>
	//	  </arg0>
	//	</ws:search>
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//TODO
		// 1 get parameters

		// 2 execute
		WSResult result = null;

		// 3 write
		GZipWriter.write(result, response);
	}
}
