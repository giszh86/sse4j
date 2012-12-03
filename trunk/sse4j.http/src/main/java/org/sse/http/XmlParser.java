package org.sse.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 * @author dux(duxionggis@126.com)
 */
class XmlParser {
	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	public static Document getDocument(String xml) {
		try {
			return factory.newDocumentBuilder().parse(
					new BufferedInputStream(new ByteArrayInputStream(xml.getBytes(GZipWriter.charset))));
		} catch (Exception ex) {
			return null;
		}
	}

	public static Document getDocument(InputStream is) {
		try {
			return factory.newDocumentBuilder().parse(is);
		} catch (Exception ex) {
			return null;
		}
	}
}
