package text.analysis.cluster.model;

import java.util.List;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午4:32:56
 */

public class TopicSet {
	private int labelName;
	private List<WordStructSet> SpeIssueSet;
	private List<NewsDetail> newsDetail;

	public int getLabelName() {
		return labelName;
	}

	public void setLabelName(int labelName) {
		this.labelName = labelName;
	}

	public List<WordStructSet> getSpeIssueSet() {
		return SpeIssueSet;
	}

	public void setSpeIssueSet(List<WordStructSet> speIssueSet) {
		SpeIssueSet = speIssueSet;
	}

	public List<NewsDetail> getNewsDetail() {
		return newsDetail;
	}

	public void setNewsDetail(List<NewsDetail> newsDetail) {
		this.newsDetail = newsDetail;
	}
}
