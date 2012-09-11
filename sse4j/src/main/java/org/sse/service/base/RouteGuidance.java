package org.sse.service.base;

import java.util.ArrayList;
import java.util.List;

import org.sse.geo.Point;

/**
 * Route(Trip) Guidance
 * 
 * @author dux(duxionggis@126.com)
 */
public class RouteGuidance {
	private String icon;
	private String name;
	private String nextName;
	private String turn;
	private int length;
	private int cost;
	private List<Point> points;
	private String remark;

	// private String vertexes;

	public RouteGuidance() {
		icon = "";
		name = "";
		nextName = "";
		turn = "";
		points = new ArrayList<Point>();
		remark = "";
		// vertexes = "";
	}

	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            转向图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNextName() {
		return nextName;
	}

	public void setNextName(String nextName) {
		this.nextName = nextName;
	}

	public String getTurn() {
		return turn;
	}

	/**
	 * @param turn
	 *            转向描述
	 */
	public void setTurn(String turn) {
		this.turn = turn;
	}

	public int getLength() {
		return length;
	}

	/**
	 * @param length
	 *            长度[米]
	 */
	public void setLength(int length) {
		this.length = length;
	}

	public int getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            时间[秒]
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	// public String getVertexes() {
	// return vertexes;
	// }
	//
	// public void setVertexes(String vertexes) {
	// this.vertexes = vertexes;
	// }

}
