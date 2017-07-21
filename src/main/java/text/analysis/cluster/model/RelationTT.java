package text.analysis.cluster.model;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午5:06:27
 */

public class RelationTT {
	private int origTopicID;
	private int destTopicID;
	private double relationValue;

	public int getOrigTopicID() {
		return origTopicID;
	}

	public void setOrigTopicID(int origTopicID) {
		this.origTopicID = origTopicID;
	}

	public int getDestTopicID() {
		return destTopicID;
	}

	public void setDestTopicID(int destTopicID) {
		this.destTopicID = destTopicID;
	}

	public double getRelationValue() {
		return relationValue;
	}

	public void setRelationValue(double relationValue) {
		this.relationValue = relationValue;
	}

}
