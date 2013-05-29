package org.sse.test.io;

import java.util.LinkedList;
import java.util.List;

import org.sse.idx.IdxWriter;
import org.sse.idx.base.Enums.AnalyzerType;
import org.sse.geo.AttributeType;
import org.sse.geo.Feature;
import org.sse.geo.FeatureCollection;
import org.sse.geo.Schema;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class IdxWriterTest {

	public static void main(String[] args) throws Exception {
		Schema s = new Schema();
		s.addAttribute("NAME", AttributeType.STRING);
		s.addAttribute("CENX", AttributeType.INTEGER);
		s.addAttribute("CENY", AttributeType.INTEGER);
		FeatureCollection fc = new FeatureCollection(s);
		GeometryFactory gf = new GeometryFactory();
		for (int i = 12954955; i < 12955955; i = i + 3) {
			for (int j = 4856545; j < 4856645; j = j + 2) {
				Feature f = new Feature(s);
				f.setAttribute("NAME", "parking");
				f.setAttribute("CENX", i);
				f.setAttribute("CENY", j);
				f.setGeometry(gf.createPoint(new Coordinate(i, j)));

				fc.add(f);
			}
		}

		String idxpath = "D:/temp/parking";
		IdxWriter iw = new IdxWriter(idxpath, AnalyzerType.CN);
		List<String> fields = new LinkedList<String>();
		fields.add("NAME");

		iw.create(fc, fields);

	}
}
