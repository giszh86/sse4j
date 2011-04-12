package org.sse.map;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

class Tip {
	static class TipPoiBase implements Serializable, Cloneable {
		private static final long serialVersionUID = 1L;
		private String id;
		private String title;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public TipPoiBase clone() {
			try {
				return (TipPoiBase) super.clone();
			} catch (CloneNotSupportedException e1) {
				return null;
			}
		}
	}

	static class TipPoi extends TipPoiBase {
		private static final long serialVersionUID = 1L;
		private int x;
		private int y;
		private List<TipPoiBase> subs = new LinkedList<TipPoiBase>();

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

		public List<TipPoiBase> getSubs() {
			return subs;
		}

		public void setSubs(List<TipPoiBase> subs) {
			this.subs = subs;
		}

		public void addSub(TipPoiBase sub) {
			if (this.subs.size() <= 25) // TODO 25
				this.subs.add(sub);
		}

		public boolean equals(Object pt) {
			if (pt instanceof TipPoi) // TODO 4
				return (Math.abs(this.getX() - ((TipPoi) pt).getX()) <= 4 && Math
						.abs(this.getY() - ((TipPoi) pt).getY()) <= 4);
			return false;
		}
	}

	static class TipTile implements Serializable {
		private static final long serialVersionUID = 1L;

		private int zoom;
		private int x;
		private int y;
		private List<TipPoi> tips = new LinkedList<TipPoi>();

		public int getZoom() {
			return zoom;
		}

		public void setZoom(int zoom) {
			this.zoom = zoom;
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

		public List<TipPoi> getTips() {
			return tips;
		}

		public void setTips(List<TipPoi> tips) {
			this.tips = tips;
		}
	}
}
