package org.sse.service.base;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * BaseNode
 * 
 * @author dux(duxionggis@126.com)
 */
public class BaseNode implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int[] edgeIds;

	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            索引编号，1开始连续
	 */
	public void setId(int id) {
		this.id = id;
	}

	public int[] getEdgeIds() {
		return edgeIds;
	}

	/**
	 * @param edgeIds
	 *            关联道路编号
	 */
	public void setEdgeIds(int[] edgeIds) {
		this.edgeIds = edgeIds;
	}

	/**
	 * remove edgeId from edgeIds
	 * 
	 * @param edgeId
	 */
	public void removeEdgeId(Integer edgeId) {
		List<Integer> ids = new LinkedList<Integer>();
		for (int i : this.edgeIds) {
			ids.add(i);
		}
		if (ids.contains(edgeId)) {
			ids.remove(edgeId);
			this.edgeIds = new int[ids.size()];
			for (int i = 0; i < ids.size(); i++)
				this.edgeIds[i] = ids.get(i).intValue();
		}
		ids = null;
	}

	public int hashCode() {
		return this.id;
	}

	public BaseNode clone() {
		try {
			return (BaseNode) super.clone();
		} catch (CloneNotSupportedException e) {
			BaseNode n = new BaseNode();
			n.setEdgeIds(edgeIds);
			n.setId(id);
			return n;
		}
	}

	public LinkedNode toLinked() {
		LinkedNode to = new LinkedNode();
		to.id = this.id;
		return to;
	}

}
