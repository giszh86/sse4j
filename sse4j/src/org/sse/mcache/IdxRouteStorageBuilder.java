package org.sse.mcache;

import java.util.Date;
import java.util.Map;
import org.sse.service.IdxRouteStorage;
import org.sse.service.base.Net;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class IdxRouteStorageBuilder implements IStorageBuilder {

	@Override
	public IStorage create(Map<String, String> map) throws Exception {
		Date date1 = new Date();
		Net net = new IdxNetCacher().create(map.get("junction-path"), map
				.get("pathline-path"));
		System.out.println("R:" + ((new Date()).getTime() - date1.getTime()));

		return new IdxRouteStorage(map.get("junction-path"), map
				.get("pathline-path"), net);
	}

}
