package org.sse.test.geo;

import org.sse.geo.AttributeType;
import org.sse.geo.Feature;
import org.sse.geo.FeatureCollection;
import org.sse.geo.Schema;

public class FeatureCollectionTest {

	public static void main(String[] args) {
		Schema s = new Schema();
		s.addAttribute("Name", AttributeType.STRING);
		s.addAttribute("Turn", AttributeType.STRING);
		FeatureCollection fc = new FeatureCollection(s);
		Feature f = new Feature(s);
		f.setAttribute("Name", "aaaaaaaaaaaa");
		f.setAttribute("Turn", "bbbbbbbbbbbb");

		fc.add(f);
	}

}
