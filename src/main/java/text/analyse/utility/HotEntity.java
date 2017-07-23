package text.analyse.utility;

import java.sql.Date;
import java.util.Hashtable;

public class HotEntity {
	Date timeid;
	Hashtable<String, Object> pers = new Hashtable<String, Object>();
	Hashtable<String, Object> locs = new Hashtable<String, Object>();
	Hashtable<String, Object> orgs = new Hashtable<String, Object>();
	String maxper;
	String maxloc;
	String maxorg;
	int max1 = 0;
	int max2 = 0;
	int max3 = 0;

	public HotEntity() {

	}

	public HotEntity(Date timeid) {
		this.timeid = timeid;
	}

	/**
	 * @功能 添加热点实体到热点实体集合中，共有PER、ORG、LOC三种类型
	 * @param word
	 */
	public void addEntity(Word word) {
		if (word != null) {
			if (word.type.equals("PER")) {
				if (pers.contains(word)) {
					Word w = (Word) pers.get(word.key);
					w.value += 1;
					if (max1 < w.value) {
						max1 = (int) w.value;
						maxper = w.key;
					}
				} else {
					pers.put(word.key, word);
					word.value = 1;
					if (max1 < word.value) {
						max1 = (int) word.value;
						maxper = word.key;
					}
				}

			} else if (word.type.equals("LOC")) {
				if (locs.contains(word)) {
					Word w = (Word) locs.get(word.key);
					w.value += 1;
					if (max2 < w.value) {
						max2 = (int) w.value;
						maxloc = w.key;
					}
				} else {
					locs.put(word.key, word);
					word.value = 1;
					if (max2 < word.value) {
						max2 = (int) word.value;
						maxloc = word.key;
					}
				}
			} else if (word.type.equals("ORG")) {
				if (orgs.contains(word)) {
					Word w = (Word) orgs.get(word.key);
					w.value += 1;
					if (max3 < w.value) {
						max3 = (int) w.value;
						maxorg = w.key;
					}
				} else {
					orgs.put(word.key, word);
					word.value = 1;
					if (max3 < word.value) {
						max3 = (int) word.value;
						maxorg = word.key;
					}
				}

			}
		}
	}

	/**
	 * @功能 在热点实体集合中分别寻找3种类型中出现次数最多的实体
	 * @param type
	 * @return
	 */
	public Word getTopEntity(String type) {
		Word word = null;
		if (type.equals("PER")) {
			if (pers.size() != 0)
				word = (Word) pers.get(maxper);
		} else if (type.equals("LOC")) {
			if (locs.size() != 0)
				word = (Word) locs.get(maxloc);

		} else if (type.equals("ORG")) {
			if (orgs.size() != 0)
				word = (Word) orgs.get(maxorg);
		}
		return word;
	}

	/**
	 * @功能 用时间判断两个热点实体集合是否相等
	 * @param obj
	 */
	/*
	 * public boolean equals(Object obj) { Date time = ((HotEntity)obj).timeid;
	 * if(this.timeid.equals(time)) return true; else return false; }
	 */
}
