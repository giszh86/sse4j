package org.sse.mcache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.lucene.document.Document;
import org.sse.NaviConfig;
import org.sse.io.IdxReader;
import org.sse.service.base.Edge;
import org.sse.service.base.EdgePtyName;
import org.sse.service.base.Net;
import org.sse.service.base.Node;
import org.sse.service.base.NodePtyName;
import org.sse.squery.Searcher;
import org.sse.util.MercatorUtil;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class IdxNetCacher {

	private class EdgeComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			Edge e1 = (Edge) o1;
			Edge e2 = (Edge) o2;
			if (e1.getId() < e2.getId())
				return 0;
			else
				return 1;
		}
	}

	private class NodeComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			Node n1 = (Node) o1;
			Node n2 = (Node) o2;
			if (n1.getId() < n2.getId())
				return 0;
			else
				return 1;
		}
	}

	public Net create(String idxpath_node, String idxpath_edge)
			throws IOException {
		Envelope extent1 = null;
		IdxReader nodeReader = new IdxReader(idxpath_node);
		List<Node> nodes = new ArrayList<Node>(nodeReader.getReader().numDocs());
		SpatialIndex nodesIndex = new STRtree();

		// TODO Version=3.1 TermDocs Bug
		// TermDocs nodedocs = nodeReader.getReader().termDocs();
		// while (nodedocs.next()) {
		// Document doc = nodeReader.getReader().document(nodedocs.doc());
		// Node node = this.createNode(doc);
		// nodes.add(node);
		// Geometry g = MercatorUtil.toGeometry(doc.get(NodePtyName.GID),
		// NaviConfig.WGS);
		// node.setX((int) g.getCoordinate().x);
		// node.setY((int) g.getCoordinate().y);
		// nodesIndex.insert(g.getEnvelopeInternal(), node.getId());
		// if (extent1 == null)
		// extent1 = g.getEnvelopeInternal();
		// else
		// extent1.expandToInclude(g.getEnvelopeInternal());
		// }
		// nodedocs.close();
		for (int i = 0; i < nodeReader.getReader().numDocs(); i++) {
			Document doc = nodeReader.getReader().document(i);
			Node node = this.createNode(doc);
			nodes.add(node);
			Geometry g = MercatorUtil.toGeometry(doc.get(NodePtyName.GID),
					NaviConfig.WGS);
			node.setX((int) g.getCoordinate().x);
			node.setY((int) g.getCoordinate().y);
			nodesIndex.insert(g.getEnvelopeInternal(), node.getId());
			if (extent1 == null)
				extent1 = g.getEnvelopeInternal();
			else
				extent1.expandToInclude(g.getEnvelopeInternal());
		}

		Collections.sort(nodes, new NodeComparator());
		((STRtree) nodesIndex).build();
		Searcher.getInstance().put(idxpath_node, nodeReader, nodesIndex,
				extent1);

		/******************************************************************************/

		Envelope extent2 = null;
		IdxReader edgeReader = new IdxReader(idxpath_edge);
		List<Edge> edges = new ArrayList<Edge>(edgeReader.getReader().numDocs());
		SpatialIndex edgesIndex = new STRtree();

		// TODO Version=3.1 TermDocs Bug
		// TermDocs edgedocs = edgeReader.getReader().termDocs();
		// while (edgedocs.next()) {
		// Document doc = edgeReader.getReader().document(edgedocs.doc());
		// Geometry g = MercatorUtil.toGeometry(doc.get(EdgePtyName.GID),
		// NaviConfig.WGS);
		// Edge edge = this.createEdge(doc);
		// edge.setLength((int) g.getLength());
		// edges.add(edge);
		// edgesIndex.insert(g.getEnvelopeInternal(), edge.getId());
		// if (extent2 == null)
		// extent2 = g.getEnvelopeInternal();
		// else
		// extent2.expandToInclude(g.getEnvelopeInternal());
		// }
		// edgedocs.close();
		for (int i = 0; i < edgeReader.getReader().numDocs(); i++) {
			Document doc = edgeReader.getReader().document(i);
			Geometry g = MercatorUtil.toGeometry(doc.get(EdgePtyName.GID),
					NaviConfig.WGS);
			Edge edge = this.createEdge(doc);
			edge.setLength((int) g.getLength());
			edges.add(edge);
			edgesIndex.insert(g.getEnvelopeInternal(), edge.getId());
			if (extent2 == null)
				extent2 = g.getEnvelopeInternal();
			else
				extent2.expandToInclude(g.getEnvelopeInternal());
		}

		Collections.sort(edges, new EdgeComparator());
		((STRtree) edgesIndex).build();
		Searcher.getInstance().put(idxpath_edge, edgeReader, edgesIndex,
				extent2);

		Net net = new Net();
		net.setEdges(new CopyOnWriteArrayList<Edge>(edges));
		net.setNodes(new CopyOnWriteArrayList<Node>(nodes));
		return net;
	}

	private Edge createEdge(Document doc) {
		Edge edge = new Edge();
		edge.setId(Integer.valueOf(doc.get(EdgePtyName.OID)).intValue());
		edge.setStartNodeId(Integer.valueOf(doc.get(EdgePtyName.SNODEID))
				.intValue());
		edge.setEndNodeId(Integer.valueOf(doc.get(EdgePtyName.ENODEID))
				.intValue());
		edge.setKind(Integer.valueOf(doc.get(EdgePtyName.KIND)).shortValue());
		edge.setType(Integer.valueOf(doc.get(EdgePtyName.TYPE)).shortValue());
		edge.setDirection(Integer.valueOf(doc.get(EdgePtyName.DIRECTION))
				.shortValue());
		edge.setToll(Integer.valueOf(doc.get(EdgePtyName.TOLL)).shortValue());
		return edge;
	}

	private Node createNode(Document doc) {
		Node node = new Node();
		node.setId(Integer.valueOf(doc.get(NodePtyName.OID)).intValue());
		node.setLightFlag(Integer.valueOf(doc.get(NodePtyName.LIGHTFLAG))
				.shortValue());
		String[] ids = doc.get(NodePtyName.NODELINK).split("\\|");
		int[] edgeids = new int[ids.length];
		for (int i = 0; i < ids.length; i++)
			edgeids[i] = Integer.valueOf(ids[i]).intValue();
		node.setEdgeIds(edgeids);
		return node;
	}
}
