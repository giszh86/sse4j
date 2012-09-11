package org.sse.ws.base;

import java.io.Serializable;

/**
 * @author dux(duxionggis@126.com)
 */
public class WSFilterRM implements Serializable {
	private static final long serialVersionUID = 1L;
	private WSPointF startPoint;// [WGS84]
	private WSPointF endPoint;// [WGS84]

	public WSPointF getStartPoint() {
		return startPoint;
	}

	/**
	 * @param startPoint
	 *            起点[WGS84]
	 */
	public void setStartPoint(WSPointF startPoint) {
		this.startPoint = startPoint;
	}

	public WSPointF getEndPoint() {
		return endPoint;
	}

	/**
	 * @param endPoint
	 *            终点[WGS84]
	 */
	public void setEndPoint(WSPointF endPoint) {
		this.endPoint = endPoint;
	}

}
