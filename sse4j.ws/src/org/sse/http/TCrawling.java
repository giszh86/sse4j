package org.sse.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sse.symbiote.Spiderer;
import org.sse.symbiote.SECResult.Item;

/**
 * Test class Desc: http://<server>:<port>/sse4j/servlet/Crawling?keyword=
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class TCrawling extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(TCrawling.class.getName());

	public TCrawling() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		if (keyword != null) {
			try {
				List<Item> result = Spiderer.getInstance().crawl(keyword);
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				int index = 0;
				for (Iterator<Item> ii = result.iterator(); ii.hasNext();) {
					if (index >= 10)
						break;
					Item item = ii.next();
					out.println("<a href=\"" + item.getHref()
							+ "\" target=\"_blank\">" + item.getTitle()
							+ "</a></br>");
					index += 1;
				}
				out.flush();
				out.close();
				result = null;
			} catch (InterruptedException e) {
				logger.severe(e.getMessage() + "\t keyword=" + keyword);
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
