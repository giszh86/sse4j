package org.sse.map;

import java.io.Serializable;
import java.util.ArrayList;
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
		private List<TipPoiBase> subs = new ArrayList<TipPoiBase>();

		private static transient int limitCount = 25;
		private static transient int width = 4;
		private static transient int height = 4;
		public static void setLimitCount(int count){
			limitCount = count;
		}
		public static void setBufferSize(int w,int h){
			width = w;
			height = h;
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

		public List<TipPoiBase> getSubs() {
			return subs;
		}

		public void setSubs(List<TipPoiBase> subs) {
			this.subs = subs;
		}

		public void addSub(TipPoiBase sub) {
			if (this.subs.size() <= limitCount)
				this.subs.add(sub);
		}

		public boolean equals(Object pt) {
			if (pt instanceof TipPoi)
				return (Math.abs(this.getX() - ((TipPoi) pt).getX()) <= width && Math
						.abs(this.getY() - ((TipPoi) pt).getY()) <= height);
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
