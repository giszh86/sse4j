package org.sse.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.sse.NaviConfig;
import org.sse.StorageFactory;
import org.sse.StorageFactory.StorageType;
import org.sse.mcache.Storage;
import org.sse.service.base.Poi;
import org.sse.service.base.PoiPtyName;
import org.sse.squery.Filter;
import org.sse.squery.Searcher;
import org.sse.util.MercatorUtil;

import com.vividsolutions.jts.geom.Geometry;

/**
 * POI search depends on city district
 * 
 * @author dux(duxionggis@126.com)
 */
public class PoiService implements IPoiService {
	public List<Poi> search(Filter filter, String key) throws Exception {
		Storage storage = (Storage) StorageFactory.getInstance().getStorage(key, StorageType.POI);
		if (storage == null)
			throw new Exception("not found poi data!");
		if (filter == null)
			return null;
		if (filter.getProperties() == null && filter.getGeometry() == null)
			return null;
		Geometry geo = filter.getGeometry();
		MercatorUtil.toMercator(geo, true);
		List<Document> docs = Searcher.getInstance().search(storage.getKey(), filter);
		if (docs != null && docs.size() > 0) {
			List<Poi> pois = new ArrayList<Poi>();

			// geometry intersection
			for (Iterator<Document> it = docs.iterator(); it.hasNext();) {
				this.add(pois, it.next(), geo);
				if (pois.size() == filter.getCount())
					break;
			}

			return pois;
		}
		return null;
	}

	public Poi tipInfo(String id, String key) throws Exception {
		Storage storage = (Storage) StorageFactory.getInstance().getStorage(key, StorageType.POI);
		if (storage == null)
			throw new Exception("not found poi data!");
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Term(PoiPtyName.OID, id));
		List<Document> docs = Searcher.getInstance().search(storage.getKey(), terms);
		if (docs != null && docs.size() > 0) {
			Poi poi = new Poi();
			poi.setId(docs.get(0).get(PoiPtyName.OID));
			poi.setName(docs.get(0).get(PoiPtyName.NAMEC));
			poi.setKind(docs.get(0).get(PoiPtyName.KIND));
			poi.setPhone(docs.get(0).get(PoiPtyName.TEL));
			poi.setRemark(docs.get(0).get(PoiPtyName.NAMEP));
			poi.setAddress(docs.get(0).get(PoiPtyName.ADDRESS));
			poi.setVertex(MercatorUtil.toPoint(
					MercatorUtil.toGeometry(docs.get(0).get(PoiPtyName.GID), NaviConfig.WGS).getCoordinate(), false)
					.toString());

			return poi;
		} else {
			throw new Exception("not found!");
		}
	}

	private void add(List<Poi> pois, Document doc, Geometry geo) {
		Geometry g = MercatorUtil.toGeometry(doc.get(PoiPtyName.GID), NaviConfig.WGS);
		if ((geo != null && geo.intersects(g)) || (geo == null)) {
			Poi poi = new Poi();
			poi.setId(doc.get(PoiPtyName.OID));
			poi.setName(doc.get(PoiPtyName.NAMEC));
			poi.setKind(doc.get(PoiPtyName.KIND));
			poi.setPhone(doc.get(PoiPtyName.TEL));
			poi.setRemark(doc.get(PoiPtyName.NAMEP));
			poi.setAddress(doc.get(PoiPtyName.ADDRESS));
			poi.setVertex(MercatorUtil.toPoint(g.getCoordinate(), false).toString());
			pois.add(poi);
		}
	}

}
