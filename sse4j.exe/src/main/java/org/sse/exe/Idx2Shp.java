package org.sse.exe;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.ReaderUtil;
import org.sse.squery.PtyName;
import org.sse.util.MercatorUtil;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.ShapefileWriter;

/**
 * @author dux(duxionggis@126.com)
 */
public class Idx2Shp {

	public static void main(String[] args) throws Exception {
		if (args == null || args.length != 2) {
			System.out.println("Idx2Shp [source lucene path] [target shp path]");
			return;
		}

		File source = new File(args[0]);
		if (!source.exists()) {
			System.out.println("input path is invalid!");
			return;
		}

		System.out.println("------------------start transfer: " + new Date() + "------------------");

		IndexReader sourceReader = IndexReader.open(FSDirectory.open(source));
		TermDocs sourceDocs = sourceReader.termDocs(null);

		// create schema
		FeatureSchema fs = new FeatureSchema();
		FieldInfos fields = ReaderUtil.getMergedFieldInfos(sourceReader);
		for (Iterator<FieldInfo> it = fields.iterator(); it.hasNext();) {
			FieldInfo finfo = it.next();
			if (!finfo.name.equalsIgnoreCase(PtyName.GID)) {
				fs.addAttribute(finfo.name, AttributeType.STRING);
			}
		}
		fs.addAttribute(PtyName.GID, AttributeType.GEOMETRY);

		// trans
		FeatureCollection fc = new FeatureDataset(fs);
		while (sourceDocs.next()) {
			Document tdoc = sourceReader.document(sourceDocs.doc());
			BasicFeature f = new BasicFeature(fs);
			for (int i = 0; i < fs.getAttributeCount(); i++) {
				String name = fs.getAttributeName(i);
				if (name.equalsIgnoreCase(PtyName.GID)) {
					f.setGeometry(MercatorUtil.toGeometry(tdoc.get(PtyName.GID), false));
				} else {
					f.setAttribute(name, tdoc.get(name));
				}
			}
			fc.add(f);
		}
		sourceDocs.close();

		// save shp
		DriverProperties dp = new DriverProperties();
		dp.setProperty("File", args[1]);
		dp.setProperty("charset", "gbk");
		new ShapefileWriter().write(fc, dp);

		System.out.println("------------------end transfer: " + new Date() + "------------------");

	}

}
