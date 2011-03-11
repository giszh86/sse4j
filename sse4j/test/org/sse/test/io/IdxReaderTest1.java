package org.sse.test.io;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.sse.io.IdxParser;
import org.sse.io.IdxReader;
import org.sse.io.Enums.AnalyzerType;
import org.sse.io.Enums.OccurType;
import org.sse.io.Enums.QueryType;
import org.sse.squery.Filter;
import org.sse.squery.Property;
import org.sse.squery.Searcher;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

@SuppressWarnings("unused")
public class IdxReaderTest1 {
	public static void main(String[] args) throws Exception {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 99; i++) {
			// sb.append("D:/data/index").append(",");
		}
		sb.append("D:/data/index");

		// Searcher.getInstance().check("test", sb.toString());
		//
		// Filter filter = new Filter();
		// GeometryFactory gf = new GeometryFactory();
		// Geometry g = gf.createPoint(new Coordinate(121.5, 31.3));
		// filter.setGeometry(g);
		// filter.setBuffer(0.1);
		// List<Property> pty = new ArrayList<Property>();
		// pty.add(new Property("NAMEC", "同济大学"));
		// filter.setProperties(pty);
		// filter.setCount(2000);
		//
		// Date date1 = new Date();
		// List<Document> docs = Searcher.getInstance().search("test", filter);
		// System.out.println("c:" + ((new Date()).getTime() - date1.getTime())
		// + " s:" + docs.size());
		// for (Document doc : docs) {
		// // System.out.println(doc.get("ID") + doc.get("NAMEC"));
		// }

		IdxReader idx = new IdxReader(sb.toString().split(","), false);
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Term("NAMEC", "同济大学"));
		Query query = IdxParser.getInstance().createQuery(QueryType.Standard,
				OccurType.Or,
				IdxParser.getInstance().getAnalyzer(AnalyzerType.SmartCN),
				terms);
		Date date2 = new Date();
		List<Document> docs2 = idx.query(query, null, 20000);
		System.out.println("c:" + ((new Date()).getTime() - date2.getTime())
				+ " s:" + docs2.size());
		for (Document doc : docs2) {
			// System.out.println(doc.get("NAMEC") + "--" +
			// doc.get("GEOMETRY"));
		}

		idx = new IdxReader(sb.toString().split(","), true);
		date2 = new Date();
		docs2 = idx.query(query, null, 20000);
		System.out.println("c:" + ((new Date()).getTime() - date2.getTime())
				+ " s:" + docs2.size());
	}

}
