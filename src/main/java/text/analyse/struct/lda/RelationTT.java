package text.analyse.struct.lda;

public class RelationTT {

	int origTopicID;
	int destTopicID;
	double relationValue;

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

	// public String getJson() {
	// return "{"
	// + "origTopicID:" + getOrigTopicID() + ","
	// + "destTopicID:" + getDestTopicID() + ","
	// + "relationValue:" + getRelationValue()
	// + "}";
	// }

}
