package org.sse.spider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class SECBaidu extends SECallable {
	private String url = "http://www.baidu.com/s?wd=";

	public SECBaidu() {
	}

	public SECBaidu(String keyword) {
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
