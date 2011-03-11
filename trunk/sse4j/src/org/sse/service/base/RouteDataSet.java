package org.sse.service.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Route(Trip) dataset
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class RouteDataSet {
	private int distance;// 总路程[米]
	private int cost;// 总花费[秒]
	private int minx;
	private int miny;
	private int maxx;
	private int maxy;
	private List<RouteSegment> segments;
	private List<RouteGuidance> guidances;

	public RouteDataSet() {
		segments = new ArrayList<RouteSegment>();
		guidances = new ArrayList<RouteGuidance>();
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getMinx() {
		return minx;
	}

	public void setMinx(int minx) {
		this.minx = minx;
	}

	public int getMiny() {
		return miny;
	}

	public void setMiny(int miny) {
		this.miny = miny;
	}

	public int getMaxx() {
		return maxx;
	}

	public void setMaxx(int maxx) {
		this.maxx = maxx;
	}

	public int getMaxy() {
		return maxy;
	}

	public void setMaxy(int maxy) {
		this.maxy = maxy;
	}

	public List<RouteSegment> getSegments() {
		return segments;
	}

	public void setSegments(List<RouteSegment> segments) {
		this.segments = segments;
	}

	public List<RouteGuidance> getGuidances() {
		return guidances;
	}

	public void setGuidances(List<RouteGuidance> guidances) {
		this.guidances = guidances;
	}

}
