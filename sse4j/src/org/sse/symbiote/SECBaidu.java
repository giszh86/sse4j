package org.sse.symbiote;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class SECBaidu extends SECallable {

	public SECBaidu(String keyword) {
		super(keyword);
		init();
	}

	private void init() {
		url = "http://www.baidu.com/s?wd=";
		source = SECResult.Type.BAIDU.name();
		startSprit = "<table cellpadding=\"0\" cellspacing=\"0\" class=\"result\" id=";
		endSprit = "</td></tr></table><br>";
	}

}
