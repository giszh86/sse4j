package org.sse.squery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FieldCacheTermsFilter;
import org.apache.lucene.search.Query;
import org.sse.io.IdxParser;
import org.sse.io.IdxReader;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author dux(duxionggis@126.com)
 */
class SQuery {
	private IdxReader reader;
	private STree tree;

	SQuery(IdxReader reader, STree tree) {
		this.reader = reader;
		this.tree = tree;
	}

	List<Document> query(Filter filter) {
		if (filter == null)
			return null;
		if (filter.getProperties() == null && filter.getGeometry() == null)
			return null;

		BooleanQuery bQuery = new BooleanQuery();
		Query tQuery = IdxParser.getInstance().createQuery(filter.getType(), filter.getProperties());
		if (tQuery != null) {
			if (tQuery instanceof BooleanQuery) {
				bQuery = (BooleanQuery) tQuery;
			} else {
				bQuery.add(tQuery, BooleanClause.Occur.MUST);
			}
		}

		FieldCacheTermsFilter tfilter = null;
		if (filter.getGeometry() != null) {
			Envelope extent = filter.getGeometry().getEnvelopeInternal();
			if (tree != null) {
				List ids = tree.spatialFilter(extent);
				if (ids != null && ids.size() > 0) {
					if (tQuery == null) {
						List<Term> tterms = new ArrayList<Term>(ids.size());
						for (Iterator i = ids.iterator(); i.hasNext();) {
							tterms.add(new Term(PtyName.OID, i.next().toString()));
						}
						return reader.query(tterms);
					} else {
						String[] terms = new String[ids.size()];
						for (int i = 0; i < terms.length; i++) {
							terms[i] = ids.get(i).toString();
						}
						tfilter = new FieldCacheTermsFilter(PtyName.OID, terms);
					}
				}
			} else {
				IdxParser.spatialQuery(extent, bQuery);
			}
		}

		return reader.query(bQuery, tfilter, filter.getCount());
	}

	List<Document> query(Query query, int count) {
		return reader.query(query, null, count);
	}

	List<Document> query(List<Term> terms) {
		return reader.query(terms);
	}

	IdxReader getReader() {
		return reader;
	}

	Envelope getExtent() {
		if (tree != null) {
			return tree.getExtent();
		}
		return null;
	}

	List spatialFilter(Envelope envelope) {
		if (tree != null) {
			return tree.spatialFilter(envelope);
		} else {
			BooleanQuery bQuery = new BooleanQuery();
			IdxParser.spatialQuery(envelope, bQuery);
			List<Document> docs = this.query(bQuery, 1000);// TODO
			List<Integer> ids = new ArrayList<Integer>(docs.size());
			for (Document doc : docs) {
				ids.add(Integer.valueOf(doc.get(PtyName.OID)));
			}
			return ids;
		}
	}

}
