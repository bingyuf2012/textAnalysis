package text.searchSDK.search;

import java.util.List;

import text.searchSDK.model.NewsSearchResult;

public interface NewsSearch {
	/**
	 * Get NewsSearchResult according to keyWord and k
	 * 
	 * @param keyWord
	 *            The keyword of search engine
	 * @param k
	 *            The count of NewsSearchResult in its result set
	 * @return NewsSearchResult
	 */
	List<NewsSearchResult> search(String keyWord, int k);
}
