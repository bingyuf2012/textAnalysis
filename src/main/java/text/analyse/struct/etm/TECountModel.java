package text.analyse.struct.etm;

import java.util.List;

import text.analyse.etmutility.Entity;

public class TECountModel {
	private int etid;
	private List<Entity> entityList;
	private int count;

	public TECountModel(int etid, List<Entity> entityList, int count) {
		this.etid = etid;
		this.entityList = entityList;
		this.count = count;
	}

	public List<Entity> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<Entity> entityList) {
		this.entityList = entityList;
	}

	public int getEtid() {
		return etid;
	}

	public void setEtid(int etid) {
		this.etid = etid;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
