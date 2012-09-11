package org.sse.test.geo;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.gml2.GMLReader;
import com.vividsolutions.jts.io.gml2.GMLWriter;

public class GMLTest {

	public static void main(String[] args) throws Exception {
		String gml = "<gml:Point><gml:coordinates>116.130635,39.934696 </gml:coordinates></gml:Point>";
		gml = "<gml:LineString><gml:coordinates>115.695034,39.971414 115.695287,39.971515 115.695957,39.971888 115.69746,39.97268 </gml:coordinates></gml:LineString>";

		GMLReader reader = new GMLReader();
		Geometry g = reader.read(gml, new GeometryFactory());
		System.out.println(g);
		GMLWriter writer = new GMLWriter();
		System.out.println(writer.write(g));

		Coordinate[] coords = new Coordinate[8];
		coords[0] = new Coordinate(1, 3);
		coords[1] = new Coordinate(2, 3);
		coords[2] = new Coordinate(3, 3);
		coords[3] = new Coordinate(3, 2);
		coords[4] = new Coordinate(3, 1);
		coords[5] = new Coordinate(4, 1);
		coords[6] = new Coordinate(5, 1);
		coords[7] = new Coordinate(6, 1);
		List<Coordinate> lcoords = new ArrayList<Coordinate>();
		lcoords.add(coords[0]);
		double range = (coords[1].y - coords[0].y) / (coords[1].x - coords[0].x);
		for (int j = 1; j < coords.length - 1; j++) {
			double cur = (coords[j + 1].y - coords[j].y) / (coords[j + 1].x - coords[j].x);
			if (Math.abs(cur - range) > 0.01) {
				lcoords.add(coords[j]);
			} else {
				System.out.println(range + " --- " + cur);
			}
			range = cur;
		}
		lcoords.add(coords[coords.length - 1]);
		System.out.println(lcoords);

		System.out.println(3.0 / 0.0);

		System.out.println(Integer.toHexString(257));
	}

}
