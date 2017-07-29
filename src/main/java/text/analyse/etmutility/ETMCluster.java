package text.analyse.etmutility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import text.analyse.struct.etm.AllTopicsETM;
import text.analyse.struct.etm.TECountModel;
import text.analyse.struct.lda.AllEntity;
import text.analyse.struct.lda.NewsDetail;
import text.analyse.struct.lda.RelationEE;
import text.analyse.struct.lda.RelationTE;
import text.analyse.struct.lda.RelationTT;
import text.analyse.struct.lda.TopWords;
import text.analyse.struct.lda.TopicSet;
import text.analyse.utility.Word;
import text.analysis.utils.ConstantUtil;
import text.analysis.utils.SegUtil;
import text.searchSDK.model.NewsSet;
import text.searchSDK.model.WebSearchResult;
import text.thu.keg.smartsearch.jgibbetm.ETM;
import text.thu.keyword.extract.ExtFactory;

@Component
public class ETMCluster {
	private Logger LOG = LoggerFactory.getLogger(ETMCluster.class);

	@Autowired
	SegUtil segUtil;
	@Autowired
	ETM etm;
	@Autowired
	ExtFactory extFactory;

	public static int maxDocLength = -1;
	public static boolean enableExtraLabel = false;
	public static double maxOverLapRate = 0.5;
	private AllTopicsETM allTopics;

	public AllTopicsETM GetAllTopicsETM() {
		return allTopics;
	}

	private List<RelationEE> relee;
	private List<NewsDetail> newsDetails;
	private Map<String, String> lbstempmap;
	public List<String> labelList;

	@PostConstruct
	private void init() {
		allTopics = new AllTopicsETM();
		relee = new ArrayList<RelationEE>();
		newsDetails = new ArrayList<NewsDetail>();
		lbstempmap = new HashMap<String, String>();
		labelList = new ArrayList<String>();
	}

	/**
	 * 过滤特殊字符
	 * 
	 * @param data
	 *            需要过滤的字符串
	 * @return
	 */
	/*public String FilterData(String data) {
		data = data.substring(data.indexOf("|") + 1, data.length());
		data = data.replaceAll("[@.*?:]", "。");
		data = data.replaceAll("\\pP|\\pS", "。");
		return data.toUpperCase();
	}*/

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
	public AllTopicsETM runLDA(String keyword, List<WebSearchResult> list, String m_outputPath, int nK,
			int nTopicLable) {
		String allTokens = "";

		File dirFile = new File(m_outputPath);
		if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
			boolean createDir = dirFile.mkdirs();
			if (!createDir) {
				LOG.info(
						"fail to create the specified path folder for saving the matrix, make sure you have the authority to create files or check the file path and make sure it's correct");
			}
		} else if (dirFile.exists() && !(dirFile.isDirectory())) {
			LOG.info("the specified path already contains a same name file, can't create the same name directory");
		}

		/*
		 * 以下为Topic对应的具体news的内容输出到文件
		 */
		String documentdir = m_outputPath + ConstantUtil.DOC_FILENAME;
		File file = new File(documentdir);
		String tokendir = m_outputPath + ConstantUtil.DOC_TOKEN_FILENAME;
		File fileTokens = new File(tokendir);

		PrintWriter pw = null;
		PrintWriter pwTokens = null;

