package org.sse.service.rp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.sse.StorageFactory;
import org.sse.StorageFactory.StorageType;
import org.sse.service.base.AStar;
import org.sse.service.base.LinkedNode;
import org.sse.service.base.RouteDataSet;
import org.sse.service.base.RouteEdge;
import org.sse.service.base.RouteNode;
import org.sse.service.base.RouteSegment;
import org.sse.service.base.Router;
import org.sse.service.base.RouterPreference;
import org.sse.util.Maths;
import org.sse.util.MercatorUtil;

import com.vividsolutions.jts.geom.Point;

/**
 * route plan depends on city district
 * 
 * @author dux(duxionggis@126.com)
 */
public class SimpleRouteService implements IRouteService {
	private double buffer = 500; // meter

	public void setBuffer(double buffer) {
		this.buffer = buffer;
	}

	public RouteDataSet plan(Router router, String key) throws Exception {
		MercatorUtil.toMercator(router.getStartPoint(), true);
		MercatorUtil.toMercator(router.getEndPoint(), true);
		double dis = Maths.getDistance(router.getStartPoint().getX(), router.getStartPoint().getY(), router
				.getEndPoint().getX(), router.getEndPoint().getY());
		if (dis < 100)
			throw new Exception("destination is near!");

		IdxRouteStorage storage = (IdxRouteStorage) StorageFactory.getInstance().getStorage(key, StorageType.NET);
		if (storage == null)
			throw new Exception("not found network data!");
		long t1 = System.currentTimeMillis();
		List<RouteSegment> traces = this.run(storage, router);
		System.out.println("plan:" + key + "--" + (System.currentTimeMillis() - t1));
		return RouteSegmentManager.createDataSet(traces, router.getControls());
	}

	private List<RouteSegment> run(IdxRouteStorage storage, Router router) throws Exception {
		List<RouteSegment> result = new LinkedList<RouteSegment>();
		result.add(null);

		RouteNode start = storage.nearestNode(router.getStartPoint(), buffer);
		if (start == null)
			throw new Exception("not found start nearest road!");
		RouteNode end = storage.nearestNode(router.getEndPoint(), buffer);
		if (end == null)
			throw new Exception("not found end nearest road!");
		if (start.getId() == end.getId())
			throw new Exception("destination is near!");

		List<Integer> nids = new ArrayList<Integer>();
		nids.add(start.getId());
		for (Point pt : router.getViaPoints()) {
			MercatorUtil.toMercator(pt, true);
			RouteNode n = storage.nearestNode(pt, buffer);
			if (n != null && !nids.contains(n.getId()))
				nids.add(n.getId());
		}
		nids.add(end.getId());

		float coef = 1.0f;
		if (router.getPreference() == RouterPreference.Fastest || router.getPreference() == RouterPreference.Cheapest)
			coef = 0.06f;// 0.04 0.12
		List<Integer> roadIds = new LinkedList<Integer>();
		List<Integer> nodeIds = new LinkedList<Integer>();
		List<Boolean> vias = new LinkedList<Boolean>();
		for (int i = nids.size() - 1; i > 0; i--) {
			boolean via = (i != nids.size() - 1);
			int startNodeId = nids.get(i - 1);
			LinkedNode trace = new AStar().find(startNodeId, nids.get(i), storage.getNodes(), storage.getEdges(), coef,
					router.getPreference());
			while (trace != null && trace.id != startNodeId) {
				roadIds.add(trace.preEdgeId);
				nodeIds.add(trace.preNode.id);
				vias.add(via);
				trace = trace.preNode;
				via = false;
			}
		}
		this.setSegments(storage, result, roadIds, nodeIds, vias);

		result.remove(result.size() - 1);
		return result;
	}

	private void setSegments(IdxRouteStorage storage, List<RouteSegment> result, List<Integer> roadIds,
			List<Integer> nodeIds, List<Boolean> vias) {
		Map<Integer, RouteEdge> roads = storage.getRouteEdges(roadIds);
		Iterator<Integer> ri = roadIds.iterator();
		Iterator<Integer> ni = nodeIds.iterator();
		Iterator<Boolean> vi = vias.iterator();
		for (; (ri.hasNext() && ni.hasNext() && vi.hasNext());) {
			RouteSegment seg = this.createSegment(storage, ni.next(), vi.next(), roads.get(ri.next()));
			if (!RouteSegmentManager.combine(result.get(0), seg, false))
				result.add(0, seg);
		}
		roads = null;
		roadIds = null;
		nodeIds = null;
		vias = null;
	}

	private RouteSegment createSegment(IdxRouteStorage storage, int preNodeId, boolean via, RouteEdge edge) {
		RouteSegment seg = new RouteSegment();
		seg.getIds().add(edge.getId());
		seg.setKind(edge.getKind());
		seg.setAttrib(edge.getType());
		seg.setLength(edge.getLength());
		seg.setName(edge.getName());
		if (edge.getStartNodeId() == preNodeId) {
			seg.setLightFlag(storage.getNodes().get(edge.getEndNodeId() - 1).getLightFlag());
			for (org.sse.geo.Point pt : edge.getPoints())
				seg.getPoints().add(pt);
		} else if (edge.getEndNodeId() == preNodeId) {
			seg.setLightFlag(storage.getNodes().get(edge.getStartNodeId() - 1).getLightFlag());
			for (org.sse.geo.Point pt : edge.getPoints())
				seg.getPoints().add(0, pt);
		}
		RouteSegmentManager.setRouteName(seg);
		if (via) {
			seg.setName(seg.getName() + "[VIA]");
		}
		return seg;
	}

}
