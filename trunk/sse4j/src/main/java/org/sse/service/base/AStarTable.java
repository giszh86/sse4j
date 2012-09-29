package org.sse.service.base;

import java.util.ArrayList;
import java.util.List;

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

	float getFn(int id) {
		return 0f;
	}

	void put(LinkedNode next) {
	}

	void remove(float fn, int id) {
	}
}

/**
 * AStarTable using ArrayList
 * 
 * @author dux(duxionggis@126.com)
 */
class AStarArrayTable extends AStarTable {
	private List<LinkedNode> openAsc = null;
	private float[] openFn = null;

	AStarArrayTable(int nodesize) {
		openAsc = new ArrayList<LinkedNode>(defaultSize());
		openFn = new float[nodesize];
	}

	boolean isEmpty() {
		return openAsc.isEmpty();
	}

	int size() {
		return openAsc.size();
	}

	LinkedNode pollFirst() {
		LinkedNode first = openAsc.remove(0);
		openFn[first.id - 1] = 0f;
		return first;
	}

	LinkedNode pollLast() {
		LinkedNode last = openAsc.remove(size() - 1);
		openFn[last.id - 1] = 0f;
		return last;
	}

	float getFn(int id) {
		return openFn[id - 1];
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
		openFn[next.id - 1] = next.fn;
	}

	void remove(float fn, int id) {
		int idx = binarySearch(0, (size() - 1) / 2, (size() - 1), fn, id);
		if (idx >= 0) {
			openAsc.remove(idx);
		}
		openFn[id - 1] = 0f;
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

	private int binarySearch(int sidx, int midx, int eidx, float fn, int id) {
		if ((eidx - sidx) <= 1) {
			LinkedNode mid = openAsc.get(sidx);
			if (mid.id == id)
				return sidx;
			mid = openAsc.get(eidx);
			if (mid.id == id)
				return eidx;
			return -1;
		}

		LinkedNode mid = openAsc.get(midx);
		if (Math.abs(mid.fn - fn) <= 1.0e-5 && mid.id == id) {
			return midx;
		} else if (mid.fn > fn) {
			return binarySearch(sidx, (sidx + midx) / 2, midx, fn, id);
		} else {
			return binarySearch(midx, (midx + eidx) / 2, eidx, fn, id);
		}
	}

}
