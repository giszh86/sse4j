package org.sse.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Routing extends HttpServlet {
	private static final long serialVersionUID = 1L;
	org.sse.ws.Routing routing = new org.sse.ws.Routing();
	
	public Routing() {
		super();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
