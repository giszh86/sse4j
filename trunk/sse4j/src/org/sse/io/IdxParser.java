package org.sse.io;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	private static Object lock = new Object();

	public static IdxParser getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new IdxParser();
				}
			}
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
		List<String> texts = new ArrayList<String>(terms.size());
		List<String> fields = new ArrayList<String>(terms.size());
		for (Iterator<Term> i = terms.iterator(); i.hasNext();) {
			Term term = i.next();
			if (term != null) {
				texts.add(term.text());
				fields.add(term.field());
			}
		}
		if (texts.size() == 0)
			return null;

		if (qtype == QueryType.Fuzzy) {
			FuzzyLikeThisQuery query = new FuzzyLikeThisQuery(3, analyzer);
			for (int i = 0; i < fields.size(); i++) {
				query.addTerms(texts.get(i), fields.get(i), 0.7f, 0);
			}
			texts.clear();
			fields.clear();
			return query;
		} else if (qtype == QueryType.IK) {
			BooleanClause.Occur[] flags = new BooleanClause.Occur[fields.size()];
			for (int i = 0; i < flags.length; i++)
				if (otype == OccurType.And)
					flags[i] = BooleanClause.Occur.MUST;
				else if (otype == OccurType.Or)
					flags[i] = BooleanClause.Occur.SHOULD;
				else
					flags[i] = BooleanClause.Occur.MUST_NOT;
			try {
				IKQueryParser.setMaxWordLength(true); // TODO
				return IKQueryParser.parseMultiField(fields
						.toArray(new String[fields.size()]), texts
						.toArray(new String[texts.size()]), flags);
			} catch (IOException e) {
				return null;
			} finally {
				texts.clear();
				fields.clear();
			}
		} else {
			BooleanClause.Occur[] flags = new BooleanClause.Occur[fields.size()];
			for (int i = 0; i < flags.length; i++)
				if (otype == OccurType.And)
					flags[i] = BooleanClause.Occur.MUST;
				else if (otype == OccurType.Or)
					flags[i] = BooleanClause.Occur.SHOULD;
				else
					flags[i] = BooleanClause.Occur.MUST_NOT;
			try {
				return QueryParserUtil.parse(texts.toArray(new String[texts
						.size()]), fields.toArray(new String[fields.size()]),
						flags, analyzer);
			} catch (QueryNodeException e) {
				return null;
			} finally {
				texts.clear();
				fields.clear();
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
