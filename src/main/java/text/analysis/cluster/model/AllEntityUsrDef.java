package text.analysis.cluster.model;

import java.util.List;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午5:03:59
 */

public class AllEntityUsrDef extends AllEntity {
	private List<Integer> equs;
	private List<Integer> tecs;
	private List<Integer> pros;

	public List<Integer> getEqus() {
		return equs;
	}

	public void setEqus(List<Integer> equs) {
		this.equs = equs;
	}

	public List<Integer> getTecs() {
		return tecs;
	}

	public void setTecs(List<Integer> tecs) {
		this.tecs = tecs;
	}

	public List<Integer> getPros() {
		return pros;
	}

	public void setPros(List<Integer> pros) {
		this.pros = pros;
	}
}
