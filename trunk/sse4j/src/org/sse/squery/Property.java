package org.sse.squery;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Property {
	private String field;
	private String text;

	public Property() {
		this("", "");
	}

	public Property(String field, String text) {
		this.field = field;
		this.text = text;
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
}
