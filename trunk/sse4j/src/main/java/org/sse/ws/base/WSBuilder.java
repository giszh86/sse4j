package org.sse.ws.base;

import java.util.ArrayList;
import java.util.List;

import org.sse.io.Enums.OccurType;
import org.sse.service.base.PoiPtyName;
import org.sse.service.base.RouteDataSet;
import org.sse.service.base.RouteGuidance;
import org.sse.service.base.RouteSegment;
import org.sse.service.base.Router;
import org.sse.service.base.RouterPreference;
import org.sse.squery.Filter;
import org.sse.squery.Property;
import org.sse.squery.PtyName;
import org.sse.util.MercatorUtil;
import org.sse.util.Radix;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author dux(duxionggis@126.com)
 */
public class WSBuilder {
	public static Router router(WSRouter wsRouter) {
		Router router = new Router();
		router.setStartPoint(toPt(wsRouter.getStartPoint()));
		router.setEndPoint(toPt(wsRouter.getEndPoint()));
		router.setPreference(Enum.valueOf(RouterPreference.class, wsRouter.getPreference()));
		if (wsRouter.getViaPoints() != null) {
			List<Point> vias = new ArrayList<Point>();
			for (WSPointF p : wsRouter.getViaPoints())
				if (p != null)
					vias.add(toPt(p));
			router.setViaPoints(vias);
		}
		return router;
	}

	public static Filter filter(WSFilter wsFilter) throws Exception {
		Filter filter = new Filter();
		String keyword = wsFilter.getKeyword();
		if (keyword != null && !keyword.trim().isEmpty()) {
			List<Property> ptyes = new ArrayList<Property>();
			ptyes.add(new Property(PtyName.TITLE, keyword, OccurType.AND));
			// TODO
			if (wsFilter.getPreference().equalsIgnoreCase("POI")) {
				ptyes.add(new Property(PoiPtyName.ADDRESS, keyword, OccurType.OR));
				// ptyes.add(new Property(PoiPtyName.NAMEP, keyword, OccurType.OR));
				// ptyes.add(new Property(PoiPtyName.KIND, keyword, OccurType.OR));
			}
			filter.setProperties(ptyes);
		}
		if (wsFilter.getGeometryWKT() != null && !wsFilter.getGeometryWKT().trim().isEmpty()) {
			Geometry g = MercatorUtil.toGeometry(wsFilter.getGeometryWKT(), true);
			filter.setGeometry(g);
		}
		filter.setCount(wsFilter.getCount());
		if (wsFilter.getDistance() > 0) {
			filter.setGeometry(filter.getGeometry().buffer(wsFilter.getDistance()));
		}
		return filter;
	}

	public static WSRouteDataSet build(RouteDataSet ds, boolean webplan) throws Exception {
		WSRouteDataSet ws = new WSRouteDataSet();
		ws.setDis(ds.getDistance());
		ws.setCost(ds.getCost() / 60);
		ws.setMaxx(ds.getMaxx());
		ws.setMaxy(ds.getMaxy());
		ws.setMinx(ds.getMinx());
		ws.setMiny(ds.getMiny());
		int x = (ws.getMinx() + ws.getMaxx()) / 2;
		int y = (ws.getMiny() + ws.getMaxy()) / 2;
		if (ds.getSegments() == null || ds.getSegments().size() == 0)
			throw new Exception("not found!");
		if (webplan) {
			ws.setSegs(new WSRouteSeg[0]);
			WSRouteGuid[] guids = new WSRouteGuid[ds.getGuidances().size()];
			for (int i = 0; i < ds.getGuidances().size(); i++) {
				guids[i] = buildGuid(ds.getGuidances().get(i), webplan, x, y);
			}
			ws.setGuids(guids);
		} else {
			WSRouteSeg[] segs = new WSRouteSeg[ds.getSegments().size()];
			for (int i = 0; i < ds.getSegments().size(); i++) {
				segs[i] = buildSeg(ds.getSegments().get(i), x, y);
			}
			ws.setSegs(segs);
			WSRouteGuid[] guids = new WSRouteGuid[ds.getGuidances().size()];
			for (int i = 0; i < ds.getGuidances().size(); i++) {
				guids[i] = buildGuid(ds.getGuidances().get(i), webplan, x, y);
			}
			ws.setGuids(guids);
		}
		ds.getGuidances().clear();
		ds.getSegments().clear();
		ds = null;
		return ws;
	}

