package org.sse.mcache;

import org.sse.StorageFactory.StorageType;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class StorageBuilderer {

	public static IStorageBuilder Find(StorageType type) {
		if (type == StorageType.NET) {
			return new IdxRouteStorageBuilder();
		} else if (type == StorageType.POI) {
			return new PoiStorageBuilder();
		} else if (type == StorageType.DIST) {
			return new DistStorageBuilder();
		}
		return null;
	}

}
