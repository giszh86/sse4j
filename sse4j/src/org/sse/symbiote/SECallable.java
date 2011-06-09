package org.sse.symbiote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
abstract class SECallable implements Callable<SECResult> {
	protected String keyword = "";
	protected String url = "";
	protected String startSprit = "";
	protected String endSprit = "";
	protected String charset = "GBK";

	SECallable(String keyword) {
		this.keyword = keyword;
	}

	public SECResult call() throws Exception {
		SECResult result = new SECResult();

		URL uri = new URL(url + keyword);
		BufferedReader reader = new BufferedReader(new InputStreamReader(uri
				.openStream(), charset));
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
			if (endIndex > startIndex && startIndex >= 0) {
				String link = sb.substring(startIndex, endIndex
						+ endSprit.length());
				// System.out.println(link);
				result.addLink(this.buildItem(link));
				fromIndex = endIndex + endSprit.length();
			} else {
				fromIndex = sb.length();
			}
		}

		return result;
	}

	protected SECResult.Item buildItem(String link) throws Exception {
		throw new Exception("unimplement function!");
	}

}
