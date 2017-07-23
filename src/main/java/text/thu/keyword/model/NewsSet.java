package text.thu.keyword.model;

import java.util.ArrayList;
import java.util.Hashtable;

public class NewsSet {

	public void addNews(News news) {
		if (newsTable == null) {
			newsTable = new Hashtable();
			newsArray = new ArrayList();
		} else {
			if (this.newsTable.get(news.name) == null) {
				this.newsTable.put(news.name, news);
				this.newsArray.add(news);
			}
		}
	}

	public void removeNews(News news) {
		if (newsTable != null) {
			newsTable.remove(news.name);
			newsArray.remove(news);
		}
	}

	public Hashtable getNewsHashtable() {
		return newsTable;
	}

	public ArrayList getNewsArrayList() {
		return newsArray;
	}

	public News getNews(String name) {
		if (newsTable == null) {
			return null;
		} else {
			return (News) newsTable.get(name);
		}
	}

	public Hashtable newsTable = new Hashtable();
	public ArrayList newsArray = new ArrayList();

}
