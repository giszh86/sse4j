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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;

import org.apache.lucene.document.Document;
import org.sse.NaviConfig;
import org.sse.StorageFactory;
import org.sse.StorageFactory.StorageType;
import org.sse.geoc.District;
import org.sse.geoc.Matcher;
import org.sse.map.Tip.TipPoi;
import org.sse.map.Tip.TipTile;
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
 * @author dux(duxionggis@126.com)
 */
public class HotMapper {
	private static HotMapper[] instances;
	private static Lock lock = new ReentrantLock();

	private BufferedImage icon;
	private GeometryFactory gf;
	private String outpath = "";
	private int count = 2500; // TODO 2500

	public static HotMapper getInstance() {
		int idx = ((int) (Math.random() * 1e6)) % 5;
		if (instances == null) {
			lock.lock();
			instances = new HotMapper[5];
			for (int i = 0; i < 5; i++) {
				instances[i] = new HotMapper();
			}
			lock.unlock();
		}
		return instances[idx];
	}

	protected HotMapper() {
		try {
			gf = new GeometryFactory();
			icon = ImageIO.read(HotMapper.class.getResource("hot.png"));
			outpath = URLUtil.getClassPathFile(HotMapper.class).getParentFile().getParentFile().getPath();
			if (outpath.startsWith("file:"))
				outpath = outpath.substring(5);
			outpath = URLDecoder.decode(outpath, "UTF-8");
			File file = new File(outpath);
			if (!file.exists())
				file.mkdirs();
			TipPoi.setLimitCount(25);
			TipPoi.setBufferSize(icon.getWidth() / 3 + 1, icon.getHeight() / 3 + 1);
		} catch (IOException e) {
		}
	}

	/**
	 * @param zoom
	 *            10 ~ 17
	 * @param x
	 *            0 ~ Pow(2,zoom)-1
	 * @param y
	 *            0 ~ Pow(2,zoom)-1
	 * @param keyword
	 * @param key
	 *            citycode[in navi.xml]
	 * @return common path
	 * @throws Exception
	 */
	public synchronized String createHotmap(int zoom, int x, int y, String keyword, String key) throws Exception {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keyword.length(); i = i + 2) {
			long c = keyword.charAt(i);
			if (i + 1 < keyword.length()) {
				c = Long.valueOf(c + "" + (int) keyword.charAt(i + 1));
			}
			sb.append(Long.toHexString(c));
		}
		// make path
		String path = "/cache/" + sb.toString() + "/" + zoom + "/" + x + "/" + y;
		File jsfile = new File(outpath + path + ".js");
		File imgfile = new File(outpath + path + ".png");
		if (jsfile.exists() && imgfile.exists()) {
			return path;
		} else {
			imgfile.mkdirs();
		}
		// tile extent
		int bufx = icon.getWidth() / 2;
		int bufy = icon.getHeight() / 2;
		EarthPos min = Google.pixelToDegree(x * Google.getSize() - bufx, (y + 1) * Google.getSize() + bufy, zoom);
		EarthPos max = Google.pixelToDegree((x + 1) * Google.getSize() + bufx, y * Google.getSize() - bufy, zoom);
		Coordinate[] coords = new Coordinate[5];
		coords[0] = new Coordinate(min.xLon, min.yLat);
		coords[1] = new Coordinate(min.xLon, max.yLat);
		coords[2] = new Coordinate(max.xLon, max.yLat);
		coords[3] = new Coordinate(max.xLon, min.yLat);
		coords[4] = new Coordinate(min.xLon, min.yLat);
		Geometry extent = gf.createPolygon(gf.createLinearRing(coords), null);
		MercatorUtil.toMercator(extent, true);
		// search
		String keypath = null;
		if (key == null || key.isEmpty()) {
			District dis = new Matcher().districtMatch(gf.createPoint(new Coordinate(min.xLon, min.yLat)));
			keypath = StorageFactory.getInstance().getStorage(dis.getCityCode(), StorageType.POI).getKey();
		} else {
			keypath = StorageFactory.getInstance().getStorage(key, StorageType.POI).getKey();
		}
		Filter filter = new Filter();
		List<Property> ptyes = new ArrayList<Property>(1);
		ptyes.add(new Property(PtyName.TITLE, keyword));
		filter.setProperties(ptyes);
		filter.setCount(count);
		filter.setGeometry(extent);
		List<Document> result = Searcher.getInstance().search(keypath, filter);

