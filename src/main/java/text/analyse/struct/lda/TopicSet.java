package text.analyse.struct.lda;

import java.util.ArrayList;

public class TopicSet {

	int labelName;
	ArrayList<WordStructSet> SpeIssueSet;
	ArrayList<NewsDetail> newsDetail;

	public int getLabelName() {
		return labelName;
	}

	public void setLabelName(int labelName) {
		this.labelName = labelName;
	}

	public ArrayList<WordStructSet> getSpeIssueSet() {
		return SpeIssueSet;
	}

	public void setSpeIssueSet(ArrayList<WordStructSet> speIssueSet) {
		SpeIssueSet = speIssueSet;
	}

	public ArrayList<NewsDetail> getNewsDetail() {
		return newsDetail;
	}

	public void setNewsDetail(ArrayList<NewsDetail> newsDetail) {
		this.newsDetail = newsDetail;
	}

	// public String getJson(){
	// StringBuilder json = new StringBuilder("{");
	// json.append("Index:" + getLabelName() + ",");
	// json.append("TopicWord: [");
	// for(WordStructSet word:SpeIssueSet){
	// json.append(word.getJson()).append(",");
	// }
	// json.deleteCharAt(json.length() - 1);
	// json.append(']').append(",");
	//		
	// json.append("Url: [");
	// for(String url:strUrl){
	// json.append(url).append(",");
	// }
	// json.deleteCharAt(json.length() - 1);
	// json.append(']');
	// json.append("}");
	// return json.toString();
	//		
	// }

}
