package text.analyse.dao;

import java.util.List;

import text.analyse.model.SaveInfo;

public interface SaveInfoDao {
	/**
	 * @功能：向saveinfo 表中新添加一条信息
	 * @param saveinfo
	 */
	public abstract void addSaveInfo(SaveInfo saveinfo);

	/**
	 * @功能：向saveinfo 表中更新一条已经保存但是还没下载的信息
	 * @param saveinfo
	 */
	public abstract void updateUnStartSaveInfo(SaveInfo saveinfo);

	/**
	 * @功能：获取当前 saveinfo 表中最大的id
	 * @return int
	 */
	public abstract int selectMaxInfoID();

	/**
	 * @功能：根据保存的id检索保存的任务信息
	 * @param saveinfo
	 * @return
	 */
	public abstract List selectSaveInfo(SaveInfo saveinfo);

	/**
	 * @功能：删除任务的详细信息
	 * @param saveinfo
	 */
	public abstract void deleteSaveInfo(SaveInfo saveinfo);
}
