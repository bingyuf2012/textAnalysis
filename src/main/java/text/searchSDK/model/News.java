package text.searchSDK.model;

import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;

public class News implements Comparable {

	public int newsid;
	public String name;
	public String time;
	public String headLine;
	public String content;
	public String file_path;
	public String class_id;
	public String class_content;
	public Hashtable vsm = null;
	public TreeSet keyTs;
	public List summary;
	public Token[] keywords;
	public long ranks;

	public News() {

	}

	public News(String name) {
		this.name = name;
	}

	public News(String headLine, String content, String class_content) {
		this.headLine = headLine;
		this.content = content;
		this.class_content = class_content;
	}

	public int compareTo(Object obj) {
		News news = (News) obj;
		if (this.ranks > news.ranks)
			return 1;
		else
			return -1;
	}

}
