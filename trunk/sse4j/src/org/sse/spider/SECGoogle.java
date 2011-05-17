package org.sse.spider;

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
		// TODO
		return null;
	}
}
