package org.sse.service.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author dux(duxionggis@126.com)
 */
abstract class AStarTable {
	private int openSize = 2048;

	boolean isEmpty() {
		return true;
	}

	int size() {
		return 0;
	}

	int defaultSize() {
		return openSize;
	}

	LinkedNode pollFirst() {
		return null;
	}

	LinkedNode pollLast() {
		return null;
	}

	LinkedNode get(LinkedNode next) {
		return null;
	}

	void put(LinkedNode next) {
	}

	void remove(LinkedNode tn) {
	}
}

/**
 * AStarTable using ArrayList
 * 
 * @author dux(duxionggis@126.com)
 */
class AStarArrayTable extends AStarTable {
	List<LinkedNode> openAsc = new ArrayList<LinkedNode>(defaultSize());
	private Map<Integer, Float> openIdx = new HashMap<Integer, Float>();// key(id) - value(fn)

	boolean isEmpty() {
		return openAsc.isEmpty();
	}

	int size() {
		return openAsc.size();
	}

	LinkedNode pollFirst() {
		LinkedNode first = openAsc.remove(0);
		openIdx.remove(first.id);
		return first;
	}

	LinkedNode pollLast() {
		LinkedNode last = openAsc.remove(size() - 1);
		openIdx.remove(last.id);
		return last;
	}

	LinkedNode get(LinkedNode next) {
		Float fn = openIdx.get(next.id);
		if (fn == null) {
			return null;
		} else {
			return binarySearch(0, (size() - 1) / 2, (size() - 1), fn, next.id);
		}
	}

	void put(LinkedNode next) {
		if (size() == 0) {
			openAsc.add(next);
		} else if (openAsc.get(0).fn > next.fn) {
			openAsc.add(0, next);
		} else if (openAsc.get(size() - 1).fn < next.fn) {
			openAsc.add(size(), next);
		} else {
			binaryInsert(0, (size() - 1) / 2, (size() - 1), next);
		}
		openIdx.put(next.id, next.fn);
	}

	void remove(LinkedNode tn) {
		openAsc.remove(tn);
		openIdx.remove(tn.id);
	}

	private void binaryInsert(int sidx, int midx, int eidx, LinkedNode next) {
		if ((eidx - sidx) <= 1) {
			LinkedNode mid = openAsc.get(eidx);
			if (mid.fn < next.fn)
				openAsc.add(eidx + 1, next);
			else {
				openAsc.add(eidx, next);
			}
			return;
		}

		LinkedNode mid = openAsc.get(midx);
		if (mid.fn > next.fn) {
			binaryInsert(sidx, (sidx + midx) / 2, midx, next);
		} else {
			binaryInsert(midx, (midx + eidx) / 2, eidx, next);
		}
	}

	private LinkedNode binarySearch(int sidx, int midx, int eidx, float fn, int id) {
		if ((eidx - sidx) <= 1) {
			LinkedNode mid = openAsc.get(sidx);
			if (mid.id == id)
				return mid;
			mid = openAsc.get(eidx);
			if (mid.id == id)
				return mid;
			return null;
		}

		LinkedNode mid = openAsc.get(midx);
		if (mid.fn == fn && mid.id == id) {
			return mid;
		} else if (mid.fn > fn) {
			return binarySearch(sidx, (sidx + midx) / 2, midx, fn, id);
		} else {
			return binarySearch(midx, (midx + eidx) / 2, eidx, fn, id);
		}
	}

}
