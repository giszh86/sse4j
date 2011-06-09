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
				String link = sb.substring(startIndex, endIndex
						+ endSprit.length());
				System.out.println(link);
				result.addLink(this.buildItem(link));
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
//		int idx1 = link.indexOf("href=");
//		int idx2 = link.indexOf("target=", idx1);
//		if (idx2 > idx1) {
//			String tmp = link.substring(idx1 + 6, idx2).trim();
//			item.setHref(tmp.substring(0, tmp.length() - 1));
//			int idx3 = link.indexOf("<font size=-1>", idx2);
//			int idx4 = link.indexOf("<br><span", idx3);
//			if (idx4 > idx3) {
//				item.setRemark(link.substring(idx3 + 14, idx4).trim());
//				int idx5 = link.indexOf("href=", idx4);
//				int idx6 = link.indexOf("target=", idx5);
//				if (idx6 > idx5) {
//					String tmp1 = link.substring(idx5 + 6, idx6).trim();
//					item.setShapshot(tmp1.substring(0, tmp1.length() - 1));
//				}
//			}
//		}
		return item;
	}
}
