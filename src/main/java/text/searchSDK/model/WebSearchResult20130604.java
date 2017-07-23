package text.searchSDK.model;

/**
 * The model of WebSearchResult
 * 
 * @author Leo
 * 
 */
public class WebSearchResult20130604 {

	protected String title;

	protected String url;

	protected String content;

	/**
	 * Get title string
	 * 
	 * @return title string
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Set title string
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get url string
	 * 
	 * @return url string
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Set url string
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Get content string
	 * 
	 * @return content string
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Set content string
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public String toString() {
		return "[title=" + this.title + ";content=" + this.content + ";url="
				+ this.url + "]";
	}
}
