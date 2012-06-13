package org.sse.ws.base;

import java.io.Serializable;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class WSRouteSeg implements Serializable {
	private static final long serialVersionUID = 1L;

	private int kind;
	private int attrib;
	private int circle;
	private int light;
	// private int cost;
	private String name;
	private String roads;
	private String vertexes;

	public WSRouteSeg() {
		name = "";
		roads = "";
		vertexes = "";
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

	public int getCircle() {
		return circle;
	}

	public void setCircle(int circle) {
		this.circle = circle;
	}

	public int getLight() {
		return light;
	}

	public void setLight(int light) {
		this.light = light;
	}

	// public int getCost() {
	// return cost;
	// }
	//
	// public void setCost(int cost) {
	// this.cost = cost;
	// }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoads() {
		return roads;
	}

	public void setRoads(String roads) {
		this.roads = roads;
	}

	public String getVertexes() {
		return vertexes;
	}

	public void setVertexes(String vertexes) {
		this.vertexes = vertexes;
	}

}
