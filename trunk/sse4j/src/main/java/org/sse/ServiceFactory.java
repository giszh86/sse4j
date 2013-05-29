package org.sse;

import org.sse.service.ls.IPoiService;
import org.sse.service.ls.PoiService;
import org.sse.service.rp.IRouteService;
import org.sse.service.rp.SimpleRouteService;

/**
 * @author dux(duxionggis@126.com)
 */
public class ServiceFactory {
	public static IPoiService getPoiService() {
		return new PoiService();
	}

	public static IRouteService getRouteService() {
		return new SimpleRouteService();
	}

}
