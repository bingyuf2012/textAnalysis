package text.analyse.model;

import java.util.Date;

public class NewsDocument extends BaseModelObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7695497769221243699L;

	private String title;
	private String keywords;
	private Date time;
	private String url;
	private String author;
	private String content;
	private Category category;
	private Event relatedEvent;
	private String source;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Event getRelatedEvent() {
		return relatedEvent;
	}

	public void setRelatedEvent(Event relatedEvent) {
		this.relatedEvent = relatedEvent;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
