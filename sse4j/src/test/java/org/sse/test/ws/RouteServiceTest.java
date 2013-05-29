package org.sse.test.ws;

import org.sse.NaviConfig;
import org.sse.ServiceFactory;
import org.sse.service.base.RouteDataSet;
import org.sse.service.base.RouteGuidance;
import org.sse.service.base.Router;
import org.sse.service.base.RouterPreference;
import org.sse.service.base.TrafficCtl;
import org.sse.service.rp.IRouteService;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class RouteServiceTest {
	public static void main(String[] arg) throws Exception {
		NaviConfig.init();
		Thread.sleep(15000);
		IRouteService rs = ServiceFactory.getRouteService();
		GeometryFactory gf = new GeometryFactory();
		Router router = new Router();
		router.setPreference(RouterPreference.Fastest);
		// router.setStartPoint(gf.createPoint(new Coordinate(116.662989,
		// 39.9719897)));
		// router.setEndPoint(gf
		// .createPoint(new Coordinate(116.271701, 39.969646)));

		router.setStartPoint(gf.createPoint(new Coordinate(117.160778, 40.58228)));
		router.setEndPoint(gf.createPoint(new Coordinate(116.891613, 39.8071)));
		router.getViaPoints().add(gf.createPoint(new Coordinate(116.2753165, 39.9688405)));
		TrafficCtl ctl = new TrafficCtl();
		ctl.setRoadId(70454);
		ctl.setSpeed(1f);
		router.getControls().add(ctl);
		ctl = new TrafficCtl();
		ctl.setRoadId(55201);
		ctl.setForbidDirect((short) 0);
		router.getControls().add(ctl);
		RouteDataSet result = rs.plan(router, "110000");
		for (int i = 0; i < result.getSegments().size(); i++) {
			if (i <= 1)
				System.out.println("start:" + i + result.getSegments().get(i).getIds());
			if (i >= result.getSegments().size() - 2)
				System.out.println("end:" + i + result.getSegments().get(i).getIds());

			System.out.println(result.getSegments().get(i).getPoints().get(0) + ", ");
			System.out.print(result.getSegments().get(i).getPoints()
					.get(result.getSegments().get(i).getPoints().size() - 1)
					+ ", ");
		}
		for (RouteGuidance i : result.getGuidances()) {
			System.out.println(i.getName() + "--行使" + i.getLength() + "米--" + i.getTurn() + "--用时" + i.getCost() + "秒"
					+ i.getPoints());
		}
		System.out.println(result.getDistance());

	}
}
