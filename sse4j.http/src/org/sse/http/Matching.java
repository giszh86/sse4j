package org.sse.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sse.ws.base.WSResult;
import org.w3c.dom.Document;

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
		// 1 get parameters
		String match = request.getParameter("matching");

		// 2 write
		GZipWriter.write(this.excute(XmlParser.getDocument(match)), response);
	}
	
	private WSResult excute(Document doc) {
		return null;
	}
}