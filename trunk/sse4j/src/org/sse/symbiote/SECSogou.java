package org.sse.symbiote;

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

	protected SECResult.Item buildItem(String link) throws Exception {
		SECResult.Item item = new SECResult.Item();
		item.setSource(SECResult.Type.SOGOU.name());
		int idx1 = link.indexOf("href=");
		int idx2 = link.indexOf("\"", idx1 + 6);
		if (idx2 > idx1 && idx1 >= 0) {
			item.setHref(link.substring(idx1 + 6, idx2));
			int idx3 = link.indexOf("id=\"cacheresult_summary", idx2);
			int idx4 = link.indexOf("</div><div class=", idx3);
			if (idx4 > idx3 && idx3 >= 0) {
				item.setRemark(link.substring(idx3 + 27, idx4).trim());
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
