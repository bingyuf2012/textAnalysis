package text.analyse.struct.etm;

import java.util.List;

import text.analyse.struct.lda.AllEntity;
import text.analyse.struct.lda.RelationEE;
import text.analyse.struct.lda.RelationTE;
import text.analyse.struct.lda.RelationTT;
import text.analyse.struct.lda.TopWords;
import text.analyse.struct.lda.TopicSet;

public class AllTopicsETM {
	private List<TopicSet> topics;
	private List<TopWords> topWords;
	private AllEntity entity;
	private List<RelationTT> reltt;
	private RelationTE relte;
	private List<RelationEE> relee;

	public List<TopicSet> getTopics() {
		return topics;
	}

	public void setTopics(List<TopicSet> topics) {
		this.topics = topics;
	}

	public List<TopWords> getTopWords() {
		return topWords;
	}

	public void setTopWords(List<TopWords> topWords) {
		this.topWords = topWords;
	}

	public AllEntity getEntity() {
		return entity;
	}

	public void setEntity(AllEntity entity) {
		this.entity = entity;
	}

	public List<RelationTT> getReltt() {
		return reltt;
	}

	public void setReltt(List<RelationTT> reltt) {
		this.reltt = reltt;
	}

	public RelationTE getRelte() {
		return relte;
	}

	public void setRelte(RelationTE relte) {
		this.relte = relte;
	}

	public List<RelationEE> getRelee() {
		return relee;
	}

	public void setRelee(List<RelationEE> relee) {
		this.relee = relee;
	}

}
