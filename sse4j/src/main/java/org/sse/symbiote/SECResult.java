package org.sse.symbiote;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

/**
 * @author dux(duxionggis@126.com)
 */
public class SECResult {
	private List<Item> links = new LinkedList<Item>();

	public List<Item> getLinks() {
		return links;
	}

	public void setLinks(List<Item> links) {
		this.links = links;
	}

	public void addLink(Item link) {
		if (links == null)
			links = new LinkedList<Item>();
		links.add(link);
	}

	public static class Item {
		private String href = "";
		private String title = "";
		private String shapshot = "";
		private String source = "";
		private transient int rank = 0;

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
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

		public int getRank() {
			return rank;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}

		public String toString() {
			return new Gson().toJson(this);
		}

		public boolean equals(Object obj) {
			if (obj instanceof Item)
				return ((Item) obj).getHref().equals(this.href);
			return false;
		}
	}

	public static enum Type {
		BAIDU, SOGOU, YOUDAO, BING
	}
}
