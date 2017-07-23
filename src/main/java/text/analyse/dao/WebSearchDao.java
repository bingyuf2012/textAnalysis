package text.analyse.dao;

import java.util.Map;

public interface WebSearchDao {
	/**
	 * @功能：获取浏览器搜索结果摘要content
	 * @param contentfont
	 * @param contentdiv
	 * @return
	 */
	public abstract String getContent(String contentfont, String contentdiv);

	/**
	 * @功能：获取浏览器搜索结果title
	 * @param title
	 * @return
	 */
	public abstract String getTitle(String title);

	/**
	 * @功能：获取浏览器搜索结果类型，pdf,doc,ppt,网页等
	 * @param title
	 * @param content
	 * @return
	 */
	public abstract String getType(String url, String title, String content);

	/**
	 * @功能：获取浏览器搜索结果网站的权重
	 * @param spanstr
	 * @param map
	 * @return
	 */
	public abstract String getWeights(String spanstr, Map map, String ratio);

	/**
	 * @功能：获取新鲜度
	 * @return
	 */
	public abstract String getFresh();
}
