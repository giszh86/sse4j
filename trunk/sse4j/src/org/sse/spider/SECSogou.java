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
		// System.out.println(sb);

		String start = "<div class=\"results\" ><!--a-->";
		String end = "<!--z--></div>";
		String startSprit = "<div class=\"rb\"><h3 class=\"pt\"><a";
		String endSprit = "</a></div></div><!--STATUS VR OK-->";
		int fromIndex = sb.indexOf(start);
		int endLength = sb.indexOf(end);
		while (fromIndex < endLength) {
			int startIndex = sb.indexOf(startSprit, fromIndex);
			fromIndex = startIndex + startSprit.length();
			int endIndex = sb.indexOf(endSprit, fromIndex);
			if (endIndex > startIndex && endIndex >= 0 && endIndex < endLength
					&& startIndex >= 0) {
				// TODO
				result.addLink(sb.substring(startIndex, endIndex
						+ endSprit.length()));
				fromIndex = endIndex + endSprit.length();
			} else {
				fromIndex = endLength;
			}
		}

		return result;
	}

}
