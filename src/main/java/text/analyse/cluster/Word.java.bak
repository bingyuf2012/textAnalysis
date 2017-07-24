package text.analyse.cluster;

/*
 */
public class Word implements Comparable<Object> {
	public int id;
	public double value;

	public Word() {
	}

	public Word(int id, double value) {
		this.id = id;
		this.value = value;
	}

	public int compareTo(Object obj) {
		Word t = (Word) obj;
		if (t.value > this.value)
			return 1;
		else if (t.value < this.value)
			return -1;
		else
			return 0;
	}

}
