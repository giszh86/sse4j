package org.sse.ws.base;

import java.io.Serializable;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class WSPointF implements Serializable {
	private static final long serialVersionUID = 1L;

	private float x;
	private float y;

	public WSPointF() {
		this(0f, 0f);
	}

	public WSPointF(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String toString() {
		return Math.round(x * 1.0e6) / 1.0e6 + "," + Math.round(y * 1.0e6)
				/ 1.0e6;
	}
}
