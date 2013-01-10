package org.sse.io;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.Version;
import org.sse.squery.PtyName;
import org.sse.squery.Property;
import org.sse.io.Enums.AnalyzerType;
import org.sse.io.Enums.OccurType;
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
		scAnalyzer = new SmartChineseAnalyzer(Version.LUCENE_36, true);
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
		if (type == AnalyzerType.SMARTCN) {
			return scAnalyzer;
		} else if (type == AnalyzerType.IK) {
			return ikAnalyzer;
		} else {
			return stAnalyzer;
		}
	}

	public String tokenize(String word, AnalyzerType type) throws IOException {
		TokenStream ts = getAnalyzer(type).tokenStream("word", new StringReader(word));
		CharTermAttribute termAtt = ts.getAttribute(CharTermAttribute.class);
		StringBuffer sb = new StringBuffer();
		while (ts.incrementToken()) {
			sb.append(termAtt.toString()).append(" ");
		}
		ts.close();
		return sb.toString().trim();
	}

	public Query createQuery(AnalyzerType type, List<Property> terms) {
		if (terms == null)
			return null;

		List<String> texts = new ArrayList<String>(terms.size());
		List<String> fields = new ArrayList<String>(terms.size());
		List<BooleanClause.Occur> flags = new ArrayList<BooleanClause.Occur>(terms.size());
		for (Iterator<Property> it = terms.iterator(); it.hasNext();) {
			Property term = it.next();
			if (term != null) {
				texts.add(term.getText());
				fields.add(term.getField());
				flags.add(this.toOccurType(term.getOtype()));
			}
		}
		if (fields.size() == 0)
			return null;

		try {
			return MultiFieldQueryParser.parse(Version.LUCENE_36, texts.toArray(new String[texts.size()]),
					fields.toArray(new String[fields.size()]), flags.toArray(new BooleanClause.Occur[flags.size()]),
					getAnalyzer(type));
		} catch (ParseException e) {
			return null;
		}
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
