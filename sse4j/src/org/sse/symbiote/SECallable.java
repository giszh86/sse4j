package org.sse.symbiote;

import java.util.concurrent.Callable;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
abstract class SECallable implements Callable<SECResult> {
	public final static String charset = "GB2312";
	private String keyword = "";

	SECallable() {
	}

	SECallable(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public SECResult call() throws Exception {
		throw new Exception("unimplement function!");
	}

}
