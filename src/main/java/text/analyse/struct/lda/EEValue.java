package text.analyse.struct.lda;

public class EEValue extends AbstractRelation {
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

	// public String getJson() {
	// return "{"
	// + "orginEntityID:" + getID() + ","
	// + "destEntityID:" + getTopicID() + ","
	// + "value:" + getValue()
	// + "}";
	// }
}
