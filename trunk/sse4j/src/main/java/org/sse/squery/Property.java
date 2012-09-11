package org.sse.squery;

import org.sse.io.Enums.OccurType;

/**
 * @author dux(duxionggis@126.com)
 */
public class Property {
	private String field;
	private String text;
	private OccurType otype = OccurType.Or;

	public Property() {
		this("", "");
	}

	public Property(String field, String text) {
		this.field = field;
		this.text = text;
	}

	public Property(String field, String text, OccurType otype) {
		this.field = field;
		this.text = text;
		this.otype = otype;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public OccurType getOtype() {
		return otype;
	}

	public void setOtype(OccurType otype) {
		this.otype = otype;
	}
}
