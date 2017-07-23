package text.searchSDK.site;

public class Site {
	private int id;
	private String name;
	private String domain;
	private boolean isused;

	public Site() {
	}

	public Site(String name, String domain) {
		this.name = name;
		this.domain = domain;
	}

	public Site(int id, String name, String domain, boolean isused) {
		this.id = id;
		this.name = name;
		this.domain = domain;
		this.isused = isused;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public boolean isIsused() {
		return isused;
	}

	public void setIsused(boolean isused) {
		this.isused = isused;
	}
}
