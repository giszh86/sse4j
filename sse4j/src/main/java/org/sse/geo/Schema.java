package org.sse.geo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dux(duxionggis@126.com)
 */
public class Schema {
	private List<String> attributeNames;
	private List<String> attributeTypes;

	public Schema() {
		attributeNames = new ArrayList<String>();
		attributeTypes = new ArrayList<String>();
	}

	public int getAttributeCount() {
		return attributeNames.size();
	}

	public int getAttributeIndex(String attributeName) {
		int index = attributeNames.indexOf(attributeName);
		if (index < 0)
			throw new IllegalArgumentException("Unrecognized attribute name: " + attributeName);
		return index;
	}

	public boolean hasAttribute(String attributeName) {
		return attributeNames.indexOf(attributeName) >= 0;
	}

	public String getAttributeName(int attributeIndex) {
		return attributeNames.get(attributeIndex);
	}

	public String getAttributeType(int attributeIndex) {
		return attributeTypes.get(attributeIndex);
	}

	public String getAttributeType(String attributeName) {
		return this.getAttributeType(this.getAttributeIndex(attributeName));
	}

	public void addAttribute(String attributeName, String attributeType) {
		attributeNames.add(attributeName);
		attributeTypes.add(attributeType);
	}
}
