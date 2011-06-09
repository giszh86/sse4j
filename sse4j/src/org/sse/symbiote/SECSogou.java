package org.sse.symbiote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class SECSogou extends SECallable {

	public SECSogou(String keyword) {
		super(keyword);
		init();
	}

	private void init() {
		url = "http://www.sogou.com/web?query=";
		startSprit = "<div class=\"rb\"><h3 class=\"pt\"><a";
		endSprit = "</a></div></div><!--STATUS VR OK-->";
	}

	public SECResult call() throws Exception {
		SECResult result = new SECResult();

		URL uri = new URL(url + keyword);
		BufferedReader reader = new BufferedReader(new InputStreamReader(uri
				.openStream(), CHARSET_GB));
		String s;
		StringBuffer sb = new StringBuffer();
		while ((s = reader.readLine()) != null) {
			sb.append(s.trim());
		}
		reader.close();
		// System.out.println(sb);

		int fromIndex = 0;
		while (fromIndex < sb.length()) {
			int startIndex = sb.indexOf(startSprit, fromIndex);
			fromIndex = startIndex + startSprit.length();
			int endIndex = sb.indexOf(endSprit, fromIndex);
			if (endIndex > startIndex && endIndex >= 0 && startIndex >= 0) {
				// TODO
				result.addLink(sb.substring(startIndex, endIndex
						+ endSprit.length()));
				fromIndex = endIndex + endSprit.length();
			} else {
				fromIndex = sb.length();
			}
		}

		return result;
	}

	protected SECResult.Item buildItem(String link) throws Exception {
		SECResult.Item item = new SECResult.Item();
		item.setSource(SECResult.Type.SOGOU.name());
		// TODO
		return item;
	}
}
