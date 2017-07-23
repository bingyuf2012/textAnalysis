package text.analyse.dao;

import java.util.List;

import text.analyse.model.SearchResult;

public interface SearchResultDao {
	/**
	 * @功能： 添加一条新的搜索结果信息
	 * @param searchresult
	 */
	public abstract void addSearchResult(SearchResult searchresult);

	/**
	 * @功能：获取当前搜索信息表中的最大id
	 * @return
	 */
	public abstract int getMaxID();

	/**
	 * @功能：返回历史所有的搜索词
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract List<String> selectSearchWords();

	/**
	 * 向SearchResult表中添加基本信息id,uid,username,uip,searchword
	 * 
	 * @param searchresult
	 */
	public abstract void addSearchUserInfo(SearchResult searchresult);

	/**
	 * 向SearchResult表中添加知识库 lore的信息
	 * 
	 * @param searchresult
	 */
	public abstract void addSearchLoreInfo(SearchResult searchresult);

	/**
	 * 向SearchResult表中添加所有信息汇总的json
	 * 
	 * @param searchresult
	 */
	public abstract void addSearchJsonInfo(SearchResult searchresult);

	/**
	 * 根据id提取基本信息
	 * 
	 * @param searchresult
	 * @return
	 */
	public abstract SearchResult searchUserInfo(int sid);
}
