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
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.standard.QueryParserUtil;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.FuzzyLikeThisQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.sse.io.Enums.AnalyzerType;
import org.sse.io.Enums.OccurType;
import org.sse.io.Enums.QueryType;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
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
		scAnalyzer = new SmartChineseAnalyzer(Version.LUCENE_31, true);
		ikAnalyzer = new IKAnalyzer(true);
		stAnalyzer = new StandardAnalyzer(Version.LUCENE_31);
	}

	/**
	 * build analyzer
	 * 
	 * @param type
	 * @return
	 */
	public Analyzer getAnalyzer(AnalyzerType type) {
		if (type == AnalyzerType.SmartCN) {
			return scAnalyzer;
		} else if (type == AnalyzerType.IK) {
			return ikAnalyzer;
		} else {
			return stAnalyzer;
		}

		// if (type == AnalyzerType.SmartCN) {
		// return new SmartChineseAnalyzer(Version.LUCENE_31, true);
		// } else if (type == AnalyzerType.IK) {
		// return new IKAnalyzer(true);
		// } else {
		// return new StandardAnalyzer(Version.LUCENE_31);
		// }
	}

	public String tokenize(String word, AnalyzerType type) throws IOException {
		TokenStream ts = getAnalyzer(type).tokenStream("word",
				new StringReader(word));
		CharTermAttribute termAtt = ts.getAttribute(CharTermAttribute.class);
		StringBuffer sb = new StringBuffer();
		while (ts.incrementToken()) {
			sb.append(termAtt.toString()).append(" ");
		}
		ts.close();
		return sb.toString().trim();
	}

	public Query createQuery(QueryType qtype, OccurType otype,
			Analyzer analyzer, List<Term> terms) {
		if (terms == null)
			return null;
		BooleanClause.Occur occur;
		if (otype == OccurType.And) {
			occur = BooleanClause.Occur.MUST;
		} else if (otype == OccurType.Or) {
			occur = BooleanClause.Occur.SHOULD;
		} else {
			occur = BooleanClause.Occur.MUST_NOT;
		}

		List<String> texts = new ArrayList<String>(terms.size());
		List<String> fields = new ArrayList<String>(terms.size());
		List<BooleanClause.Occur> flags = new ArrayList<BooleanClause.Occur>(
				terms.size());
		for (Iterator<Term> i = terms.iterator(); i.hasNext();) {
			Term term = i.next();
			if (term != null) {
				texts.add(term.text());
				fields.add(term.field());
				flags.add(occur);
			}
		}
		if (fields.size() == 0)
			return null;

		if (qtype == QueryType.Fuzzy) {
			FuzzyLikeThisQuery query = new FuzzyLikeThisQuery(5, analyzer);
			for (int i = 0; i < fields.size(); i++) {
				query.addTerms(texts.get(i), fields.get(i), 0.7f, 0);
			}
			return query;
		} else if (qtype == QueryType.IK) {
			try {
				return IKQueryParser.parseMultiField(fields
						.toArray(new String[fields.size()]), texts
						.toArray(new String[texts.size()]), flags
						.toArray(new BooleanClause.Occur[flags.size()]));
			} catch (IOException e) {
				return null;
			}
		} else {
			try {
				return QueryParserUtil.parse(texts.toArray(new String[texts
						.size()]), fields.toArray(new String[fields.size()]),
						flags.toArray(new BooleanClause.Occur[flags.size()]),
						analyzer);
			} catch (QueryNodeException e) {
				return null;
			}
		}
	}

	public Query createQuery(QueryType qtype, OccurType otype, List<Term> terms) {
		if (qtype == QueryType.IK) {
			return createQuery(qtype, otype, getAnalyzer(AnalyzerType.IK),
					terms);
		} else {
			return createQuery(qtype, otype, getAnalyzer(AnalyzerType.SmartCN),
					terms);
		}
	}
}
