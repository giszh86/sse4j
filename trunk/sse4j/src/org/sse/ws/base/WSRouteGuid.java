package org.sse.ws.base;

import java.io.Serializable;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class WSRouteGuid implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String state;
	// private String icon;
	private String turn;
	private int len;
	private int cost;
	private String vertexes;

	public WSRouteGuid() {
		name = "";
		state = "";
		// icon = "";
		turn = "";
		vertexes = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	/**
	 * 
	 * @param state
	 *            交通状况[A/B/C]
	 */
	public void setState(String state) {
		this.state = state;
	}

	// public String getIcon() {
	// return icon;
	// }
	//
	// public void setIcon(String icon) {
	// this.icon = icon;
	// }

	public String getTurn() {
		return turn;
	}

	public void setTurn(String turn) {
		this.turn = turn;
	}

	public int getLen() {
		return len;
	}

	/**
	 * 
	 * @param len
	 *            道路长度[米]
	 */
	public void setLen(int len) {
		this.len = len;
	}

	public int getCost() {
		return cost;
	}

	/**
	 * 
	 * @param cost
	 *            旅行时间[秒]
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getVertexes() {
		return vertexes;
	}

	public void setVertexes(String vertexes) {
		this.vertexes = vertexes;
	}

}