		LinkedList<TipPoi> tips = new LinkedList<TipPoi>();
		BufferedImage image = new BufferedImage(Google.getSize(), Google.getSize(), BufferedImage.TYPE_4BYTE_ABGR);
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
				EarthPos gp = new EarthPos(g.getCoordinate().x, g.getCoordinate().y);
				if (!NaviConfig.WGS) {
					gp = Google.googToDegree(g.getCoordinate().x, g.getCoordinate().y);
				}
				gp = Google.degreeToPixel(gp.xLon, gp.yLat, zoom);
				gp.xLon = gp.xLon - x * Google.getSize();
				gp.yLat = gp.yLat - y * Google.getSize();
				tp.setX((int) gp.xLon);
				tp.setY((int) gp.yLat);
				int px = tp.getX() - icon.getWidth() / 2;
				int py = tp.getY() - icon.getHeight() / 2;
				graph.drawImage(icon, null, px, py);

				if (px >= 0 && py >= 0 && px <= Google.getSize() && py <= Google.getSize()) {
					int index = tips.indexOf(tp);
					if (index >= 0) {
						tips.get(index).addSub(tp.clone());
					} else {
						tips.add(tp);
					}
				}
			}
		}
		// System.out.println("r:" + result.size() + " t:" + tips.size());
		// save image and js
		ImageIO.write(image, "png", imgfile);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jsfile), "UTF-8"));
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

		return path;
	}

	/**
	 * @param zoom
	 *            10 ~ 17
	 * @param x
	 *            0 ~ Pow(2,zoom)-1
	 * @param y
	 *            0 ~ Pow(2,zoom)-1
	 * @param keyword
	 * @param key
	 *            citycode[in navi.xml]
	 * @return BufferedImage
	 * @throws Exception
	 */
	public BufferedImage getTile(int zoom, int x, int y, String keyword, String key) throws Exception {
		BufferedImage image = new BufferedImage(Google.getSize(), Google.getSize(), BufferedImage.TYPE_4BYTE_ABGR);
		// tile extent
		int bufx = icon.getWidth() / 2;
		int bufy = icon.getHeight() / 2;
		EarthPos min = Google.pixelToDegree(x * Google.getSize() - bufx, (y + 1) * Google.getSize() + bufy, zoom);
		EarthPos max = Google.pixelToDegree((x + 1) * Google.getSize() + bufx, y * Google.getSize() - bufy, zoom);
		Coordinate[] coords = new Coordinate[5];
		coords[0] = new Coordinate(min.xLon, min.yLat);
		coords[1] = new Coordinate(min.xLon, max.yLat);
		coords[2] = new Coordinate(max.xLon, max.yLat);
		coords[3] = new Coordinate(max.xLon, min.yLat);
		coords[4] = new Coordinate(min.xLon, min.yLat);
		Geometry extent = gf.createPolygon(gf.createLinearRing(coords), null);
		MercatorUtil.toMercator(extent, true);
		// search
		String keypath = null;
		if (key == null || key.isEmpty()) {
			District dis = new Matcher().districtMatch(gf.createPoint(new Coordinate(min.xLon, min.yLat)));
			keypath = StorageFactory.getInstance().getStorage(dis.getCityCode(), StorageType.POI).getKey();
		} else {
			keypath = StorageFactory.getInstance().getStorage(key, StorageType.POI).getKey();
		}
		Filter filter = new Filter();
		List<Property> ptyes = new ArrayList<Property>(1);
		ptyes.add(new Property(PtyName.TITLE, keyword));
		filter.setProperties(ptyes);
		filter.setCount(count);
		filter.setGeometry(extent);
		List<Document> result = Searcher.getInstance().search(keypath, filter);

		// create image
		if (result != null && result.size() > 0) {
			Graphics2D graph = (Graphics2D) image.getGraphics();
			WKTReader reader = new WKTReader();
			for (Iterator<Document> i = result.iterator(); i.hasNext();) {
				Document doc = i.next();

				Geometry g = reader.read(doc.get(PtyName.GID));
				EarthPos gp = new EarthPos(g.getCoordinate().x, g.getCoordinate().y);
				if (!NaviConfig.WGS) {
					gp = Google.googToDegree(g.getCoordinate().x, g.getCoordinate().y);
				}
				gp = Google.degreeToPixel(gp.xLon, gp.yLat, zoom);
				gp.xLon = gp.xLon - x * Google.getSize();
				gp.yLat = gp.yLat - y * Google.getSize();
				int px = (int) gp.xLon - icon.getWidth() / 2;
				int py = (int) gp.yLat - icon.getHeight() / 2;
				graph.drawImage(icon, null, px, py);
			}
		}
		return image;
	}

	/**
	 * @param zoom
	 *            10 ~ 17
	 * @param x
	 *            0 ~ Pow(2,zoom)-1
	 * @param y
	 *            0 ~ Pow(2,zoom)-1
	 * @param keyword
	 * @param key
	 *            citycode[in navi.xml]
	 * @return js string
	 * @throws Exception
	 */
	public String getTileJS(int zoom, int x, int y, String keyword, String key) throws Exception {
		// tile extent
		EarthPos min = Google.pixelToDegree(x * Google.getSize(), (y + 1) * Google.getSize(), zoom);
		EarthPos max = Google.pixelToDegree((x + 1) * Google.getSize(), y * Google.getSize(), zoom);
		Coordinate[] coords = new Coordinate[5];
		coords[0] = new Coordinate(min.xLon, min.yLat);
		coords[1] = new Coordinate(min.xLon, max.yLat);
		coords[2] = new Coordinate(max.xLon, max.yLat);
		coords[3] = new Coordinate(max.xLon, min.yLat);
		coords[4] = new Coordinate(min.xLon, min.yLat);
		Geometry extent = gf.createPolygon(gf.createLinearRing(coords), null);
		MercatorUtil.toMercator(extent, true);
		// search
		String keypath = null;
		if (key == null || key.isEmpty()) {
			District dis = new Matcher().districtMatch(gf.createPoint(new Coordinate(min.xLon, min.yLat)));
			keypath = StorageFactory.getInstance().getStorage(dis.getCityCode(), StorageType.POI).getKey();
		} else {
			keypath = StorageFactory.getInstance().getStorage(key, StorageType.POI).getKey();
		}
		Filter filter = new Filter();
		List<Property> ptyes = new ArrayList<Property>(1);
		ptyes.add(new Property(PtyName.TITLE, keyword));
		filter.setProperties(ptyes);
		filter.setCount(count);
		filter.setGeometry(extent);
		List<Document> result = Searcher.getInstance().search(keypath, filter);

		LinkedList<TipPoi> tips = new LinkedList<TipPoi>();
		// create js
		if (result != null && result.size() > 0) {
			WKTReader reader = new WKTReader();
			for (Iterator<Document> i = result.iterator(); i.hasNext();) {
				Document doc = i.next();

				TipPoi tp = new TipPoi();
				tp.setId(doc.get(PtyName.OID));
				tp.setTitle(doc.get(PtyName.TITLE));

				Geometry g = reader.read(doc.get(PtyName.GID));
				EarthPos gp = new EarthPos(g.getCoordinate().x, g.getCoordinate().y);
				if (!NaviConfig.WGS) {
					gp = Google.googToDegree(g.getCoordinate().x, g.getCoordinate().y);
				}
				gp = Google.degreeToPixel(gp.xLon, gp.yLat, zoom);
				gp.xLon = gp.xLon - x * Google.getSize();
				gp.yLat = gp.yLat - y * Google.getSize();
				tp.setX((int) gp.xLon);
				tp.setY((int) gp.yLat);

				int index = tips.indexOf(tp);
				if (index >= 0) {
					tips.get(index).addSub(tp);
				} else {
					tips.add(tp);
				}
			}
		}

		TipTile tt = new TipTile();
		tt.setZoom(zoom);
		tt.setX(x);
		tt.setY(y);
		tt.setTips(tips);

		return ("hotMapTip(" + new Gson().toJson(tt) + ");");
	}
}
