package org.sse.idx.base;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 * IK分词器 Lucene Tokenizer适配器类扩展
 */
public final class CIKTokenizer extends Tokenizer {
	// 智能IK分词器实现
	private IKSegmenter tIKSeg;
	// 细粒度IK分词器实现
	private IKSegmenter fIKSeg;
	// 词元文本属性
	private CharTermAttribute termAtt;
	// 词元位移属性
	private OffsetAttribute offsetAtt;
	// 记录最后一个词元的结束位置
	private int finalOffset;
	// 分词判重
	private Map<String, Byte> mTerm;

	public CIKTokenizer(Reader in) {
		super(in);
		offsetAtt = addAttribute(OffsetAttribute.class);
		termAtt = addAttribute(CharTermAttribute.class);
		String sb = read(in);
		tIKSeg = new IKSegmenter(new StringReader(sb), true);
		fIKSeg = new IKSegmenter(new StringReader(sb), false);
		mTerm = new HashMap<String, Byte>();
	}

	public boolean incrementToken() throws IOException {
		// 清除所有的词元属性
		clearAttributes();
		Lexeme tnext = tIKSeg.next();
		if (tnext != null) {
			mTerm.put(tnext.getLexemeText(), Byte.MIN_VALUE);

			// 设置词元文本
			termAtt.append(tnext.getLexemeText());
			// 设置词元长度
			termAtt.setLength(tnext.getLength());
			// 设置词元位移
			offsetAtt.setOffset(tnext.getBeginPosition(), tnext.getEndPosition());
			// 记录分词的最后位置
			finalOffset = tnext.getEndPosition();
			// 返会true告知还有下个词元
			return true;
		} else {
			Lexeme fnext = fIKSeg.next();
			while (fnext != null && mTerm.containsKey(fnext.getLexemeText())) {
				fnext = fIKSeg.next();
			}
			if (fnext != null) {
				mTerm.put(fnext.getLexemeText(), Byte.MIN_VALUE);

				// 设置词元文本
				termAtt.append(fnext.getLexemeText());
				// 设置词元长度
				termAtt.setLength(fnext.getLength());
				// 设置词元位移
				offsetAtt.setOffset(fnext.getBeginPosition(), fnext.getEndPosition());
				// 记录分词的最后位置
				finalOffset = fnext.getEndPosition();
				// 返会true告知还有下个词元
				return true;
			}
		}
		// 返会false告知词元输出完毕
		return false;
	}

	public void reset(Reader input) throws IOException {
		super.reset(input);
		String sb = read(input);
		tIKSeg.reset(new StringReader(sb));
		fIKSeg.reset(new StringReader(sb));
		mTerm.clear();
		finalOffset = 0;
	}

	public final void end() {
		// set final offset
		offsetAtt.setOffset(finalOffset, finalOffset);
	}

	private String read(Reader in) {
		StringBuffer sb = new StringBuffer();
		char[] chars = new char[128];
		try {
			int len = in.read(chars);
			while (len != -1) {
				sb.append(chars);
				chars = new char[128];
				len = in.read(chars);
			}
		} catch (IOException e) {
		}
		return sb.toString().trim();
	}
}
