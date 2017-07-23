package text.analyse.model;

import java.util.List;
import java.util.Map;

public class ThreadInfo {
	private Runnable parentThread;
	private List<Map<String, Runnable>> subthreadList;
	private boolean status;

	public ThreadInfo() {

	}

	public ThreadInfo(Runnable parentThread,
			List<Map<String, Runnable>> subthreadList, boolean status) {
		this.parentThread = parentThread;
		this.subthreadList = subthreadList;
		this.status = status;
	}

	public Runnable getParentThread() {
		return parentThread;
	}

	public void setParentThread(Thread parentThread) {
		this.parentThread = parentThread;
	}

	public List<Map<String, Runnable>> getSubthreadList() {
		return subthreadList;
	}

	public void setSubthreadList(List<Map<String, Runnable>> subthreadList) {
		this.subthreadList = subthreadList;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
