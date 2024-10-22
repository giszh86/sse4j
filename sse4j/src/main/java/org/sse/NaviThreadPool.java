package org.sse;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author dux(duxionggis@126.com)
 */
class NaviThreadPool {
	private static List<Thread> threads = new LinkedList<Thread>();

	static void put(Thread e) {
		if (!check(e.getName())) {
			threads.add(e);
		} else {
		}
	}

	static boolean check(String key) {
		for (Iterator<Thread> it = threads.iterator(); it.hasNext();) {
			if (it.next().getName().equalsIgnoreCase(key))
				return true;
		}
		return false;
	}

	static Thread get(String key) {
		for (Iterator<Thread> it = threads.iterator(); it.hasNext();) {
			Thread e = it.next();
			if (e.getName().equalsIgnoreCase(key)) {
				return e;
			}
		}
		return null;
	}

	static boolean runnable(String key) {
		for (Iterator<Thread> it = threads.iterator(); it.hasNext();) {
			Thread e = it.next();
			if (e.getName().equalsIgnoreCase(key) && e.getState() == Thread.State.RUNNABLE) {
				return true;
			}
		}
		return false;
	}
}
