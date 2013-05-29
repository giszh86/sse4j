package org.sse.ws;

import org.sse.service.gc.Geocoder;
import org.sse.service.gc.Matcher;
import org.sse.service.gc.RGeocoder;
import org.sse.ws.base.WSBuilder;
import org.sse.ws.base.WSFilterGeoc;
import org.sse.ws.base.WSPointF;
import org.sse.ws.base.WSResult;

import com.google.gson.Gson;

/**
 * geocoding depends on province district
 * 
 * @author dux(duxionggis@126.com)
 */
public class Locating {
	/**
	 * 5 ms / 1 request
	 * 
	 * @param geoc
	 * @return
	 */
	public WSResult geocoding(WSFilterGeoc geoc) {
		WSResult result = new WSResult();
		try {
			result.setResultCode(1);
			result.setJsonString(new Gson().toJson(new Geocoder().geocoding(geoc.getKey(), geoc.getAddress())));
		} catch (Exception e) {
			result.setResultCode(0);
			result.setFaultString(e.getMessage());
		}
		return result;
	}

	/**
	 * 10-20 ms / 1 request
	 * 
	 * @param point
	 *            [WGS84]
	 * @return
	 */
	public WSResult reverseGeocoding(WSPointF point) {
		WSResult result = new WSResult();
		try {
			String key = new Matcher().districtMatch(WSBuilder.toPt(point)).getProvinceCode();
			result.setResultCode(1);
			result.setJsonString("{\"geoc\":\"" + new RGeocoder().reverseGeocoding(key, WSBuilder.toPt(point)) + "\"}");
		} catch (Exception e) {
			result.setResultCode(0);
			result.setFaultString(e.getMessage());
		}
		return result;
	}
}
