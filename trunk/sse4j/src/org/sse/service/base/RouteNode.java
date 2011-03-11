package org.sse.service.base;

/**
 * Route Node
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class RouteNode extends Node {
	private static final long serialVersionUID = 1L;
	private int nodeId; // 连接点编号
	private short type;// 连接点类型

	public int getNodeId() {
		return nodeId;
	}

	/**
	 * 
	 * @param nodeId
	 *            连接点编号
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

}
