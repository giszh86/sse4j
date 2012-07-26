package org.sse.test.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.sse.squery.Property;
import org.sse.io.IdxParser;
import org.sse.io.IdxReader;
import org.sse.io.Enums.AnalyzerType;
import org.sse.io.Enums.OccurType;
import org.sse.io.Enums.QueryType;

public class IdxReaderTest {
	public static void main(String[] args) throws Exception {
		IdxReader idx = new IdxReader("data/idx/110000/Poi");
		// query(idx);
		simple(idx.getReader(0));
		// terms(idx);
		idx.close();
	}

	static void query(IdxReader idx) throws IOException {
		List<Document> docs;
		List<Property> terms = new ArrayList<Property>();

		Date date1 = new Date();
		String addr = IdxParser.getInstance().tokenize("香山南路40号",
				AnalyzerType.SmartCN);
		System.out.println(addr + "----1---"
				+ ((new Date()).getTime() - date1.getTime()));

		addr = IdxParser.getInstance().tokenize("香山南路 40号", AnalyzerType.IK);
		System.out.println(addr + "----2--"
				+ ((new Date()).getTime() - date1.getTime()));

		terms.add(new Property("ADDRESS", "XS", OccurType.And));
		terms.add(new Property("NAMEC", "XS", OccurType.And));
		terms.add(new Property("NAMEP", "XS", OccurType.And));

		Query query1 = IdxParser.getInstance().createQuery(QueryType.Standard,
				IdxParser.getInstance().getAnalyzer(AnalyzerType.SmartCN),
				terms);
		docs = idx.query(query1, null, 20);
		for (Document doc : docs) {
			System.out.println(doc.get("NAMEC") + "--" + doc.get("ADDRESS")
					+ "--" + doc.get("GEOMETRY"));
		}
		System.out.println("-----------------And:"
				+ ((new Date()).getTime() - date1.getTime()));

		date1 = new Date();
		for (int i = 0; i < terms.size(); i++) {
			terms.get(i).setOtype(OccurType.Or);
		}
		query1 = IdxParser.getInstance().createQuery(QueryType.Standard,
				IdxParser.getInstance().getAnalyzer(AnalyzerType.SmartCN),
				terms);
		docs = idx.query(query1, null, 20);
		for (Document doc : docs) {
			System.out.println(doc.get("NAMEC") + "--" + doc.get("ADDRESS")
					+ "--" + doc.get("GEOMETRY"));
		}
		System.out.println("-----------------Or:"
				+ ((new Date()).getTime() - date1.getTime()));

		date1 = new Date();
		for (int i = 0; i < terms.size(); i++) {
			terms.get(i).setOtype(OccurType.Or);
		}
		query1 = IdxParser.getInstance().createQuery(QueryType.Fuzzy,
				IdxParser.getInstance().getAnalyzer(AnalyzerType.SmartCN),
				terms);
		docs = idx.query(query1, null, 20);
		for (Document doc : docs) {
			System.out.println(doc.get("NAMEC") + "--" + doc.get("ADDRESS")
					+ "--" + doc.get("GEOMETRY"));
		}
		System.out.println("-----------------FuzzyLike:"
				+ ((new Date()).getTime() - date1.getTime()));
	}

	static void terms(IdxReader idx) {
		List<Document> docs;
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Term("ID", "100"));
		terms.add(new Term("ID", "110"));
		// terms.add(new Term("KIND", "2050201"));
		docs = idx.query(terms);
		for (Document doc : docs) {
			System.out.println(doc.get("NAMEC") + "--" + doc.get("ADDRESS")
					+ "--" + doc.get("GEOMETRY"));
		}
		System.out.println("-----------------termQuery");
	}

	static void simple(IndexReader reader) throws ParseException, IOException,
			CorruptIndexException {
		IndexSearcher searcher = new IndexSearcher(reader);

		QueryParser parser = new QueryParser(Version.LUCENE_36, "ID",
				new SmartChineseAnalyzer(Version.LUCENE_36, true));
		Query query = parser.parse("[1 TO 20]");
		TopDocs result = searcher.search(query, 20);
		for (ScoreDoc doc : result.scoreDocs) {
			System.out.println(reader.document(doc.doc).get("ID") + "--"
					+ reader.document(doc.doc).get("NAMEC") + "--"
					+ reader.document(doc.doc).get("ADDRESS") + "--"
					+ reader.document(doc.doc).get("GEOMETRY") + "--"
					+ doc.score);
		}
		System.out.println("-----------------simple");
	}
}
