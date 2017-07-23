package text.analyse.utility;

import java.util.ArrayList;
import java.util.Hashtable;

public class NewsSet {
	public Hashtable<String, News> newsTable;
	public ArrayList<News> newsArray;

	public NewsSet() {
		this.newsTable = new Hashtable<String, News>();
		this.newsArray = new ArrayList<News>();
	}

	public void addNews(News news) {
		this.newsTable.put(news.URL, news);
		this.newsArray.add(news);
	}

	public void removeNews(News news) {
		if (newsTable != null) {
			newsTable.remove(news.URL);
			newsArray.remove(news);
		}
	}

	public Hashtable<String, News> getNewsHashtable() {
		return newsTable;
	}

	public ArrayList<News> getNewsArrayList() {
		return newsArray;
	}

	public News getNews(String name) {
		if (newsTable == null) {
			return null;
		} else {
			return (News) newsTable.get(name);
		}
	}

	public int getSize() {
		return newsArray.size();
	}
}
