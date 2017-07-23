package text.analyse.utility;

import java.util.Comparator;
import java.util.HashMap;

public class Word implements Comparable<Object> {
	public int id;
	public String key;
	public String type;
	public double value;
	public HashMap<Integer, Object> docs = new HashMap<Integer, Object>();

	public Word() {

	}

	public Word(int id, String key, String type) {
		this.id = id;
		this.key = key;
		this.type = type;
	}

	public Word(String key, String type) {
		this.key = key;
		this.type = type;
	}

	public Word(String key, String type, double value) {
		this.key = key;
		this.type = type;
		this.value = value;
	}

	public Word(String key) {
		this.key = key;
	}

	public Word(int id, double value) {
		this.id = id;
		this.value = value;
	}

	public Word(String key, double value) {
		this.key = key;
		this.value = value;
	}

	public int compareTo(Object obj) {
		Word t = (Word) obj;
		if (this.value > t.value)
			return -1;
		else if (this.value < t.value)
			return 1;
		else
			return 0;
	}

	public boolean equals(Object obj) {
		String str = ((Word) obj).key;
		if (this.key.equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	public static Comparator<Word> comparator = new Comparator<Word>() {
		public int compare(Word n1, Word n2) {
			// 比率
			if (n1.id != n2.id) {
				return n2.id - n1.id;
			} else {
				return n1.key.compareTo(n2.key);
			}
		}
	};
}
