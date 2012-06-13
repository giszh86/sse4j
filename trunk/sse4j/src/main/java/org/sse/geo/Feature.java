package org.sse.geo;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Feature {
	private Object[] attributes;
	private Geometry geometry;
	private Schema schema;

	public Feature(Schema schema) {
		this.schema = schema;
		attributes = new Object[schema.getAttributeCount()];
	}

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public void setAttribute(String attributeName, Object newAttribute) {
		setAttribute(schema.getAttributeIndex(attributeName), newAttribute);
	}

	public void setAttribute(int attributeIndex, Object newAttribute) {
		attributes[attributeIndex] = newAttribute;
	}

	public Object getAttribute(String attributeName) {
		return getAttribute(schema.getAttributeIndex(attributeName));
	}

	public Object getAttribute(int attributeIndex) {
		return attributes[attributeIndex];
	}

	public Object[] getAttributes() {
		return attributes;
	}

	public String getString(int attributeIndex) {
		Object result = getAttribute(attributeIndex);
		if (result != null)
			return result.toString();
		else
			return "";
	}

	public String getString(String attributeName) {
		Object result = getAttribute(attributeName);
		if (result != null)
			return result.toString();
		else
			return "";
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

}
