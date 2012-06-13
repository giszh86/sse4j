package org.sse.test.map;

import java.io.File;

import org.sse.NaviConfig;
import org.sse.map.HotMapper;
import org.sse.util.EarthPos;
import org.sse.util.Google;

public class HotMapperTest {

	public static void main(String[] args) throws Exception {
		NaviConfig.init();
		Thread.sleep(10000);

		HotMapper hm = HotMapper.getInstance();
		int zoom = 13;
		double lon = 116.4;
		double lat = 39.9;
		EarthPos pos = Google.degreeToPixel(lon, lat, zoom);
		int x = (int) (pos.xLon / Google.getSize()); // 6744
		int y = (int) (pos.yLat / Google.getSize()); // 3104
		String keyword = "北京宾馆";

		long t1 = System.currentTimeMillis();
		String path = hm.createHotmap(zoom, x, y, keyword, null);
		System.out.println("time:" + (System.currentTimeMillis() - t1));
		System.out.println(path);
		new File(".." + path + ".js").delete();
		new File(".." + path + ".png").delete();

		t1 = System.currentTimeMillis();
		hm.createHotmap(zoom, x, y, keyword, null);
		System.out.println("time:" + (System.currentTimeMillis() - t1));
		new File(".." + path + ".js").delete();
		new File(".." + path + ".png").delete();

		t1 = System.currentTimeMillis();
		hm.createHotmap(zoom, x, y, keyword, "110000");
		System.out.println("time:" + (System.currentTimeMillis() - t1));
	}
}
