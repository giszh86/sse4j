package org.sse.idx.base;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

/**
 * IK分词器扩展,用于写索引
 */
public final class CIKAnalyzer extends Analyzer {

	public CIKAnalyzer() {
	}

	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new CIKTokenizer(reader);
	}

	public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
		Tokenizer _IKTokenizer = (Tokenizer) getPreviousTokenStream();
		if (_IKTokenizer == null) {
			_IKTokenizer = new CIKTokenizer(reader);
			setPreviousTokenStream(_IKTokenizer);
		} else {
			_IKTokenizer.reset(reader);
		}
		return _IKTokenizer;
	}
}
