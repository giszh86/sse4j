package org.sse.util;

import java.util.LinkedList;
import java.util.List;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author dux(duxionggis@126.com)
 */
public class MercatorUtil {

	public static void toMercator(Geometry g, boolean wgs) {
		if (wgs && g != null) {
			for (Coordinate coord : g.getCoordinates()) {
				EarthPos pos = Google.degreeToGoog(coord.x, coord.y);
				coord.x = Math.round(pos.xLon * 100) / 100;
				coord.y = Math.round(pos.yLat * 100) / 100;
			}
		}
	}

	public static Geometry toGeometry(String wkt, boolean wgs) {
		WKTReader reader = new WKTReader();
		try {
			Geometry g = reader.read(wkt);
			toMercator(g, wgs);
			return g;
		} catch (ParseException e) {
			System.out.println("Parse Geometry Exception!");
		}
		return null;
	}

	public static Geometry toJTSLineString(List<org.sse.geo.Point> pts) {
		if (pts == null || pts.size() < 2)
			return null;
		GeometryFactory gf = new GeometryFactory();
		Coordinate[] coords = new Coordinate[pts.size()];
		for (int i = 0; i < pts.size(); i++) {
			coords[i] = new Coordinate(pts.get(i).x, pts.get(i).y);
		}
		return gf.createLineString(coords);
	}

	public static Geometry toJTSPoint(double x, double y) {
		GeometryFactory gf = new GeometryFactory();
		Coordinate coord = new Coordinate(x, y);
		return gf.createPoint(coord);
	}

	public static org.sse.geo.Point toPoint(Coordinate coord, boolean wgs) {
		if (wgs) {
			EarthPos pos = Google.degreeToGoog(coord.x, coord.y);
			return new org.sse.geo.Point((int) Math.round(pos.xLon), (int) Math.round(pos.yLat));
		} else {
			return new org.sse.geo.Point((int) Math.round(coord.x), (int) Math.round(coord.y));
		}
	}

	public static List<org.sse.geo.Point> toPoints(Geometry g, boolean wgs) {
		if (g == null || g.isEmpty())
			return null;
		List<org.sse.geo.Point> result = new LinkedList<org.sse.geo.Point>();
		for (Coordinate coord : g.getCoordinates()) {
			result.add(toPoint(coord, wgs));
		}
		return result;
	}

}
