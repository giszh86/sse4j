package org.sse.symbiote;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class SECSogou extends SECallable {

	public SECSogou(String keyword) {
		super(keyword);
		init();
	}

	private void init() {
		url = "http://www.sogou.com/web?query=";
		source = SECResult.Type.SOGOU.name();
		startSprit = "<div class=\"rb\"><h3 class=\"pt\"><a";
		endSprit = "</a></div></div><!--STATUS VR OK-->";
	}

}
