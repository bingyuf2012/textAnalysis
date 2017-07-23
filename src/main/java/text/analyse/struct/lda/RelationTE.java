package text.analyse.struct.lda;

import java.util.ArrayList;
import java.util.List;

public class RelationTE {

	private List<RelationPerTE> relPerTe = new ArrayList<RelationPerTE>();
	private List<RelationLocTE> relLocTe = new ArrayList<RelationLocTE>();
	private List<RelationOrgTE> relOrgTe = new ArrayList<RelationOrgTE>();

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
