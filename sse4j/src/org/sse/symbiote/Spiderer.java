package org.sse.symbiote;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.sse.symbiote.SECResult.Item;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Spiderer {
	public static void main(String[] args) throws Exception {
		String key = "海淀";
		String ekey = URLEncoder.encode(key, "UTF-8");
		// new SECGoogle(ekey).call();

		List<Item> result = new LinkedList<Item>();
		ExecutorService es = Executors.newCachedThreadPool();
		ConcurrentLinkedQueue<Callable<SECResult>> tasks = new ConcurrentLinkedQueue<Callable<SECResult>>();
		tasks.add(new SECBaidu(ekey));
		tasks.add(new SECSogou(ekey));
		tasks.add(new SECYoudao(ekey));
		List<Future<SECResult>> fr = es.invokeAll(tasks, 30, TimeUnit.SECONDS);
		for (Iterator<Future<SECResult>> i = fr.iterator(); i.hasNext();) {
			SECResult r = i.next().get();
			for (Iterator<Item> ii = r.getLinks().iterator(); ii.hasNext();) {
				Item item = ii.next();
				if (!result.contains(item))
					result.add(item);
			}
		}
		es.shutdown();
		
		for (Iterator<Item> ii = result.iterator(); ii.hasNext();) {
			Item item = ii.next();
			System.out.println(item.getHref() + "_" + item.getShapshot() + "_"
					+ item.getSource());
		}

		// es.invokeAll(tasks);
		// System.out.println(ekey + "_" + URLEncoder.encode(ekey, "UTF-8"));
	}
}
