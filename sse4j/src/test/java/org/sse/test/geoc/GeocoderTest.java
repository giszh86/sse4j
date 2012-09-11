package org.sse.test.geoc;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.sse.NaviConfig;
import org.sse.geoc.Geocoder;
import org.sse.io.IdxParser;
import org.sse.io.Enums.AnalyzerType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class GeocoderTest {
	static ExecutorService es = Executors.newFixedThreadPool(64);

	public static void main(String[] args) throws Exception {
		NaviConfig.init();
		Thread.sleep(15000);
		GeometryFactory gf = new GeometryFactory();
		Coordinate c1 = new Coordinate(116.32, 39.97);
		String addr = "中关村大街20号";
		Date date1 = new Date();
		String addr1 = IdxParser.getInstance().tokenize(addr, AnalyzerType.SmartCN);
		System.out.println("tokenize:" + addr1 + ((new Date()).getTime() - date1.getTime()));

		Geocoder matcher = new Geocoder();
		date1 = new Date();
		System.out.println(matcher.geocoding("110000", addr));
		System.out.println("geocoding:" + ((new Date()).getTime() - date1.getTime()));

		date1 = new Date();
		System.out.println(matcher.reverseGeocoding("110000", gf.createPoint(c1)));
		System.out.println("reverse geocoding:" + ((new Date()).getTime() - date1.getTime()));

		// while (true) {
		// es.execute(new GeocoderRunner());
		// }
	}

	static class GeocoderRunner implements Runnable {
		public void run() {
			Geocoder matcher = new Geocoder();
			Date date1 = new Date();
			try {
				System.out.println(matcher.geocoding("110000", "中关村大街20号"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("geocoding:" + ((new Date()).getTime() - date1.getTime()));
		}
	}
}
