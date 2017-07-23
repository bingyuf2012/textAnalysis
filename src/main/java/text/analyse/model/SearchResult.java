package text.analyse.model;

public class SearchResult {
	private int id; // 数据库自动添加 id
	private int uid; // 用户ID
	private String username; // 用户名
	private String uip; // 用户 ip(预留)
	private String searchtime; // 搜索时间
	private String searchword; // 搜索词
	private String jsonstring; // 搜索得到的json
	private String lorestring; // 知识库结果

	public SearchResult() {

	}

	public SearchResult(int id, int uid, String username, String uip,
			String searchword) {
		this.id = id;
		this.uid = uid;
		this.username = username;
		this.uip = uip;
		this.searchword = searchword;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUip() {
		return uip;
	}

	public void setUip(String uip) {
		this.uip = uip;
	}

	public String getSearchtime() {
		return searchtime;
	}

	public void setSearchtime(String searchtime) {
		this.searchtime = searchtime;
	}

	public String getSearchword() {
		return searchword;
	}

	public void setSearchword(String searchword) {
		this.searchword = searchword;
	}

	public String getJsonstring() {
		return jsonstring;
	}

	public void setJsonstring(String jsonstring) {
		this.jsonstring = jsonstring;
	}

	public String getLorestring() {
		return lorestring;
	}

	public void setLorestring(String lorestring) {
		this.lorestring = lorestring;
	}

}
