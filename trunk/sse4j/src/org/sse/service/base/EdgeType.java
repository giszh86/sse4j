package org.sse.service.base;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class EdgeType {
	public static final int RD_DUAL = 1; // 双线化道路
	public static final int RD_SINGLE = 2; // 非双线化道路
	public static final int RD_SIDE = 3; // 辅路
	public static final int RD_ROUND = 4; // 环岛
	public static final int RD_IC = 5; // 匝道(高速与低等级路之间的连接路)
	public static final int RD_JCT = 6; // 匝道（主辅路间的连接路）
	public static final int RD_TURN = 7; // 掉头专用道
	public static final int RD_TURNL = 8; // 左转专用道
	public static final int RD_TURNR = 9; // 右转专用道
	public static final int RD_CONN = 10; // 路口内连接
	public static final int RD_OTHER = 20; // 其他
}
