package text.analyse.model;

public class Tasks {
	private int tid; // 下载任务 id
	private int uid; // 下载的用户
	private String username;
	private int sid; // 保存的任务id
	private String progress; // 当前进度字符串
	private String starttime; // 开始下载时间
	private String endtime; // 下载结束时间
	private String indextime; // 生成索引时间
	private String packagetime; // 打包时间
	private int status;

	public Tasks() {

	}

	public Tasks(int tid, String username, int uid) {
		this.tid = tid;
		this.username = username;
		this.uid = uid;
	}

	public Tasks(int tid, int uid, String username, String starttime, int status) {
		this.tid = tid;
		this.uid = uid;
		this.username = username;
		this.starttime = starttime;
		this.status = status;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
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

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getIndextime() {
		return indextime;
	}

	public void setIndextime(String indextime) {
		this.indextime = indextime;
	}

	public String getPackagetime() {
		return packagetime;
	}

	public void setPackagetime(String packagetime) {
		this.packagetime = packagetime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
