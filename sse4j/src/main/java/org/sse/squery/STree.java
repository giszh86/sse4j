package org.sse.squery;

import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class STree {
	private STRtree sidx = null;
	private Envelope extent = null;

	public STree(boolean isPoint) {
		sidx = new STRtree();
	}

	public void insert(Envelope envelope, Object key) {
		sidx.insert(envelope, key);

		if (extent == null)
			extent = envelope;
		else
			extent.expandToInclude(envelope);
	}

	public void build() {
		sidx.build();
	}

	List spatialFilter(Envelope envelope) {
		return sidx.query(envelope);
	}

	Envelope getExtent() {
		return extent;
	}
}