package org.sse.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;

import org.apache.lucene.document.Document;
import org.sse.NaviConfig;
import org.sse.StorageFactory;
import org.sse.StorageFactory.StorageType;
import org.sse.geoc.District;
import org.sse.geoc.Matcher;
import org.sse.map.Tip.TipPoi;
import org.sse.map.Tip.TipTile;
import org.sse.mcache.IStorage;
import org.sse.squery.Filter;
import org.sse.squery.Property;
import org.sse.squery.PtyName;
import org.sse.squery.Searcher;
import org.sse.util.EarthPos;
import org.sse.util.Google;
import org.sse.util.MercatorUtil;
import org.sse.util.URLUtil;

import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class HotMapper {
	private static HotMapper instance;
	private static Object lock = new Object();

	private BufferedImage icon;
	private GeometryFactory gf;
	private String outpath = "";
	private int count = 2500; // TODO 2500

	public static HotMapper getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new HotMapper();
				}
			}
		}
		return instance;
	}

	protected HotMapper() {
		try {
			gf = new GeometryFactory();
			icon = ImageIO.read(HotMapper.class.getResource("hot.png"));
			outpath = URLUtil.getClassPathFile(HotMapper.class).getParentFile()
					.getParentFile().getPath();
			if (outpath.startsWith("file:"))
				outpath = outpath.substring(5);
			outpath = URLDecoder.decode(outpath, "UTF-8");
			File file = new File(outpath);
			if (!file.exists())
				file.mkdirs();
		} catch (IOException e) {
		}
	}

	/**
	 * 
	 * @param zoom
	 *            10 ~ 17
	 * @param x
	 *            0 ~ Pow(2,zoom)-1
	 * @param y
	 *            0 ~ Pow(2,zoom)-1
	 * @param keyword
	 * @return common path
	 * @throws Exception
	 */
	public String createHotmap(int zoom, int x, int y, String keyword)
			throws Exception {
		char[] chars = keyword.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int c : chars) {
			sb.append(c);
		}
		// make path
		String path = "/cache/" + sb.toString() + "/" + zoom + "/" + x + "/"
				+ y;
		File jsfile = new File(outpath + path + ".js");
		File imgfile = new File(outpath + path + ".png");
		if (jsfile.exists() && imgfile.exists()) {
			return path;
		} else {
			imgfile.mkdirs();
		}
		synchronized (lock) {
			// tile extent
			EarthPos min = Google.pixelToDegree(x * Google.getSize(), (y + 1)
					* Google.getSize(), zoom);
			EarthPos max = Google.pixelToDegree((x + 1) * Google.getSize(), y
					* Google.getSize(), zoom);
			Coordinate[] coords = new Coordinate[5];
			coords[0] = new Coordinate(min.xLon, min.yLat);
			coords[1] = new Coordinate(min.xLon, max.yLat);
			coords[2] = new Coordinate(max.xLon, max.yLat);
			coords[3] = new Coordinate(max.xLon, min.yLat);
			coords[4] = new Coordinate(min.xLon, min.yLat);
			Geometry extent = gf.createPolygon(gf.createLinearRing(coords),
					null);
			MercatorUtil.toMercator(extent, true);
			// search
			District dis = new Matcher().districtMatch(new GeometryFactory()
					.createPoint(new Coordinate(min.xLon, min.yLat)));
			IStorage stg = StorageFactory.getInstance().getStorage(
					dis.getCityCode(), StorageType.POI);
			Filter filter = new Filter();
			List<Property> ptyes = new ArrayList<Property>(1);
			ptyes.add(new Property(PtyName.TITLE, keyword));
			filter.setProperties(ptyes);
			filter.setCount(count);
			filter.setGeometry(extent);
			List<Document> result = Searcher.getInstance().search(stg.getKey(),
					filter);

			LinkedList<TipPoi> tips = new LinkedList<TipPoi>();
			BufferedImage image = new BufferedImage(Google.getSize(), Google
					.getSize(), BufferedImage.TYPE_4BYTE_ABGR);
			// create image and js
			if (result != null && result.size() > 0) {
				Graphics2D graph = (Graphics2D) image.getGraphics();
				WKTReader reader = new WKTReader();
				for (Iterator<Document> i = result.iterator(); i.hasNext();) {
					Document doc = i.next();

					TipPoi tp = new TipPoi();
					tp.setId(doc.get(PtyName.OID));
					tp.setTitle(doc.get(PtyName.TITLE));

					Geometry g = reader.read(doc.get(PtyName.GID));
					EarthPos gp = new EarthPos(g.getCoordinate().x, g
							.getCoordinate().y);
					if (!NaviConfig.WGS) {
						gp = Google.googToDegree(g.getCoordinate().x, g
								.getCoordinate().y);
					}
					gp = Google.degreeToPixel(gp.xLon, gp.yLat, zoom);
					gp.xLon = gp.xLon - x * Google.getSize();
					gp.yLat = gp.yLat - y * Google.getSize();
					tp.setX((int) gp.xLon);
					tp.setY((int) gp.yLat);
					int px = tp.getX() - icon.getWidth() / 2;
					int py = tp.getY() - icon.getHeight() / 2;
					if (px >= 0 && py >= 0
							&& px <= (Google.getSize() - icon.getWidth())
							&& py <= (Google.getSize() - icon.getHeight())) {
						int index = tips.indexOf(tp);
						if (index >= 0) {
							tips.get(index).addSub(tp.clone());
						} else {
							tips.add(tp);
							graph.drawImage(icon, null, px, py);
						}
					}
				}
			}
			// System.out.println("r:" + result.size() + " t:" + tips.size());
			// save image and js
			ImageIO.write(image, "png", imgfile);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(jsfile), "UTF-8"));
			TipTile tt = new TipTile();
			tt.setZoom(zoom);
			tt.setX(x);
			tt.setY(y);
			tt.setTips(tips);
			out.write("hotMapTip(" + new Gson().toJson(tt) + ");");
			out.flush();
			out.close();
			tips = null;
			result = null;
		}

		return path;
	}
}
