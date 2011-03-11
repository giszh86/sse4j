package org.sse.squery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermsFilter;
import org.sse.io.IdxParser;
import org.sse.io.IdxReader;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
class SQuery {
	private IdxReader reader;
	private SpatialIndex idx;
	private Envelope extent;

	SQuery(IdxReader reader, SpatialIndex idx, Envelope extent) {
		this.reader = reader;
		this.idx = idx;
		this.extent = extent;
	}

	List<Document> query(Filter filter) {
		if (filter == null)
			return null;
		if (filter.getProperties() == null && filter.getGeometry() == null)
			return null;

		List ids = null;
		if (filter.getGeometry() != null) {
			Envelope extent = filter.getGeometry().getEnvelopeInternal();
			ids = idx.query(extent);
		}

		if (filter.getProperties() == null
				|| filter.getProperties().size() == 0) {
			List<Term> terms = new ArrayList<Term>();
			if (ids != null && ids.size() > 0) {
				for (Iterator i = ids.iterator(); i.hasNext();) {
					terms.add(new Term(PtyName.OID, i.next().toString()));
				}
			}
			ids = null;
			return reader.query(terms);
		}

		List<Term> terms = new ArrayList<Term>();
		for (Iterator<Property> i = filter.getProperties().iterator(); i
				.hasNext();) {
			Property term = i.next();
			if (term != null)
				terms.add(new Term(term.getField(), term.getText()));
		}
		TermsFilter tfilter = null;
		if (ids != null && ids.size() > 0) {
			tfilter = new TermsFilter();
			for (Iterator i = ids.iterator(); i.hasNext();) {
				tfilter.addTerm(new Term(PtyName.OID, i.next().toString()));
			}
		}
		ids = null;
		Query query = IdxParser.getInstance().createQuery(filter.getQtype(),
				filter.getOtype(), terms);
		return reader.query(query, tfilter, filter.getCount());
	}

	List<Document> query(Query query, int count) {
		return reader.query(query, null, count);
	}

	List<Document> query(List<Term> terms) {
		return reader.query(terms);
	}

	List box(Envelope env) {
		return idx.query(env);
	}

	Envelope getExtent() {
		return extent;
	}

	IdxReader getReader() {
		return reader;
	}

	SpatialIndex getIdx() {
		return idx;
	}
}