	private static WSRouteGuid buildGuid(RouteGuidance guid, boolean webplan, int centx, int centy) {
		WSRouteGuid result = new WSRouteGuid();
		result.setCost(guid.getCost());
		// result.setIcon(guid.getIcon());
		result.setLen(guid.getLength());
		result.setName(guid.getName());
		result.setTurn(guid.getTurn());
		if (webplan) {
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < guid.getPoints().size(); j++) {
				guid.getPoints().get(j).minus(centx, centy);
				sb.append(Radix.h2x(guid.getPoints().get(j).x, 36));
				sb.append(",");
				sb.append(Radix.h2x(guid.getPoints().get(j).y, 36));
				sb.append(";");
			}
			if (sb.length() > 0)
				result.setVertexes(sb.substring(0, sb.length() - 1));

			// //delete point
			// if (guid.getPoints().size() < 2) {
			// // TODO
			// System.out.println("Warn:" + guid.getPoints());
			// } else {
			// List<org.navi.geo.Point> lcoords = new
			// ArrayList<org.navi.geo.Point>();
			// double range = 0;
			// if (guid.getPoints().get(1).x != guid.getPoints().get(0).x)
			// range = (guid.getPoints().get(1).y - guid.getPoints()
			// .get(0).y)
			// * 1.0
			// / (guid.getPoints().get(1).x - guid.getPoints()
			// .get(0).x);
			// for (int j = 0; j < guid.getPoints().size(); j++) {
			// if (j == 0) {
			// lcoords.add(guid.getPoints().get(0));
			// lcoords.get(lcoords.size() - 1).minus(centx, centy);
			// } else if (j == guid.getPoints().size() - 1) {
			// lcoords.add(guid.getPoints().get(
			// guid.getPoints().size() - 1));
			// lcoords.get(lcoords.size() - 1).minus(centx, centy);
			// } else {
			// double cur = 0;
			// if (guid.getPoints().get(j + 1).x != guid.getPoints()
			// .get(j).x)
			// cur = (guid.getPoints().get(j + 1).y - guid
			// .getPoints().get(j).y)
			// * 1.0
			// / (guid.getPoints().get(j + 1).x - guid
			// .getPoints().get(j).x);
			// if (Math.abs(cur - range) > 0.02) {
			// lcoords.add(guid.getPoints().get(j));
			// lcoords.get(lcoords.size() - 1).minus(centx, centy);
			// }
			// range = cur;
			// }
			// }
			// String str = lcoords.toString().replaceAll(", ", ";");
			// result.setVertexes(str.substring(1, str.length() - 1));
			// lcoords.clear();
			// lcoords = null;
			// }
		}
		result.setState(guid.getRemark());

		return result;
	}

	private static WSRouteSeg buildSeg(RouteSegment seg, int centx, int centy) {
		WSRouteSeg result = new WSRouteSeg();
		result.setAttrib(seg.getAttrib());
		result.setCircle(seg.getCircleNum());
		result.setKind(seg.getKind());
		result.setLight(seg.getLightFlag());
		// result.setCost(seg.getCost());
		result.setName(seg.getName());
		String str = seg.getIds().toString().replaceAll(", ", ",");
		if (str.length() > 2)
			result.setRoads(str.substring(1, str.length() - 1));

		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < seg.getPoints().size(); j++) {
			seg.getPoints().get(j).minus(centx, centy);
			sb.append(Radix.h2x(seg.getPoints().get(j).x, 36));
			sb.append(",");
			sb.append(Radix.h2x(seg.getPoints().get(j).y, 36));
			sb.append(";");
		}
		if (sb.length() > 0)
			result.setVertexes(sb.substring(0, sb.length() - 1));

		return result;
	}

	public static Point toPt(WSPointF pt) {
		return new GeometryFactory().createPoint(toCoord(pt));
	}

	public static Coordinate toCoord(WSPointF p) {
		return new Coordinate(p.getX(), p.getY());
	}

}
