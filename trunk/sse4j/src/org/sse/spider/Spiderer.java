package org.sse.spider;

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
		ExecutorService es = Executors.newCachedThreadPool();
		Future<SECResult> baiduF = es.submit(new SECBaidu("北京海淀 香山 10号"));
		Future<SECResult> sogouF = es.submit(new SECSogou("北京海淀 香山 10号"));

		SECResult baiduR = baiduF.get();
		System.out.println(baiduR.getLinks());

		SECResult sogouR = sogouF.get();
		System.out.println(sogouR.getLinks());

		es.shutdown();
		
		String key = "北京海淀香山10号";
		String ekey = URLEncoder.encode(key, "UTF-8");
		System.out.println(ekey+"_"+URLEncoder.encode(ekey, "UTF-8"));
	}
}
