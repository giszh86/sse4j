package org.sse.ws;

import org.sse.geo.Feature;
import org.sse.geo.FeatureCollection;
import org.sse.geo.Schema;
import org.sse.util.EarthPos;
import org.sse.util.Google;
import org.sse.util.Radix;
import org.sse.ws.base.WSRouteDataSet;
import org.sse.ws.base.WSRouteGuid;
import org.sse.ws.base.WSRouteSeg;

import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author dux(duxionggis@126.com)
 */
public class RGUID2FC {
	public FeatureCollection to(String dataset) {
		WSRouteDataSet ds = new Gson().fromJson(dataset, WSRouteDataSet.class);

		Schema s = new Schema();
		s.addAttribute("Name", "String");
		FeatureCollection fc = new FeatureCollection(s);
		GeometryFactory gf = new GeometryFactory();
		int cx = (ds.getMinx() + ds.getMaxx()) / 2;
		int cy = (ds.getMiny() + ds.getMaxy()) / 2;
		if (ds.getSegs() != null && ds.getSegs().length > 0) {
			s.addAttribute("Attrib", "Integer");
			s.addAttribute("Circle", "Integer");
			s.addAttribute("Kind", "Integer");
			s.addAttribute("Light", "Integer");
			s.addAttribute("Roads", "String");
			for (WSRouteSeg g : ds.getSegs()) {
				Feature f = new Feature(s);
				f.setAttribute("Name", g.getName());
				f.setAttribute("Attrib", g.getAttrib());
				f.setAttribute("Circle", g.getCircle());
				f.setAttribute("Kind", g.getKind());
				f.setAttribute("Light", g.getLight());
				f.setAttribute("Roads", g.getRoads());

				String[] ss = g.getVertexes().split(";");
				Coordinate[] coords = new Coordinate[ss.length];
				for (int i = 0; i < ss.length; i++) {
					String[] xy = ss[i].split(",");
					long x = Radix.x2h(xy[0], 36) + cx;
					long y = Radix.x2h(xy[1], 36) + cy;
					EarthPos pos = Google.googToDegree(x, y);
					coords[i] = new Coordinate(pos.xLon, pos.yLat);
				}
				f.setGeometry(gf.createLineString(coords));
				fc.add(f);
			}
		} else if (ds.getGuids() != null && ds.getGuids().length > 0) {
			s.addAttribute("Cost", "Integer");
			s.addAttribute("Len", "Integer");
			s.addAttribute("State", "String");
			s.addAttribute("Turn", "String");
			for (WSRouteGuid g : ds.getGuids()) {
				Feature f = new Feature(s);
				f.setAttribute("Name", g.getName());
				f.setAttribute("Cost", g.getCost());
				f.setAttribute("Len", g.getLen());
				f.setAttribute("State", g.getState());
				f.setAttribute("Turn", g.getTurn());

				String[] ss = g.getVertexes().split(";");
				Coordinate[] coords = new Coordinate[ss.length];
				for (int i = 0; i < ss.length; i++) {
					String[] xy = ss[i].split(",");
					long x = Radix.x2h(xy[0], 36) + cx;
					long y = Radix.x2h(xy[1], 36) + cy;
					EarthPos pos = Google.googToDegree(x, y);
					coords[i] = new Coordinate(pos.xLon, pos.yLat);
				}
				f.setGeometry(gf.createLineString(coords));
				fc.add(f);
			}
		}
		return fc;
	}

}
