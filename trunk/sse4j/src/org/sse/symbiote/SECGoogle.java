package org.sse.symbiote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 
 * @author dux(duxionggis@126.com)
 * @deprecated
 */
public class SECGoogle extends SECallable {

	public SECGoogle(String keyword) {
		super(keyword);
		init();
	}

	private void init() {
		url = "http://www.google.com.hk/#hl=zh-CN&newwindow=1&safe=strict&q=";
	}

	public SECResult call() throws Exception {
		SECResult result = new SECResult();

		URL uri = new URL(url + keyword + "&fp=1&cad=b");
		BufferedReader reader = new BufferedReader(new InputStreamReader(uri
				.openStream(), "Big5"));
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
