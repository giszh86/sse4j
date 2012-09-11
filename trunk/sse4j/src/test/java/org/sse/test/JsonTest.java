package org.sse.test;

import org.sse.service.base.RouteDataSet;
import org.sse.util.URLUtil;

import com.google.gson.Gson;

public class JsonTest {

	public static void main(String[] args) {

		System.out.println(URLUtil.getClassPathFile(JsonTest.class));
		System.out.println(URLUtil.getClassFilePath(JsonTest.class));

		RouteDataSet dataset = new RouteDataSet();
		dataset.setDistance(67053);
		dataset.setCost(56);
		String r = new Gson().toJson(dataset);
		System.out.println(r);
		RouteDataSet result = new Gson().fromJson(r, RouteDataSet.class);
		System.out.println(result.getGuidances());
	}

}
