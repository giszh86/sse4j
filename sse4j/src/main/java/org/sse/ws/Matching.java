package org.sse.ws;

import org.sse.geoc.Matcher;
import org.sse.ws.base.WSBuilder;
import org.sse.ws.base.WSFilterRM;
import org.sse.ws.base.WSPointF;
import org.sse.ws.base.WSResult;

import com.google.gson.Gson;

/**
 * road match depends on city district
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Matching {

	/**
	 * 10-20 ms / 1 request
	 * 
	 * @param point
	 *            [WGS84]
	 * @return
	 */
	public WSResult districtMatch(WSPointF point) {
		WSResult result = new WSResult();
		try {
			result.setResultCode(1);
			result.setJsonString(new Gson().toJson(new Matcher()
					.districtMatch(WSBuilder.toPt(point))));
		} catch (Exception e) {
			result.setResultCode(0);
			result.setFaultString(e.getMessage());
		}
		return result;
	}

	/**
	 * 10-20 ms / 1 request
	 * 
	 * @param filter
	 * @return
	 */
	public WSResult roadMatch(WSFilterRM filter) {
		WSResult result = new WSResult();
		try {
			result.setResultCode(1);
			result.setJsonString(new Matcher().roadMatch(WSBuilder.toPt(filter
					.getStartPoint()), WSBuilder.toPt(filter.getEndPoint())));
		} catch (Exception e) {
			result.setResultCode(0);
			result.setFaultString(e.getMessage());
		}
		return result;
	}

}
