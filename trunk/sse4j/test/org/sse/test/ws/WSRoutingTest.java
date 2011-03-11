package org.sse.test.ws;

import java.util.Iterator;

import org.sse.geo.Feature;
import org.sse.geo.FeatureCollection;
import org.sse.ws.RGUID2FC;
import org.sse.ws.Routing;
import org.sse.ws.base.WSPointF;
import org.sse.ws.base.WSResult;
import org.sse.ws.base.WSRouter;

public class WSRoutingTest {

	public static void main(String[] args) throws Exception {
		new Routing();
		Thread.sleep(15000);
		WSRouter router = new WSRouter();
		router.setStartPoint(new WSPointF(116.40404f, 39.96872f));
		router.setEndPoint(new WSPointF(116.321613f, 39.8071f));

		router.setStartPoint(new WSPointF(117.160778f, 40.58228f));
		router.setEndPoint(new WSPointF(116.891613f, 39.8071f));
		WSPointF[] viaPoints = new WSPointF[1];
		viaPoints[0] = new WSPointF(116.2753165f, 39.9688405f);
		router.setViaPoints(viaPoints);

		// router.setStartPoint(new PointF(116.40404f, 39.96872f)); // beijing
		// router.setEndPoint(new PointF(121.481245f, 31.171978f)); // shanghai
		// PointF[] viaPoints = new PointF[1];
		// viaPoints[0] = new PointF(104.065473f, 30.658349f); // chengdu
		// router.setViaPoints(viaPoints);
		router.setPreference("Fastest");
		WSResult result = new Routing().webPlan(router);
		System.out.println(result.getJsonString());
		FeatureCollection fc = new RGUID2FC().to(result.getJsonString());
		for (Iterator<Feature> i = fc.iterator(); i.hasNext();) {
			Feature f = i.next();
			System.out.println(f.getAttribute(0) + "_" + f.getGeometry());
		}
	}

}
