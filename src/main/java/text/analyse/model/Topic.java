package text.analyse.model;

import java.util.Date;

public class Topic extends BaseModelObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1596139541470085881L;

	private String words;
	private String labelRC;
	private String label;
	private Date producedTime;

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public String getLabelRC() {
		return labelRC;
	}

	public void setLabelRC(String labelRC) {
		this.labelRC = labelRC;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getProducedTime() {
		return producedTime;
	}

	public void setProducedTime(Date producedTime) {
		this.producedTime = producedTime;
	}

}
