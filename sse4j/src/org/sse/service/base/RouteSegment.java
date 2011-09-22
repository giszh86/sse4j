package org.sse.service.base;

import java.util.ArrayList;
import java.util.List;

import org.sse.geo.Point;

/**
 * Route(Trip) Segment
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class RouteSegment {
	private String name;
	private int kind;
	private int attrib;
	private int length;
	private int speed;
	private int cost;
	private int sAngle;
	private int eAngle;
	private int circleNum;
	private int lightFlag;
	private List<Integer> ids;
	private List<Point> points;

	// private String roads;
	// private String vertexes;

	public RouteSegment() {
		ids = new ArrayList<Integer>();
		points = new ArrayList<Point>();
		name = "";
		// roads = "";
		// vertexes = "";
	}

	public boolean equalsIgnore(RouteSegment seg) {
		boolean b1 = name.equalsIgnoreCase(seg.getName())
				&& attrib == seg.getAttrib() && kind == seg.getKind();
		boolean b2 = name.equalsIgnoreCase(seg.getName())
				&& kind == seg.getKind()
				&& (seg.getAttrib() == EdgeType.RD_CONN || attrib == EdgeType.RD_CONN);
		return b1 || b2;
	}

	/**
	 * seg first point equals this last point
	 * 
	 * @param seg
	 * @return
	 */
	public boolean connect(RouteSegment seg) {
		return points.get(points.size() - 1).equals(seg.getPoints().get(0));
	}

	public boolean connectFlag(RouteSegment seg, boolean start) {
		if (start)
			return !(this.lightFlag == 1 && seg.lightFlag == 0);
		else
			return !(this.lightFlag == 0 && seg.lightFlag == 1);
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public int getAttrib() {
		return attrib;
	}

	public void setAttrib(int attrib) {
		this.attrib = attrib;
	}

	public int getLength() {
		return length;
	}

	/**
	 * 
	 * @param length
	 *            长度[米]
	 */
	public void setLength(int length) {
		this.length = length;
	}

	public int getSpeed() {
		return speed;
	}

	/**
	 * 
	 * @param speed
	 *            车速[公里/小时]
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getCost() {
		return cost;
	}

	/**
	 * 
	 * @param cost
	 *            花费[秒]
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getSAngle() {
		return sAngle;
	}

	public void setSAngle(int angle) {
		sAngle = angle;
	}

	public int getEAngle() {
		return eAngle;
	}

	public void setEAngle(int angle) {
		eAngle = angle;
	}

	public int getCircleNum() {
		return circleNum;
	}

	/**
	 * 
	 * @param circleNum
	 *            环岛出口序号
	 */
	public void setCircleNum(int circleNum) {
		this.circleNum = circleNum;
	}

	public int getLightFlag() {
		return lightFlag;
	}

	/**
	 * 
	 * @param lightFlag
	 *            道路终点红绿灯标识
	 */
	public void setLightFlag(int lightFlag) {
		this.lightFlag = lightFlag;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	// public String getRoads() {
	// return roads;
	// }
	//
	// public void setRoads(String roads) {
	// this.roads = roads;
	// }
	//
	// public String getVertexes() {
	// return vertexes;
	// }
	//
	// public void setVertexes(String vertexes) {
	// this.vertexes = vertexes;
	// }
}
