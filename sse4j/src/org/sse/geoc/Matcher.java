package org.sse.geoc;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.sse.NaviConfig;
import org.sse.StorageFactory;
import org.sse.StorageFactory.StorageType;
import org.sse.mcache.DistStorage;
import org.sse.squery.Searcher;
import org.sse.util.MercatorUtil;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 * road or district or gps match
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Matcher {
	private double buf = 50; // meter

	/**
	 * 
	 * @param point
	 *            [WGS84]
	 * @return
	 * @throws Exception
	 */
	public District districtMatch(Point point) throws Exception {
		DistStorage storage = (DistStorage) StorageFactory.getInstance()
				.getStorage(NaviConfig.BASE_KEY, StorageType.DIST);
		MercatorUtil.toMercator(point, true);
		Envelope env = new Envelope(point.getX() - buf, point.getX() + buf,
				point.getY() - buf, point.getY() + buf);
		List<String> result = Searcher.getInstance().boxQuery(storage.getKey(),
				env);
		if (result == null || result.size() == 0)
			throw new Exception("not found!");
		for (String id : result) {
			if (storage.getGeos().get(id).contains(point)) {
				List<Term> terms = new ArrayList<Term>(1);
				terms.add(new Term(DistPtyName.OID, id));
				List<Document> docs = Searcher.getInstance().search(
						storage.getKey(), terms);

				District dis = new District();
				dis.setCityCode(docs.get(0).get(DistPtyName.CITYCODE));
				dis.setProvinceCode(docs.get(0).get(DistPtyName.PROVCODE));
				dis.setProvince(docs.get(0).get(DistPtyName.PROVINCE));
				dis.setCity(docs.get(0).get(DistPtyName.CITY));
				dis.setCounty(docs.get(0).get(DistPtyName.COUNTY));
				return dis;
			}
		}
		throw new Exception("not found!");
	}

	/**
	 * 
	 * @param from
	 *            [WGS84]
	 * @param to
	 *            [WGS84]
	 * @return {"ID":"","TITLE":"","WKT":""}
	 * 
	 * @throws Exception
	 * 
	 */
	public String roadMatch(Point from, Point to) throws Exception {
		// TODO
		return null;
	}

}
