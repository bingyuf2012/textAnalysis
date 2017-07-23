package text.searchSDK.search;

import java.util.List;

import text.searchSDK.model.WebSearchResult;

/**
 * The interface of WebSearch
 * 
 * @author Leo
 * 
 */
public interface WebSearch {

	/**
	 * Get WebSearchResult according to keyWord and k
	 * 
	 * @param keyWord
	 *            The keyword of search engine
	 * @param k
	 *            The count of pages
	 * @return WebSearchResult
	 */
	List<WebSearchResult> search(String keyWord, int k);
}
