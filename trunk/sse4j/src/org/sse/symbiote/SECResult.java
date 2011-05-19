package org.sse.symbiote;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class SECResult {
	private List<String> links = new LinkedList<String>();

	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	public void addLink(String link) {
		if (links == null)
			links = new LinkedList<String>();
		links.add(link);
	}

	public static class Item {
		private String href;
		private String remark;
		private String shapshot;
		private String source;

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getShapshot() {
			return shapshot;
		}

		public void setShapshot(String shapshot) {
			this.shapshot = shapshot;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String toString() {
			return new Gson().toJson(this);
		}
	}

	public static enum Type {
		BAIDU, SOGOU, YOUDAO
	}
}
