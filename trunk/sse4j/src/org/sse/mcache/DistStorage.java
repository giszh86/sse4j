package org.sse.mcache;

import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class DistStorage extends Storage {
	private Map<String, Geometry> geos;

	public DistStorage(String key, Map<String, Geometry> geos) {
		super(key);
		this.geos = geos;
	}

	public Map<String, Geometry> getGeos() {
		return geos;
	}

}
