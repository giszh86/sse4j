package org.sse.service;

import java.util.List;

import org.sse.service.base.Poi;
import org.sse.squery.Filter;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public interface IPoiService {
	/**
	 * common query(attribute or spatial or buffer or composite query)
	 * 
	 * @param filter
	 * @param key
	 *            from xml config(poi key in navi.xml)
	 * @return
	 * @throws Exception
	 */
	public List<Poi> search(Filter filter, String key) throws Exception;

	/**
	 * get extra poi info
	 * 
	 * @param id
	 *            POI OID
	 * @param key
	 * @return
	 */
	public Poi tipInfo(String id, String key) throws Exception;

	/**
	 * same as search(...),but save memory
	 * 
	 * @param filter
	 * @param key
	 * @return json format poi List
	 * @throws Exception
	 */
	public String jsonSearch(Filter filter, String key) throws Exception;
}
