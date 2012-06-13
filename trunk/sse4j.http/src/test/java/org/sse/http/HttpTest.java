package org.sse.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

public class HttpTest {

	public static void main(String[] args) throws IOException {
		URL url = new URL("http://localhost:8080/sse4j/servlet/Searching");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Connection", "Keep-Alive");

		conn.connect();

		// send
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		// "xml=<ws:poiInfo><arg0><id>200</id><key>110000</key></arg0></ws:poiInfo>";
		String xml = "xml=<ws:search><arg0><count>50</count><distance></distance><geometryWKT></geometryWKT><key>110000</key><keyword>";
		xml += URLEncoder.encode("中关村北大街", "utf-8");
		xml += "</keyword><preference>POI</preference></arg0></ws:search>";
		out.writeBytes(xml);
		out.flush();
		out.close();

		// return
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new GZIPInputStream(conn.getInputStream())));
		String s;
		StringBuffer sb = new StringBuffer();
		while ((s = reader.readLine()) != null) {
			// System.out.println(s);
			sb.append(s.trim());
		}
		reader.close();

		conn.disconnect();

		// result
		System.out.println(sb.toString());
	}
}
