package org.sse.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sse.ws.base.WSResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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
		try {
			// 1 get parameters
			request.setCharacterEncoding(GZipWriter.charset);
			String xml = request.getParameter("xml");
			// 2 write
			GZipWriter.write(this.excute(XmlParser.getDocument(xml)), response);
		} catch (Exception ex) {
			WSResult result = new WSResult();
			result.setResultCode(0);
			result.setFaultString(ex.getMessage());
			GZipWriter.write(result, response);
		}
	}
	
	private WSResult excute(Document doc) {
		if(doc==null)
			return null;
		String firstTag = doc.getDocumentElement().getTagName();
		NodeList list = doc.getDocumentElement().getFirstChild()
				.getChildNodes();
		if (firstTag == null || list == null || list.getLength() == 0)
			return null;
		
		return null;
	}
}
