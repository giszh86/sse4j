package org.sse.ws;

import org.sse.ws.base.WSResult;
import org.sse.ws.base.WSRouter;

@javax.jws.WebService(targetNamespace = "http://ws.sse.org/", serviceName = "RoutingService", portName = "RoutingPort", wsdlLocation = "WEB-INF/wsdl/RoutingService.wsdl")
public class RoutingDelegate {

	org.sse.ws.Routing routing = new org.sse.ws.Routing();

	public WSResult plan(WSRouter wsRouter) {
		return routing.plan(wsRouter);
	}

	public WSResult webPlan(WSRouter wsRouter) {
		return routing.webPlan(wsRouter);
	}

}