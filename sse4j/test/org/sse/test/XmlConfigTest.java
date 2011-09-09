package org.sse.test;

import org.sse.NaviConfig;

public class XmlConfigTest {
	public static void main(String[] args) throws Exception {
		System.out.println("WGS:" + NaviConfig.WGS);
		NaviConfig.init();
		System.out.println("WGS:" + NaviConfig.WGS);
	}
}