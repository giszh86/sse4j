package org.sse.idx;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.Version;
import org.sse.squery.PtyName;
import org.sse.squery.Property;
import org.sse.idx.base.CIKAnalyzer;
import org.sse.idx.base.Enums.AnalyzerType;
import org.sse.idx.base.Enums.OccurType;
import org.wltea.analyzer.lucene.IKAnalyzer;
import com.vividsolutions.jts.geom.Envelope;

/**
 * @author dux(duxionggis@126.com)
 */
public class IdxParser {
	private Analyzer scAnalyzer;
	private Analyzer ikAnalyzer;
	private Analyzer stAnalyzer;

	private static IdxParser instance;
	private static Lock lock = new ReentrantLock();

	public static IdxParser getInstance() {
		if (instance == null) {
			lock.lock();
			instance = new IdxParser();
			lock.unlock();
		}
		return instance;
	}

	private IdxParser() {
		scAnalyzer = new CIKAnalyzer();
		ikAnalyzer = new IKAnalyzer(true);
		stAnalyzer = new StandardAnalyzer(Version.LUCENE_36);
	}

	/**
	 * build analyzer
	 * 
	 * @param type
	 * @return
	 */
	public Analyzer getAnalyzer(AnalyzerType type) {
		if (type == AnalyzerType.CN) {
			return scAnalyzer;
		} else if (type == AnalyzerType.IK) {
			return ikAnalyzer;
		} else {
			return stAnalyzer;
		}
	}

	public List<String> tokenize(String word, AnalyzerType type) throws IOException {
		List<String> tokens = new ArrayList<String>();
		TokenStream ts = getAnalyzer(type).tokenStream("word", new StringReader(word));
		CharTermAttribute termAtt = ts.getAttribute(CharTermAttribute.class);
		while (ts.incrementToken()) {
			tokens.add(termAtt.toString());
		}
		ts.close();
		return tokens;
	}

	/**
	 * create query using QueryParser
	 * 
	 * @param type
	 *            AnalyzerType
	 * @param terms
	 *            List<Property>
	 * @return
	 */
	public Query createQuery(AnalyzerType type, List<Property> terms) {
		if (terms == null)
			return null;

		BooleanQuery query = new BooleanQuery();
		for (Iterator<Property> it = terms.iterator(); it.hasNext();) {
			Property term = it.next();
			BooleanClause.Occur occur = toOccurType(term.getOtype());

			Query q = this.createQuery(type, term, true);
			if ((q == null) || ((q instanceof BooleanQuery) && (((BooleanQuery) q).clauses().size() == 0)))
				continue;

			query.add(q, occur);
		}
		return (query.clauses().size() == 0 ? null : query);
	}

	/**
	 * create single query using QueryParser
	 * 
	 * @param type
	 *            AnalyzerType
	 * @param term
	 *            Property
	 * @param useAnd
	 *            QueryParser.AND_OPERATOR or not
	 * @return
	 */
	public Query createQuery(AnalyzerType type, Property term, boolean useAnd) {
		if (term == null)
			return null;

		QueryParser parser = new QueryParser(Version.LUCENE_36, term.getField(), getAnalyzer(type));
		if (useAnd) {
			parser.setDefaultOperator(QueryParser.AND_OPERATOR);
		}
		try {
			return parser.parse(term.getText());
		} catch (ParseException e) {
			try {
				OccurType otype = (useAnd ? OccurType.AND : OccurType.OR);
				return this.createQuery(term.getField(), this.tokenize(term.getText(), type), otype);
			} catch (IOException ee) {
				return null;
			}
		}
	}

	/**
	 * create query by TermQuery
	 * 
	 * @param field
	 *            String
	 * @param tokens
	 *            List<String>
	 * @param otype
	 *            OccurType
	 * @return
	 */
	public BooleanQuery createQuery(String field, List<String> tokens, OccurType otype) {
		if (tokens == null)
			return null;
		BooleanQuery bQuery = new BooleanQuery();
		BooleanClause.Occur occur = toOccurType(otype);
		for (String t : tokens) {
			if (t != null && !t.isEmpty()) {
				bQuery.add(new TermQuery(new Term(field, t)), occur);
			}
		}
		return bQuery;
	}

	public static void spatialQuery(Envelope envelope, BooleanQuery bQuery) {
		Query minxQ = new TermRangeQuery(PtyName.CENX, String.valueOf((int) Math.floor(envelope.getMinX())), String.valueOf((int) Math
				.round(envelope.getMaxX())), true, true);
		Query minyQ = new TermRangeQuery(PtyName.CENY, String.valueOf((int) Math.floor(envelope.getMinY())), String.valueOf((int) Math
				.round(envelope.getMaxY())), true, true);
		bQuery.add(minxQ, BooleanClause.Occur.MUST);
		bQuery.add(minyQ, BooleanClause.Occur.MUST);
	}

	private BooleanClause.Occur toOccurType(OccurType otype) {
		BooleanClause.Occur occur;
		if (otype == OccurType.AND) {
			occur = BooleanClause.Occur.MUST;
		} else if (otype == OccurType.OR) {
			occur = BooleanClause.Occur.SHOULD;
		} else {
			occur = BooleanClause.Occur.MUST_NOT;
		}
		return occur;
	}

}
