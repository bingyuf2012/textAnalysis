package text.analyse.struct.lda;

import java.util.ArrayList;
import java.util.List;

public class AllEntity {
	private List<Integer> pers = new ArrayList<Integer>();
	private List<Integer> locs = new ArrayList<Integer>();
	private List<Integer> orgs = new ArrayList<Integer>();
	private List<String> All_Entitys = new ArrayList<String>();

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
