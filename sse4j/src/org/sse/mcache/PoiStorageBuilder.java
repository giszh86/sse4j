package org.sse.mcache;

import java.util.Date;
import java.util.Map;

import org.sse.NaviConfig;
import org.sse.squery.Searcher;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class PoiStorageBuilder implements IStorageBuilder {

	@Override
	public IStorage create(Map<String, String> map) throws Exception {
		Date date1 = new Date();
		String path = map.get("item-path");
		boolean cache = map.get("item-cache").equalsIgnoreCase("true");
		Searcher.getInstance().check(path, path, NaviConfig.WGS, cache);
		System.out.println("P:" + ((new Date()).getTime() - date1.getTime()));
		return new Storage(path);
	}

}
