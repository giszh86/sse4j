package org.sse.symbiote;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
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
	private static Spiderer instance;
	private static Object lock = new Object();

	ExecutorService es = null;

	public static Spiderer getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new Spiderer();
				}
			}
		}
		return instance;
	}

	protected Spiderer() {
		es = Executors.newCachedThreadPool();
	}

	/**
	 * 
	 * @param keyword
	 *            UTF-8 Encoding format
	 * @return
	 * @throws InterruptedException
	 */
	public List<Item> crawl(String keyword) throws InterruptedException {
		List<Item> result = new LinkedList<Item>();
		ConcurrentLinkedQueue<Callable<SECResult>> tasks = new ConcurrentLinkedQueue<Callable<SECResult>>();
		tasks.add(new SECBaidu(keyword));
		tasks.add(new SECSogou(keyword));
		tasks.add(new SECYoudao(keyword));
		List<Future<SECResult>> fr = es.invokeAll(tasks, 10, TimeUnit.SECONDS);
		for (Iterator<Future<SECResult>> i = fr.iterator(); i.hasNext();) {
			try {
				SECResult r = i.next().get();
				for (Iterator<Item> ii = r.getLinks().iterator(); ii.hasNext();) {
					Item item = ii.next();
					if (!result.contains(item))
						result.add(item);
				}
			} catch (ExecutionException e) {
				// TODO
			}
		}
		return result;
	}

	public void stop() {
		es.shutdown();
	}

}
