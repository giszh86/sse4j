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
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.ParallelMultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermsFilter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class IdxReader {
	private MultiReader mr = null;
	private boolean useParal = false;

	/**
	 * 
	 * @param idxPath
	 *            support multi-paths separated by ','
	 * @throws IOException
	 */
	public IdxReader(String idxPath) throws IOException {
		this(idxPath.split(","), false);
	}

	public IdxReader(String[] idxPaths, boolean useParallel) throws IOException {
		IndexReader[] readers = new IndexReader[idxPaths.length];
		for (int i = 0; i < idxPaths.length; i++)
			readers[i] = IndexReader.open(FSDirectory
					.open(new File(idxPaths[i])), true);
		mr = new MultiReader(readers, true);
		useParal = useParallel;
	}

	/**
	 * 
	 * @return MultiReader object
	 */
	public IndexReader getReader() {
		return mr;
	}

	/**
	 * using multi thread
	 * 
	 * @param useParallel
	 */
	public void useParallel(boolean useParallel) {
		useParal = useParallel;
	}

	public void close() {
		try {
			mr.close();
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
		if (mr == null)
			return null;
		if (count < 1)
			count = 50;
		List<Document> docs = new ArrayList<Document>();
		try {
			Searcher s = null;
			IndexReader[] readers = mr.getSequentialSubReaders();
			if (readers.length == 1) {
				s = new IndexSearcher(readers[0]);
			} else {
				IndexSearcher[] searchers = new IndexSearcher[readers.length];
				for (int i = 0; i < searchers.length; i++)
					searchers[i] = new IndexSearcher(readers[i]);
				if (!useParal) {
					// result contains same object
					s = new MultiSearcher(searchers);
				} else {
					// delete same object from result
					s = new ParallelMultiSearcher(searchers);
				}
			}
			TopDocs result = s.search(query.weight(s), filter, count);
			for (ScoreDoc doc : result.scoreDocs) {
				docs.add(mr.document(doc.doc));
			}
			s.close();
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
		if (mr == null || terms == null)
			return null;
		try {
			TermsFilter tfilter = new TermsFilter();
			for (Iterator<Term> i = terms.iterator(); i.hasNext();) {
				Term term = i.next();
				if (term != null)
					tfilter.addTerm(term);
			}
			List<Document> docs = new ArrayList<Document>();
			DocIdSet set = tfilter.getDocIdSet(mr);
			DocIdSetIterator di = set.iterator();
			while (di.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
				docs.add(mr.document(di.docID()));
			}
			tfilter = null;
			terms = null;
			return docs;
		} catch (IOException e) {
		} finally {
		}
		return null;
	}
}
