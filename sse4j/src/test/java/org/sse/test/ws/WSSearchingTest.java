package org.sse.test.ws;

import org.sse.ws.Routing;
import org.sse.ws.Searching;
import org.sse.ws.base.WSFilter;
import org.sse.ws.base.WSResult;

import com.google.gson.Gson;

public class WSSearchingTest {

	public static void main(String[] args) throws InterruptedException {
		new Routing();
		Thread.sleep(10000);

		Gson json = new Gson();
		Result[] r = new Result[0];

		WSFilter wsFilter = new WSFilter();
		wsFilter.setKey("110000");
		wsFilter.setGeometryWKT("POINT (116.32f 39.97f)");
		wsFilter.setDistance(500);
		wsFilter.setKeyword("中关村");
		wsFilter.setPreference("NET");
		WSResult result = new Searching().search(wsFilter);
		System.out.println(result.getJsonString());
		r = json.fromJson(result.getJsonString(), r.getClass());
		System.out.println("size:" + r.length);

		wsFilter.setKeyword("大学");
		wsFilter.setPreference("POI");
		result = new Searching().search(wsFilter);
		System.out.println(result.getJsonString());
		r = json.fromJson(result.getJsonString(), r.getClass());
		System.out.println("size:" + r.length);

		wsFilter.setKey(" ");
		wsFilter.setKeyword(" ");
		wsFilter.setPreference("DIST");
		result = new Searching().search(wsFilter);
		System.out.println(result.getJsonString());
		r = json.fromJson(result.getJsonString(), r.getClass());
		System.out.println("size:" + r.length);
	}

	public static class Result {
		private String id;
		private String title;
		private String wkt;

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

		public String getWkt() {
			return wkt;
		}

		public void setWkt(String wkt) {
			this.wkt = wkt;
		}
	}
}
