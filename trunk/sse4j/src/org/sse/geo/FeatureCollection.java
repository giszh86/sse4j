package org.sse.geo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class FeatureCollection {
	private Schema schema;
	List<Feature> features;

	public FeatureCollection(Schema schema) {
		this.schema = schema;
		features = new LinkedList<Feature>();
	}

	public Schema getSchema() {
		return schema;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public Iterator<Feature> iterator() {
		return features.iterator();
	}

	public int size() {
		return features.size();
	}

	public void add(Feature feature) {
		features.add(feature);
	}

	public Feature get(int index) {
		return features.get(index);
	}

	public void remove(int index) {
		features.remove(index);
	}

	public void remove(Feature feature) {
		features.remove(feature);
	}

	public void clear() {
		features.clear();
	}
}
