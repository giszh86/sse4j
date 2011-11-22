package org.sse.io;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.sse.geo.Feature;
import org.sse.geo.FeatureCollection;
import org.sse.io.Enums.AnalyzerType;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class IdxWriter {
	private Analyzer analyzer;

	public IdxWriter() {
		analyzer = IdxParser.getInstance().getAnalyzer(AnalyzerType.SmartCN);
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * 
	 * @param fc
	 *            FeatureCollection
	 * @param analyzedFields
	 *            list of attributename
	 * @param idxPath
	 */
	public void create(FeatureCollection fc, List<String> analyzedFields,
			String idxPath) {
		IndexWriter writer = this.build(idxPath);
		if (writer == null)
			return;
		for (Iterator<Feature> i = fc.iterator(); i.hasNext();) {
			this.write(i.next(), writer, analyzedFields);
		}
		this.close(writer);
	}

	/**
	 * 
	 * build index writer,parameters contains:
	 * <p>
	 * mergeFactor(合并因子): 决定把磁盘上的索引块合并成一个大的索引块的频率，默认值为10 。 比如， 10个Segment
	 * 会被合并成一个新的 Segment 。如果合并后的这个大的Segment的数量达到10的话还会被合并成一个更大的Segment 。直到
	 * Segment中索引的文件数量达到maxMergeDocs时不在合并。
	 * <p>
	 * maxMergeDocs(最大合并文档数)：决定了一个索引块中的最大的文档数。默认值是 Integer.MAX_VALUE 。
	 * <p>
	 * maxBufferedDocs(最大内存文档数): 控制写入一个新的segment前内存中保存的document的数目，
	 * 设置较大的数目可以加快建索引速度，默认为10。
	 * <p>
	 * maxFieldLength(Field中最大Term数目)：超过部分忽略，不会index到field中，所以自然也就搜索不到。
	 * <p>
	 * RAMBufferSizeMB(缓存大小)：与maxBufferedDocs相当，默认值为16 。
	 * 
	 * @param idxPath
	 * @return
	 */
	public IndexWriter build(String idxPath) {
		try {
			IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_33,
					analyzer);
			cfg.setRAMBufferSizeMB(64);
			IndexWriter writer = new IndexWriter(FSDirectory.open(new File(
					idxPath)), cfg);
			return writer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param f
	 *            Feature
	 * @param writer
	 *            IndexWriter
	 * @param analyzedFields
	 *            list of attributename
	 */
	public void write(Feature f, IndexWriter writer, List<String> analyzedFields) {
		if (analyzedFields == null || analyzedFields.size() == 0) {
			return;
		}
		try {
			Document doc = new Document();
			for (int i = 0; i < f.getSchema().getAttributeCount(); i++) {
				String name = f.getSchema().getAttributeName(i);
				if (analyzedFields.contains(name)) {
					doc.add(new Field(name, f.getString(i), Field.Store.YES,
							Field.Index.ANALYZED_NO_NORMS));
				} else {
					doc.add(new Field(name, f.getString(i), Field.Store.YES,
							Field.Index.NOT_ANALYZED_NO_NORMS));
				}
			}
			// geometry
			String str = f.getGeometry().toString();
			doc.add(new Field("GEOMETRY", str, Field.Store.YES,
					Field.Index.NOT_ANALYZED_NO_NORMS));
			writer.addDocument(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close(IndexWriter writer) {
		try {
			writer.optimize();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
