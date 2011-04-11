package org.sse.util;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class EarthPos {
	public double xLon;
	public double yLat;
	private int width = 1;
	private int height = 1;

	public EarthPos(double xLon, double yLat) {
		this.xLon = xLon;
		this.yLat = yLat;
	}

	public void setBuffer(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public boolean equals(Object pt) {
		if (pt instanceof EarthPos)
			return (Math.abs(this.xLon - ((EarthPos) pt).xLon) <= width && Math
					.abs(this.yLat - ((EarthPos) pt).yLat) <= height);
		return false;
	}

}
