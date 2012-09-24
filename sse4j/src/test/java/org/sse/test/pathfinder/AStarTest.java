package org.sse.test.pathfinder;

import java.util.LinkedList;
import java.util.List;

import org.sse.NaviConfig;
import org.sse.StorageFactory;
import org.sse.StorageFactory.StorageType;
import org.sse.geo.AttributeType;
import org.sse.geo.Feature;
import org.sse.geo.FeatureCollection;
import org.sse.geo.Schema;
import org.sse.service.IdxRouteStorage;
import org.sse.service.base.AStar;
import org.sse.service.base.LinkedNode;
import org.sse.service.base.RouterPreference;
import org.sse.util.EarthPos;
import org.sse.util.Google;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class AStarTest {

	public static void main(String[] args) throws Exception {
		NaviConfig.init();
		Thread.sleep(10000);

		String key = "110000";
		IdxRouteStorage stg = (IdxRouteStorage) StorageFactory.getInstance().getStorage(key, StorageType.NET);

		Schema s = new Schema();
		s.addAttribute("Id", AttributeType.INTEGER);
		FeatureCollection fc = new FeatureCollection(s);
		GeometryFactory gf = new GeometryFactory();

		int startNodeId = 83161;
		int endNodeId = 39516;
		AStar star = new AStar();
		float flag = 1.0f;// Fastest[0.04 0.06 0.12]
		long t = System.currentTimeMillis();
		LinkedNode rn = star.find(startNodeId, endNodeId, stg.getNodes(), stg.getEdges(), flag,
				RouterPreference.Shortest);
		System.out.println(System.currentTimeMillis() - t);
		List<Integer> nodeIds = new LinkedList<Integer>();
		if (rn != null) {
			while (rn.preNode != null) {
				Feature f = new Feature(s);
				f.setAttribute("Id", rn.id);
				EarthPos pos = Google.googToDegree(stg.getNodes().get(rn.id - 1).getX(), stg.getNodes().get(rn.id - 1)
						.getY());
				Coordinate coord = new Coordinate(pos.xLon, pos.yLat);
				f.setGeometry(gf.createPoint(coord));
				fc.add(f);

				nodeIds.add(rn.id);
				rn = rn.preNode;
			}
			nodeIds.add(startNodeId);

			Feature f = new Feature(s);
			f.setAttribute("Id", startNodeId);
			EarthPos pos = Google.googToDegree(stg.getNodes().get(startNodeId - 1).getX(),
					stg.getNodes().get(startNodeId - 1).getY());
			Coordinate coord = new Coordinate(pos.xLon, pos.yLat);
			f.setGeometry(gf.createPoint(coord));
			fc.add(f);
		}
		System.out.println(nodeIds);

	}

}
