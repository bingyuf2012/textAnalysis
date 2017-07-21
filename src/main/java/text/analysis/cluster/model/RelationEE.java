package text.analysis.cluster.model;

import java.util.List;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午4:35:13
 */

public class RelationEE {
	private int topicID;
	private List<EEValue> reltt;

	public int getTopicID() {
		return topicID;
	}

	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}

	public List<EEValue> getReltt() {
		return reltt;
	}

	public void setReltt(List<EEValue> reltt) {
		this.reltt = reltt;
	}
}
