package org.sse.service.base;

import java.util.List;

import org.sse.geo.Point;

/**
 * Route Edge
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class RouteEdge extends Edge {
	private static final long serialVersionUID = 1L;

	private int edgeId;
	private String name;
	private List<Point> points;

	// private LineString geometry;

	public int getEdgeId() {
		return edgeId;
	}

	/**
	 * 
	 * @param edgeId
	 *            道路编号
	 */
	public void setEdgeId(int edgeId) {
		this.edgeId = edgeId;
	}

	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            路段名
	 */
	public void setName(String name) {
		this.name = name;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	// public LineString getGeometry() {
	// return geometry;
	// }
	// public void setGeometry(LineString geometry) {
	// this.geometry = geometry;
	// }

}
