package org.sse.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.sse.ws.base.WSResult;

/**
 * @author dux(duxionggis@126.com)
 */
class JSONPWriter {
	private final static String contentTypeJson = "application/javascript;charset=UTF-8";

	// {faultString:"",jsonString:{},resultCode:0|1}
	public static void write(WSResult result, HttpServletResponse response, String callback) throws IOException {
		StringBuffer sb = new StringBuffer();
		if (result == null) {
			sb.append(callback).append("({faultString:\"null\",jsonString:{},resultCode:0})");
		} else {
			sb.append(callback).append("({faultString:\"" + result.getFaultString() + "\",");
			sb.append("jsonString:" + result.getJsonString() + ",resultCode:" + result.getResultCode() + "})");
		}

		response.setContentType(contentTypeJson);
		PrintWriter out = response.getWriter();
		out.print(sb.toString());
		out.close();
	}
}
