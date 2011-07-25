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
	String keyword = "";
	String url = "";
	String source = "";
	String startSprit = "";
	String endSprit = "";
	String charset = "GBK";

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
		int rank = 0;
		while (fromIndex < sb.length()) {
			int startIndex = sb.indexOf(startSprit, fromIndex);
			fromIndex = startIndex + startSprit.length();
			int endIndex = sb.indexOf(endSprit, fromIndex);
			if (endIndex > startIndex && startIndex >= 0) {
				String link = sb.substring(startIndex, endIndex
						+ endSprit.length());
				// System.out.println(link);
				result.addLink(this.buildItem(link, source, rank));
				fromIndex = endIndex + endSprit.length();
				rank++;
			} else {
				fromIndex = sb.length();
			}
		}

		return result;
	}

	SECResult.Item buildItem(String link, String source, int rank)
			throws Exception {
		SECResult.Item item = new SECResult.Item();
		item.setSource(source);
		item.setRank(rank);
		int idx1 = link.indexOf("href=");
		int idx2 = link.indexOf("\"", idx1 + 6);
		if (idx2 > idx1 && idx1 >= 0) {
			item.setHref(link.substring(idx1 + 6, idx2));
			int idx3 = link.indexOf(">", idx2);
			int idx4 = link.indexOf("</a>", idx3);
			if (idx4 > idx3 && idx3 >= 0) {
				item.setTitle(link.substring(idx3 + 1, idx4).trim());
				int idx5 = link.indexOf("href=", idx4);
				int idx6 = link.indexOf("\"", idx5 + 6);
				if (idx6 > idx5 && idx5 >= 0) {
					item.setShapshot(link.substring(idx5 + 6, idx6));
				}
			}
		}
		return item;
	}

}
