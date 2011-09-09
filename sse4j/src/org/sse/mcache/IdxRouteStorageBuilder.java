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
		String jpath = map.get("junction-path");
		boolean jcache = map.get("junction-cache").equalsIgnoreCase("true");
		String ppath = map.get("pathline-path");
		boolean pcache = map.get("pathline-cache").equalsIgnoreCase("true");
		Net net = new IdxNetCacher().create(jpath, jcache, ppath, pcache);
		System.out.println("R:" + ((new Date()).getTime() - date1.getTime()));

		return new IdxRouteStorage(jpath, ppath, net);
	}

}
