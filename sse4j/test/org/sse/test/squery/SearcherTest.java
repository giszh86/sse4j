package org.sse.test.squery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.document.Document;
import org.sse.util.MercatorUtil;
import org.sse.squery.Filter;
import org.sse.squery.Property;
import org.sse.squery.Searcher;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class SearcherTest {

	public static void main(String[] args) {
		String path = "data/idx/110000/Poi";
		Filter filter = new Filter();
		GeometryFactory gf = new GeometryFactory();
		Geometry g = gf.createPoint(new Coordinate(116.4, 39.9)).buffer(0.01);
		MercatorUtil.toMercator(g, true);
		filter.setGeometry(g);
		List<Property> terms = new ArrayList<Property>();
		// terms.add(new Property("KIND", "BJ"));
		// terms.add(new Property("NAMEC", "BJ"));
		terms.add(new Property("NAMEP", "BJ"));
		// terms.add(new Property("ADDRESS", "BJ"));
		filter.setProperties(terms);

		List<Document> docs;
		if (Searcher.getInstance().check(path, path, false)) {
			Date date1 = new Date();
			docs = Searcher.getInstance().search(path, filter);
			System.out.println("c:"
					+ ((new Date()).getTime() - date1.getTime()) + " s:"
					+ docs.size());
			for (Document doc : docs) {
				System.out.println(doc.get("ID") + doc.get("NAMEC") + "--"
						+ doc.get("ADDRESS") + "--" + doc.get("KIND"));
			}
		}

		Date date1 = new Date();
		Searcher.getInstance().boxQuery(path, g.getEnvelopeInternal());
		System.out.println("c:" + ((new Date()).getTime() - date1.getTime()));
	}

}
