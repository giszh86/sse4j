package org.sse.service.base;

import java.io.Serializable;

/**
 * BaseEdge
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class BaseEdge implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private int id;// 索引编号，1开始连续
	private int startNodeId;// 起始连接点编号
	private int endNodeId;// 终止连接点编号
	private byte direction;// 交通流方向
	private int gn;// 权重

	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            索引编号，1开始连续
	 */
	public void setId(int id) {
		this.id = id;
	}

	public int getStartNodeId() {
		return startNodeId;
	}

	public void setStartNodeId(int startNodeId) {
		this.startNodeId = startNodeId;
	}

	public int getEndNodeId() {
		return endNodeId;
	}

	public void setEndNodeId(int endNodeId) {
		this.endNodeId = endNodeId;
	}

	public short getDirection() {
		return direction;
	}

	/**
	 * 
	 * @param direction
	 *            交通流方向 1:默认 2:与矢量化同向 3:与矢量化反向
	 */
	public void setDirection(short direction) {
		this.direction = (byte) direction;
	}

	public int getGn() {
		return gn;
	}

	/**
	 * 
	 * 
	 * @param gn
	 */
	public void setGn(int gn) {
		this.gn = gn;
	}

	public int hashCode() {
		return this.id;
	}

	public BaseEdge clone() {
		try {
			return (BaseEdge) super.clone();
		} catch (CloneNotSupportedException e1) {
			BaseEdge e = new BaseEdge();
			e.setDirection(direction);
			e.setEndNodeId(endNodeId);
			e.setGn(gn);
			e.setId(id);
			e.setStartNodeId(startNodeId);
			return e;
		}
	}

}
