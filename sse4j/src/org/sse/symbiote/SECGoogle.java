package org.sse.symbiote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class SECGoogle extends SECallable {
	private String url = "http://www.google.com.hk/search?hl=zh-CN&newwindow=1&safe=strict&q=";

	public SECGoogle() {
	}

	public SECGoogle(String keyword) {
		super(keyword);
	}

	public SECResult call() throws Exception {
		SECResult result = new SECResult();

		URL uri = new URL(url + this.getKeyword());
		BufferedReader reader = new BufferedReader(new InputStreamReader(uri
				.openStream(), charset));
		String s;
		StringBuffer sb = new StringBuffer();
		while ((s = reader.readLine()) != null) {
			sb.append(s.trim());
		}
		reader.close();
		System.out.println(sb);
		
		return result;
	}
}
