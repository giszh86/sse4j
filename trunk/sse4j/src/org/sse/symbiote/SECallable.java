package org.sse.symbiote;

import java.util.concurrent.Callable;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
abstract class SECallable implements Callable<SECResult> {
	protected final static String CHARSET_GB = "GBK";
	protected final static String CHARSET_UTF = "UTF-8";
	protected String keyword = "";
	protected String url = "";
	protected String startSprit = "";
	protected String endSprit = "";

	SECallable(String keyword) {
		this.keyword = keyword;
	}

	public SECResult call() throws Exception {
		throw new Exception("unimplement function!");
	}

	protected SECResult.Item buildItem(String link) throws Exception {
		throw new Exception("unimplement function!");
	}

}
