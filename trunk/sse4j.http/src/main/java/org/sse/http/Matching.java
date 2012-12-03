package org.sse.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sse.ws.base.WSFilterRM;
import org.sse.ws.base.WSPointF;
import org.sse.ws.base.WSResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Desc: xml=&gzip={true,false}
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1 get parameters
		String xml = request.getParameter("xml");
		// System.out.println(xml);
		String callback = request.getParameter("callback");
		if (callback != null && !callback.isEmpty()) {
			JSONPWriter.write(this.excute(XmlParser.getDocument(xml)), response, callback);
			return;
		}
		
		String gzip = request.getParameter("gzip");
		boolean zip = (gzip == null ? true : gzip.equalsIgnoreCase("true"));
		try {
			// 2 write
			GZipWriter.write(this.excute(XmlParser.getDocument(xml)), response, zip);
		} catch (Exception ex) {
			WSResult result = new WSResult();
			result.setResultCode(0);
			result.setFaultString(ex.getMessage());
			GZipWriter.write(result, response, zip);
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	private WSResult excute(Document doc) {
		if (doc == null)
			return null;
		String firstTag = doc.getDocumentElement().getTagName();
		NodeList list = doc.getDocumentElement().getFirstChild().getChildNodes();
		if (firstTag == null || list == null || list.getLength() == 0)
			return null;
		if (firstTag.equalsIgnoreCase("ws:districtMatch")) {
			WSPointF pt = new WSPointF();
			String name = null;
			String val = null;
			for (int i = 0; i < list.getLength(); i++) {
				name = list.item(i).getNodeName();
				val = list.item(i).getTextContent();
				if (!val.isEmpty())
					if (name.equalsIgnoreCase("x"))
						pt.setX(Float.valueOf(val).floatValue());
					else if (name.equalsIgnoreCase("y"))
						pt.setY(Float.valueOf(val).floatValue());
			}
			return matching.districtMatch(pt);
		} else if (firstTag.equalsIgnoreCase("ws:roadMatch")) {
			WSFilterRM filter = new WSFilterRM();
			String name = null;
			String val = null;
			for (int i = 0; i < list.getLength(); i++) {
				name = list.item(i).getNodeName();
				if (name.equalsIgnoreCase("endPoint")) {
					NodeList ends = list.item(i).getChildNodes();
					if (ends != null && ends.getLength() > 0) {
						WSPointF end = new WSPointF();
						for (int j = 0; j < ends.getLength(); j++) {
							name = ends.item(j).getNodeName();
							val = ends.item(j).getTextContent();
							if (!val.isEmpty())
								if (name.equalsIgnoreCase("x"))
									end.setX(Float.valueOf(val).floatValue());
								else if (name.equalsIgnoreCase("y"))
									end.setY(Float.valueOf(val).floatValue());
						}
						filter.setEndPoint(end);
					}
				} else if (name.equalsIgnoreCase("startPoint")) {
					NodeList starts = list.item(i).getChildNodes();
					if (starts != null && starts.getLength() > 0) {
						WSPointF start = new WSPointF();
						for (int j = 0; j < starts.getLength(); j++) {
							name = starts.item(j).getNodeName();
							val = starts.item(j).getTextContent();
							if (!val.isEmpty())
								if (name.equalsIgnoreCase("x"))
									start.setX(Float.valueOf(val).floatValue());
								else if (name.equalsIgnoreCase("y"))
									start.setY(Float.valueOf(val).floatValue());
						}
						filter.setStartPoint(start);
					}
				}
			}
			return matching.roadMatch(filter);
		}
		return null;
	}
}
