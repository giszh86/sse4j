package org.sse.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Locating extends HttpServlet {
	private static final long serialVersionUID = 1L;
	org.sse.ws.Locating locating = new org.sse.ws.Locating();
	
	public Locating() {
		super();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
