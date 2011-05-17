package org.sse.spider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class SECSogou extends SECallable {
	private String url = "http://www.sogou.com/web?query=";

	public SECSogou() {
	}

	public SECSogou(String keyword) {
		super(keyword);
	}

	public SECResult call() throws Exception {
		URL uri = new URL(url + this.getKeyword());
		BufferedReader reader = new BufferedReader(new InputStreamReader(uri
				.openStream(), charset));
		String s;
		StringBuffer sb = new StringBuffer();
		while ((s = reader.readLine()) != null) {
			sb.append(s.trim());
		}
		reader.close();
		System.out.println(sb.toString());

		// TODO
		return null;
	}

}
