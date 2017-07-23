package text.analyse.struct.lda;

public class WordStructSet {
	public String strWord;
	public double value;

	public WordStructSet(String strWord, double value) {
		this.strWord = strWord;
		this.value = value;
	}

	public WordStructSet() {

	}

	public String getStrWord() {
		return strWord;
	}

	public void setStrWord(String strWord) {
		this.strWord = strWord;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	// public String getJson(){
	// return "{"
	// + "TopicWord:" + getStrWord() + "," + "value" + getValue()
	// + "}";
	// }

}
