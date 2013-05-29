package org.sse.squery;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.sse.idx.IdxReader;
import org.sse.idx.Sidxer;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author dux(duxionggis@126.com)
 */
public class Searcher {
	private static Searcher instance;
	private static Lock lock = new ReentrantLock();
	private Map<String, SQuery> queries;

	protected Searcher() {
		queries = new HashMap<String, SQuery>();
	}

	public static Searcher getInstance() {
		if (instance == null) {
			lock.lock();
			instance = new Searcher();
			lock.unlock();
		}
		return instance;
	}

	public Set<String> getKeys() {
		return queries.keySet();
	}

	/**
	 * set SQuery metadata
	 * 
	 * @param key
	 *            Map's key
	 * @param reader
	 *            index reader object
	 * @param tree
	 *            spatial index object
	 */
	public void put(String key, IdxReader reader, STree tree) {
		queries.put(key, new SQuery(reader, tree));
	}

	/**
	 * check Searcher contains named key's squery
	 * 
	 * @param key
	 *            Map's key
	 * @param idxpath
	 *            support multi-paths separated by ','
	 * @param wgs
	 *            WGS84
	 * @param useCache
	 *            cache SpatialIndex
	 * @return
	 */
	public boolean check(String key, String idxpath, boolean wgs, boolean useCache) {
		try {
			if (!queries.containsKey(key)) {
				if (useCache) {
					Sidxer idxer = new Sidxer();
					String path = idxpath.split(",")[0];
					File file = new File(path + ".sidx");
					if (!file.exists()) {
						idxer.save(idxpath, PtyName.OID, wgs);
					} else {
						idxer.read(path + ".sidx");
					}
					queries.put(key, new SQuery(new IdxReader(idxpath), idxer.getTree()));
				} else {
					queries.put(key, new SQuery(new IdxReader(idxpath), null));
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public List<Document> search(String key, Filter filter) {
		return queries.get(key).query(filter);
	}

	public List<Document> search(String key, List<Term> terms) {
		return queries.get(key).query(terms);
	}

	public List<Document> search(String key, Query query, int count) {
		return queries.get(key).query(query, count);
	}

	public Envelope fullExtent(String key) {
		return queries.get(key).getExtent();
	}

	public List spatialFilter(String key, Envelope envelope) {
		return queries.get(key).spatialFilter(envelope);
	}

}
