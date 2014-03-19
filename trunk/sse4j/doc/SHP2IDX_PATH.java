package org.navi.tool;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.navi.geo.Feature;
import org.navi.geo.FeatureCollection;
import org.navi.idx.IdxWriter;
import org.navi.idx.base.Enums.AnalyzerType;
import org.navi.io.shp.ShapefileReader;
import org.navi.util.MercatorUtil;

public class SHP2IDX_PATH {

	public static void main(String[] args) throws Exception {
		if (args == null || args.length < 2) {
			System.out.println("IdxBuilder [input path] [output path]");
			System.out.println("eg: IdxBuilder data/Beijing.shp data/idx/Beijing");
			return;
		}

		Properties dp1 = new Properties();
		dp1.setProperty("File", args[0]);
		FeatureCollection fc = new ShapefileReader().read(dp1);
		for (Iterator<Feature> it = fc.iterator(); it.hasNext();) {
			Feature f = it.next();
			MercatorUtil.toMercator(f.getGeometry(), true);
			int fw = (Integer) f.getAttribute("FW");
			if (fw <= 4) {
			} else if (fw >= 7 && fw <= 10) {
				fw = 5;
			} else if (fw == 11) {
				fw = 6;
			} else if (fw == 12) {
				fw = 7;
			} else if (fw == 13) {
				fw = 8;
			} else if (fw == 14) {
				fw = 9;
			} else if (fw == 19) {
				fw = 10;
			} else {
				fw = 20;
			}
			f.setAttribute("FW", fw);
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
