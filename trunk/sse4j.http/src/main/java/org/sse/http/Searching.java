package org.sse.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sse.ws.base.WSFilter;
import org.sse.ws.base.WSFilterPoi;
import org.sse.ws.base.WSResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Desc: xml=&gzip={true,false}
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Searching extends HttpServlet {
	private static final long serialVersionUID = 1L;
	org.sse.ws.Searching searching = new org.sse.ws.Searching();

	public Searching() {
		super();
	}

	// <ws:poiInfo>
	// 	<arg0>
	// 		<id></id>
	// 		<key></key>
	// 	</arg0>
	// </ws:poiInfo>
	//
	// <ws:search>
	// 	<arg0>
	// 		<count></count>
	// 		<!--distance></distance>
	// 		<geometryWKT></geometryWKT-->
	// 		<key></key>
	// 		<keyword></keyword>
	// 		<preference></preference>
	// 	</arg0>
	// </ws:search>
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
		if (firstTag.equalsIgnoreCase("ws:poiInfo")) {
			WSFilterPoi filter = new WSFilterPoi();
			String name = null;
			String val = null;
			for (int i = 0; i < list.getLength(); i++) {
				name = list.item(i).getNodeName();
				val = list.item(i).getTextContent();
				if (!val.isEmpty())
					if (name.equalsIgnoreCase("id"))
						filter.setId(val);
					else if (name.equalsIgnoreCase("key"))
						filter.setKey(val);
			}
			return searching.poiInfo(filter);
		} else if (firstTag.equalsIgnoreCase("ws:search")) {
			WSFilter filter = new WSFilter();
			String name = null;
			String val = null;
			for (int i = 0; i < list.getLength(); i++) {
				name = list.item(i).getNodeName();
				val = list.item(i).getTextContent();
				if (!val.isEmpty())
					if (name.equalsIgnoreCase("count"))
						filter.setCount(Integer.valueOf(val).intValue());
					else if (name.equalsIgnoreCase("distance"))
						filter.setDistance(Integer.valueOf(val).intValue());
					else if (name.equalsIgnoreCase("geometryWKT"))
						filter.setGeometryWKT(val);
					else if (name.equalsIgnoreCase("key"))
						filter.setKey(val);
					else if (name.equalsIgnoreCase("keyword"))
						filter.setKeyword(val);
					else if (name.equalsIgnoreCase("preference"))
						filter.setPreference(val);
			}
			return searching.search(filter);
		}
		return null;
	}
}
