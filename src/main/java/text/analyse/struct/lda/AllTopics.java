package text.analyse.struct.lda;

import java.util.ArrayList;

public class AllTopics {

	ArrayList<TopicSet> topics = new ArrayList<TopicSet>();

	ArrayList<TopWords> topWords = new ArrayList<TopWords>();
	AllEntity entity = new AllEntity();

	ArrayList<RelationTT> reltt = new ArrayList<RelationTT>();
	RelationTE relte = new RelationTE();
	ArrayList<RelationEE> relee = new ArrayList<RelationEE>();

	public ArrayList<TopicSet> getTopics() {
		return topics;
	}

	public void setTopics(ArrayList<TopicSet> topics) {
		this.topics = topics;
	}

	public ArrayList<TopWords> getTopWords() {
		return topWords;
	}

	public void setTopWords(ArrayList<TopWords> topWords) {
		this.topWords = topWords;
	}

	public AllEntity getEntity() {
		return entity;
	}

	public void setEntity(AllEntity entity) {
		this.entity = entity;
	}

	public ArrayList<RelationTT> getReltt() {
		return reltt;
	}

	public void setReltt(ArrayList<RelationTT> reltt) {
		this.reltt = reltt;
	}

	public RelationTE getRelte() {
		return relte;
	}

	public void setRelte(RelationTE relte) {
		this.relte = relte;
	}

	public ArrayList<RelationEE> getRelee() {
		return relee;
	}

	public void setRelee(ArrayList<RelationEE> relee) {
		this.relee = relee;
	}

	// public String getJson() {
	// StringBuilder json = new StringBuilder("{");
	// json.append("reltt: [");
	// for(RelationTT tt : reltt) {
	// json.append(tt.getJson()).append(',');
	// }
	// json.deleteCharAt(json.length() - 1);
	// json.append(']');
	// json.append("}");
	// return json.toString();
	// }
}
