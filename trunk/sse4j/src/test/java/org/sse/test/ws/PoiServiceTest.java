package org.sse.test.ws;

import java.util.ArrayList;
import java.util.List;

import org.sse.NaviConfig;
import org.sse.ServiceFactory;
import org.sse.io.Enums.OccurType;
import org.sse.service.IPoiService;
import org.sse.service.base.Poi;
import org.sse.squery.Filter;
import org.sse.squery.Property;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class PoiServiceTest {
	public static void main(String[] args) throws Exception {
		NaviConfig.init();
		Thread.sleep(15000);
		IPoiService ps = ServiceFactory.getPoiService();
		Filter filter = new Filter();
		GeometryFactory gf = new GeometryFactory();
		filter.setGeometry(gf.createPoint(new Coordinate(116.32, 39.97)).buffer(0.01));
		List<Property> terms = new ArrayList<Property>();
		terms.add(new Property("ADDRESS", "中关村大街50", OccurType.AND));
		terms.add(new Property("NAMEC", "当代", OccurType.AND));
		filter.setProperties(terms);
		// filter.setCount(5);
		List<Poi> pois = ps.search(filter, "110000");
		if (pois != null) {
			for (int i = 0; i < pois.size(); i++) {
				System.out.println(pois.get(i).getId() + "--" + pois.get(i).getName() + "--" + pois.get(i).getAddress()
						+ "--" + pois.get(i).getVertex());
			}
		}

	}
}
