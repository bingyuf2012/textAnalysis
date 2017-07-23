package text.searchSDK.model;

/**
 * The model of NewsSearchResult
 * 
 * @author Leo
 * 
 */
public class NewsSearchResult extends WebSearchResult {

	protected String publisher;

	protected String time;

	/**
	 * Get publisher string
	 * 
	 * @return publisher string
	 */
	public String getPublisher() {
		return this.publisher;
	}

	/**
	 * Set publisher string
	 * 
	 * @param publisher
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * Get time string
	 * 
	 * @return time string
	 */
	public String getTime() {
		return this.time;
	}

	/**
	 * Set time string
	 * 
	 * @param time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	public String toString() {
		return "[title=" + this.title + ";content=" + this.content + ";url="
				+ this.url + ";publisher=" + this.publisher + ";time="
				+ this.time + "]";
	}
}
