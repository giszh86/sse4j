package org.sse.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sse.ws.base.WSResult;

public class Routing extends HttpServlet {
	private static final long serialVersionUID = 1L;
	org.sse.ws.Routing routing = new org.sse.ws.Routing();
	
	public Routing() {
		super();
	}
	
	//	<ws:webPlan>         
	//	  <arg0>            
	//	    <endPoint>
	//	      <x></x>
	//	      <y></y>
	//	    </endPoint>            
	//	    <key></key>            
	//	    <preference></preference>            
	//	    <startPoint>
	//	      <x></x>
	//	      <y></y>
	//	    </startPoint>            
	//	    <!--viaPoints>
	//	      <x></x>
	//	      <y></y>
	//	    </viaPoints-->
	//	  </arg0>
	//	</ws:webPlan>
	//
	//	<ws:plan>         
	//	  <arg0>            
	//	    <endPoint>
	//	      <x></x>
	//	      <y></y>
	//	    </endPoint>            
	//	    <key></key>            
	//	    <preference></preference>            
	//	    <startPoint>
	//	      <x></x>
	//	      <y></y>
	//	    </startPoint>            
	//	    <!--viaPoints>
	//	      <x></x>
	//	      <y></y>
	//	    </viaPoints-->
	//	  </arg0>
	//	</ws:plan>
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
