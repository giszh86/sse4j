package org.sse.service;

import java.util.*;

import org.sse.geo.Point;
import org.sse.service.base.EdgeType;
import org.sse.service.base.RouteDataSet;
import org.sse.service.base.RouteGuidance;
import org.sse.service.base.RouteSegment;
import org.sse.service.base.TrafficCtl;
import org.sse.util.Maths;

/**
 * @author dux(duxionggis@126.com)
 */
class RouteSegmentManager {
	static RouteDataSet createDataSet(List<RouteSegment> result, List<TrafficCtl> controls) {
		RouteDataSet dataset = new RouteDataSet();
		if (result == null || result.size() == 0)
			return dataset;
		Map<Integer, Float> ctls = new HashMap<Integer, Float>();
		if (controls != null) {
			for (Iterator<TrafficCtl> it = controls.iterator(); it.hasNext();) {
				TrafficCtl ctl = it.next();
				if (ctl.getSpeed() > 0)
					ctls.put(ctl.getRoadId(), ctl.getSpeed());
			}
		}

		int dis = 0;
		int cost = 0;
		int xmin = Integer.MAX_VALUE, ymin = Integer.MAX_VALUE, xmax = Integer.MIN_VALUE, ymax = Integer.MIN_VALUE;
		for (int i = 0; i < result.size(); i++) {
			RouteSegment seg = result.get(i);
			float speed = Maths.initSpeed(seg.getKind(), seg.getAttrib());
			if (controls != null) {
				float speeds = 0;
				for (int id : seg.getIds()) {
					if (ctls.containsKey(id))
						speeds += ctls.get(id);
					else
						speeds += speed;
				}
				speed = speeds / seg.getIds().size();// none weighted mean
			}
			seg.setSpeed((int) Math.round(speed));
			seg.setCost((int) Math.round(Maths.getCost(seg.getLength(), speed, seg.getLightFlag())));
			dis += seg.getLength();
			cost += seg.getCost();
			seg.setSAngle(Maths.getAngle(seg.getPoints(), 1));
			seg.setEAngle(Maths.getAngle(seg.getPoints(), 2));

			if (seg.getPoints().get(0).x < xmin)
				xmin = seg.getPoints().get(0).x;
			if (seg.getPoints().get(0).x > xmax)
				xmax = seg.getPoints().get(0).x;
			if (seg.getPoints().get(0).y < ymin)
				ymin = seg.getPoints().get(0).y;
			if (seg.getPoints().get(0).y > ymax)
				ymax = seg.getPoints().get(0).y;
			if (seg.getPoints().get(seg.getPoints().size() - 1).x < xmin)
				xmin = seg.getPoints().get(seg.getPoints().size() - 1).x;
			if (seg.getPoints().get(seg.getPoints().size() - 1).x > xmax)
				xmax = seg.getPoints().get(seg.getPoints().size() - 1).x;
			if (seg.getPoints().get(seg.getPoints().size() - 1).y < ymin)
				ymin = seg.getPoints().get(seg.getPoints().size() - 1).y;
			if (seg.getPoints().get(seg.getPoints().size() - 1).y > ymax)
				ymax = seg.getPoints().get(seg.getPoints().size() - 1).y;
		}
		dataset.setDistance(dis);
		dataset.setCost(cost);
		dataset.setMaxx(xmax);
		dataset.setMaxy(ymax);
		dataset.setMinx(xmin);
		dataset.setMiny(ymin);
		dataset.setSegments(result);
		dataset.setGuidances(RouteSegmentManager.compute(result));
		ctls.clear();
		ctls = null;
		return dataset;
	}

