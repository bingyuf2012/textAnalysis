package text.analysis.cluster.model;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午4:33:24
 */

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
}
