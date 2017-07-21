package text.analysis.cluster.model;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午4:38:41
 */

public class TopWords {
	private int topicID;
	private String labelWords;

	public int getTopicID() {
		return topicID;
	}

	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}

	public String getLabelWords() {
		return labelWords;
	}

	public void setLabelWords(String labelWords) {
		this.labelWords = labelWords;
	}
}
