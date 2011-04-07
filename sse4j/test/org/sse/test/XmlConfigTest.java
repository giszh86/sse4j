package org.sse.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class XmlConfigTest {
	public static void main(String[] args) throws XMLStreamException,
			FileNotFoundException {
		System.out.println(getMap("netes", "net"));
		getMap("poies", "poi");
		getMap("buses", "bus");
	}

	public static Map<String, Map<String, String>> getMap(String root,
			String element) throws XMLStreamException, FileNotFoundException {
		Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		XMLEventReader reader = null;
		try {
			reader = XMLInputFactory.newInstance().createXMLEventReader(
					new BufferedInputStream(new FileInputStream(new File(
							"cfg/navi.xml"))), "UTF-8");
			while (reader.hasNext()) {
				XMLEvent e = reader.nextEvent();
				String start = null;
				if (e.isStartElement())
					start = e.asStartElement().getName().getLocalPart();
				if (start != null && start.equals(root)) {
					while (reader.hasNext()) {
						e = reader.nextEvent();
						if (e.isStartElement()
								&& e.asStartElement().getName().getLocalPart()
										.equals(element)) {
							String key = e.asStartElement().getAttributeByName(
									QName.valueOf("key")).getValue();
							map = new HashMap<String, String>();
							while (reader.hasNext()) {
								e = reader.nextEvent();
								if (e.isStartElement()) {
									String local = e.asStartElement().getName()
											.getLocalPart().toLowerCase();
									map.put(local + "-name", e.asStartElement()
											.getAttributeByName(
													QName.valueOf("name"))
											.getValue());
									map.put(local + "-path", e.asStartElement()
											.getAttributeByName(
													QName.valueOf("path"))
											.getValue());
								}
								if (e.isEndElement()
										&& e.asEndElement().getName()
												.getLocalPart().equals(element)) {
									maps.put(key, map);
									break;
								}
							}
						}
						if (e.isEndElement()
								&& e.asEndElement().getName().getLocalPart()
										.equals(root))
							break;
					}
				}
			}
		} finally {
			if (reader != null)
				reader.close();
		}
		return maps;
	}
}
