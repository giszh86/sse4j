package org.sse;

import org.sse.service.IPoiService;
import org.sse.service.IRouteService;
import org.sse.service.PoiService;
import org.sse.service.SimpleRouteService;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class ServiceFactory {
	public static IPoiService getPoiService() {
		return new PoiService();
	}

	public static IRouteService getRouteService() {
		return new SimpleRouteService();
	}

}
