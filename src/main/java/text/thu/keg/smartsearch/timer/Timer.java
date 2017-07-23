package text.thu.keg.smartsearch.timer;

import text.searchSDK.util.PrintConsole;

public class Timer {
	public long startTime;
	public String name;

	public Timer(String name) {
		this.name = name;
	}

	public void start() {
		this.startTime = System.currentTimeMillis();
		PrintConsole.PrintLog(name, " time start");
	}

	public void getTime() {
		long endTime = System.currentTimeMillis() - startTime;
		PrintConsole.PrintLog(name, " time:" + formatDuring(endTime));
	}

	public String formatDuring(long mss) {
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		return hours + " hours: " + minutes + " minutes: " + seconds
				+ " seconds ";
	}

}
