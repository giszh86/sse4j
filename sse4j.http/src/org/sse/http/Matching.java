package org.sse.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sse.ws.base.WSResult;

public class Matching extends HttpServlet {
	private static final long serialVersionUID = 1L;
	org.sse.ws.Matching matching = new org.sse.ws.Matching();
	
	public Matching() {
		super();
	}

	//	<ws:districtMatch>
	//	  <arg0>
	//	    <x></x>
	//	    <y></y>
	//	  </arg0>
	//	</ws:districtMatch>
	//
	//	<ws:roadMatch>         
	//	  <arg0>            
	//	    <endPoint>
	//	      <x></x>
	//	      <y></y>
	//	    </endPoint>    
	//	    <startPoint>
	//	      <x></x>
	//	      <y></y>
	//	    </startPoint>            
	//	  </arg0>
	//	</ws:roadMatch>
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
