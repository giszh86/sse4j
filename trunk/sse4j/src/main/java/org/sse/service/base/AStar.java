package org.sse.service.base;

import java.util.List;
import org.sse.util.Maths;

/**
 * @author dux(duxionggis@126.com)
 */
public class AStar {

	/**
	 * plan shortest or fastest path by RouterPreference
	 * 
	 * @param startNodeId
	 *            Start Node Id
	 * @param endNodeId
	 *            End Node Id
	 * @param nodes
	 * @param edges
	 * @param coef
	 *            Shortest={1.0 1.4} or Fastest={0.04 0.06 0.12}
	 * @param pf
	 *            Shortest or Fastest or Cheapest
	 * @return LinkedNode
	 */
	public LinkedNode find(int startNodeId, int endNodeId, List<Node> nodes, List<Edge> edges, float coef,
			RouterPreference pf) {
		LinkedNode result = null;
		AStarTable open = new AStarArrayTable();		
		LinkedNode[] close = new LinkedNode[nodes.size()];

		LinkedNode start = new LinkedNode();
		start.id = startNodeId;
		start.fn = hn(nodes.get(start.id - 1), nodes.get(endNodeId - 1), coef);
		open.put(start);

		while (!open.isEmpty()) {
			LinkedNode cur = open.pollFirst();
			if (cur.id == endNodeId) {
				result = cur;
				break;
			}

			int[] eids = nodes.get(cur.id - 1).getEdgeIds();
			for (int id : eids) {
				LinkedNode next = getNextNode(cur, edges.get(id - 1), pf);
				if (next == null)
					continue;
				next.fn = next.gn + hn(nodes.get(next.id - 1), nodes.get(endNodeId - 1), coef);

				LinkedNode tn = open.get(next);
				if (tn == null && close[next.id - 1] == null) {
					open.put(next);
				} else if (tn != null) { // in open
					if (next.fn < tn.fn) {
						open.remove(tn);
						open.put(next);
					}
				} else { // in close
					if (next.fn < close[next.id - 1].fn) {
						close[next.id - 1] = null;
						open.put(next);
					}
				}
			}
			close[cur.id - 1] = cur;

			while (open.size() > open.defaultSize()) {// limit open size
				open.pollLast();
			}
		}
		open = null;
		close = null;
		return result;
	}

	private float hn(Node from, Node to, float coef) {
		return (float) (Maths.getDistance(from.getX(), from.getY(), to.getX(), to.getY()) * coef);
	}

	private LinkedNode getNextNode(LinkedNode cur, Edge edge, RouterPreference pf) {
		LinkedNode next = null;
		if (cur.id == edge.getStartNodeId()) {
			if (edge.getDirection() != 3) {
				next = new LinkedNode();
				next.id = edge.getEndNodeId();
				next.gn = edge.getGn(pf, false) + cur.gn;
				next.preEdgeId = edge.getId();
				next.preNode = cur;
			}
		} else if (cur.id == edge.getEndNodeId()) {
			if (edge.getDirection() != 2) {
				next = new LinkedNode();
				next.id = edge.getStartNodeId();
				next.gn = edge.getGn(pf, false) + cur.gn;
				next.preEdgeId = edge.getId();
				next.preNode = cur;
			}
		}
		return next;
	}

}
