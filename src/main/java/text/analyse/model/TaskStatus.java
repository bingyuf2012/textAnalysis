package text.analyse.model;

public class TaskStatus {
	private String current;
	private String pause;
	private String restart;

	public TaskStatus(String current, String pause, String restart) {
		this.current = current;
		this.pause = pause;
		this.restart = restart;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getPause() {
		return pause;
	}

	public void setPause(String pause) {
		this.pause = pause;
	}

	public String getRestart() {
		return restart;
	}

	public void setRestart(String restart) {
		this.restart = restart;
	}

}
