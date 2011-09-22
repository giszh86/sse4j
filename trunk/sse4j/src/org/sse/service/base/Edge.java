package org.sse.service.base;

import org.sse.util.Maths;

/**
 * Edge
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Edge extends BaseEdge {
	private static final long serialVersionUID = 1L;

	private byte kind;
	private byte type;
	private byte toll;
	private int length;

	public short getKind() {
		return kind;
	}

	/**
	 * 
	 * @param kind
	 *            道路等级
	 *            <p>
	 *            1:高速公路、快速路
	 *            <p>
	 *            2:国道
	 *            <p>
	 *            3:省道
	 *            <p>
	 *            4:城市主要道路
	 *            <p>
	 *            5:城市次要道路
	 *            <p>
	 *            6:城市一般道路
	 *            <p>
	 *            >7:细道路
	 *            <p>
	 */
	public void setKind(short kind) {
		this.kind = (byte) kind;
	}

	public short getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *            道路形态及特性
	 *            <p>
	 *            1：双线化道路
	 *            <p>
	 *            2：非双线化道路
	 *            <p>
	 *            3：辅路
	 *            <p>
	 *            4：环岛
	 *            <p>
	 *            5: 匝道(高速与低等级路之间的连接路)
	 *            <p>
	 *            6：匝道（主辅路间的连接路）
	 *            <p>
	 *            7：掉头专用道
	 *            <p>
	 *            8：左转专用道
	 *            <p>
	 *            9：右转专用道
	 *            <p>
	 *            10：路口内连接
	 *            <p>
	 *            20: 其他
	 */
	public void setType(short type) {
		this.type = (byte) type;
	}

	public short getToll() {
		return toll;
	}

	/**
	 * 
	 * @param toll
	 *            收费 0:不收 1:收
	 */
	public void setToll(short toll) {
		this.toll = (byte) toll;
	}

	public int getLength() {
		return length;
	}

	/**
	 * 
	 * @param length
	 *            路段长度[米]
	 */
	public void setLength(int length) {
		this.length = length;
		this.setGn(length);
	}

	public int getGn(RouterPreference pf, boolean isLightFlag) {
		if (pf == RouterPreference.Shortest) {
			return this.length;
		} else if (pf == RouterPreference.OnFoot) {
			if (this.kind == 1 || this.type == EdgeType.RD_IC)
				return this.length * 10;
			else
				return this.length;
		} else {
			int k = Maths.getCost(this.length, this.kind, this.type, 0);
			if (isLightFlag)
				k = k + 10;
			if (this.toll == 1 && pf == RouterPreference.Cheapest)
				k = k * 10;
			return k;
		}
	}

}