	static void setRouteName(RouteSegment rd) {
		if (rd.getName().isEmpty()) {
			if (rd.getAttrib() == EdgeType.RD_SIDE) {
				rd.setName("辅路");
			} else if (rd.getAttrib() == EdgeType.RD_ROUND) {
				rd.setName("环岛");
			} else if (rd.getAttrib() == EdgeType.RD_IC || rd.getAttrib() == EdgeType.RD_JCT) {
				rd.setName("匝道");
			} else if (rd.getAttrib() == EdgeType.RD_TURN) {
				rd.setName("掉头专用道");
			} else if (rd.getAttrib() == EdgeType.RD_TURNL) {
				rd.setName("左转专用道");
			} else if (rd.getAttrib() == EdgeType.RD_TURNR) {
				rd.setName("右转专用道");
			} else if (rd.getAttrib() == EdgeType.RD_CONN) {
				rd.setName("路口连接线");
			} else {
				rd.setName("无名路");
			}
		}
	}

	static boolean combine(RouteSegment rd1, RouteSegment rd2, boolean start) {
		if (rd1 == null)
			return false;
		if (!rd1.equalsIgnore(rd2))
			return false;
		double tmpdegree = Maths.getAngle(rd2.getPoints(), 1) - Maths.getAngle(rd1.getPoints(), 2);
		if (tmpdegree < 0)
			tmpdegree += 360;
		if (tmpdegree >= 165 && tmpdegree < 195) // turn round
			return false;
		if (!rd1.connectFlag(rd2, start))
			return false;

		if (start) {
			rd2.getPoints().remove(0);
			rd1.getPoints().addAll(rd2.getPoints());
			rd1.getIds().addAll(rd2.getIds());
			rd1.setLightFlag(rd2.getLightFlag());
		} else {
			rd2.getPoints().remove(rd2.getPoints().size() - 1);
			rd1.getPoints().addAll(0, rd2.getPoints());
			rd1.getIds().addAll(0, rd2.getIds());
		}
		if (rd1.getAttrib() == EdgeType.RD_CONN)// crossing link
			rd1.setAttrib(rd2.getAttrib());
		rd1.setLength(rd1.getLength() + rd2.getLength());
		if (rd1.getAttrib() == EdgeType.RD_ROUND) // circle
			rd1.setCircleNum(rd1.getCircleNum() + 1);
		return true;
	}

	/**
	 * @param traces
	 * @return
	 */
	static List<RouteGuidance> compute(List<RouteSegment> traces) {
		List<RouteGuidance> ris = new ArrayList<RouteGuidance>();
		if (traces == null || traces.size() == 0)
			return ris;

		RouteGuidance ri = new RouteGuidance();
		RouteSegment rd = traces.get(0);
		int index = 0;
		List<Point> temp = new LinkedList<Point>();
		if (index == traces.size() - 1) {
			ri.setName(rd.getName());
			ri.setLength(ri.getLength() + rd.getLength());
			ri.setCost(ri.getCost() + rd.getCost());
			ri.setRemark(toState(rd, ri));
			ri.setNextName("目的地");
			ri.setTurn("到达");
			ri.setIcon("DEST");
			temp.addAll(rd.getPoints());
			// ri.setVertexes(toPts(temp));
			ri.setPoints(toArrayPts(temp));
			ris.add(ri);
			return ris;
		}
		while (index < traces.size() - 1) {
			if (ri.getName().isEmpty())
				ri.setName(rd.getName());
			ri.setLength(ri.getLength() + rd.getLength());
			ri.setCost(ri.getCost() + rd.getCost());
			ri.setRemark(toState(rd, ri));
			if (temp.size() == 0)
				temp.addAll(rd.getPoints());
			else
				temp.addAll(rd.getPoints().subList(1, rd.getPoints().size()));

			if (rd.getAttrib() == EdgeType.RD_CONN && (index - 1 >= 0))
				rd = traces.get(index - 1);

			index++;
			RouteSegment rdNext = traces.get(index);
			if (rdNext.getAttrib() != EdgeType.RD_CONN) {// crossing link
				int tmpdegree = rdNext.getSAngle() - rd.getEAngle();
				if (tmpdegree < 0)
					tmpdegree += 360;
				if (!rdNext.getName().equalsIgnoreCase(rd.getName())) {
					ri.setNextName(rdNext.getName());
					if (rd.getAttrib() == EdgeType.RD_ROUND)
						ri.setTurn("从第" + (rd.getCircleNum() + 1) + "个路口");
					setTurn(ri, rdNext.getSAngle(), tmpdegree);
					// ri.setVertexes(toPts(temp));
					ri.setPoints(toArrayPts(temp));
					ris.add(ri);
					ri = new RouteGuidance();
				} else {
					if (tmpdegree >= 165 && tmpdegree < 195) {// turn round
						ri.setNextName(rdNext.getName());
						if (rd.getAttrib() == EdgeType.RD_ROUND)
							ri.setTurn("从第" + (rd.getCircleNum() + 1) + "个路口");
						setTurn(ri, rdNext.getSAngle(), tmpdegree);
						// ri.setVertexes(toPts(temp));
						ri.setPoints(toArrayPts(temp));
						ris.add(ri);
						ri = new RouteGuidance();
					}
				}
			}
			rd = rdNext;
			if (index == traces.size() - 1) {
				if (ri.getName().isEmpty())
					ri.setName(rd.getName());
				ri.setLength(ri.getLength() + rd.getLength());
				ri.setCost(ri.getCost() + rd.getCost());
				ri.setRemark(toState(rd, ri));
				ri.setNextName("目的地");
				ri.setTurn("到达");
				ri.setIcon("DEST");
				if (temp.size() == 0)
					temp.addAll(rd.getPoints());
				else
					temp.addAll(rd.getPoints().subList(1, rd.getPoints().size()));
				// ri.setVertexes(toPts(temp));
				ri.setPoints(toArrayPts(temp));
				ris.add(ri);
				break;
			}
		}
		return ris;
	}

