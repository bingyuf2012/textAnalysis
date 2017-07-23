package text.analyse.model;

import java.util.Date;

public class Event extends BaseModelObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5559156021551391709L;

	private String name;
	private String description;
	private Date occur;
	private long newsCount;

	public Event() {
		name = null;
		description = null;
		occur = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getOccur() {
		return occur;
	}

	public void setOccur(Date occur) {
		this.occur = occur;
	}

	public long getNewsCount() {
		return newsCount;
	}

	public void setNewsCount(long newsCount) {
		this.newsCount = newsCount;
	}
}
