package org.sse.test.map;

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
		int x = (int) (pos.xLon / Google.getSize());
		int y = (int) (pos.yLat / Google.getSize());
		String keyword = "大厦";

		long t1 = System.currentTimeMillis();
		String path = hm.createHotmap(zoom, x, y, keyword);
		System.out.println("time:" + (System.currentTimeMillis() - t1));
		System.out.println(path);
	}

}
