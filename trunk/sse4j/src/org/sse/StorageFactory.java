package org.sse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.sse.mcache.IStorage;
import org.sse.mcache.StorageBuilderer;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class StorageFactory {

	public enum StorageType {
		NET, /* route network */
		BUS, /* bus exchange */
		POI, /* local search */
		GEOC, /* geocoding */
		DIST, /* district */
	}

	private Map<StorageType, Map<String, IStorage>> storages;
	private static StorageFactory instance;
	private static Object lock = new Object();

	public static StorageFactory getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new StorageFactory();
				}
			}
		}
		return instance;
	}

	protected StorageFactory() {
		storages = new HashMap<StorageType, Map<String, IStorage>>();
		storages.put(StorageType.NET, new HashMap<String, IStorage>());
		storages.put(StorageType.BUS, new HashMap<String, IStorage>());
		storages.put(StorageType.POI, new HashMap<String, IStorage>());
		storages.put(StorageType.DIST, new HashMap<String, IStorage>());
	}

	public IStorage getStorage(String key, StorageType type) throws Exception {
		if (NaviThreadPool.check(type.name())) {
			if (NaviThreadPool.runnable(type.name())) {
				throw new Exception("waiting(runnable)!");
			} else {
				if (storages.get(type).containsKey(key)) {
					return storages.get(type).get(key);
				} else {
					NaviThreadPool.get(type.name()).run();
					throw new Exception("waiting(init again)!");
				}
			}
		} else {
			Thread e = create(type);
			e.start();
			NaviThreadPool.put(e);
			throw new Exception("waiting(init)!");
		}
	}

	Thread create(final StorageType type) {
		return (new Thread(type.name()) {
			public void run() {
				Map<String, Map<String, String>> maps;
				maps = NaviConfig.getMap(type.name().toLowerCase() + "es", type
						.name().toLowerCase());

				for (Iterator<String> i = maps.keySet().iterator(); i.hasNext();) {
					String key = i.next();
					if (!storages.get(type).containsKey(key)) {
						try {
							IStorage stg = StorageBuilderer.Find(type).create(
									maps.get(key));
							storages.get(type).put(key, stg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

}
