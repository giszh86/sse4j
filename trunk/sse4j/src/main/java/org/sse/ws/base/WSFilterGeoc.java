package org.sse.ws.base;

import java.io.Serializable;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class WSFilterGeoc implements Serializable {
	private static final long serialVersionUID = 1L;

	private String key;
	private String address;

	/**
	 * @return citycode(110000)
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            citycode(110000)
	 */
	public void setKey(String key) {
		this.key = key;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
