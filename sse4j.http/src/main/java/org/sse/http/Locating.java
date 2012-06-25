package org.sse.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sse.ws.base.WSFilterGeoc;
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
public class Locating extends HttpServlet {
	private static final long serialVersionUID = 1L;
	org.sse.ws.Locating locating = new org.sse.ws.Locating();

	public Locating() {
		super();
	}

	// <ws:geocoding>
	// 	<arg0>
	// 		<address></address>
	// 		<key></key>
	// 	</arg0>
	// </ws:geocoding>
	//
	// <ws:reverseGeocoding>
	// 	<arg0>
	// 		<x></x>
	// 		<y></y>
	// 	</arg0>
	// </ws:reverseGeocoding>
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1 get parameters			
		String xml = request.getParameter("xml");
		// System.out.println(xml);
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

	private WSResult excute(Document doc) {
		if (doc == null)
			return null;
		String firstTag = doc.getDocumentElement().getTagName();
		NodeList list = doc.getDocumentElement().getFirstChild()
				.getChildNodes();
		if (firstTag == null || list == null || list.getLength() == 0)
			return null;
		if (firstTag.equalsIgnoreCase("ws:geocoding")) {
			WSFilterGeoc geoc = new WSFilterGeoc();
			String name = null;
			String val = null;
			for (int i = 0; i < list.getLength(); i++) {
				name = list.item(i).getNodeName();
				val = list.item(i).getTextContent();
				if (!val.isEmpty())
					if (name.equalsIgnoreCase("address"))
						geoc.setAddress(val);
					else if (name.equalsIgnoreCase("key"))
						geoc.setKey(val);
			}
			return locating.geocoding(geoc);
		} else if (firstTag.equalsIgnoreCase("ws:reverseGeocoding")) {
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
			return locating.reverseGeocoding(pt);
		}
		return null;
	}
}
