package org.sse.map;

import java.io.Serializable;
import java.util.List;

class Tip {
	static class PoiTip implements Serializable {
		private static final long serialVersionUID = 1L;
		private String id;
		private String title;
		private int x;
		private int y;

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
	}

	static class TileTip implements Serializable {
		private static final long serialVersionUID = 1L;

		private int zoom;
		private int x;
		private int y;
		private List<PoiTip> tips;

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

		public List<PoiTip> getTips() {
			return tips;
		}

		public void setTips(List<PoiTip> tips) {
			this.tips = tips;
		}
	}
}
