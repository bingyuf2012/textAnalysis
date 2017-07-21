package text.analysis.cluster.model;

import java.util.Comparator;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午4:36:16
 */

public abstract class AbstractRelation {
	protected int ID;
	protected int topicID;
	protected String value;

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getTopicID() {
		return topicID;
	}

	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static Comparator<AbstractRelation> comparator = new Comparator<AbstractRelation>() {
		public int compare(AbstractRelation a1, AbstractRelation a2) {
			// 比率
			return (int) (a2.value.compareTo(a1.value));
		}
	};
}