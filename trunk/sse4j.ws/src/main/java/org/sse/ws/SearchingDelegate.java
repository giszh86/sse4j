package org.sse.ws;

import org.sse.ws.base.WSFilter;
import org.sse.ws.base.WSFilterPoi;
import org.sse.ws.base.WSResult;

@javax.jws.WebService(targetNamespace = "http://ws.sse.org/", serviceName = "SearchingService", portName = "SearchingPort", wsdlLocation = "WEB-INF/wsdl/SearchingService.wsdl")
public class SearchingDelegate {

	org.sse.ws.Searching searching = new org.sse.ws.Searching();

	public WSResult search(WSFilter wsFilter) {
		return searching.search(wsFilter);
	}

	public WSResult poiInfo(WSFilterPoi poi) {
		return searching.poiInfo(poi);
	}

}