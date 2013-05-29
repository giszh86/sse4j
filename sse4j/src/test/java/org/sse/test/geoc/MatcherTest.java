package org.sse.test.geoc;

import java.util.Date;

import org.sse.NaviConfig;
import org.sse.service.gc.District;
import org.sse.service.gc.Matcher;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class MatcherTest {

	public static void main(String[] args) throws Exception {
		NaviConfig.init();
		Thread.sleep(15000);
		GeometryFactory gf = new GeometryFactory();
		Matcher matcher = new Matcher();
		Date date1 = new Date();
		District dis = matcher.districtMatch(gf.createPoint(new Coordinate(116.320271, 39.970319)));
		System.out.println("district match:" + ((new Date()).getTime() - date1.getTime()));
		System.out.println(dis.getCityCode() + "--" + dis.getProvinceCode() + "--" + dis.getProvince());
	}
}
