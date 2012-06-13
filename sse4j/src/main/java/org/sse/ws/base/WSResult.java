package org.sse.ws.base;

import java.io.Serializable;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class WSResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private int resultCode;
	private String faultString = "";
	private String jsonString = "";

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getFaultString() {
		return faultString;
	}

	public void setFaultString(String faultString) {
		this.faultString = faultString;
	}
}
