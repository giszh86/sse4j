package org.sse.service.base;

/**
 * Traffic Control
 * 
 * @author dux(duxionggis@126.com)
 */
public class TrafficCtl {
	private int roadId = -1;
	private short forbidDirect = -1;
	private float speed = -1;

	public int getRoadId() {
		return roadId;
	}

	/**
	 * @param roadId
	 *            路段ID,与Edge ID相同
	 */
	public void setRoadId(int roadId) {
		this.roadId = roadId;
	}

	public short getForbidDirect() {
		return forbidDirect;
	}

	/**
	 * @param forbidDirect
	 *            禁行方向 0:双向 1:顺向 2:反向
	 */
	public void setForbidDirect(short forbidDirect) {
		this.forbidDirect = forbidDirect;
	}

	public float getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            路段车速[公里/小时]
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
