package text.searchSDK.model;

/**
 * The model of WebSearchResult
 * 
 * @author Leo
 * 
 */
public class WebSearchResult {

	protected String title;

	protected String url;

	protected String content;

	protected String time;

	protected String type;

	protected String weights;

	protected String fresh;

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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWeights() {
		return weights;
	}

	public void setWeights(String weights) {
		this.weights = weights;
	}

	public String getFresh() {
		return fresh;
	}

	public void setFresh(String fresh) {
		this.fresh = fresh;
	}

	public String toString() {
		return "[title=" + this.title + ";content=" + this.content + ";url="
				+ this.url + ";time=" + this.time + ";type=" + this.type
				+ ";weights=" + this.weights + ";fresh=" + this.fresh + "]";
	}

	public String toJsonString() {
		StringBuffer buf = new StringBuffer();

		buf = buf.append('{').append("\"title\":").append('"').append(
				this.title).append("\",").append("\"content\":").append('"')
				.append(this.content).append("\",").append("\"url\":").append(
						'"').append(this.url).append("\",").append("\"time\":")
				.append('"').append(this.time).append("\",").append(
						"\"weights\":").append('"').append(this.weights)
				.append("\",").append("\"fresh\":").append('"').append(
						this.fresh).append("\",").append("\"type\":").append(
						'"').append(this.type).append("\"").append('}');

		return buf.toString();
	}
	
}
