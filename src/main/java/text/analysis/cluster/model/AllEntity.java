package text.analysis.cluster.model;

import java.util.List;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午5:05:07
 */

public class AllEntity {
	private List<Integer> pers;
	private List<Integer> locs;
	private List<Integer> orgs;
	private List<String> All_Entitys;

	public List<Integer> getPers() {
		return pers;
	}

	public void setPers(List<Integer> pers) {
		this.pers = pers;
	}

	public List<Integer> getLocs() {
		return locs;
	}

	public void setLocs(List<Integer> locs) {
		this.locs = locs;
	}

	public List<Integer> getOrgs() {
		return orgs;
	}

	public void setOrgs(List<Integer> orgs) {
		this.orgs = orgs;
	}

	public List<String> getAll_Entitys() {
		return All_Entitys;
	}

	public void setAll_Entitys(List<String> all_Entitys) {
		All_Entitys = all_Entitys;
	}
}
