package org.sse.squery;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.kdtree.KdNode;
import com.vividsolutions.jts.index.kdtree.KdTree;
import com.vividsolutions.jts.index.strtree.STRtree;

/**
 * @author dux(duxionggis@126.com)
 */
public class STree_bak {
	private Object sidx = null;
	private Envelope extent = null;

	public STree_bak(boolean isPoint) {
		if (isPoint) {
			sidx = new KdTree();
		} else {
			sidx = new STRtree();
		}
	}

	public void insert(Envelope envelope, Object key) {
		if (sidx instanceof KdTree) {
			((KdTree) sidx).insert(envelope.centre(), key);
		} else {
			((STRtree) sidx).insert(envelope, key);
		}
		if (extent == null)
			extent = envelope;
		else
			extent.expandToInclude(envelope);
	}

	public void build() {
		if (sidx instanceof STRtree) {
			((STRtree) sidx).build();
		}
	}

	List spatialFilter(Envelope envelope) {
		if (sidx instanceof KdTree) {
			List list = ((KdTree) sidx).query(envelope);
			List result = new ArrayList(list.size());
			for (int i = 0; i < list.size(); i++) {
				result.add(((KdNode) list.get(i)).getData());
			}
			return result;
		} else {
			return ((STRtree) sidx).query(envelope);
		}
	}

	Envelope getExtent() {
		return extent;
	}
}
