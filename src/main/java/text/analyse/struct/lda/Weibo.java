package text.analyse.struct.lda;

import java.util.ArrayList;

public class Weibo {
	String userID;
	ArrayList<WeiboSet> data = new ArrayList<WeiboSet>();

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public ArrayList<WeiboSet> getData() {
		return data;
	}

	public void setData(ArrayList<WeiboSet> data) {
		this.data = data;
	}

}
