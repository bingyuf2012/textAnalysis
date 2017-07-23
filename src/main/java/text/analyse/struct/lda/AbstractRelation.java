package text.analyse.struct.lda;

import java.util.Comparator;

public abstract class AbstractRelation {
	protected int ID;
	protected int topicID;
	protected String value;

	// public String getJson() {
	// return "{"
	// + "locID:" + getID() + ","
	// + "topicID:" + getTopicID() + ","
	// + "value:" + getValue()
	// + "}";
	// }

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
