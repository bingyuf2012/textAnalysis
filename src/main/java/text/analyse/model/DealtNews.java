/**
 * 
 */
package text.analyse.model;

/**
 * @author Alex
 * 
 */
public class DealtNews extends NewsDocument {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6897981530253792543L;

	private String tokens;
	private int wordNumber;
	private String generatedKeywords;

	/*
	 * Added at 2012-03-20, for segmentation results
	 */
	private String segTitle;
	private String segContent;

	public String getTokens() {
		return tokens;
	}

	public void setTokens(String tokens) {
		this.tokens = tokens;
	}

	public int getWordNumber() {
		return wordNumber;
	}

	public void setWordNumber(int wordNumber) {
		this.wordNumber = wordNumber;
	}

	public String getGeneratedKeywords() {
		return generatedKeywords;
	}

	public void setGeneratedKeywords(String generatedKeywords) {
		this.generatedKeywords = generatedKeywords;
	}

	public String getSegTitle() {
		return segTitle;
	}

	public void setSegTitle(String segTitle) {
		this.segTitle = segTitle;
	}

	public String getSegContent() {
		return segContent;
	}

	public void setSegContent(String segContent) {
		this.segContent = segContent;
	}

}
