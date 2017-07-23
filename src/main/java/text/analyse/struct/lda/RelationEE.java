package text.analyse.struct.lda;

import java.util.ArrayList;

public class RelationEE {

	int topicID;
	ArrayList<EEValue> reltt = new ArrayList<EEValue>();

	public int getTopicID() {
		return topicID;
	}

	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}

	public ArrayList<EEValue> getReltt() {
		return reltt;
	}

	public void setReltt(ArrayList<EEValue> reltt) {
		this.reltt = reltt;
	}

}
