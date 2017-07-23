package text.analyse.model;

public class SaveInfo {
	private int id; // 数据库自动添加 id
	private int uid; // 用户ID
	private String username; // 用户名
	private String uip; // 用户 ip(预留)
	private String searchtime; // 搜索时间
	private String keyword; // 关键词
	private String alljsonstr; // LDA 处理后的所有信息
	private String jsonstring; // 需要保存的LDA json
	private String lorestring; // 勾取的 Lore string
	private String mapjson; // 关系图json

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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public String getMapjson() {
		return mapjson;
	}

	public void setMapjson(String mapjson) {
		this.mapjson = mapjson;
	}

	public String getAlljsonstr() {
		return alljsonstr;
	}

	public void setAlljsonstr(String alljsonstr) {
		this.alljsonstr = alljsonstr;
	}
}
