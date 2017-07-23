package text.analyse.utility;

import java.util.Comparator;

public class NewsSort {
	public int index;
	public double score;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public NewsSort(int index, double score) {
		this.index = index;
		this.score = score;
	}

	public String toString() {
		return index + "  " + score;
	}

	public static Comparator<NewsSort> comparator = new Comparator<NewsSort>() {
		public int compare(NewsSort n1, NewsSort n2) {
			// 比率
			if (n1.score != n2.score) {
				return (int) ((n2.score - n1.score) * 100000);
			} else {
				return n1.index - n2.index;
			}
		}
	};
}
