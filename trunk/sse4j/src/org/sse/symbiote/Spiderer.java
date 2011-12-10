package org.sse.symbiote;

import java.util.Collections;
import java.util.Comparator;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.sse.symbiote.SECResult.Item;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Spiderer {
	private static Spiderer instance;
	private static Lock lock = new ReentrantLock();

	ExecutorService es = null;

	public static Spiderer getInstance() {
		if (instance == null) {
			lock.lock();
			instance = new Spiderer();
			lock.unlock();
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
	 *             cache then invoke stop() function
	 */
	public List<Item> crawl(String keyword) throws InterruptedException {
		List<Item> result = new LinkedList<Item>();
		ConcurrentLinkedQueue<Callable<SECResult>> tasks = new ConcurrentLinkedQueue<Callable<SECResult>>();
		tasks.add(new SECBaidu(keyword));
		tasks.add(new SECSogou(keyword));
		List<Future<SECResult>> fr = es.invokeAll(tasks, 30, TimeUnit.SECONDS);
		for (Iterator<Future<SECResult>> i = fr.iterator(); i.hasNext();) {
			try {
				SECResult r = i.next().get();
				for (Iterator<Item> ii = r.getLinks().iterator(); ii.hasNext();) {
					Item item = ii.next();
					if (!result.contains(item)) {
						result.add(item);
					}
				}
			} catch (ExecutionException e) {
				// TODO
			}
		}
		Collections.sort(result, new RankComparator());
		return result;
	}

	public void stop() {
		es.shutdown();
	}

	private class RankComparator implements Comparator<Item> {
		public int compare(Item o1, Item o2) {
			if (o1.getRank() <= o2.getRank())
				return 0;
			else
				return 1;
		}
	}
}
