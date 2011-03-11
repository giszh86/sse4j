package org.sse.test.squery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.document.Document;
import org.sse.squery.Filter;
import org.sse.squery.Property;
import org.sse.squery.Searcher;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class SearcherTest {

	public static void main(String[] args) {
		Filter filter = new Filter();
		GeometryFactory gf = new GeometryFactory();
		Geometry g = gf.createPoint(new Coordinate(116.4, 39.9));
		// filter.setGeometry(g);
		List<Property> terms = new ArrayList<Property>();
		terms.add(new Property("KIND", "XSGY"));
		terms.add(new Property("NAMEC", "XSGY"));
		terms.add(new Property("NAMEP", "XSGY"));
		terms.add(new Property("ADDRESS", "XSGY"));
		filter.setProperties(terms);

		List<Document> docs;
		if (Searcher.getInstance().check(args[0], args[0], false)) {
			Date date1 = new Date();
			docs = Searcher.getInstance().search(args[0], filter);
			System.out.println("c:"
					+ ((new Date()).getTime() - date1.getTime()) + " s:"
					+ docs.size());
			for (Document doc : docs) {
				System.out.println(doc.get("ID") + doc.get("NAMEC") + "--"
						+ doc.get("ADDRESS") + "--" + doc.get("KIND"));
			}
		}

		Date date1 = new Date();
		Searcher.getInstance().boxQuery(args[0],
				g.buffer(0.01).getEnvelopeInternal());
		System.out.println("c:" + ((new Date()).getTime() - date1.getTime()));
	}

}
