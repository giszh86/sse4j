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
		Future<SECResult> baiduResult = es.submit(new SECBaidu("北京大厦"));
		Future<SECResult> sogouResult = es.submit(new SECSogou("sse4j"));
		es.shutdown();
	}
}
