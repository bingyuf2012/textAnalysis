package text.analyse.utility;

import java.util.ArrayList;
import java.util.Comparator;

public class TopicSort {
	public ArrayList<Integer> list;
	public Double scores;

	public ArrayList<Integer> getList() {
		return list;
	}

	public void setList(ArrayList<Integer> list) {
		this.list = list;
	}

	public Double getScores() {
		return scores;
	}

	public void setScores(Double scores) {
		this.scores = scores;
	}

	public TopicSort(ArrayList<Integer> list, double scores) {
		this.list = list;
		this.scores = scores;
	}

	public String toString() {
		return list.size() + "  " + scores;
	}

	public static Comparator<TopicSort> comparator = new Comparator<TopicSort>() {
		public int compare(TopicSort t1, TopicSort t2) {
			// 比率
			if (t1.scores != t2.scores) {
				return (int) ((t2.scores - t1.scores) * 100000);
			} else {
				return t1.list.size() - t2.list.size();
			}
		}
	};
}
