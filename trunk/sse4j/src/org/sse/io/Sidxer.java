package org.sse.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.document.Document;
import org.sse.util.MercatorUtil;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Sidxer {
	private SpatialIndex tree;
	private Envelope extent;

	/**
	 * save spatial index to sidx file
	 * 
	 * @param idxpath
	 *            support multi-paths separated by ','
	 * @param keyname
	 * @param wgs
	 * @throws IOException
	 */
	public void save(String idxpath, String keyname, boolean wgs)
			throws IOException {
		extent = null;
		tree = new STRtree();

		IdxReader reader = new IdxReader(idxpath);
		List<Sidx> lsidx = new ArrayList<Sidx>();

		// TODO Version=3.1 TermDocs Bug
		// TermDocs docs = reader.getReader().termDocs();
		// while (docs.next()) {
		// Document doc = reader.getReader().document(docs.doc());
		// Sidx sidx = new Sidx();
		// Envelope env = MercatorUtil.toGeometry(doc.get("GEOMETRY"), wgs)
		// .getEnvelopeInternal();
		// sidx.setX1((float) env.getMinX());
		// sidx.setY1((float) env.getMinY());
		// sidx.setX2((float) env.getMaxX());
		// sidx.setY2((float) env.getMaxY());
		// sidx.setKey(doc.get(keyname));
		// lsidx.add(sidx);
		//
		// if (extent == null)
		// extent = env;
		// else
		// extent.expandToInclude(env);
		// tree.insert(env, sidx.getKey());
		// }
		// docs.close();
		for (int i = 0; i < reader.getReader().numDocs(); i++) {
			Document doc = reader.getReader().document(i);
			Sidx sidx = new Sidx();
			Envelope env = MercatorUtil.toGeometry(doc.get("GEOMETRY"), wgs)
					.getEnvelopeInternal();
			sidx.setX1((float) env.getMinX());
			sidx.setY1((float) env.getMinY());
			sidx.setX2((float) env.getMaxX());
			sidx.setY2((float) env.getMaxY());
			sidx.setKey(doc.get(keyname));
			lsidx.add(sidx);

			if (extent == null)
				extent = env;
			else
				extent.expandToInclude(env);
			tree.insert(env, sidx.getKey());
		}
		reader.close();

		File file = new File(idxpath.split(",")[0] + ".sidx");
		FileOutputStream fos = new FileOutputStream(file);
		ObjectTransCoder otc = new ObjectTransCoder();
		otc.encode(fos, lsidx);
		fos.close();
		lsidx.clear();
		lsidx = null;
		((STRtree) tree).build();
	}

	public void read(String sidxpath) throws IOException {
		FileInputStream fis = new FileInputStream(new File(sidxpath));
		ObjectTransCoder otc = new ObjectTransCoder();
		List<Sidx> lsidx = (List<Sidx>) otc.decode(fis);
		fis.close();

		extent = null;
		tree = new STRtree();
		Sidx key;
		for (Iterator<Sidx> i = lsidx.iterator(); i.hasNext();) {
			key = i.next();
			Envelope env = new Envelope(key.getX1(), key.getX2(), key.getY1(),
					key.getY2());
			if (extent == null)
				extent = env;
			else
				extent.expandToInclude(env);
			tree.insert(env, key.getKey());
		}
		lsidx.clear();
		lsidx = null;
		((STRtree) tree).build();
	}

	public SpatialIndex getTree() {
		return tree;
	}

	public Envelope getExtent() {
		return extent;
	}

}
