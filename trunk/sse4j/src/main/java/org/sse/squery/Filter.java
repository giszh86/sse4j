package org.sse.squery;

import java.util.List;

import org.sse.idx.base.Enums.AnalyzerType;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author dux(duxionggis@126.com)
 */
public class Filter {
	private Geometry geometry;
	private List<Property> properties;
	private int count = 50;
	private AnalyzerType type = AnalyzerType.IK;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		if (count < 1)
			count = 50;
		this.count = count;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * @param geometry
	 *            [WGS84]
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public AnalyzerType getType() {
		return type;
	}

	public void setType(AnalyzerType type) {
		this.type = type;
	}

}
