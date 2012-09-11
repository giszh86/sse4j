package org.sse.service.base;

import java.util.*;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Route Input Reference
 * 
 * @author dux(duxionggis@126.com)
 */
public class Router {
	private Point startPoint;
	private Point endPoint;
	private List<Point> viaPoints;
	private List<Geometry> barriers;
	private List<TrafficCtl> controls;
	private RouterPreference preference;

	public Router() {
		GeometryFactory gf = new GeometryFactory();
		startPoint = gf.createPoint(new Coordinate());
		endPoint = gf.createPoint(new Coordinate());
		viaPoints = new ArrayList<Point>();
		barriers = new ArrayList<Geometry>();
		controls = new ArrayList<TrafficCtl>();
		preference = RouterPreference.Shortest;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * @param startPoint
	 *            起点[WGS84]
	 */
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	/**
	 * @param endPoint
	 *            终点[WGS84]
	 */
	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public List<Point> getViaPoints() {
		return viaPoints;
	}

	/**
	 * @param viaPoints
	 *            途径点[WGS84]
	 */
	public void setViaPoints(List<Point> viaPoints) {
		this.viaPoints = viaPoints;
	}

	public List<Geometry> getBarriers() {
		return barriers;
	}

	/**
	 * @param barriers
	 *            障碍[WGS84][Point LineString Polygon LineRing]
	 */
	public void setBarriers(List<Geometry> barriers) {
		this.barriers = barriers;
	}

	public List<TrafficCtl> getControls() {
		return controls;
	}

	/**
	 * @param controls
	 *            交通管制
	 */
	public void setControls(List<TrafficCtl> controls) {
		this.controls = controls;
	}

	public RouterPreference getPreference() {
		return preference;
	}

	/**
	 * @param preference
	 *            寻路方式
	 */
	public void setPreference(RouterPreference preference) {
		this.preference = preference;
	}
}
