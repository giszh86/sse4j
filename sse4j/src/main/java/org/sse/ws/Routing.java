package org.sse.ws;

import org.sse.NaviConfig;
import org.sse.ServiceFactory;
import org.sse.geoc.District;
import org.sse.geoc.Matcher;
import org.sse.service.IRouteService;
import org.sse.ws.base.WSBuilder;
import org.sse.ws.base.WSResult;
import org.sse.ws.base.WSRouteDataSet;
import org.sse.ws.base.WSRouter;

/**
 * route plan depends on city district
 * 
 * @author dux(duxionggis@126.com)
 */
public class Routing {
	static {
		NaviConfig.init();
	}

	/**
	 * <300 ms / 1 request
	 * 
	 * @param wsRouter
	 * @return
	 */
	public WSResult plan(WSRouter wsRouter) {
		WSResult result = new WSResult();
		try {
			String key = NaviConfig.BASE_KEY;
			if (wsRouter.getKey() == null || wsRouter.getKey().isEmpty()) {
				District sd = new Matcher().districtMatch(WSBuilder.toPt(wsRouter.getStartPoint()));
				District ed = new Matcher().districtMatch(WSBuilder.toPt(wsRouter.getEndPoint()));
				if (sd.getProvinceCode().equals(ed.getProvinceCode())) {
					if (sd.getCityCode().equals(ed.getCityCode()))
						key = sd.getCityCode();
					else
						key = sd.getProvinceCode();
				}
			} else {
				key = wsRouter.getKey();
			}
			IRouteService rs = ServiceFactory.getRouteService();
			if (key.equals(NaviConfig.BASE_KEY))
				rs.setBuffer(5000);
			WSRouteDataSet ds = WSBuilder.build(rs.plan(WSBuilder.router(wsRouter), key), false);
			result.setJsonString(ds.toString());
			result.setResultCode(1);
		} catch (Exception e) {
			result.setResultCode(0);
			result.setFaultString(e.getMessage());
		}
		return result;
	}

	/**
	 * <300 ms / 1 request
	 * 
	 * @param wsRouter
	 * @return
	 */
	public WSResult webPlan(WSRouter wsRouter) {
		WSResult result = new WSResult();
		try {
			String key = NaviConfig.BASE_KEY;
			if (wsRouter.getKey() == null || wsRouter.getKey().isEmpty()) {
				District sd = new Matcher().districtMatch(WSBuilder.toPt(wsRouter.getStartPoint()));
				District ed = new Matcher().districtMatch(WSBuilder.toPt(wsRouter.getEndPoint()));
				if (sd.getProvinceCode().equals(ed.getProvinceCode())) {
					if (sd.getCityCode().equals(ed.getCityCode()))
						key = sd.getCityCode();
					else
						key = sd.getProvinceCode();
				}
			} else {
				key = wsRouter.getKey();
			}
			IRouteService rs = ServiceFactory.getRouteService();
			if (key.equals(NaviConfig.BASE_KEY))
				rs.setBuffer(5000);
			WSRouteDataSet ds = WSBuilder.build(rs.plan(WSBuilder.router(wsRouter), key), true);
			result.setJsonString(ds.toString());
			result.setResultCode(1);
		} catch (Exception e) {
			result.setResultCode(0);
			result.setFaultString(e.getMessage());
		}
		return result;
	}
}
