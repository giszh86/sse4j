package org.navi.tool;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.navi.geo.AttributeType;
import org.navi.geo.Feature;
import org.navi.geo.FeatureCollection;
import org.navi.geo.Schema;
import org.navi.idx.IdxWriter;
import org.navi.idx.base.Enums.AnalyzerType;
import org.navi.io.shp.ShapefileReader;
import org.navi.squery.PtyName;
import org.navi.util.MercatorUtil;

import com.vividsolutions.jts.geom.Point;

public class SHP2IDX {

	public static void main(String[] args) throws Exception {
		if (args == null || args.length < 2) {
			System.out.println("IdxBuilder [input path] [output path]");
			System.out.println("eg: IdxBuilder data/Beijing.shp data/idx/Beijing");
			return;
		}

		Properties dp1 = new Properties();
		dp1.setProperty("File", args[0]);
		FeatureCollection fc = new ShapefileReader().read(dp1);

		Schema schema = fc.getSchema();
		schema.addAttribute(PtyName.CENX, AttributeType.INTEGER);
		schema.addAttribute(PtyName.CENY, AttributeType.INTEGER);
		int count = schema.getAttributeCount();
		for (Iterator<Feature> it = fc.iterator(); it.hasNext();) {
			Feature f = it.next();
			Object[] attribs = Arrays.copyOf(f.getAttributes(), count);
			MercatorUtil.toMercator(f.getGeometry(), true);
			Point p = f.getGeometry().getCentroid();
			attribs[count - 2] = (int) Math.round(p.getX());
			attribs[count - 1] = (int) Math.round(p.getY());
			f.setAttributes(attribs);
		}

		IdxWriter iw = new IdxWriter(args[1], AnalyzerType.CN);
		List<String> fields = new LinkedList<String>();
		fields.add("ADDRESS");
		fields.add("NAMEC");
		fields.add("NAMEP");
		iw.create(fc, fields);
		iw.close();
	}
}
