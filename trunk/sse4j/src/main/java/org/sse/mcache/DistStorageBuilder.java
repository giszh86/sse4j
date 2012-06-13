package org.sse.mcache;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.sse.squery.STree;
import org.sse.NaviConfig;
import org.sse.geoc.DistPtyName;
import org.sse.io.IdxReader;
import org.sse.squery.Searcher;
import org.sse.util.MercatorUtil;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class DistStorageBuilder implements IStorageBuilder {

	public IStorage create(Map<String, String> map) throws Exception {
		STree tree = new STree(false);
		IdxReader reader = new IdxReader(map.get("item-path"));
		Map<String, Geometry> geos = new HashMap<String, Geometry>(reader
				.getReader().numDocs());

		// TODO Version=3.1 TermDocs Bug
		// TermDocs docs = reader.getReader().termDocs();
		// while (docs.next()) {
		// Document doc = reader.getReader().document(docs.doc());
		// Geometry g = MercatorUtil.toGeometry(doc.get(DistPtyName.GID),
		// NaviConfig.WGS);
		// idx.insert(g.getEnvelopeInternal(), doc.get(DistPtyName.OID));
		// geos.put(doc.get(DistPtyName.OID), g);
		// if (ext == null)
		// ext = g.getEnvelopeInternal();
		// else
		// ext.expandToInclude(g.getEnvelopeInternal());
		// }
		// docs.close();
		for (int i = 0; i < reader.getReader().numDocs(); i++) {
			Document doc = reader.getReader().document(i);
			Geometry g = MercatorUtil.toGeometry(doc.get(DistPtyName.GID),
					NaviConfig.WGS);
			tree.insert(g.getEnvelopeInternal(), doc.get(DistPtyName.OID));
			geos.put(doc.get(DistPtyName.OID), g);
		}
		tree.build();
		Searcher.getInstance().put(map.get("item-path"), reader, tree);
		return new DistStorage(map.get("item-path"), geos);
	}

}
