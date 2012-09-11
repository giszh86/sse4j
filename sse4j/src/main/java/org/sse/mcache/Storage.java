package org.sse.mcache;

/**
 * @author dux(duxionggis@126.com)
 */
public class Storage implements IStorage {
	private String key;

	public Storage(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

}
