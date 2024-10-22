package org.sse.http.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.sse.ws.base.WSResult;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class GZipWriter {
	public final static String charset = "UTF-8";
	private final static String contentTypeZip = "application/zip";
	private final static String contentTypeXml = "text/xml";

	// <return>
	//   <faultString></faultString>
	// 	 <jsonString></jsonString>
	// 	 <resultCode></resultCode>
	// </return>
	public static void write(WSResult result, HttpServletResponse response, boolean zip) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		if (result == null) {
			sb.append("<return><faultString>null</faultString>");
			sb.append("<jsonString></jsonString>");
			sb.append("<resultCode>0</resultCode></return>");
		} else {
			sb.append("<return><faultString>").append(result.getFaultString());
			sb.append("</faultString><jsonString>").append(result.getJsonString());
			sb.append("</jsonString><resultCode>").append(result.getResultCode());
			sb.append("</resultCode></return>");
		}

		response.setCharacterEncoding(charset);
		if (zip) {
			response.setHeader("Content-disposition", "attachment;filename=" + System.currentTimeMillis() + ".zip");
			response.setContentType(contentTypeZip);
			OutputStream ops = response.getOutputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			GZIPOutputStream gos = new GZIPOutputStream(bos);
			gos.write(sb.toString().getBytes(charset));
			gos.finish();
			gos.close();

			response.setContentLength(bos.size());
			ops.write(bos.toByteArray());
			bos.flush();
			bos.close();
			ops.flush();
			ops.close();
		} else {
			response.setContentType(contentTypeXml);
			PrintWriter out = response.getWriter();
			out.print(sb.toString());
			out.close();
		}
	}

}
