package org.sse.symbiote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class SECYoudao extends SECallable {
	private String url = "http://www.youdao.com/search?q=";

	public SECYoudao() {
	}

	public SECYoudao(String keyword) {
		super(keyword);
	}

	public SECResult call() throws Exception {
		SECResult result = new SECResult();

		URL uri = new URL(url + this.getKeyword());
		BufferedReader reader = new BufferedReader(new InputStreamReader(uri
				.openStream(), "UTF-8"));
		String s;
		StringBuffer sb = new StringBuffer();
		while ((s = reader.readLine()) != null) {
			sb.append(s.trim());
		}
		reader.close();
		// System.out.println(sb);

		String start = "<ul id=\"results\" class=\"rz\">";
		String end = "</a></span></p></li></ul>";
		String startSprit = "<li><h3><a href=";
		String endSprit = "</a></span></p></li>";
		int fromIndex = sb.indexOf(start);
		int endLength = sb.indexOf(end) + end.length();
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

	protected SECResult.Item buildItem(String link) throws Exception {
		SECResult.Item item = new SECResult.Item();
		item.setSource(SECResult.Type.YOUDAO.name());
		// TODO
		return item;
	}
}
