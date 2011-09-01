package org.sse.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.sse.NaviConfig;
import org.sse.mcache.IStorage;
import org.sse.service.base.Edge;
import org.sse.service.base.EdgePtyName;
import org.sse.service.base.Net;
import org.sse.service.base.Node;
import org.sse.service.base.NodePtyName;
import org.sse.service.base.RouteEdge;
import org.sse.service.base.RouteNode;
import org.sse.squery.Searcher;
import org.sse.util.MercatorUtil;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.distance.DistanceOp;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class IdxRouteStorage implements IStorage {
	private String key_edge;
	private String key_node;
	private Net net;

	public IdxRouteStorage(String key_node, String key_edge, Net net)
			throws Exception {
		this.key_edge = key_edge;
		this.key_node = key_node;
		this.net = net;
	}

	@Override
	public String getKey() {
		return key_edge;
	}

	public String getKey_edge() {
		return key_edge;
	}

	public String getKey_node() {
		return key_node;
	}

	public List<Node> getNodes() {
		return net.getNodes();
	}

	public List<Edge> getEdges() {
		return net.getEdges();
	}

	protected RouteNode nearestNode(Point pt, double buffer) {
		RouteNode node = null;
		if (pt == null)
			return node;
		List<Integer> result = this.queryNodes(pt.buffer(buffer)
				.getEnvelopeInternal());
		if (result == null || result.size() == 0)
			result = this.queryNodes(pt.buffer(buffer * 5)
					.getEnvelopeInternal());
		if (result == null || result.size() == 0)
			return node;
		double min = Double.MAX_VALUE;
		int minid = 0;
		for (int id : result) {
			double dis = DistanceOp.distance(pt, MercatorUtil.toJTSPoint(
					getNodes().get(id - 1).getX(), getNodes().get(id - 1)
							.getY()));
			if (dis < min) {
				min = dis;
				minid = id;
			}
		}
		node = new RouteNode();
		node.setId(minid);
		return node;
	}

	protected RouteNode createNode(Document doc) {
		RouteNode result = new RouteNode();
		result.setType(Integer.valueOf(doc.get(NodePtyName.CROSSFLAG))
				.shortValue());
		int id = Integer.valueOf(doc.get(NodePtyName.OID));
		Node node = net.getNodes().get(id - 1);
		result.setId(node.getId());
		result.setLightFlag(node.getLightFlag());
		result.setEdgeIds(node.getEdgeIds());
		result.setX(node.getX());
		result.setY(node.getY());
		return result;
	}

	protected RouteEdge createEdge(Document doc) {
		RouteEdge result = new RouteEdge();
		result.setName(doc.get(EdgePtyName.NAMEC));
		result.setPoints(MercatorUtil.toPoints(MercatorUtil.toGeometry(doc
				.get(EdgePtyName.GID), NaviConfig.WGS), false));
		int id = Integer.valueOf(doc.get(EdgePtyName.OID));
		Edge edge = net.getEdges().get(id - 1);
		result.setId(edge.getId());
		result.setStartNodeId(edge.getStartNodeId());
		result.setEndNodeId(edge.getEndNodeId());
		result.setKind(edge.getKind());
		result.setType(edge.getType());
		result.setDirection(edge.getDirection());
		result.setToll(edge.getToll());
		result.setLength(edge.getLength());
		return result;
	}

	protected RouteEdge getRouteEdge(int edgeid) {
		List<Term> terms = new ArrayList<Term>(1);
		terms.add(new Term(EdgePtyName.OID, edgeid + ""));
		List<Document> docs = Searcher.getInstance().search(this.key_edge,
				terms);
		return this.createEdge(docs.get(0));
	}

	protected Map<Integer, RouteEdge> getRouteEdges(List<Integer> roadIds) {
		List<Term> terms = new ArrayList<Term>(roadIds.size());
		for (Integer id : roadIds)
			terms.add(new Term(EdgePtyName.OID, id.toString()));
		List<Document> docs = Searcher.getInstance().search(this.key_edge,
				terms);
		Map<Integer, RouteEdge> map = new HashMap<Integer, RouteEdge>(roadIds
				.size());
		for (Document doc : docs) {
			RouteEdge edge = this.createEdge(doc);
			map.put(edge.getId(), edge);
		}
		terms = null;
		docs = null;
		return map;
	}

	public List<Integer> queryNodes(Envelope env) {
		return (List<Integer>) Searcher.getInstance().spatialFilter(
				this.key_node, env);
	}

	public List<Integer> queryEdges(Envelope env) {
		return (List<Integer>) Searcher.getInstance().spatialFilter(
				this.key_edge, env);
	}

}
