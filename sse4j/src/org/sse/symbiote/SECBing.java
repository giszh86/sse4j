package org.sse.symbiote;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class SECBing extends SECallable {

	public SECBing(String keyword) {
		super(keyword);
		init();
	}

	private void init() {
		url = "http://cn.bing.com/search?q=##&intlF=1&FORM=R5FD";// 海外版
		source = SECResult.Type.BING.name();
		startSprit = "";
		endSprit = "";
	}

}
