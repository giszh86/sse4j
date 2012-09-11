package org.sse.geo;

import java.io.Serializable;

/**
 * @author dux(duxionggis@126.com)
 */
public class Point implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	public int x;
	public int y;

	public Point() {
		this(0, 0);
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point clone() {
		try {
			return (Point) super.clone();
		} catch (CloneNotSupportedException e) {
			return new Point(x, y);
		}
	}

	public boolean equals(Point pt) {
		return (Math.abs(this.x - pt.x) < 2 && Math.abs(this.y - pt.y) < 2);
	}

	public void minus(int x, int y) {
		this.x -= x;
		this.y -= y;
	}

	public void plus(int x, int y) {
		this.x += x;
		this.y += y;
	}

	public String toString() {
		return x + "," + y;
	}
}
