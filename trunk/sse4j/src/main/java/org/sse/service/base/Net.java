package org.sse.service.base;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Net implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Node> nodes;
	private List<Edge> edges;

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

}
