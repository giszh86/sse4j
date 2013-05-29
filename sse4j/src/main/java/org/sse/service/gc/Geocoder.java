package org.sse.service.gc;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.sse.squery.PtyName;
import org.sse.StorageFactory;
import org.sse.StorageFactory.StorageType;
import org.sse.geo.Point;
import org.sse.idx.IdxParser;
import org.sse.idx.base.Enums.AnalyzerType;
import org.sse.idx.base.Enums.OccurType;
import org.sse.mcache.Storage;
import org.sse.squery.Searcher;

/**
 * geocoding depends on province district
 * 
 * @author dux(duxionggis@126.com)
 */
public class Geocoder {

	public ResultGC geocoding(String key, String keyword) throws Exception {
		Storage storage = (Storage) StorageFactory.getInstance().getStorage(key, StorageType.POI);

		List<String> tokens = IdxParser.getInstance().tokenize(keyword, AnalyzerType.IK);
		// address operate 'and' by tokens[length]
		Query query = IdxParser.getInstance().createQuery(PtyName.TITLE, tokens, OccurType.AND);
		List<Document> docs = Searcher.getInstance().search(storage.getKey(), query, 100);

		// address operate 'or' by tokens[length]
		if (docs == null || docs.size() == 0) {
			query = IdxParser.getInstance().createQuery(PtyName.TITLE, tokens, OccurType.OR);
			docs = Searcher.getInstance().search(storage.getKey(), query, 100);
		}

		if (docs == null || docs.size() == 0) {
			throw new Exception("not found!");
		}

		for (Document d : docs) {
			String addr = d.get(PtyName.TITLE);
			boolean ismatch = true;
			int idxT = -1;

			// match 100% tokens
			for (String t : tokens) {
				int idx = addr.indexOf(t);
				ismatch = ismatch && idx != -1 && idx > idxT;
				if (!ismatch)
					break;
				idxT = idx;
			}
			if (ismatch) {
				Point pt = new Point(Integer.valueOf(d.get(PtyName.CENX)), Integer.valueOf(d.get(PtyName.CENY)));
				return new ResultGC(d.get(PtyName.TITLE), pt);
				// break;
			}
			// match 80% tokens
		}

		throw new Exception("not found!");
	}

	public class ResultGC {
		private String desc;
		private Point point;

		public ResultGC(String desc, Point point) {
			this.desc = desc;
			this.point = point;
		}

		public String getDesc() {
			return desc;
		}

		public Point getPoint() {
			return point;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{desc:\"").append(desc).append("\",");
			sb.append("coord:\"").append(point).append("\"}");
			return sb.toString();
		}
	}
}