	static String toPts(List<Point> temp) {
		String str = temp.toString().replaceAll(", ", ";");
		temp.clear();
		temp = null;
		return str.substring(1, str.length() - 1);
	}

	private static List<Point> toArrayPts(List<Point> temp) {
		List<Point> result = new ArrayList<Point>(temp.size());
		for (Iterator<Point> it = temp.iterator(); it.hasNext();)
			result.add(it.next().clone()); // Seg and Guid maybe minus or plus pt
		temp.clear();
		temp = null;
		return result;
	}

	private static String toState(RouteSegment seg, RouteGuidance guid) {
		String state;
		double speed = Maths.initSpeed(seg.getKind(), seg.getAttrib()) * 0.8;
		double curspeed = guid.getLength() * 3.6 / guid.getCost();
		if (curspeed >= speed) {
			state = "C";
		} else {
			if (curspeed >= 35)
				state = "C";// flow
			else if (curspeed < 15)
				state = "A";// congestion
			else
				state = "B";// slow
		}
		return state;
	}

	private static void setTurn(RouteGuidance ist, int nextSAngle, int tmpdegree) {
		if (tmpdegree >= 345 || tmpdegree < 15) {
			ist.setIcon("DIR");
			ist.setTurn(ist.getTurn() + "直行");
		} else if (tmpdegree >= 15 && tmpdegree < 45) {
			ist.setIcon("L045");
			ist.setTurn(ist.getTurn() + "左前转");
		} else if (tmpdegree >= 45 && tmpdegree < 135) {
			ist.setIcon("L090");
			ist.setTurn(ist.getTurn() + "左转");
		} else if (tmpdegree >= 135 && tmpdegree < 165) {
			ist.setIcon("L135");
			ist.setTurn(ist.getTurn() + "左后转");
		} else if (tmpdegree >= 165 && tmpdegree < 195) {
			ist.setIcon("TR");
			ist.setTurn(ist.getTurn() + "掉头");
		} else if (tmpdegree >= 195 && tmpdegree < 225) {
			ist.setIcon("R135");
			ist.setTurn(ist.getTurn() + "右后转");
		} else if (tmpdegree >= 225 && tmpdegree < 315) {
			ist.setIcon("R090");
			ist.setTurn(ist.getTurn() + "右转");
		} else if (tmpdegree >= 315 && tmpdegree < 345) {
			ist.setIcon("R045");
			ist.setTurn(ist.getTurn() + "右前转");
		}
	}
}
