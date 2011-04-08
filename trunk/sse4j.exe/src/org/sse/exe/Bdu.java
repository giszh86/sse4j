package org.sse.exe;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.sse.util.Google;
import org.sse.util.EarthPos;
import com.google.gson.Gson;

public class Bdu {

	public static void main(String[] arg) throws Exception {
		double lon = 116.32;
		double lat = 39.97;
		int zoom = 14;
		int city = 131; // 北京
		String key = "停车场";

		EarthPos tile = Google.degreeToPixel(lon, lat, zoom);
		Google.pixelToTile(tile);
		int xFrom = (int) tile.xLon - 10;
		int xTo = xFrom + 20;
		int yFrom = (int) tile.yLat - 10;
		int yTo = yFrom + 20;

		List<BduTip> list = getTips(xFrom, yFrom, xTo, yTo, zoom, city, key);
		System.out.println("Size:" + list.size() + list);
	}

	static List<BduTip> getTips(int xFrom, int yFrom, int xTo, int yTo,
			int zoom, int city, String key) {
		List<BduTip> list = new LinkedList<BduTip>();
		for (int i = xFrom; i <= xTo; i++)
			for (int j = yFrom; j <= yTo; j++) {
				String url = getJsUrl(i, j, zoom, city, key);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try {
					InputStream input = (new URL(url)).openStream();
					int b = 0;
					while ((b = input.read()) != -1)
						baos.write(b);
					baos.close();
					input.close();
				} catch (Exception ex) {
					System.out.println("Error:" + ex.getMessage());
				}

				String data = baos.toString().trim();
				data = data.substring(9, data.length() - 1);
				BduTips dd = new Gson().fromJson(data, BduTips.class);
				if (dd.getUid_num() > 0)
					list.addAll(dd.getUids());
			}
		return list;
	}

	static String getJsUrl(int x, int y, int zoom, int city, String key) {
		EarthPos f = Google.pixelToDegree(x * 256, y * 256, zoom);
		EarthPos t = Google.pixelToDegree((x + 1) * 256, (y + 1) * 256, zoom);
		f = Google.degreeToGoog(f.xLon, f.yLat);
		t = Google.degreeToGoog(t.xLon, t.yLat);

		StringBuffer sb = new StringBuffer();// &c=&ie=utf-8&wd=&l=&xy=&callback=getMData&b=&t=
		sb.append("http://ditu.baidu.com/?newmap=1&qt=bkg_data");
		sb.append("&c=" + city);
		sb.append("&ie=utf-8&wd=" + key);
		sb.append("&l=" + zoom);
		sb.append("&xy=" + getXY(f.xLon, f.yLat, zoom));
		sb.append("&callback=getMData");
		sb.append("&b=(" + (int) f.xLon + "," + (int) t.yLat + ";"
				+ (int) t.xLon + "," + (int) f.yLat + ")");
		sb.append("&t=" + System.currentTimeMillis());
		return sb.toString();
	}

	static String getXY(double x, double y, int zoom) {
		double size = 256 * Math.pow(2, (18 - zoom));
		int xT = (int) (x / size);
		int yT = (int) (y / size);
		return xT + "_" + yT;
	}

}
