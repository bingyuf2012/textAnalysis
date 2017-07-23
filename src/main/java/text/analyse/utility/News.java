package text.analyse.utility;

import java.sql.Date;
import java.util.HashMap;

public class News implements Comparable<Object> {
	public int ID;
	public String URL;
	public Date time;
	public String headLine;
	public String author;
	public String content;
	public String allTokens;
	public String keyWords;
	public HashMap<String, Object> terms;
	public int wordNums;
	public long ranks;
	public String specialIssue;
	public String category;

	public News() {
	}

	public News(int id, String headLine, String content) {
		this.ID = id;
		this.headLine = headLine;
		this.content = content;
	}

	public News(String headLine, String content) {
		this.headLine = headLine;
		this.content = content;
	}

	public int compareTo(Object obj) {
		News news = (News) obj;
		if (this.ranks > news.ranks)
			return 1;
		else if (this.ranks < news.ranks)
			return -1;
		else
			return 0;
	}

}
