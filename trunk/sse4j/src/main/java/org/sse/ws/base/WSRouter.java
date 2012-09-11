package org.sse.ws.base;

import java.io.Serializable;

/**
 * @author dux(duxionggis@126.com)
 */
public class WSRouter implements Serializable {
	private static final long serialVersionUID = 1L;
	private WSPointF startPoint;// [WGS84]
	private WSPointF endPoint;// [WGS84]
	private WSPointF[] viaPoints;// [WGS84]
	private String preference = "Shortest"; // Fastest,Shortest,Cheapest,OnFoot
	private String key;

	public WSPointF getStartPoint() {
		return startPoint;
	}

	/**
	 * @param startPoint
	 *            起点[WGS84]
	 */
	public void setStartPoint(WSPointF startPoint) {
		this.startPoint = startPoint;
	}

	public WSPointF getEndPoint() {
		return endPoint;
	}

	/**
	 * @param endPoint
	 *            终点[WGS84]
	 */
	public void setEndPoint(WSPointF endPoint) {
		this.endPoint = endPoint;
	}

	public String getPreference() {
		return preference;
	}

	/**
	 * @param preference
	 *            Fastest,Shortest,Cheapest,OnFoot
	 */
	public void setPreference(String preference) {
		this.preference = preference;
	}

	public WSPointF[] getViaPoints() {
		return viaPoints;
	}

	/**
	 * @param viaPoints
	 *            途径点[WGS84]
	 */
	public void setViaPoints(WSPointF[] viaPoints) {
		this.viaPoints = viaPoints;
	}

	/**
	 * @return citycode(110000)
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            citycode(110000)
	 */
	public void setKey(String key) {
		this.key = key;
	}
}
