package text.analyse.model;

public class History {
	private String username;
	private String backurl;
	private String htmlcontent;

	public History() {

	}

	public History(String username, String backurl, String htmlcontent) {
		this.username = username;
		this.backurl = backurl;
		this.htmlcontent = htmlcontent;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBackurl() {
		return backurl;
	}

	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}

	public String getHtmlcontent() {
		return htmlcontent;
	}

	public void setHtmlcontent(String htmlcontent) {
		this.htmlcontent = htmlcontent;
	}

}
