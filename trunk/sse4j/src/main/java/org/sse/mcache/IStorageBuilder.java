package org.sse.mcache;

import java.util.Map;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public interface IStorageBuilder {
	IStorage create(Map<String, String> map) throws Exception;
	// IStorage create(String key, Map<String, String> map) throws Exception;
}
