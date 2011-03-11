package org.sse.service.base;

/**
 * Node
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Node extends BaseNode {
	private static final long serialVersionUID = 1L;

	private byte lightFlag;// 红绿灯标识
	private int x;
	private int y;

	public short getLightFlag() {
		return lightFlag;
	}

	/**
	 * 
	 * @param lightFlag
	 *            红绿灯标识 0:无 1:有
	 */
	public void setLightFlag(short lightFlag) {
		this.lightFlag = (byte) lightFlag;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
