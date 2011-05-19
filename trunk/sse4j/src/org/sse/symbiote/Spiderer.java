package org.sse.symbiote;

import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Spiderer {
	public static void main(String[] args) throws Exception {
		String key = "海淀香山";
		String ekey = URLEncoder.encode(key, "UTF-8");
		// new SECGoogle(ekey).call();

		ExecutorService es = Executors.newCachedThreadPool();
		Future<SECResult> baiduF = es.submit(new SECBaidu(ekey));
		Future<SECResult> sogouF = es.submit(new SECSogou(ekey));
		Future<SECResult> youdaoF = es.submit(new SECYoudao(ekey));

		SECResult baiduR = baiduF.get();
		System.out.println(baiduR.getLinks().size() + "_" + baiduR.getLinks());

		SECResult sogouR = sogouF.get();
		System.out.println(sogouR.getLinks().size() + "_" + sogouR.getLinks());

		SECResult youdaoR = youdaoF.get();
		System.out
				.println(youdaoR.getLinks().size() + "_" + youdaoR.getLinks());

		es.shutdown();

		System.out.println(ekey + "_" + URLEncoder.encode(ekey, "UTF-8"));
	}
}
