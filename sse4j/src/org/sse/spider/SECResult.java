package org.sse.spider;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class SECResult {
	private List<String> links = new LinkedList<String>();

	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	public void addLink(String link) {
		if (links == null)
			links = new LinkedList<String>();
		links.add(link);
	}
}
