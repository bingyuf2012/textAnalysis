package text.analysis.cluster;

import java.util.ArrayList;
import java.util.List;

import text.analysis.cluster.model.AllEntityUsrDef;
import text.analysis.cluster.model.RelationEE;
import text.analysis.cluster.model.RelationETMTE;
import text.analysis.cluster.model.RelationTT;
import text.analysis.cluster.model.TopWords;
import text.analysis.cluster.model.TopicSet;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午4:31:53
 */

public class AllTopicsETM {
	private List<TopicSet> topics;
	private List<TopWords> topWords;
	private AllEntityUsrDef entity;
	private List<RelationTT> reltt;
	private RelationETMTE relte;
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

	public AllEntityUsrDef getEntity() {
		return entity;
	}

	public void setEntity(AllEntityUsrDef entity) {
		this.entity = entity;
	}

	public List<RelationTT> getReltt() {
		return reltt;
	}

	public void setReltt(List<RelationTT> reltt) {
		this.reltt = reltt;
	}

	public RelationETMTE getRelte() {
		return relte;
	}

	public void setRelte(RelationETMTE relte) {
		this.relte = relte;
	}

	public List<RelationEE> getRelee() {
		return relee;
	}

	public void setRelee(List<RelationEE> relee) {
		this.relee = relee;
	}

}
