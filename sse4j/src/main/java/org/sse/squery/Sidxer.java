package org.sse.squery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.TermDocs;
import org.sse.io.IdxReader;
import org.sse.io.ObjectTransCoder;
import org.sse.util.MercatorUtil;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author dux(duxionggis@126.com)
 */
class Sidxer {
	STree tree;

	/**
	 * save spatial index to sidx file
	 * 
	 * @param idxpath
	 *            support multi-paths separated by ','
	 * @param keyname
	 * @param wgs
	 * @throws IOException
	 */
	void save(String idxpath, String keyname, boolean wgs) throws IOException {
		IdxReader reader = new IdxReader(idxpath);
		List<Sidx> lsidx = new ArrayList<Sidx>();
		tree = null;
		// TODO Version=3.1 TermDocs Bug
		TermDocs docs = reader.getReader(0).termDocs(null);
		while (docs.next()) {
			Document doc = reader.getReader(0).document(docs.doc());
			if (tree == null) {
				boolean isPt = MercatorUtil.toGeometry(doc.get(PtyName.GID), wgs).getGeometryType()
						.equalsIgnoreCase("Point");
				tree = new STree(isPt);
			}
			Sidx sidx = new Sidx();
			Envelope env = MercatorUtil.toGeometry(doc.get(PtyName.GID), wgs).getEnvelopeInternal();
			sidx.setX1((float) env.getMinX());
			sidx.setY1((float) env.getMinY());
			sidx.setX2((float) env.getMaxX());
			sidx.setY2((float) env.getMaxY());
			sidx.setKey(doc.get(keyname));
			lsidx.add(sidx);

			tree.insert(env, sidx.getKey());
		}
		docs.close();
		reader.close();
		tree.build();

		File file = new File(idxpath.split(",")[0] + ".sidx");
		FileOutputStream fos = new FileOutputStream(file);
		ObjectTransCoder otc = new ObjectTransCoder();
		otc.encode(fos, lsidx);
		fos.close();
		lsidx = null;
	}

	void read(String sidxpath) throws IOException {
		FileInputStream fis = new FileInputStream(new File(sidxpath));
		ObjectTransCoder otc = new ObjectTransCoder();
		List<Sidx> lsidx = (List<Sidx>) otc.decode(fis);
		fis.close();

		tree = null;
		for (Iterator<Sidx> i = lsidx.iterator(); i.hasNext();) {
			Sidx key = i.next();
			if (tree == null) {
				boolean isPt = ((key.getX1() == key.getX2()) && (key.getY1() == key.getY2()));
				tree = new STree(isPt);
			}
			Envelope env = new Envelope(key.getX1(), key.getX2(), key.getY1(), key.getY2());
			tree.insert(env, key.getKey());
		}
		lsidx = null;
		tree.build();
	}

}
