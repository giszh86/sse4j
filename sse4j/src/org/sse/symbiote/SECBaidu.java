package org.sse.symbiote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class SECBaidu extends SECallable {

	public SECBaidu(String keyword) {
		super(keyword);
		init();
	}

	private void init() {
		url = "http://www.baidu.com/s?wd=";
		startSprit = "<table cellpadding=\"0\" cellspacing=\"0\" class=\"result\" id=";
		endSprit = "</td></tr></table><br>";
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
		item.setSource(SECResult.Type.BAIDU.name());
		// TODO
		return item;
	}
}
