package text.analysis.cluster.model;

import java.util.List;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午5:58:29
 */

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
