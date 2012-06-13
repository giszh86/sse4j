package org.sse.service.base;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class LinkedNode {
	public int id;
	public int gn;
	public int fn;
	public int preEdgeId;
	public LinkedNode preNode;

	public int hashCode() {
		return id;
	}

	public boolean equals(Object obj) {
		if (obj instanceof LinkedNode) {
			return (hashCode() == ((LinkedNode) obj).hashCode());
		}
		return false;
	}
}