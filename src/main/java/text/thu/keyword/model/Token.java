package text.thu.keyword.model;

public class Token implements Comparable {
	public int id;
	public int tf;
	public String word;
	public String type;// 词性
	public double score;
	public int df;
	public int isHead = 0;
	public int inQuotation = 0;
	public int bonus = 0;
	public int firstpara = 0;
	public double stability = 0.0;
	public boolean del = true;
	public boolean completeness = true;

	public Token() {

	}

	public Token(String word, String type) {
		this.word = word;
		this.type = type;
		this.tf = 1;
	}

	public Token(String word, String type, int isHead, int firstpara) {
		this.word = word;
		this.type = type;
		this.tf = 1;
		this.isHead = isHead;
		this.firstpara = firstpara;
	}

	public Token(String word, String type, int isHead, int inQuotation, int tf) {
		this.word = word;
		this.type = type;
		this.tf = tf;
		this.score = tf;
		this.isHead = isHead;
		this.inQuotation = inQuotation;
	}

	public Token(String word, String type, int isHead, int inQuotation, int tf,
			double stability) {
		this.word = word;
		this.type = type;
		this.tf = tf;
		this.score = tf;
		this.isHead = isHead;
		this.inQuotation = inQuotation;
		this.stability = stability;
	}

	public Token(String key, int i) {
		this.word = key;
		this.tf = i;
	}

	public int compareTo(Object obj) {
		// TODO Auto-generated method stub
		Token t = (Token) obj;
		if (this.score > t.score)
			return -1;
		else
			return 1;
	}

	public String outputToken() {
		// if (isHead)
		return (word + "\t\t" + type + "\t\t" + tf + "\t\t" + df + "\t\t"
				+ score + "\t\t" + isHead + inQuotation + completeness + " " + stability);

	}

	// Override by lijun
	public boolean equals(Object obj) {
		// TODO 自动生成方法存根
		// return super.equals(obj);
		String str = ((Token) obj).word;
		if (this.word.equals(str)) {
			return true;
		} else {
			return false;
		}
	}

}
