package org.sse.idx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.TermDocs;
import org.sse.idx.base.Sidx;
import org.sse.squery.PtyName;
import org.sse.squery.STree;
import org.sse.util.MercatorUtil;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author dux(duxionggis@126.com)
 */
public class Sidxer {
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
	public void save(String idxpath, String keyname, boolean wgs) throws IOException {
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

	public void read(String sidxpath) throws IOException {
		FileInputStream fis = new FileInputStream(new File(sidxpath));
		ObjectTransCoder otc = new ObjectTransCoder();
		List<Sidx> lsidx = (List<Sidx>) otc.decode(fis);
		fis.close();

		tree = null;
		for (Iterator<Sidx> it = lsidx.iterator(); it.hasNext();) {
			Sidx key = it.next();
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
	
	public STree getTree(){
		return this.tree;
	}
	
	/**
	 * @author dux(duxionggis@126.com)
	 */
	class ObjectTransCoder {
		class ContextObjectInputStream extends ObjectInputStream {
			ClassLoader mLoader;

			ContextObjectInputStream(InputStream in, ClassLoader loader) throws IOException, SecurityException {
				super(in);
				mLoader = loader;
			}

			@Override
			protected Class<?> resolveClass(ObjectStreamClass v) throws IOException, ClassNotFoundException {
				if (mLoader == null)
					return super.resolveClass(v);
				else
					return Class.forName(v.getName(), true, mLoader);
			}
		}

		public Object decode(final InputStream input) throws IOException {
			Object obj = null;
			ObjectInputStream ois = new ObjectInputStream(input);
			try {
				obj = ois.readObject();
			} catch (ClassNotFoundException e) {
				throw new IOException(e.getMessage());
			}
			ois.close();
			return obj;
		}

		public void encode(final OutputStream output, final Object object) throws IOException {
			ObjectOutputStream oos = new ObjectOutputStream(output);
			oos.writeObject(object);
			oos.close();
		}

		public Object decode(InputStream input, ClassLoader classLoader) throws IOException {
			Object obj = null;
			ContextObjectInputStream ois = new ContextObjectInputStream(input, classLoader);
			try {
				obj = ois.readObject();
			} catch (ClassNotFoundException e) {
				throw new IOException(e.getMessage());
			}
			ois.close();
			return obj;
		}

	}

}
