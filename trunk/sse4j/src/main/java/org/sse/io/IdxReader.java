package org.sse.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class IdxReader {
	private IndexReader mReader = null;
	private IndexSearcher mSearcher = null;
	private ExecutorService es = null;

	/**
	 * 
	 * @param idxPath
	 *            support multi-paths separated by ','
	 * @throws IOException
	 */
	public IdxReader(String idxPath) throws IOException {
		this(idxPath.split(","), true);
	}

	public IdxReader(String[] idxPaths, boolean win) throws IOException {
		IndexReader[] readers = new IndexReader[idxPaths.length];
		for (int i = 0; i < idxPaths.length; i++) {
			// use NIOFSDirectory under linux
			if (win) {
				readers[i] = IndexReader.open(FSDirectory.open(new File(
						idxPaths[i])));
			} else {
				readers[i] = IndexReader.open(NIOFSDirectory.open(new File(
						idxPaths[i])));
			}
		}

		mReader = new MultiReader(readers, false);
		if (readers.length > 1) {
			es = Executors.newCachedThreadPool();
			mSearcher = new IndexSearcher(mReader, es);
		} else {
			mSearcher = new IndexSearcher(mReader);
		}
	}

	/**
	 * 
	 * @return MultiReader object
	 */
	public IndexReader getReader() {
		return mReader;
	}

	public void close() {
		try {
			mSearcher.close();
			mReader.close();
			if (es != null)
				es.shutdown();
		} catch (IOException e) {
		}
	}

	/**
	 * <p>
	 * TermQuery:条件查询
	 * <p>
	 * MultiTermQuery:同一关键字多字段查询 MultiFieldQueryParser.parse("我",new String[]
	 * {"title","content"},analyzer)
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
		if (mReader == null)
			return null;
		if (count < 1)
			count = 50;
		List<Document> docs = new ArrayList<Document>();
		try {
			// mSearcher.search(query, filter, n, sort); // Sort(SortField())
			TopDocs result = mSearcher.search(query, filter, count);
			for (ScoreDoc doc : result.scoreDocs) {
				docs.add(mReader.document(doc.doc));
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
		if (mReader == null || terms == null)
			return null;
		try {
			List<Document> docs = new ArrayList<Document>();
			for (Iterator<Term> i = terms.iterator(); i.hasNext();) {
				Term term = i.next();
				if (term != null) {
					TermDocs td = mReader.termDocs(term);
					while (td.next())
						docs.add(mReader.document(td.doc()));
				}
			}
			terms = null;
			return docs;
		} catch (IOException e) {
		}
		return null;
	}
}
