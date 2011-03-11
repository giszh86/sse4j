package org.sse.ws;

import org.sse.ws.base.WSFilterGeoc;
import org.sse.ws.base.WSPointF;
import org.sse.ws.base.WSResult;

@javax.jws.WebService(targetNamespace = "http://ws.sse.org/", serviceName = "LocatingService", portName = "LocatingPort", wsdlLocation = "WEB-INF/wsdl/LocatingService.wsdl")
public class LocatingDelegate {

	org.sse.ws.Locating locating = new org.sse.ws.Locating();

	public WSResult geocoding(WSFilterGeoc geoc) {
		return locating.geocoding(geoc);
	}

	public WSResult reverseGeocoding(WSPointF point) {
		return locating.reverseGeocoding(point);
	}

}