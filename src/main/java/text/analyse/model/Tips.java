package text.analyse.model;

import text.searchSDK.util.PrintConsole;

public class Tips {
	private String username;
	private String stips;
	private String endtips;
	private String time;
	private String warning;
	private String status;

	public Tips(String username, String stips, String endtips, String time,
			String warning, String status) {
		this.username = username;
		this.stips = stips;
		this.endtips = endtips;
		this.time = time;
		this.warning = warning;
		this.status = status;
	}

	public String getStips() {
		return stips;
	}

	public void setStips(String stips) {
		this.stips = stips;
	}

	public String getEndtips() {
		return endtips;
	}

	public void setEndtips(String endtips) {
		this.endtips = endtips;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String toString() {
		StringBuffer sbtips = new StringBuffer();
		sbtips.append('{').append("\"username\":").append('"').append(
				this.username).append("\",").append("\"stips\":").append('"')
				.append(this.stips).append("\",").append("\"endtips\":")
				.append('"').append(this.endtips).append("\",").append(
						"\"warning\":").append('"').append(this.warning)
				.append("\",").append("\"time\":").append('"')
				.append(this.time).append("\",").append("\"status\":").append(
						'"').append(this.status).append("\"").append('}');
		return sbtips.toString();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static void main(String[] args) {
		Tips tips = new Tips("admin1", "知识库搜索结束....", "开始百度搜索结果抓取...", "1秒",
				"", "success");
		PrintConsole.PrintLog("tips.toString()", tips.toString());
	}
}
