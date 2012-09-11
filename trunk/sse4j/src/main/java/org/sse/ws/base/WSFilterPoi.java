package org.sse.ws.base;

import java.io.Serializable;

import com.google.gson.Gson;

/**
 * @author dux(duxionggis@126.com)
 */
public class WSFilterPoi implements Serializable {
	private static final long serialVersionUID = 1L;

	private String key;
	private String id;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String toString() {
		return new Gson().toJson(this);
	}

}
