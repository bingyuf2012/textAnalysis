package text.analyse.cluster;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import text.analyse.model.DealtNews;
import text.analysis.utils.SegUtil;
import text.searchSDK.model.WebSearchResult;
import text.searchSDK.util.PrintConsole;

@Component
public class ETMClusterbak {
	@Autowired
	SegUtil segUtil;

	/**
	 * 过滤一些特殊字符
	 * 
	 * @param data
	 *            文章的标题和正文
	 * @return
	 */
	public String FilterData(String data) {
		data = data.substring(data.indexOf("|") + 1, data.length());
		data = data.replaceAll("@.*?:", "");
		data = data.replaceAll("\\pP|\\pS", "");
		return data;
	}

	/**
	 * 对分词后的文章进行处理
	 * 
	 * @param m_content
	 *            分词后的文章
	 * @return
	 */
	public String GetKeywords(String m_content) {
		String allTokens = "";
		String[] allWords = m_content.trim().split(" ");

		for (int j = 0; j < allWords.length; j++) {

			int pos1 = allWords[j].lastIndexOf("/");

			if (pos1 > 0) {
				String type = allWords[j].substring(pos1 + 1);
				String key = allWords[j].substring(0, pos1).replaceAll(" ", "").replaceAll("　", "");

				int cut = key.indexOf("/?");
				if (cut > -1)
					key = key.substring(cut + 2);

				PrintConsole.PrintLog("allWords[j]", allWords[j]);
				PrintConsole.PrintLog("type", type);
				PrintConsole.PrintLog("key", key);
				if (type.matches("u|c|p|w|d|r|f|z|q|o|mq")) {
					continue;
				}

				if (type.matches("NUM|TIM")) {
					continue;
				}

				if (key.length() == 1 || StopWordsFilter.isStopWord(key)) {
					continue;
				}

				if (type.equals("LOC") && (key.endsWith("市") || key.endsWith("省"))) {
					key = key.substring(0, key.length() - 1);
				}

				if (!key.contains("(")) {
					allTokens = allTokens + key + "/" + type + " ";
				}

			}
		}
		return allTokens;
	}

	/**
	 * 根据百度抓取的结果List集合进行 LDA分析
	 * 
	 * @param list
	 *            百度搜索结果List集合
	 * @param m_outputPath
	 *            设置存放矩阵数据的路径
	 * @param nK
	 *            设置分类数
	 * @param nTopicLable
	 *            设置保存前N个Label
	 */
	public void runLDA(List<WebSearchResult> list, String m_outputPath, int nK, int nTopicLable) {
		List<DealtNews> dealtNewsList = new ArrayList<DealtNews>();

		ArrayList<String> TopicArray = new ArrayList<String>();
		ArrayList<String> TokensArray = new ArrayList<String>();

		TopicArray.clear();
		TokensArray.clear();

		String allTokens = "";

		for (int i = 0; i < list.size(); i++) {
			String data = list.get(i).getTitle() + list.get(i).getContent();
			TopicArray.add(data);
			String fc = segUtil.segText(FilterData(data), true);

			allTokens = GetKeywords(fc);

			if (allTokens.equals(""))
				continue;
			TokensArray.add(allTokens);

			DealtNews news = new DealtNews();
			news.setTokens(allTokens);
			dealtNewsList.add(news);
		}
	}
}
