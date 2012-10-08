package org.sse.geoc;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.sse.squery.Filter;
import org.sse.squery.Property;
import org.sse.NaviConfig;
import org.sse.StorageFactory;
import org.sse.StorageFactory.StorageType;
import org.sse.io.IdxParser;
import org.sse.io.Enums.QueryType;
import org.sse.mcache.Storage;
import org.sse.service.base.PoiPtyName;
import org.sse.squery.Searcher;
import org.sse.util.MercatorUtil;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.distance.DistanceOp;

/**
 * geocoding depends on province district
 * 
 * @author dux(duxionggis@126.com)
 */
public class Geocoder {
	private double buf = 100; // meter

	public org.sse.geo.Point geocoding(String key, String keyword) throws Exception {
		Storage storage = (Storage) StorageFactory.getInstance().getStorage(key, StorageType.POI);
		List<Property> pty = new ArrayList<Property>();
		pty.add(new Property(PoiPtyName.ADDRESS, keyword));
		List<Document> docs = Searcher.getInstance().search(storage.getKey(),
				IdxParser.getInstance().createQuery(QueryType.IK, pty), 2);
		if (docs == null || docs.size() == 0)
			throw new Exception("not found!");

		System.out.println(docs.get(0).get(PoiPtyName.ADDRESS) + "-" + keyword);
		return MercatorUtil.toPoint(MercatorUtil.toGeometry(docs.get(0).get(PoiPtyName.GID), NaviConfig.WGS)
				.getCoordinate(), false);
	}

	/**
	 * @param key
	 * @param point
	 *            [WGS84]
	 * @return
	 * @throws Exception
	 */
	public String reverseGeocoding(String key, Point point) throws Exception {
		Storage storage = (Storage) StorageFactory.getInstance().getStorage(key, StorageType.POI);
		MercatorUtil.toMercator(point, true);
		Filter filter = new Filter();
		filter.setGeometry(point.buffer(buf));
		List<Document> docs = Searcher.getInstance().search(storage.getKey(), filter);
		if (docs == null || docs.size() == 0)
			throw new Exception("not found!");

		double min = Double.MAX_VALUE;
		Document doc = null;
		for (Document i : docs) {
			double dis = DistanceOp.distance(point, MercatorUtil.toGeometry(i.get(PoiPtyName.GID), NaviConfig.WGS));
			if (dis < min) {
				min = dis;
				doc = i;
			}
		}
		if (doc != null && min < buf * 10)
			return doc.get(PoiPtyName.ADDRESS);
		else
			throw new Exception("not found!");
	}
}
