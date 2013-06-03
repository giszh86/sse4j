package org.sse.idx;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.TieredMergePolicy;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.sse.geo.AttributeType;
import org.sse.geo.Feature;
import org.sse.geo.FeatureCollection;
import org.sse.idx.base.Enums.AnalyzerType;
import org.sse.squery.PtyName;

/**
 * @author dux(duxionggis@126.com)
 */
public class IdxWriter {
	private IndexWriter writer;

	public IdxWriter(String idxPath, AnalyzerType type) {
		writer = this.build(idxPath, IdxParser.getInstance().getAnalyzer(type));
	}

	/**
	 * @param fc
	 *            FeatureCollection
	 * @param analyzedFields
	 *            list of attributename
	 */
	public void create(FeatureCollection fc, List<String> analyzedFields) {
		if (writer == null)
			return;
		for (Iterator<Feature> it = fc.iterator(); it.hasNext();) {
			this.write(it.next(), analyzedFields);
		}
		this.close();
	}

	/**
	 * build index writer,parameters contains:
	 * <p>
	 * mergeFactor(合并因子): 决定把磁盘上的索引块合并成一个大的索引块的频率，默认值为10 。 比如， 10个Segment 会被合并成一个新的 Segment
	 * 。如果合并后的这个大的Segment的数量达到10的话还会被合并成一个更大的Segment 。直到 Segment中索引的文件数量达到maxMergeDocs时不在合并。
	 * <p>
	 * maxMergeDocs(最大合并文档数)：决定了一个索引块中的最大的文档数。默认值是 Integer.MAX_VALUE 。
	 * <p>
	 * maxBufferedDocs(最大内存文档数): 控制写入一个新的segment前内存中保存的document的数目， 设置较大的数目可以加快建索引速度，默认为10。
	 * <p>
	 * maxFieldLength(Field中最大Term数目)：超过部分忽略，不会index到field中，所以自然也就搜索不到。
	 * <p>
	 * RAMBufferSizeMB(缓存大小)：与maxBufferedDocs相当，默认值为16 。
	 * 
	 * @param idxPath
	 * @return
	 */
	private IndexWriter build(String idxPath, Analyzer analyzer) {
		try {
			TieredMergePolicy mergePolicy = new TieredMergePolicy();
			// mergePolicy.setNoCFSRatio(1.0);
			IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_36, analyzer);
			cfg.setRAMBufferSizeMB(128);
			cfg.setMergePolicy(mergePolicy);
			IndexWriter writer = new IndexWriter(FSDirectory.open(new File(idxPath)), cfg);
			return writer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param f
	 *            Feature
	 * @param analyzedFields
	 *            list of attributename
	 */
	public void write(Feature f, List<String> analyzedFields) {
		if (writer == null)
			return;
		try {
			boolean afNull = (analyzedFields == null || analyzedFields.size() == 0);
			Document doc = new Document();
			for (int i = 0; i < f.getSchema().getAttributeCount(); i++) {
				String name = f.getSchema().getAttributeName(i);
				if (!afNull && analyzedFields.contains(name)) {
					doc.add(new Field(name, f.getString(i), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
				} else {
					AttributeType attrib = f.getSchema().getAttributeType(i);
					if (attrib == AttributeType.STRING && afNull) {
						doc.add(new Field(name, f.getString(i), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
					} else {
						doc.add(new Field(name, f.getString(i), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
					}
				}
			}
			// geometry
			String str = f.getGeometry().toString();
			doc.add(new Field(PtyName.GID, str, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			writer.addDocument(doc);
		} catch (Exception e) {
			System.out.println("write index error:" + e);
		}
	}

	public IndexWriter getWriter() {
		return this.writer;
	}

	public void close() {
		if (writer == null)
			return;
		try {
			writer.forceMerge(1);// TODO
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
