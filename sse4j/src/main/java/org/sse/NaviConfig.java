package org.sse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.sse.StorageFactory.StorageType;
import org.sse.util.URLUtil;

/**
 * read navi.xml,then init network(cnotians spatial index)
 * 
 * @author dux(duxionggis@126.com)
 */
public class NaviConfig {
	public static final String BASE_KEY = "560000";
	// public static boolean IDX = true;
	public static boolean WGS = true;

	public static void init() {
		setWGS();
		List<StorageType> types = new ArrayList<StorageType>(4);
		types.add(StorageType.NET);
		types.add(StorageType.POI);
		types.add(StorageType.BUS);
		types.add(StorageType.DIST);
		for (final StorageType type : types) {
			Thread e1 = StorageFactory.getInstance().create(type);
			e1.start();
			NaviThreadPool.put(e1);
		}
		types.clear();
		types = null;
	}

	static void setWGS() {
		XMLEventReader reader = null;
		try {
			reader = XMLInputFactory.newInstance().createXMLEventReader(new BufferedReader(new FileReader(path())));
			while (reader.hasNext()) {
				XMLEvent e = reader.nextEvent();
				if (e.isStartElement()) {
					String start = e.asStartElement().getName().getLocalPart();
					if (start.equals("navi")) {
						String wgs = e.asStartElement().getAttributeByName(QName.valueOf("wgs")).getValue();
						NaviConfig.WGS = wgs.equalsIgnoreCase("true");
						break;
					}
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
			}
		}
	}

	static Map<String, Map<String, String>> getMap(String root, String element) {
		Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>();
		XMLEventReader reader = null;
		try {
			reader = XMLInputFactory.newInstance().createXMLEventReader(new BufferedReader(new FileReader(path())));
			while (reader.hasNext()) {
				XMLEvent e = reader.nextEvent();
				if (e.isStartElement()) {
					String start = e.asStartElement().getName().getLocalPart();
					if (start.equals(root)) {
						NaviConfig.getElement(root, element, maps, reader);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			maps.clear();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
				maps.clear();
			}
		}
		return maps;
	}

	static void getElement(String root, String element, Map<String, Map<String, String>> maps, XMLEventReader reader)
			throws XMLStreamException {
		Map<String, String> map;
		XMLEvent e;
		while (reader.hasNext()) {
			e = reader.nextEvent();
			if (e.isStartElement() && e.asStartElement().getName().getLocalPart().equals(element)) {
				String key = e.asStartElement().getAttributeByName(QName.valueOf("key")).getValue();
				map = new HashMap<String, String>();
				while (reader.hasNext()) {
					e = reader.nextEvent();
					if (e.isStartElement()) {
						String local = e.asStartElement().getName().getLocalPart().toLowerCase();
						map.put(local + "-name", e.asStartElement().getAttributeByName(QName.valueOf("name"))
								.getValue());
						map.put(local + "-path", e.asStartElement().getAttributeByName(QName.valueOf("path"))
								.getValue());
						map.put(local + "-cache", e.asStartElement().getAttributeByName(QName.valueOf("cache"))
								.getValue());
					}
					if (e.isEndElement() && e.asEndElement().getName().getLocalPart().equals(element)) {
						maps.put(key, map);
						break;
					}
				}
			}
			if (e.isEndElement() && e.asEndElement().getName().getLocalPart().equals(root))
				break;
		}
	}

	static String path() {
		File file = URLUtil.getClassPathFile(NaviConfig.class);
		file = new File(file.getParentFile(), "/cfg/navi.xml");
		String path = file.getPath();
		if (path.startsWith("file:"))
			path = path.substring(5);

		try {
			return URLDecoder.decode(path, "UTF-8");
		} catch (Exception e) {
			return path;
		}
	}
}
