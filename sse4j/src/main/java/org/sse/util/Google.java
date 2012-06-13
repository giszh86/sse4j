package org.sse.util;

import java.util.List;
import java.util.Vector;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public strictfp class Google {
	private static List<Double> Ac;
	private static List<Double> Bc;
	private static List<Double> Cc;
	private static List<Double> Zc;
	static {
		int levels = 22;
		double c = 256;
		double e;
		Ac = new Vector<Double>(levels);
		Bc = new Vector<Double>(levels);
		Cc = new Vector<Double>(levels);
		Zc = new Vector<Double>(levels);
		for (int d = 0; d < levels; d++) {
			e = c / 2;
			Bc.add(c / 360.0);
			Cc.add(c / (2 * Math.PI));
			Zc.add(e);
			Ac.add(c);
			c *= 2;
		}
	}

	public static int getSize() {
		return 256;
	}

	private static double minmax(double a, double b, double c) {
		return Math.min(Math.max(a, b), c);
	}

	public static EarthPos degreeToPixel(double lon, double lat, int zoom) {
		double d = Zc.get(zoom);
		double e = Math.round(d + lon * Bc.get(zoom));
		double f = minmax(Math.sin((Math.PI / 180.0) * lat), -0.9999, 0.9999);
		double g = Math.round(d + 0.5 * Math.log((1 + f) / (1 - f))
				* -Cc.get(zoom));
		return new EarthPos(e, g);
	}

	public static void pixelToTile(EarthPos pos) {
		pos.xLon = (int) (pos.xLon / 256);
		pos.yLat = (int) (pos.yLat / 256);
	}

	public static EarthPos pixelToDegree(int x, int y, int zoom) {
		double e = Zc.get(zoom);
		double f = (x - e) / Bc.get(zoom);
		double g = (y - e) / -Cc.get(zoom);
		double h = (180.0 / Math.PI)
				* (2 * Math.atan(Math.exp(g)) - 0.5 * Math.PI);
		return new EarthPos(f, h);
	}

	public static EarthPos degreeToGoog(double lon, double lat) {
		double a = Math.log(Math.tan((90 + lat) * Math.PI / 360))
				/ (Math.PI / 180);
		double custLat = a * 20037508.342789 / 180;
		double custLon = lon;
		custLon = custLon * 20037508.342789 / 180;
		return new EarthPos(custLon, custLat);
	}

	public static EarthPos googToDegree(double x, double y) {
		double lat_deg, lon_deg;
		lat_deg = (y / 20037508.342789) * 180;
		lon_deg = (x / 20037508.342789) * 180;
		lat_deg = 180
				/ Math.PI
				* (2 * Math.atan(Math.exp(lat_deg * Math.PI / 180)) - Math.PI / 2);
		return new EarthPos(lon_deg, lat_deg);
	}

}
