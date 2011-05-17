package org.sse.http;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sse.map.HotMapper;

/**
 * Desc:
 * http://<server>:<port>/sse4j/servlet/HotTile?zoom=&x=&y=&type=&keyword=[
 * &key=]
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class HotTile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(HotTile.class.getName());

	public HotTile() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String charset = request.getCharacterEncoding();
		if (charset == null)
			charset = "ISO-8859-1";

		String type = request.getParameter("type"); // "img" or "js"
		String x = request.getParameter("x");
		String y = request.getParameter("y");
		String zoom = request.getParameter("zoom");
		String keyword = new String(request.getParameter("keyword").getBytes(
				charset));
		if (x != null && y != null && zoom != null && type != null
				&& keyword != null) {
			response.setCharacterEncoding(charset);
			try {
				String path = HotMapper.getInstance().createHotmap(
						Integer.valueOf(zoom), Integer.valueOf(y),
						Integer.valueOf(x), keyword,
						request.getParameter("key"));
				if (type.equalsIgnoreCase("img")) {
					response.setContentType("image/png");
					request.getRequestDispatcher(path + ".png").forward(
							request, response);
				} else {
					response.setContentType("application/javascript");
					request.getRequestDispatcher(path + ".js").forward(request,
							response);
				}
			} catch (Exception e) {
				logger.severe(e.getMessage() + "\n x=" + x + "&y=" + y
						+ "&zoom=" + zoom + "&keyword=" + keyword + "&type="
						+ type);
			}
		} else {
			logger.warning("x=" + x + "&y=" + y + "&zoom=" + zoom + "&keyword="
					+ keyword + "&type=" + type);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
