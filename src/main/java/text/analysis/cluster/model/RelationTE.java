package text.analysis.cluster.model;

import java.util.List;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午5:08:03
 */

public class RelationTE {
	private List<RelationPerTE> relPerTe;
	private List<RelationLocTE> relLocTe;
	private List<RelationOrgTE> relOrgTe;

	public List<RelationPerTE> getRelPerTe() {
		return relPerTe;
	}

	public void setRelPerTe(List<RelationPerTE> relPerTe) {
		this.relPerTe = relPerTe;
	}

	public List<RelationLocTE> getRelLocTe() {
		return relLocTe;
	}

	public void setRelLocTe(List<RelationLocTE> relLocTe) {
		this.relLocTe = relLocTe;
	}

	public List<RelationOrgTE> getRelOrgTe() {
		return relOrgTe;
	}

	public void setRelOrgTe(List<RelationOrgTE> relOrgTe) {
		this.relOrgTe = relOrgTe;
	}

}
