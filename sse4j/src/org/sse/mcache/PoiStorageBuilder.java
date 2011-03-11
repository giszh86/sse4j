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
		Searcher.getInstance().check(map.get("item-path"),
				map.get("item-path"), NaviConfig.WGS);
		System.out.println("P:" + ((new Date()).getTime() - date1.getTime()));
		return new Storage(map.get("item-path"));
	}

}
