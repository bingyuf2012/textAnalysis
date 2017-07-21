package text.analysis.cluster.model;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午4:35:38
 */

public class EEValue extends AbstractRelation{
	/*
	 * int orginEntityID; int destEntityID; String value;
	 */
	public int getOrginEntityID() {
		return getID();
	}

	public void setOrginEntityID(int orginEntityID) {
		// this.orginEntityID = orginEntityID;
		this.setID(orginEntityID);
	}

	public int getDestEntityID() {
		return getTopicID();
	}

	public void setDestEntityID(int destEntityID) {
		this.setTopicID(destEntityID);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
