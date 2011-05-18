package org.sse.spider;

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
		Future<SECResult> baiduF = es.submit(new SECBaidu("大厦"));
		Future<SECResult> sogouF = es.submit(new SECSogou("大厦"));

		SECResult baiduR = baiduF.get();
		System.out.println(baiduR.getLinks().size());

		SECResult sogouR = sogouF.get();
		System.out.println(sogouR.getLinks().size());

		es.shutdown();
	}
}
