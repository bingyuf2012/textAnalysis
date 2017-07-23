package text.searchSDK.search;

import java.util.List;

import text.searchSDK.model.WebSearchResult20130604;

/**
 * The interface of WebSearch
 * 
 * @author Leo
 * 
 */
public interface WebSearch20130604 {

	/**
	 * Get WebSearchResult according to keyWord and k
	 * 
	 * @param keyWord
	 *            The keyword of search engine
	 * @param k
	 *            The count of pages
	 * @return WebSearchResult
	 */
	List<WebSearchResult20130604> search(String keyWord, int k);
}