		String fc = null;
		try {
			pw = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), ConstantUtil.UTF8)));
			pwTokens = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTokens), ConstantUtil.UTF8)));
			// 在 token 文件中输出新闻文档数据集的大小
			pwTokens.write(list.size() + "\n");
			for (int i = 0; i < list.size(); i++) {
				NewsDetail newsDetail = new NewsDetail();
				newsDetail.setTitle(list.get(i).getTitle());
				newsDetail.setUrl(list.get(i).getUrl());
				newsDetail.setContent(list.get(i).getContent());
				newsDetail.setTime(list.get(i).getTime());
				newsDetail.setType(list.get(i).getType());
				newsDetail.setWeights(list.get(i).getWeights());
				newsDetail.setFresh(list.get(i).getFresh());
				newsDetails.add(newsDetail);
				String data = list.get(i).getTitle() + list.get(i).getContent();
				fc = segUtil.segText(data.toUpperCase(), true);
				allTokens = segUtil.getKeywords(fc);

				if (allTokens.equals(""))
					continue;

				String news = list.get(i).getTitle() + "@@@@" + list.get(i).getContent() + "@@@@" + list.get(i).getUrl()
						+ "@@@@" + list.get(i).getTime() + "@@@@" + list.get(i).getType() + "@@@@"
						+ list.get(i).getWeights() + "@@@@" + list.get(i).getFresh();

				pw.write(news + "\n");
				pwTokens.write(allTokens + "\n");
				pw.flush();
				pwTokens.flush();
			}
			pw.close();
			pwTokens.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 对文档进行模型处理
		etm.est(ConstantUtil.DOC_TOKEN_FILENAME, m_outputPath, newsDetails.size());
		return RunUtility(m_outputPath, keyword, tokendir, list);
	}

	@SuppressWarnings("unchecked")
	public AllTopicsETM RunUtility(String outputdir, String keyword, String tokendir, List<WebSearchResult> list) {
		// AllTopicsETM allTopics = new AllTopicsETM();
		// String filename = outputdir + "model-final.tEAssign";
		String filename = outputdir + ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_TEASSIGN;
		// String filename1 = outputdir + "model-final.xEAssign";
		ETMUtility utility = new ETMUtility(filename, list);
		relee = utility.relEE(outputdir);
		try {
			ArrayList<TopicSet> topicsets = utility.getTopicSet(outputdir, newsDetails);
			allTopics.setTopics(topicsets);
			allTopics.setRelee(relee);

			ArrayList<RelationTT> relTT = new ArrayList<RelationTT>();
			Map<Integer, TECountModel> te1 = utility.getTeCountMap(filename);
			Map<Integer, TECountModel> te2 = utility.getTeCountMap(filename);
			relTT = utility.getTTRelation(te1, te2, outputdir);
			allTopics.setReltt(relTT);
			// ----------------------------------------------------------
			ArrayList<TopWords> topWords = new ArrayList<TopWords>();
			topWords = utility.getTopWords(outputdir);// topic的前x个label

			luluTopicname(topWords);

			for (int i = 0; i < topWords.size(); i++) {
				String labelstr = topWords.get(i).getLabelWords();
				if (!labelstr.contains("###")) {
					String[] topwordArray = labelstr.split(" ");
					String label = topwordArray[0].split("/")[0] + topwordArray[1].split("/")[0];

					LOG.info("label = {} ", label);
					String labels = "";

					for (int j = 0; j < topwordArray.length; j++) {
						labels += topwordArray[j].split("/")[0] + " ";
					}
					labels = labels.trim();
					label += "###" + labels;
					TopWords topwords = topWords.get(i);
					topwords.setLabelWords(label);
				}
			}

			/* ExtFactory factory = new ExtFactory(); */
			// System.out.println("m_outputPath : " + m_outputPath);
			// NewsSet newsSet = factory.loadLDAphi(outputdir +
			// Constant.DOC_TOKEN_FILENAME);
			LOG.info("抽取关键词，开始加载 phi 矩阵文件..........");
			NewsSet newsSet = extFactory.loadLDAphi(outputdir + ConstantUtil.TOP_DOCS);
			LOG.info("抽取关键词，加载 phi 矩阵文件结束，开始抽取关键词...");
			List<TopWords> topWordsListkeywords = extFactory.outputKeywors(newsSet);
			LOG.info("抽取关键词完毕，关键词个数 = {} ", topWordsListkeywords.size());
			ArrayList<TopWords> topWordsResult = new ArrayList<TopWords>();
			for (int i = 0; i < topWordsListkeywords.size(); i++) {
				TopWords topWordstemp = new TopWords();
				topWordstemp.setTopicID(topWordsListkeywords.get(i).getTopicID());
				String label = topWords.get(i).getLabelWords().split("###")[0];
				label += "###" + topWordsListkeywords.get(i).getLabelWords();
				topWordstemp.setLabelWords(label);
				topWordsResult.add(topWordstemp);
			}
			allTopics.setTopWords(topWordsResult);
			LOG.info("重置话题标签完毕......");

			// allTopics.setTopWords(topWords);
			// lzxTopicname(topWords);

			// ----------------------------------------------------------
			LOG.info("获取实体列表...");
			AllEntity entity = new AllEntity();
			LOG.info("获取实体...");
			utility.loadWord(outputdir + ConstantUtil.ENTITY_MAP);
			List<Word> entitylist = utility.getEntityList(outputdir);
			entity = utility.GetEntityUsrDef(entitylist);// 获取实体
			allTopics.setEntity(entity);
			// ----------------------------------------------------------
			LOG.info("实体和话题之间的关系...");
			RelationTE relte = new RelationTE();
			Map<String, Integer> topiccountMap = utility.getTopicCountMap(outputdir);
			Map<String, Integer> entitycountMap = utility.getEntityCountMap(outputdir);
			relte = utility.getTERelation(topiccountMap, entitycountMap, entitylist);
			allTopics.setRelte(relte);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return allTopics;
	}

	// 露露程序获得分词类名
	private void luluTopicname(List<TopWords> topWords) {
		List[] newsListinfo = new List[topWords.size()];
		LOG.info("topWords.size() = {} ", topWords.size());
		for (int i = 0; i < topWords.size(); i++) {
			TopicSet ts = allTopics.getTopics().get(i);
			List<?> newslist = ts.getNewsDetail();
			newsListinfo[i] = new ArrayList();
			for (int z = 0; z < newslist.size(); z++) {
				NewsDetail newsdetail = (NewsDetail) newslist.get(z);
				String newsinfo = newsdetail.getTitle() + " " + newsdetail.getContent();
				newsListinfo[i].add(newsinfo);
			}

			System.out.print("");

			TopWords topwords = topWords.get(i);
			String[] toplabels = topwords.getLabelWords().split(" ");
			List<Word> list = new ArrayList<Word>();
			for (int j = 0; j < toplabels.length; j++) {
				String[] wordinfo = toplabels[j].split("/");
				if (wordinfo.length == 3) {
					// PrintConsole.PrintLog("wordinfo",
					// toplabels[j],"wordinfo.length",wordinfo.length,"");
					String key = wordinfo[0];
					String type = wordinfo[1];
					String value = wordinfo[2];
					Word word = new Word(key, type, Double.parseDouble(value));
					list.add(word);
				}
			}

			do_Topic_Lable(topWords.size(), i, 1, list, newsListinfo);

			LOG.info("Label Index = {} , labelList.size()", i, labelList.size());

			for (int a = 0; a < labelList.size(); a++) {
				LOG.info("labelList.get(a).toString() = {} ", labelList.get(a).toString());
				String topword = "";

				String[] toplabel = topwords.getLabelWords().split(" ");

				for (int b = 0; b < toplabel.length; b++) {
					String[] wordinfo = toplabels[b].split("/");
					topword += wordinfo[0] + " ";
				}

				String label = "";

				if (labelList.get(a).toString().contains("/") && labelList.get(a).toString().split(" ").length == 1) {
					String[] tws = labelList.get(a).toString().split(" ");
					label = tws[0].split("/")[0] + tws[1].split("/")[0];
				} else {
					label = labelList.get(a).toString();
				}

				topwords.setLabelWords(label + "###" + topword.trim());
			}
		}
		lbstempmap.clear();
	}

	private void do_Topic_Lable(int K, int id, int labelsize, List<Word> list, List[] newsList) {
		// List<String> labelist = new ArrayList<String>();
		TreeSet<Word> candTS = new TreeSet<Word>();
		List<Word> candList = new ArrayList<Word>();
		for (int i = 0; i < list.size(); i++) {
			Word w1 = (Word) list.get(i);
			for (int j = 0; j < list.size(); j++) {
				if (i == j) {
					continue;
				} else {
					Word w2 = (Word) list.get(j);
					if (w1.key.contains("清华") || w2.key.contains("清华"))
						continue;
					String str = w1.type + "_" + w2.type;

					String key = w1.key + w2.key;

					// ==========================================(b|s)_(vn|n|nz|ORG|LOC|nx|j)
					if (str.matches("(b|s)_(vn|n|nz|nx|j)")) {
						Word w = new Word(w1.key + w2.key, str, w1.value + w2.value);
						if (!lbstempmap.containsKey(key)) {
							candList.add(w);
						}
					}
					// ==========================================(a|ad)_(vn|n|j)
					else if (str.matches("(a|ad)_(vn|n|j)")) {
						Word w = new Word(w1.key + w2.key, str, w1.value + w2.value);
						// candList.add(w);
						if (!lbstempmap.containsKey(key)) {
							candList.add(w);
						}
					}
					// +++++++++++++++++++++++++++++++++++++++++++v_(n|ORG|LOC|j|vn)
					else if (str.matches("v_(n|j|vn)")) {
						Word w = new Word(w1.key + w2.key, str, w1.value + w2.value);
						// candList.add(w);
						if (!lbstempmap.containsKey(key)) {
							candList.add(w);
						}
					}
					// +++++++++++++++++++++++++++++++++++++++++++v_(n|ORG|LOC|j|vn)
					else if (str.matches("(n|j|vn)_v")) {
						Word w = new Word(w1.key + w2.key, str, w1.value + w2.value);
						// candList.add(w);
						if (!lbstempmap.containsKey(key)) {
							candList.add(w);
						}
					}
				}
			}
		}

		for (int i = 0; i < candList.size(); i++) {
			Word w = (Word) candList.get(i);
			double score = 0;
			for (int j = 0; j < newsList[id].size(); j++) {
				String str = (String) newsList[id].get(j);

				if (str.contains(w.key)) {
					score++;
				}
			}

			double num1 = 0, num2 = 0;
			for (int j = 0; j < K; j++) {
				if (j == id)
					continue;
				for (int k = 0; k < newsList[id].size(); k++) {
					String str = (String) newsList[id].get(k);
					num2++;
					if (str.contains(w.key)) {
						num1++;
					}
				}
			}

			score = score * Math.log((num2 + 1) / (num1 + 1)) * w.value;
			Word word = new Word(w.key, w.type, score);
			candTS.add(word);
		}

		Iterator<Word> it = candTS.iterator();

		int count = 0;
		// ------------------------mile
		String lbs = "";

		while (it.hasNext() && count < labelsize) {
			Word w = (Word) it.next();
			if (lbs.contains(w.key) || lbstempmap.containsKey(w.key))
				// if (lbs.contains(w.key))
				continue;
			if (count != 0) {
				lbs += "/" + w.key;
			} else {
				lbs = w.key;
				count++;
				lbstempmap.put(lbs, lbs);
				labelList.add(lbs);
				LOG.info("w.key = {} , w.value = {} , count = {} ", w.key, w.value, count);
			}
		}
	}

}
