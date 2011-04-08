package org.sse.ws;

import org.sse.geoc.Matcher;
import org.sse.ws.base.WSBuilder;
import org.sse.ws.base.WSFilterRM;
import org.sse.ws.base.WSPointF;
import org.sse.ws.base.WSResult;
import com.google.gson.Gson;

@javax.jws.WebService(targetNamespace = "http://ws.sse.org/", serviceName = "MatchingService", portName = "MatchingPort", wsdlLocation = "WEB-INF/wsdl/MatchingService.wsdl")
public class MatchingDelegate {

	org.sse.ws.Matching matching = new org.sse.ws.Matching();

	public WSResult districtMatch(WSPointF point) {
		return matching.districtMatch(point);
	}

	public WSResult roadMatch(WSFilterRM filter) {
		return matching.roadMatch(filter);
	}

}