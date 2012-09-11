package org.sse.service;

import org.sse.service.base.RouteDataSet;
import org.sse.service.base.Router;

/**
 * @author dux(duxionggis@126.com)
 */
public interface IRouteService {
	/**
	 * compute shortest or fastest path
	 * 
	 * @param router
	 * @param key
	 *            from xml config(route key in navi.xml)
	 * @return
	 * @throws Exception
	 */
	public RouteDataSet plan(Router router, String key) throws Exception;

	/**
	 * @param buffer
	 *            unit(meter)
	 */
	public void setBuffer(double buffer);
}
