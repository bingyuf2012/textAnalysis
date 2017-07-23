package text.analyse.dao;

import java.util.List;

import text.analyse.model.Tasks;
import text.analyse.model.UnTaskResult;

public interface TasksDao {
	/**
	 * @功能：向任务表中新添加一条任务
	 * @param tasks
	 */
	public abstract void addTask(Tasks tasks);

	/**
	 * @功能：更新任务开始时间
	 * @param tasks
	 */
	public abstract void updateTaskStarttime(Tasks tasks);

	/**
	 * @功能：更新进度条信息
	 * @param tasks
	 */
	public abstract void updateProgress(Tasks tasks);

	/**
	 * @功能：更新下载完成时间
	 * @param tasks
	 */
	public abstract void updateProgressend(Tasks tasks);

	/**
	 * @功能：获取任务表中的最大 id
	 * @return
	 */
	public abstract int selectMaxTaskID();

	/**
	 * @功能：根据用户信息和任务id获取任务的开始时间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract String selectStarttime(Tasks tasks);

	/**
	 * @功能：根据用户信息，获取已经保存但是仍未开始下载的任务id
	 * @return
	 */
	public abstract int selectUnTaskID(UnTaskResult untaskresult);

	/**
	 * @功能：根据用户信息，获取已经开始的下载任务id
	 * @return
	 */
	public abstract int selecTaskid(UnTaskResult untaskresult);

	/**
	 * @功能：检索当前用户的所有下载任务数
	 * @param tasks
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract List selectTasksAsUser(Tasks tasks);

	/**
	 * @功能：获取某个下载任务的keyword 和开始时间
	 * @param tasks
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract List selecZipdir(Tasks tasks);

	/**
	 * @功能：根据任务id获取saveinfo表中的信息
	 * @param tasks
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract List selectTask(Tasks tasks);

	/**
	 * @功能：更新生成索引时间
	 * @param tasks
	 */
	public abstract void updateIndextime(Tasks tasks);

	/**
	 * @功能：更新打包时间
	 * @param tasks
	 */
	public abstract void updatePackagetime(Tasks tasks);

	/**
	 * @功能：获取正在下载的任务信息
	 * @param tasks
	 * @return
	 */
	public abstract List selectStartTasks(Tasks tasks);

	/**
	 * @功能：获取进度条信息
	 * @param tasks
	 * @return
	 */
	public abstract String selectProgress(Tasks tasks);

	/**
	 * @功能：获取打包时间
	 * @param tasks
	 * @return
	 */
	public abstract String selectPackagetime(Tasks tasks);

	/**
	 * @功能：根据任务开始时间获取任务信息
	 * @param starttime
	 * @return
	 */
	public abstract Tasks selectByStarttime(Tasks tasks);

	/**
	 * @功能：删除任务
	 * @param tasks
	 */
	public abstract void deleteTask(Tasks tasks);

	/**
	 * @功能：获取某个任务完成所经历的时间
	 * @param tid
	 * @return
	 */
	public abstract String diffTime(int tid);

	/**
	 * 根据任务id检索任务信息
	 * 
	 * @param tasks
	 * @return
	 */
	public abstract List selectIDTasks(int taskid);

	/**
	 * 根据任务id，检索任务的当前状态
	 * 
	 * @param taskid
	 *            任务id号
	 * @return
	 */
	public abstract int selectStatus(int taskid);

	public abstract void updateStatus(Tasks tasks);
}
