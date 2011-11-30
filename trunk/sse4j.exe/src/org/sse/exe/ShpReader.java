package org.sse.exe;

import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.*;

public class ShpReader {

	public static void main(String[] args) throws Exception {
		DriverProperties dp = new DriverProperties();
		dp.set("File", "d:/temp.shp");
		FeatureCollection fc = new ShapefileReader().read(dp);
		// TODO convert to org.sse.geo.FeatureCollection
		
	}

}
