package org.sse.symbiote;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class SECYoudao extends SECallable {

	public SECYoudao(String keyword) {
		super(keyword);
		init();
	}

	private void init() {
		url = "http://www.youdao.com/search?q=";
		source = SECResult.Type.YOUDAO.name();
		startSprit = "<li><h3><a href=";
		endSprit = "</a></span></p></li>";
		charset = "UTF-8";
	}

	@Override
	SECResult.Item buildItem(String link, String source) throws Exception {
		SECResult.Item item = new SECResult.Item();
		item.setSource(source);
		int idx1 = link.indexOf("href=");
		int idx2 = link.indexOf("\"", idx1 + 6);
		if (idx2 > idx1 && idx1 >= 0) {
			item.setHref(link.substring(idx1 + 6, idx2));
			int idx3 = link.indexOf(">", idx2);
			int idx4 = link.indexOf("</a>", idx3);
			if (idx4 > idx3 && idx3 >= 0) {
				item.setTitle(link.substring(idx3 + 1, idx4).trim());
				int idx5 = link.indexOf("cache?docid", idx4);
				int idx6 = link.indexOf("\"", idx5);
				if (idx6 > idx5 && idx5 >= 0) {
					item.setShapshot("http://www.youdao.com/"
							+ link.substring(idx5, idx6));
				}
			}
		}
		return item;
	}

}
