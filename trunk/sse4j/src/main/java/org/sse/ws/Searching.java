package org.sse.ws;

import java.util.Iterator;
import java.util.List;

import org.apache.lucene.document.Document;
import org.sse.NaviConfig;
import org.sse.ServiceFactory;
import org.sse.StorageFactory;
import org.sse.StorageFactory.StorageType;
import org.sse.mcache.IStorage;
import org.sse.service.ls.IPoiService;
import org.sse.squery.Filter;
import org.sse.squery.PtyName;
import org.sse.squery.Searcher;
import org.sse.util.MercatorUtil;
import org.sse.ws.base.WSBuilder;
import org.sse.ws.base.WSFilter;
import org.sse.ws.base.WSFilterPoi;
import org.sse.ws.base.WSResult;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * POI depends on city district
 * 
 * @author dux(duxionggis@126.com)
 */
public class Searching {
	/**
	 * 30-60 ms / 1 request
	 * 
	 * @param wsFilter
	 * @return
	 */
	public WSResult search(WSFilter wsFilter) {
		WSResult result = new WSResult();
		try {
			if (wsFilter.getPreference().trim().isEmpty())
				wsFilter.setPreference("POI");
			StorageType type = Enum.valueOf(StorageType.class, wsFilter.getPreference());
			String key = wsFilter.getKey();
			if (key == null || key.trim().isEmpty())
				key = NaviConfig.BASE_KEY;
			IStorage storage = StorageFactory.getInstance().getStorage(key, type);

			Filter filter = WSBuilder.filter(wsFilter);
			List<Document> docs = Searcher.getInstance().search(storage.getKey(), filter);
			if (docs == null || docs.size() == 0)
				throw new Exception("not found!");

			Geometry geo = filter.getGeometry();
			int count = 1;
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for (Iterator<Document> it = docs.iterator(); it.hasNext();) {
				Document doc = it.next();
				Geometry g = MercatorUtil.toGeometry(doc.get(PtyName.GID), NaviConfig.WGS);
				if ((geo != null && geo.intersects(g)) || (geo == null)) {
					sb.append("{");
					sb.append("\"id\":\"" + doc.get(PtyName.OID) + "\",");
					sb.append("\"title\":\"" + doc.get(PtyName.TITLE) + "\",");

					for (Coordinate c : g.getCoordinates()) {
						c.x = (int) Math.round(c.x);
						c.y = (int) Math.round(c.y);
					}
					sb.append("\"wkt\":\"" + g.toString() + "\"");
					sb.append("}");
					sb.append(",");
					if (count == filter.getCount())
						break;
					count += 1;
				}
			}
			sb.delete(sb.length() - 1, sb.length());
			sb.append("]");
			result.setJsonString(sb.toString());
			result.setResultCode(1);
		} catch (Exception e) {
			result.setResultCode(0);
			result.setFaultString(e.getMessage());
		}
		return result;
	}

	/**
	 * 5 ms / 1 request
	 * 
	 * @param poi
	 * @return
	 */
	public WSResult poiInfo(WSFilterPoi poi) {
		WSResult result = new WSResult();
		try {
			IPoiService ps = ServiceFactory.getPoiService();
			result.setJsonString(ps.tipInfo(poi.getId(), poi.getKey()).toString());
			result.setResultCode(1);
		} catch (Exception e) {
			result.setResultCode(0);
			result.setFaultString(e.getMessage());
		}
		return result;
	}

}
