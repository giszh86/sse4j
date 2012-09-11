package org.sse.service.base;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.sse.util.Maths;

/**
 * @author dux(duxionggis@126.com)
 */
public class AStar {
	int opensize = 1024;

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
	 *            Shortest={1.0} or Fastest={0.04 0.06 0.12}
	 * @param pf
	 *            Shortest or Fastest or Cheapest
	 * @return LinkedNode
	 */
	public LinkedNode find(int startNodeId, int endNodeId, List<Node> nodes, List<Edge> edges, float coef,
			RouterPreference pf) {
		LinkedNode result = null;
		LinkedList<LinkedNode> open = new LinkedList<LinkedNode>();
		LinkedNode[] close = new LinkedNode[nodes.size()];

		LinkedNode start = new LinkedNode();
		start.id = startNodeId;
		start.fn = hn(nodes.get(start.id - 1), nodes.get(endNodeId - 1), coef);
		open.addFirst(start);

		while (!open.isEmpty()) {
			LinkedNode cur = open.pollFirst();
			if (cur.id == endNodeId) {
				result = cur;
				break;
			}

			for (int i = 0; i < nodes.get(cur.id - 1).getEdgeIds().length; i++) {
				LinkedNode next = getNextNode(cur, edges.get(nodes.get(cur.id - 1).getEdgeIds()[i] - 1), pf);
				if (next == null)
					continue;
				next.fn = next.gn + hn(nodes.get(next.id - 1), nodes.get(endNodeId - 1), coef);

				int idxo = open.indexOf(next);
				if (idxo == -1 && close[next.id - 1] == null) {
					insert(open, next);
				} else if (idxo != -1) { // in open
					if (next.fn < open.get(idxo).fn) {
						open.remove(idxo);
						insert(open, next);
					}
				} else { // in close
					if (next.fn < close[next.id - 1].fn) {
						close[next.id - 1] = null;
						insert(open, next);
					}
				}
			}
			close[cur.id - 1] = cur;

			while (open.size() > opensize) {// limit open size
				open.pollLast();
			}
		}
		open = null;
		close = null;
		return result;
	}

	int hn(Node from, Node to, float coef) {
		return (int) (Maths.getDistance(from.getX(), from.getY(), to.getX(), to.getY()) * coef);
	}

	LinkedNode getNextNode(LinkedNode cur, Edge edge, RouterPreference pf) {
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

	void insert(LinkedList<LinkedNode> open, LinkedNode cur) {
		int index = -1;
		if (open.size() > 0) {
			for (ListIterator<LinkedNode> i = open.listIterator(); i.hasNext();) {
				if (i.next().fn >= cur.fn) {
					index = i.nextIndex();
					break;
				}
			}
		}
		if (index == -1) {
			open.addLast(cur);
		} else {
			open.add(index, cur);
		}
	}

}
