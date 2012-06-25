package org.sse.ws.base;

import java.io.Serializable;

import com.google.gson.Gson;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class WSFilter implements Serializable {
	private static final long serialVersionUID = 1L;

	private String key;
	private String keyword;
	private String geometryWKT;// [WGS84]
	private String preference = "POI";// POI, NET, DIST
	private int distance = -1;// meter
	private int count = 50;

	/**
	 * @return citycode(110000)
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            citycode(110000)
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 获取几何对象[WGS84]
	 * <p>
	 * POINT(0 0) LineString(0 0, 1 1, 2 2) POLYGON((0 0, 1 1, 2 2, 0 0))
	 * 
	 * @return WKT format geometry string
	 */
	public String getGeometryWKT() {
		return geometryWKT;
	}

	/**
	 * 设置几何对象[WGS84]
	 * 
	 * @param geometryWKT
	 *            WKT format geometry string[WGS84]
	 *            <p>
	 *            POINT(0 0) LineString(0 0, 1 1, 2 2) POLYGON((0 0, 1 1, 2 2, 0
	 *            0))
	 */
	public void setGeometryWKT(String geometryWKT) {
		this.geometryWKT = geometryWKT;
	}

	/**
	 * 
	 * @return POI, NET, DIST
	 */
	public String getPreference() {
		return preference;
	}

	/**
	 * 
	 * @param preference
	 *            POI, NET, DIST
	 */
	public void setPreference(String preference) {
		this.preference = preference;
	}

	public int getDistance() {
		return distance;
	}

	/**
	 * 设置距离
	 * 
	 * @param distance
	 *            unit = meter
	 */
	public void setDistance(int distance) {
		if (distance > 50000)
			distance = 50000;
		this.distance = distance;
	}

	public int getCount() {
		return count;
	}

	/**
	 * 设置返回记录数
	 * 
	 * @param count
	 *            默认50
	 */
	public void setCount(int count) {
		if (count < 1)
			count = 50;
		else if (count > 2500)
			count = 2500;
		this.count = count;
	}

	/**
	 * 
	 * 
	 * @return 查询关键字
	 *         <p>
	 *         NAMEC(香山公园) or NAMEP(XSGY) or ADDRESS(香山) or KIND(2010101)
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * 
	 * 
	 * @param fields
	 *            查询关键字
	 *            <p>
	 *            NAMEC(香山公园) or NAMEP(XSGY) or ADDRESS(香山) or KIND(2010101)
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}

}
