package org.sse.test;

import java.net.URL;
import java.net.URLEncoder;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class HtmlcleanerTest {

	public static void main(String[] args) throws Exception {
		String key = "海淀";
		String ekey = URLEncoder.encode(key, "UTF-8");
		String url = "http://www.baidu.com/s?wd=" + ekey;
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(new URL(url), "GBK");
		System.out.println(node);
	}

}
