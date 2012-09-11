package org.sse.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.NIOFSDirectory;

/**
 * @author dux(duxionggis@126.com)
 */
public class IdxReader {
	private IndexSearcher[] mis = null;

	/**
	 * @param idxPath
	 *            support multi-paths separated by ','
	 * @throws IOException
	 */
	public IdxReader(String idxPath) throws IOException {
		this(idxPath.split(","), 4);
	}

	/**
	 * @param idxPaths
	 *            lucene path
	 * @param threads
	 *            >0 && <=64
	 * @throws IOException
	 */
	public IdxReader(String[] idxPaths, int threads) throws IOException {
		if (threads <= 0 || threads > 64) {
			throw new IOException("threads>0 && threads<=64");
		}
		mis = new IndexSearcher[threads];
		for (int j = 0; j < threads; j++) {
			IndexReader[] readers = new IndexReader[idxPaths.length];
			for (int i = 0; i < idxPaths.length; i++) {
				readers[i] = IndexReader.open(NIOFSDirectory.open(new File(idxPaths[i])));
			}
			mis[j] = new IndexSearcher(new MultiReader(readers, false));
		}
	}

	// /**
	// *
	// * @return MultiReader object
	// */
	// public IndexReader getReader() {
	// int idx = (int) (System.currentTimeMillis() % mis.length);
	// return mis[idx].getIndexReader();
	// }

	/**
	 * @param index
	 * @return MultiReader object
	 */
	public IndexReader getReader(int index) {
		if (index < 0)
			index = 0;
		return mis[index % mis.length].getIndexReader();
	}

	public void close() {
		try {
			for (IndexSearcher s : mis)
				s.close();
		} catch (IOException e) {
		}
	}

	/**
	 * <p>
	 * TermQuery:条件查询
	 * <p>
	 * MultiTermQuery:同一关键字多字段查询 MultiFieldQueryParser.parse("我",new String[] {"title","content"},analyzer)
	 * <p>
	 * WildcardQuery:语义查询（通配符查询）new WildcardQuery(new Term("sender","*davy*"))
	 * <p>
	 * PhraseQuery:短语查询
	 * <p>
	 * MultiPhraseQuery:多字段短语查询
	 * <p>
	 * PrefixQuery:前缀查询
	 * <p>
	 * PhrasePrefixQuery:短语前缀查询
	 * <p>
	 * RangeQuery:范围查询
	 * <p>
	 * SpanQuery:范围查询
	 * <p>
	 * BooleanQuery:与或查询
	 * <p>
	 * BoostingQuery:
	 * <p>
	 * FuzzyQuery:模糊查询
	 * <p>
	 * FuzzyLikeThisQuery:
	 * <p>
	 * MoreLikeThisQuery:
	 * 
	 * @param query
	 * @param filter
	 * @param count
	 * @return
	 */
	public List<Document> query(Query query, Filter filter, int count) {
		if (count < 1)
			count = 50;
		List<Document> docs = new ArrayList<Document>();
		try {
			int idx = (int) (System.currentTimeMillis() % mis.length);
			// mis[idx].search(query, filter, n, sort); // Sort(SortField())
			TopDocs result = mis[idx].search(query, filter, count);
			// result.totalHits;
			for (ScoreDoc doc : result.scoreDocs) {
				docs.add(mis[idx].doc(doc.doc));
			}
			filter = null;
			query = null;
			return docs;
		} catch (IOException e) {
		} finally {
		}
		return null;
	}

	/**
	 * term query
	 * 
	 * @param terms
	 * @return
	 */
	public List<Document> query(List<Term> terms) {
		if (terms == null || terms.size() == 0)
			return null;
		try {
			int idx = (int) (System.currentTimeMillis() % mis.length);
			IndexReader mReader = getReader(idx);
			List<Document> docs = new ArrayList<Document>();
			for (Iterator<Term> i = terms.iterator(); i.hasNext();) {
				Term term = i.next();
				if (term != null) {
					TermDocs td = mReader.termDocs(term);
					while (td.next())
						docs.add(mReader.document(td.doc()));
					td.close();
				}
			}
			terms = null;
			return docs;
		} catch (IOException e) {
		}
		return null;
	}
}