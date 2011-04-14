package org.sse.exe;

import java.util.LinkedList;

import com.google.gson.Gson;

class Tips {
	/**
	 * 
	 * @author dux(duxionggis@126.com)
	 * 
	 */
	static class BduTips {
		private int uid_num;
		private int err_no;
		private String tileid;
		private LinkedList<BduTip> uids;

		public int getUid_num() {
			return uid_num;
		}

		public void setUid_num(int uid_num) {
			this.uid_num = uid_num;
		}

		public int getErr_no() {
			return err_no;
		}

		public void setErr_no(int err_no) {
			this.err_no = err_no;
		}

		public String getTileid() {
			return tileid;
		}

		public void setTileid(String tileid) {
			this.tileid = tileid;
		}

		public LinkedList<BduTip> getUids() {
			return uids;
		}

		public void setUids(LinkedList<BduTip> uids) {
			this.uids = uids;
		}

	}

	/**
	 * 
	 * @author dux(duxionggis@126.com)
	 * 
	 */
	static class BduTip {
		private float x;
		private float y;
		private String uid;
		private String name;

		public float getX() {
			return x;
		}

		public void setX(float x) {
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String toString() {
			return new Gson().toJson(this);
		}

	}
}
