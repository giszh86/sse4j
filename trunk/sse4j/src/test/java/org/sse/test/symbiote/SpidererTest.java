package org.sse.test.symbiote;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import org.sse.symbiote.Spiderer;
import org.sse.symbiote.SECResult.Item;

public class SpidererTest {

	public static void main(String[] args) throws Exception {
		String key = "sse4j";
		String ekey = URLEncoder.encode(key, "UTF-8");
		System.out.println(ekey + "_" + URLEncoder.encode(ekey, "UTF-8"));

		List<Item> result = Spiderer.getInstance().crawl(ekey);
		for (Iterator<Item> it = result.iterator(); it.hasNext();) {
			Item item = it.next();
			System.out.println(item.getSource() + " -- " + item.getHref() + " -- " + item.getShapshot() + " -- "
					+ item.getTitle());
		}
		Spiderer.getInstance().stop();
	}
}
