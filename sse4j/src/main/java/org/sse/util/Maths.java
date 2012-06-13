package org.sse.util;

import java.util.ArrayList;
import java.util.List;

import org.sse.geo.IndexPoint;
import org.sse.geo.Point;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public strictfp class Maths {

	public static void main(String[] arg) {
		System.out.println(Math.PI * 6378137);
		List<Point> points = new ArrayList<Point>();
		points.add(new Point(2, 4));
		points.add(new Point(1, 2));
		points.add(new Point(0, 0));
		// System.out.println(getAngle(points, 1));
		IndexPoint ip = getVerticalIndex(points, new Point(1, 1));
		System.out.println(ip.idx + "  " + ip.pt);
	}

	public static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	public static int getLength(List<Point> pts) {
		if (pts == null || pts.size() < 2)
			return 0;
		double dis = 0;
		for (int i = 0; i < pts.size() - 1; i++) {
			dis += getDistance(pts.get(i).x, pts.get(i).y, pts.get(i + 1).x,
					pts.get(i + 1).y);
		}
		return (int) dis;
	}

	public static Point getVertical(Point a, Point b, Point c) {
		float abx = b.x - a.x;
		float aby = b.y - a.y;
		float acx = c.x - a.x;
		float acy = c.y - a.y;
		float f = (abx * acx + aby * acy) / (abx * abx + aby * aby);
		float x = a.x + f * abx;
		float y = a.y + f * aby;
		return new Point((int) x, (int) y);
	}

	public static IndexPoint getVerticalIndex(List<Point> coords, Point point) {
		Point minP = new Point(0, 0);
		int minDis = Integer.MAX_VALUE;
		int index = 0;
		for (int j = 0; j < coords.size() - 1; j++) {
			int dx = coords.get(j).x - point.x;
			int dy = coords.get(j).y - point.y;
			int aa = dx * dx + dy * dy;

			dx = coords.get(j + 1).x - point.x;
			dy = coords.get(j + 1).y - point.y;
			int bb = dx * dx + dy * dy;

			dx = coords.get(j + 1).x - coords.get(j).x;
			dy = coords.get(j + 1).y - coords.get(j).y;
			int cc = dx * dx + dy * dy;

			int distance;
			Point minPoint;
			int idx;
			if (Math.abs(aa - bb) > cc) {
				if (aa > bb) {
					distance = bb;
					minPoint = coords.get(j + 1);
					idx = j + 1;
				} else {
					distance = aa;
					minPoint = coords.get(j);
					idx = j;
				}
			} else {
				minPoint = getVertical(coords.get(j), coords.get(j + 1), point);
				distance = (point.x - minPoint.x) * (point.x - minPoint.x)
						+ (point.y - minPoint.y) * (point.y - minPoint.y);
				idx = j;
			}

			if (minDis > distance) {
				minDis = distance;
				minP = minPoint;
				index = idx;
			}
		}
		return new IndexPoint(index, minP);
	}

	public static int getAngle(List<Point> vertexes, int iType) {
		if (vertexes.size() < 2)
			return 0;

		double Threashold = 25;
		Point dp1 = new Point(0, 0), dp2 = new Point(0, 0);
		if (iType == 1) {
			dp1.x = vertexes.get(0).x;
			dp1.y = vertexes.get(0).y;

			int index = 1;
			while (index < vertexes.size()) {
				dp2.x = vertexes.get(index).x;
				dp2.y = vertexes.get(index).y;
				double d = getDistance(dp1.x, dp1.y, dp2.x, dp2.y);
				if (d > Threashold)
					break;
				index++;
			}

		} else if (iType == 2) {
			dp2.x = vertexes.get(vertexes.size() - 1).x;
			dp2.y = vertexes.get(vertexes.size() - 1).y;

			int index = vertexes.size() - 2;
			while (index >= 0) {
				dp1.x = vertexes.get(index).x;
				dp1.y = vertexes.get(index).y;
				double d = getDistance(dp1.x, dp1.y, dp2.x, dp2.y);
				if (d > Threashold)
					break;
				index--;
			}
		}
		double flag = (double) (dp2.y - dp1.y) / (double) (dp2.x - dp1.x);
		double angle = Math.atan(flag) * 180 / Math.PI;
		if (dp2.x < dp1.x)
			angle += 180;
		if (angle < 0)
			angle += 360;
		return (int) angle;
	}

	public static int getCost(int len, int rc, int ra, int lf) {
		return (int) (len * 3.6 / initSpeed(rc, ra) + lf * 10);
	}

	public static int getCost(int len, double sp, int lf) {
		return (int) (len * 3.6 / sp + lf * 10);
	}

	public static int initSpeed(int rc, int ra) {
		int k = 10;
		if (rc == 1) {// 高速、快速 90 - 80
			if (ra == 1)// 双
				k = 90;
			else if (ra == 2)// 单
				k = 80;
			else
				k = 70;
		} else if (rc == 2) {// 国道 70 - 60
			if (ra == 1)
				k = 70;
			else
				k = 60;
		} else if (rc == 3) {// 省道 60 - 50
			if (ra == 1)// 双
				k = 60;
			else
				k = 50;
		} else if (rc == 4) {// 主干道 50 - 40
			if (ra == 1)
				k = 50;
			else
				k = 40;
		} else if (rc == 5) {// 次干道 40 - 30
			if (ra == 1)
				k = 40;
			else
				k = 30;
		} else if (rc == 6) {// 一般道路 25 - 15
			if (ra == 1)
				k = 25;
			else
				k = 15;
		}
		return k;
	}

}
