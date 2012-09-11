package org.sse.ws.base;

import java.io.Serializable;

import com.google.gson.Gson;

/**
 * @author dux(duxionggis@126.com)
 */
public class WSRouteDataSet implements Serializable {
	private static final long serialVersionUID = 1L;

	private int dis;
	private int cost;
	private int minx;
	private int miny;
	private int maxx;
	private int maxy;
	private WSRouteSeg[] segs;
	private WSRouteGuid[] guids;

	public int getDis() {
		return dis;
	}

	/**
	 * @param dis
	 *            总路程[米]
	 */
	public void setDis(int dis) {
		this.dis = dis;
	}

	public int getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            总旅行时间[分钟]
	 */
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

	public WSRouteSeg[] getSegs() {
		return segs;
	}

	public void setSegs(WSRouteSeg[] segs) {
		this.segs = segs;
	}

	public WSRouteGuid[] getGuids() {
		return guids;
	}

	public void setGuids(WSRouteGuid[] guids) {
		this.guids = guids;
	}

	public String toString() {
		return new Gson().toJson(this);
	}

}
