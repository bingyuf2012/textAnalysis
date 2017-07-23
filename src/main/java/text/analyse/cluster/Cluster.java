package text.analyse.cluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import text.analyse.model.DealtNews;
import text.analyse.model.Topic;
import text.analyse.struct.lda.AllEntity;
import text.analyse.struct.lda.AllTopics;
import text.analyse.struct.lda.EEValue;
import text.analyse.struct.lda.NewsDetail;
import text.analyse.struct.lda.RelationEE;
import text.analyse.struct.lda.RelationTE;
import text.analyse.struct.lda.RelationTT;
import text.analyse.struct.lda.TopWords;
import text.analyse.struct.lda.TopicSet;
import text.analyse.struct.lda.WordStructSet;
import text.analyse.utility.HotEntity;
import text.analyse.utility.NewsSort;
import text.analyse.utility.TopicSort;
import text.analyse.utility.Utility;
import text.analyse.utility.Word;
import text.analysis.utils.SegUtil;
import text.searchSDK.model.WebSearchResult;
import text.searchSDK.util.Constant;
import text.searchSDK.util.HtmlSplit;
import text.searchSDK.util.PrintConsole;

@Component
public class Cluster {
	@Autowired
	SegUtil segUtil;

	public List<String> labelList = new ArrayList<String>();
	Map<String, String> lbstempmap = new HashMap<String, String>();
	// ArrayList<AllTopics> aTopic = new ArrayList<AllTopics> ();
	AllTopics allTopics = new AllTopics();
	ArrayList<String> allData = new ArrayList<String>();// 待分析的微博数据
	ArrayList<RelationEE> relee = new ArrayList<RelationEE>();
	public ArrayList<TopicSet> topics = new ArrayList<TopicSet>();// 存放TopicWords内容
	ArrayList<TopicSet> newTopics = new ArrayList<TopicSet>();// 存放TopicWords内容

	public List<String> wordvocabulary = new ArrayList<String>();// 存放所有的词
	public HashMap<String, Object> wordtfHM = new HashMap<String, Object>();// 计算LDA中的所有词的词频
	public HashMap<String, Object> worddfHM = new HashMap<String, Object>();// 倒排文档频率

	/**
	 * vocabulary size
	 */
	private int V;
	/**
	 * news number
	 */
	private int M;
	/**
	 * topic number
	 */
	private int K;
	/**
	 * LDA model parameter
	 */
	private double alpha;
	/**
	 * LDA model parameter
	 */
	private double beta;
	/**
	 * default iterations for LDA model
	 */
	public static int iterations = 2000;
	/**
	 * default sample lag for LDA model
	 */
	public static int sampleLag = 200;
	/**
	 * default topic words number
	 */
	public static int topicWordsNum = 20;
	/**
	 * default wordmap file name
	 */
	public static String wordMapName = Constant.WORD_MAP;
	/**
	 * default topicWords file name
	 */
	public static String topicWordName = "topicWords.txt";
	/**
	 * default phi matrix file name
	 */
	public static String phiMatrixName = "phi";
	/**
	 * default theta matrix file name
	 */
	public static String thetaMatrixName = "theta";
	/**
	 * LDA theta matrix
	 */
	private double[][] theta;
	/**
	 * LDA phi matrix
	 */
	private double[][] phi;
	/**
	 * phi and theta matrix, wordMap, topic words file saving path
	 */
	private String savePath;
	/**
	 * news represented in vector space by matrix
	 */
	private int[][] documents;
	/**
	 * news list for the news about to be clustered
	 */
	private List<DealtNews> dealtNewsList;
	/**
	 * store the most related words to a topic
	 */
	private Word[][] topicWordArray;
	/**
	 * store which news belong to the specified topic
	 */
	private ArrayList<Integer>[] newsBelongToTopic;

	/**
	 * 临时存储 topic 信息
	 */
	private ArrayList<Integer>[] newsBelongToTopictemp;

	/**
	 * wordmap list
	 */
	private List<String> vocabulary = new ArrayList<String>();

	/**
	 * cluster news in the dealtNews list via LDA model; before calling
	 * function, K, dealtNews, savePath must be initialized or an exception(or
	 * bug) will occur.
	 * 
	 * @return Topic : member variable words, producedTime and id are
	 *         set;ArrayList<Integer>:it contains the indexes of the news are
	 *         classified to this topic
	 * @throws IOException
	 */
	public HashMap<Topic, ArrayList<Integer>> clusterViaLDA() throws IOException {

		File dirFile = new File(savePath);
		if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
			boolean createDir = dirFile.mkdirs();
			if (!createDir) {
				PrintConsole.PrintLog(
						"fail to create the specified path folder for saving the matrix, make sure you have the authority to create files or check the file path and make sure it's correct",
						null);
				return null;
			}
		} else if (dirFile.exists() && !(dirFile.isDirectory())) {
			PrintConsole.PrintLog(
					"the specified path already contains a same name file, can't create the same name directory", null);
			return null;
		}

		topicWordArray = new Word[K][topicWordsNum];

		doLDA();

		doCluster();

		HashMap<Topic, ArrayList<Integer>> clusterResult = generateTopics();

		return clusterResult;

	}

	/**
	 * do the LDA process, create the wordmap,phi,theta and topic words(the most
	 * related words to a topic) file in the specified directory
	 * 
	 * @throws IOException
	 */
	public void doLDA() throws IOException {

		readNews();

		saveWordMap();

		alpha = 50.0 / K;
		beta = 0.01;

		GibbsSampler gs = new GibbsSampler(documents, V);
		gs.configure(iterations, sampleLag);
		gs.gibbs(K, alpha, beta);

		theta = gs.getTheta();
		phi = gs.getPhi();

		saveMatrix(theta, thetaMatrixName);
		saveMatrix(phi, phiMatrixName);

		// KL_complexity(phi);

		saveTopicWords();
	}

	public void KL_complexity(double[][] phi) throws IOException {
		double perplexity = 0.0;
		int nCount = 0;

		int m_topicnum = phi.length;
		int m_wordnum = phi[0].length;

		// BufferedWriter bw;
		PrintWriter bw;
		String filename = "d:\\test113.txt";
		// bw = new BufferedWriter(new FileWriter(filename));
		bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), Constant.UTF8)));

		for (int k = 0; k < m_topicnum; k++) {
			int l = k + 1;
			for (; l < m_topicnum; l++) {
				double p = 0.0;
				for (int w = 0; w < m_wordnum; w++) {
					if (phi[l][w] == 0)
						continue;

					double value = phi[k][w] * Math.log(phi[k][w] / phi[l][w]);
					bw.write(k + " " + l + " " + w + " " + value + "\n");
					p += (-value);

				}
				bw.write("求和: " + p + "\n");
				PrintConsole.PrintLog("p", p);
				perplexity += p;
				nCount++;
			}
		}
		bw.close();

		PrintConsole.PrintLog("nCount", nCount);
		PrintConsole.PrintLog("perplexity:", perplexity / nCount);
	}

	/**
	 * cluster news documents according to their topic distribution
	 */
	public void doCluster_old() {
		newsBelongToTopic = new ArrayList[K];

		for (int i = 0; i < newsBelongToTopic.length; i++) {
			newsBelongToTopic[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < M; i++) {
			double max = theta[i][0];
			int index = 0;
			for (int j = 1; j < K; j++) {
				if (theta[i][j] > max) {
					max = theta[i][j];
					index = j;
				}
			}
			newsBelongToTopic[index].add(i);
		}

		// sortNews();
	}

	/**
	 * cluster news documents according to their topic distribution
	 */
	public void doCluster() {
		newsBelongToTopic = new ArrayList[K];
		newsBelongToTopictemp = new ArrayList[K];

		for (int i = 0; i < newsBelongToTopictemp.length; i++) {
			newsBelongToTopic[i] = new ArrayList<Integer>();
			newsBelongToTopictemp[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < M; i++) {
			double max = theta[i][0];
			int index = 0;
			for (int j = 1; j < K; j++) {
				if (theta[i][j] > max) {
					max = theta[i][j];
					index = j;
				}
			}
			newsBelongToTopictemp[index].add(i);
		}

		sortNews();
	}

	/**
	 * @功能：给 topic 下的News按照相关度 排序
	 */
	public void sortNews() {
		for (int i = 0; i < K; i++) {
			List<NewsSort> newslist = new ArrayList<NewsSort>();
			for (int j = 0; j < newsBelongToTopictemp[i].size(); j++) {
				int index = newsBelongToTopictemp[i].get(j);
				Double score = theta[newsBelongToTopictemp[i].get(j)][i];
				NewsSort ns = new NewsSort(index, score);
				newslist.add(ns);
			}
			Collections.sort(newslist, NewsSort.comparator);

			// display(newslist);

			for (NewsSort s : newslist) {
				newsBelongToTopic[i].add(s.index);
			}
		}

		sortTopic();
	}

	/**
	 * @功能：对 topic 进行相关度排序
	 */
	public void sortTopic() {
		ArrayList<TopicSort> tslist = new ArrayList<TopicSort>();
		for (int i = 0; i < K; i++) {
			PrintConsole.PrintLog("size :", i);
			PrintConsole.PrintLog("newsBelongToTopic[i].size() :", newsBelongToTopic[i].size());
			Double d = 0.00;
			for (int j = 0; j < M; j++) {
				d += theta[j][i];
			}
			TopicSort ts = new TopicSort(newsBelongToTopic[i], d);
			tslist.add(ts);
		}
		Collections.sort(tslist, TopicSort.comparator);

		for (int i = 0; i < K; i++) {
			newsBelongToTopic[i] = tslist.get(i).list;
		}

		topicdisplay(tslist);
	}

	/**
	 * @功能：显示排序后sortNews的结果
	 */
	public void newsdisplay(List<NewsSort> newslist) {
		for (NewsSort s : newslist)
			PrintConsole.PrintLog("s.index", s.index);
	}

	/**
	 * @功能：显示排序后topicNews的结果
	 */
	public void topicdisplay(List<TopicSort> topiclist) {
		for (TopicSort t : topiclist)
			PrintConsole.PrintLog("t", t);
	}

	/**
	 * build the cluster results
	 * 
	 * @return Topic : member variable words, producedTime and id are
	 *         set;ArrayList<Integer>:it contains the indexes of the news are
	 *         classified to this topic
	 */
	public HashMap<Topic, ArrayList<Integer>> generateTopics() {
		HashMap<Topic, ArrayList<Integer>> result = new HashMap<Topic, ArrayList<Integer>>();
		for (int i = 0; i < K; i++) {
			int id = i;
			Date date = new Date();
			String str = null;
			for (int j = 0; j < topicWordsNum; j++) {
				str += vocabulary.get(topicWordArray[i][j].id) + " ";
			}
			Topic topic = new Topic();
			topic.setId(id);
			topic.setProducedTime(date);
			topic.setWords(str);
			result.put(topic, newsBelongToTopic[i]);
		}
		return null;
	}

	/**
	 * read news list and represent them in vector space
	 */
	private void readNews() {
		M = dealtNewsList.size();
		documents = new int[M][];
		HashMap<String, Integer> wordHashmap = new HashMap<String, Integer>();
		int wordIndex = -1;
		int newsIndex = -1;
		for (int i = 0; i < dealtNewsList.size(); i++) {
			String[] fields = dealtNewsList.get(i).getTokens().split(" ");
			int length = fields.length;
			newsIndex++;
			documents[newsIndex] = new int[length];
			for (int j = 0; j < length; j++) {
				String word = fields[j].trim();
				int index;
				if (!wordHashmap.containsKey(word)) {
					wordIndex++;
					vocabulary.add(word);
					wordHashmap.put(word, wordIndex);
					index = wordIndex;
				} else {
					index = (Integer) wordHashmap.get(word);
				}
				documents[newsIndex][j] = index;
			}
		}
		V = vocabulary.size();
		topicWordsNum = topicWordsNum < V ? topicWordsNum : V;
	}

	/**
	 * save the word mapping relation into the file
	 */
	private void saveWordMap() {

		File file = new File(savePath + File.separator + wordMapName);
		// BufferedWriter bw;
		PrintWriter bw;
		try {
			// bw = new BufferedWriter(new FileWriter(file));
			bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Constant.UTF8)));
			bw.write(V + "\n");
			for (int i = 0; i < V; i++) {
				bw.write(vocabulary.get(i) + " " + i + "\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * save the phi or theta matrix into the file
	 * 
	 * @param d
	 *            matrix to be saved
	 * @param name
	 *            saved file name
	 */
	private void saveMatrix(double[][] d, String name) {
		File file = new File(savePath + File.separator + name);
		// BufferedWriter bw;
		PrintWriter bw;
		try {
			// bw = new BufferedWriter(new FileWriter(file));
			bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Constant.UTF8)));
			for (int i = 0; i < d.length; i++) {
				for (int j = 0; j < d[i].length; j++) {
					bw.write(d[i][j] + " ");
				}
				// bw.newLine();
				bw.write("\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTopicWords() {

	}

	public void getTopicWords() {

	}

	/**
	 * save the most related words to the topics into the file
	 */
	private void saveTopicWords() {

		// ArrayList<TopicSet> topics = new ArrayList<TopicSet>();

		int topicID = 0;
		File file = new File(savePath + File.separator + topicWordName);
		// BufferedWriter bw;
		PrintWriter bw;
		try {
			// bw = new BufferedWriter(new FileWriter(file));
			bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Constant.UTF8)));

			for (int i = 0; i < phi.length; i++) {
				TreeSet<Word> ts = new TreeSet<Word>();

				for (int j = 0; j < phi[i].length; j++)
					ts.add(new Word(j, phi[i][j]));
				Iterator<Word> it = ts.iterator();

				bw.write(topicID + ":");
				// bw.newLine();
				bw.write("\n");
				int count = 0;

				TopicSet topicSet = new TopicSet();
				ArrayList<WordStructSet> speIssueSet = new ArrayList<WordStructSet>();
				topicSet.setLabelName(topicID);

				while (it.hasNext() && (count < topicWordsNum)) {
					Word w = (Word) it.next();
					topicWordArray[i][count] = w;
					bw.write((String) vocabulary.get(w.id) + "|" + String.valueOf(w.value));

					WordStructSet wordSet = new WordStructSet((String) vocabulary.get(w.id), w.value);
					speIssueSet.add(wordSet);
					topicSet.setSpeIssueSet(speIssueSet);

					// bw.newLine();
					bw.write("\n");
					count++;
				}

				// bw.newLine();
				bw.write("\n");
				topicID++;

				topics.add(topicSet);
			}
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public List<DealtNews> getDealtNews() {
		return dealtNewsList;
	}

	public void setDealtNews(List<DealtNews> dealtNewsList) {
		this.dealtNewsList = dealtNewsList;
	}

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
				/*
				 * 
				 * if(type.equals("nt")){ type = "ORG"; }
				 * 
				 * if(type.equals("ns")){ type = "LOC"; }
				 * 
				 * if(type.equals("nr")){ type = "PER"; }
				 */

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

		// call example�?
		// Step1:initialize savePath, topic number(K), dealt news
		// list(dealtNewsList)
		// Step2:call function clusterViaLDA and return the answer
		// Cluster cluster = new Cluster();
		try {
			setSavePath(m_outputPath);
			setK(nK);
			setDealtNews(dealtNewsList);
			clusterViaLDA();
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * 以下为Topic对应的具体news的内容输出到文件
		 */
		File file = new File(m_outputPath + "TopicToNews");
		File fileTokens = new File(m_outputPath + "TokensToNews");

		// BufferedWriter bw;
		// BufferedWriter bwTokens;
		PrintWriter bw;
		PrintWriter bwTokens;
		try {
			// bw = new BufferedWriter(new FileWriter(file));
			// bwTokens = new BufferedWriter(new FileWriter(fileTokens));
			bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Constant.UTF8)));
			bwTokens = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTokens), Constant.UTF8)));
			for (int k = 0; k < nK; k++) {
				ArrayList<NewsDetail> newsDetailSet = new ArrayList<NewsDetail>();
				// List testlist = new ArrayList<NewsDetail>();
				int nCount = newsBelongToTopic[k].size();
				bw.write("Topic " + k + " size:" + nCount + "\n");
				bwTokens.write("Topic " + k + " size:" + nCount + "\n");
				for (int j = 0; j < nCount; j++) {
					NewsDetail newsDetail = new NewsDetail();
					int nIndex = newsBelongToTopic[k].get(j);

					newsDetail.setTitle(list.get(nIndex).getTitle());
					newsDetail.setUrl(list.get(nIndex).getUrl());
					newsDetail.setContent(list.get(nIndex).getContent());
					newsDetail.setTime(list.get(nIndex).getTime());
					newsDetail.setType(list.get(nIndex).getType());
					newsDetail.setWeights(list.get(nIndex).getWeights());

					newsDetailSet.add(newsDetail);

					String strTmp = (String) TopicArray.get(newsBelongToTopic[k].get(j));
					bw.write(strTmp + "\n");

					String strTokens = (String) TokensArray.get(newsBelongToTopic[k].get(j));
					bwTokens.write(strTokens + "\n");
				}
				topics.get(k).setNewsDetail(newsDetailSet);

				bw.write("#####" + "\n");
				bwTokens.write("#####" + "\n");
			}
			bw.close();
			bwTokens.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		RunUtility(m_outputPath, nTopicLable);

	}

	public String FilterData(String data) {
		data = data.substring(data.indexOf("|") + 1, data.length());
		data = data.replaceAll("@.*?:", "");
		data = data.replaceAll("\\pP|\\pS", "。");
		return data.toUpperCase();
	}

	public int[][] readToken2Matrix(List<DealtNews> dealtNewsList) throws InterruptedException {

		HashMap<String, Integer> vhashmap = new HashMap<String, Integer>();
		int vindex = 0;
		int d[][] = null;
		int count = -1;

		d = new int[dealtNewsList.size()][];
		for (int k = 0; k < dealtNewsList.size(); k++) {
			String strTmp = dealtNewsList.get(k).getTokens().toString();
			String[] fields = strTmp.split(" ");// 把一条新闻的词汇按空格区分，映射到字符串数组中。
			int length = fields.length;// 计算一条新闻的词条个数
			count++;
			d[count] = new int[length];
			PrintConsole.PrintLog("reading document:", count);
			for (int i = 0; i < length; i++) {
				String word = fields[i].trim();
				int index;
				if (!vhashmap.containsKey(word)) {
					vhashmap.put(word, vindex);// 存放该词条的值和Index
					index = vindex;// 记录索引值
					vindex++;// Index值自增
				} else {
					index = (Integer) vhashmap.get(word);
				}
				d[count][i] = index;// d矩阵中存放某一个词的索引
			}
		}

		for (int t = 0; t < vhashmap.size(); t++)
			wordvocabulary.add("");

		Iterator<Entry<String, Integer>> it = vhashmap.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry<String, Integer> entry = (java.util.Map.Entry<String, Integer>) it.next();
			String key = (String) entry.getKey();
			int value = (Integer) entry.getValue();
			wordvocabulary.set(value, key);
		}
		PrintConsole.PrintLog("共有词:", vhashmap.size());
		Thread.sleep(10000);
		PrintConsole.PrintLog("词目读入完毕", null);

		int[] tb1 = new int[wordvocabulary.size()];
		int[] tb2 = new int[wordvocabulary.size()];
		for (int i = 0; i < d.length; i++) {
			HashMap<Integer, Integer> hashmap = new HashMap<Integer, Integer>();
			for (int j = 0; j < d[i].length; j++) {
				tb1[d[i][j]]++;// 计算词频
				if (!hashmap.containsKey(d[i][j])) {
					tb2[d[i][j]]++;// 统计倒排文档
					hashmap.put(d[i][j], 1);
				}
			}
		}
		PrintConsole.PrintLog("词数统计完毕", null);
		for (int i = 0; i < tb1.length; i++) {
			String wd = (String) wordvocabulary.get(i);
			wordtfHM.put(wd, tb1[i]);
			worddfHM.put(wd, tb2[i]);
		}
		PrintConsole.PrintLog("计入hashmap完毕", null);
		if (dealtNewsList.size() != (count + 1)) {
			PrintConsole.PrintLog("读取的文档数目不正确！！！", null);
			return null;
		} else {
			return d;
		}

	}

	public double computePerplexity() {
		double perplexity = 0.0;
		double denominator = 0.0;
		double numerator = 0.0;
		double perp = 0.0;

		for (int m = 0; m < M; m++) {
			int Nd = documents[m].length;
			if (Nd == 0)
				PrintConsole.PrintLog("length:", Nd);
			perp = 0.0;
			double p = 0.0;
			for (int n = 0; n < Nd; n++) {
				int w = documents[m][n];
				for (int k = 0; k < K; k++) {
					p += phi[k][w] * theta[m][k];
				}

				perp += Math.log(p);
			}
			numerator = perp;
			denominator = Nd;
			perplexity += Math.exp(-numerator / denominator);
		}

		// perplexity = exp(-numerator/denominator);*/
		return perplexity;
	}

	public void RunUtility(String m_outputPath, int nTopicLable) {

		try {
			newTopics = (ArrayList<TopicSet>) topics.clone();
			allTopics.setTopics(newTopics);
			loadData(m_outputPath);
			allTopics.setRelee(relee);

			ArrayList<RelationTT> relTT = new ArrayList<RelationTT>();
			Utility utility = new Utility(m_outputPath + "phi");
			PrintConsole.PrintLog("读取phi矩阵...", null);
			utility.readPhiMatrix(m_outputPath + "phi");// 读取phi矩阵
			PrintConsole.PrintLog("计算话题间相似度...", null);
			relTT = utility.topic_similarity();// 计算话题之间相似度
			allTopics.setReltt(relTT);
			// ----------------------------------------------------------
			ArrayList<TopWords> topWords = new ArrayList<TopWords>();
			PrintConsole.PrintLog("读取Constant.WORD_MAP...", null);
			utility.loadWord(m_outputPath + Constant.WORD_MAP);// 读取wordmap.txt
			PrintConsole.PrintLog("得到topic的前x个label...", null);
			topWords = utility.topic_lable_Candicate(nTopicLable);// topic的前x个label
			// lzxTopicname(topWords);

			luluTopicname(topWords);
			allTopics.setTopWords(topWords);
			// ----------------------------------------------------------
			PrintConsole.PrintLog("获取实体列表...", null);
			AllEntity entity = new AllEntity();
			PrintConsole.PrintLog("获取实体...", null);
			entity = utility.GetEntity();// 获取实体
			allTopics.setEntity(entity);
			// ----------------------------------------------------------
			PrintConsole.PrintLog("实体和话题之间的关系...", "");
			RelationTE relte = new RelationTE();
			relte = utility.saveTop100_entity_topic_Relation();
			allTopics.setRelte(relte);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 露露程序获得分词类名
	@SuppressWarnings("unchecked")
	private void luluTopicname(List<TopWords> topWords) {
		List[] newsListinfo = new List[topWords.size()];
		PrintConsole.PrintLog("topWords.size()", topWords.size());
		for (int i = 0; i < topWords.size(); i++) {
			TopicSet ts = allTopics.getTopics().get(i);
			List<?> newslist = ts.getNewsDetail();
			newsListinfo[i] = new ArrayList();
			for (int z = 0; z < newslist.size(); z++) {
				NewsDetail newsdetail = (NewsDetail) newslist.get(z);
				String newsinfo = newsdetail.getTitle() + " " + newsdetail.getContent();
				newsListinfo[i].add(newsinfo);
			}

			TopWords topwords = topWords.get(i);
			String[] toplabels = topwords.getLabelWords().split(" ");
			List<Word> list = new ArrayList<Word>();
			for (int j = 0; j < toplabels.length; j++) {
				String[] wordinfo = toplabels[j].split("/");
				String key = wordinfo[0];
				String type = wordinfo[1];
				String value = wordinfo[2];
				Word word = new Word(key, type, Double.parseDouble(value));
				list.add(word);
			}
			do_Topic_Lable(topWords.size(), i, 1, list, newsListinfo);

			PrintConsole.PrintLog("i", i);
			PrintConsole.PrintLog("labelList.size()", labelList.size());

			for (int a = 0; a < labelList.size(); a++) {
				PrintConsole.PrintLog("labelList.get(a).toString()", labelList.get(a).toString());
				String topword = "";

				String[] toplabel = topwords.getLabelWords().split(" ");

				for (int b = 0; b < toplabel.length; b++) {
					String[] wordinfo = toplabels[b].split("/");
					topword += wordinfo[0] + " ";
				}

				topwords.setLabelWords(labelList.get(a).toString() + "###" + topword.trim());
			}
		}
		lbstempmap.clear();
	}

	/*@SuppressWarnings("unused")
	private void lzxTopicname(List<TopWords> topWords) throws Exception {

		List<String[]> newsList = new ArrayList<String[]>();

		List<String[]> toplabelist = new ArrayList<String[]>();
		for (int i = 0; i < topWords.size(); i++) {
			TopicSet ts = allTopics.getTopics().get(i);
			List<?> newslist = ts.getNewsDetail();
			String[] newsarray = new String[newslist.size()];
			for (int z = 0; z < newslist.size(); z++) {
				NewsDetail newsdetail = (NewsDetail) newslist.get(z);
				String newsinfo = newsdetail.getTitle().replaceAll("@@@@", "") + "@@@@"
						+ newsdetail.getContent().replaceAll("@@@@", "");
				HtmlSplit htmlSplit = new HtmlSplit();
				// newsinfo = CommonUtil.SplitHtml(newsinfo);
				newsinfo = htmlSplit.SplitHtml(newsinfo);
				// News news = new
				// News(newsdetail.get_title(),newsdetail.get_content());
				newsarray[z] = newsinfo;
			}

			TopWords topwords = topWords.get(i);
			String[] toplabels = topwords.getLabelWords().split(" ");
			newsList.add(newsarray);

			for (int z = 0; z < toplabels.length; z++) {
				toplabels[z] = toplabels[z].split("/")[0];
			}

			toplabelist.add(toplabels);
		}

		Miner.enableExtraLabel = false;
		// 最大文档长度，用于减少分析时间
		Miner.maxDocLength = 100;
		// 标签的最大重叠率
		Miner.maxOverLapRate = 0.45;
		ArrayList<String> labelslist = Miner.doLabeling(newsList, toplabelist);

		for (int a = 0; a < labelslist.size(); a++) {
			TopWords topwords = topWords.get(a);
			topwords.setLabelWords(labelslist.get(a).toString().replaceAll("_", ""));
		}
	}*/

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
				PrintConsole.PrintLog("w.key", w.key);
				PrintConsole.PrintLog("w.value", w.value);
				PrintConsole.PrintLog("count", count);
			}
		}
	}

	public AllTopics GetAllTopics() {
		return allTopics;
	}

	public void ClearNewTopic() {
		topics.clear();
	}

	public void loadData(String filename) {
		ArrayList dataArray = new ArrayList();

		int nCount = 0;
		try {
			String m_inputFile = filename + "TokensToNews";
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(m_inputFile), Constant.UTF8));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (line.equals("#####")) {
					dealEntity(dataArray, nCount);
					dataArray.clear();
					nCount++;
				} else {
					if (line.contains("Topic"))
						continue;
					dataArray.add(line);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dealEntity(ArrayList dataArray, int nCount) {
		// 定义存放PER,LOC和ORG的list
		Hashtable<String, Word> perHT = new Hashtable<String, Word>();
		Hashtable<String, Word> locHT = new Hashtable<String, Word>();
		Hashtable<String, Word> orgHT = new Hashtable<String, Word>();
		List<HotEntity> hotList = new ArrayList<HotEntity>();//

		int topicID = 0;
		List<Word> entityList = new ArrayList<Word>();// 存放一个话题中的三种实体：PER、ORG、LOC
		for (int i = 0; i < dataArray.size(); i++) {
			String[] words = dataArray.get(i).toString().split(" ");// 获得某一个话题下某篇新闻文档的词条
			List<String> filtList = new ArrayList<String>();// 存放一条新闻中所有不重复的词条

			HotEntity hotentity = new HotEntity();// 存放一个话题下某个文档的3种热点实体
			if (hotList.contains(hotentity)) {
				hotentity = (HotEntity) hotList.get(hotList.indexOf(hotentity));
			}
			for (int k = 0; k < words.length; k++) {
				if (!filtList.contains(words[k]))
					filtList.add(words[k]);
				else
					continue;
				String key = words[k].substring(0, words[k].indexOf("/"));// 获取词条的值
				String type = words[k].substring(words[k].indexOf("/") + 1);// 获取词条的类型

				// 过滤掉不是PER、LOC和ORG的词条
				if (!(type.equals("nr") || type.equals("ns") || type.equals("nt")))
					continue;

				// 过滤一些不正确的命名实体
				// if(MyFilter.filtlist.contains(words[k]))
				// continue;
				// if(words[k].contains("日电")) continue;

				// 提取该topic下面所有的命名实体以便于后边进行关联提取
				Word tmp1 = new Word(words[k]);
				if (!entityList.contains(tmp1)) {
					tmp1.docs.put(i, null);// 存放词条对应的文档ID
					entityList.add(tmp1);
				} else {
					Word temp2 = (Word) entityList.get(entityList.indexOf(tmp1));
					temp2.docs.put(i, null);
				}

				if (type.equals("nr")) {
					Word word = new Word(key, "nr");
					hotentity.addEntity(new Word(key, "nr"));
					if (!perHT.containsKey(key)) {
						word.value = 1;
						perHT.put(key, word);
					} else {
						Word w = (Word) perHT.get(key);
						w.value += 1;
					}
				} else if (type.equals("ns")) {
					Word word = new Word(key, "ns");
					hotentity.addEntity(new Word(key, "ns"));
					if (!locHT.containsKey(key)) {
						word.value = 1;
						locHT.put(key, word);
					} else {
						Word w = (Word) locHT.get(key);
						w.value += 1;
					}

				} else if (type.equals("nt")) {
					Word word = new Word(key, "nt");
					hotentity.addEntity(new Word(key, "nt"));
					if (!orgHT.containsKey(key)) {
						word.value = 1;
						orgHT.put(key, word);
					} else {
						Word w = (Word) orgHT.get(key);
						w.value += 1;
					}
				}
			}
			if (!hotList.contains(hotentity)) {
				hotList.add(hotentity);// 存放一个话题中的热点实体，并且按照时间顺序排列
			}

		}

		RelationEE relEE = new RelationEE();
		relEE.setTopicID(topicID);
		ArrayList<EEValue> reltt = new ArrayList<EEValue>();

		for (int j = 0; j < entityList.size(); j++) {
			Word w1 = (Word) entityList.get(j);
			for (int k = j + 1; k < entityList.size(); k++) {

				Word w2 = (Word) entityList.get(k);

				// n代表两者的同现次数，n1和n2分别代表两者各自出现的次数
				int n = 0, n1 = 0, n2 = 0;
				n1 = w1.docs.size();
				n2 = w2.docs.size();

				Iterator<Integer> iterator = w1.docs.keySet().iterator();
				while (iterator.hasNext()) {
					Integer inte = (Integer) iterator.next();
					if (w2.docs.containsKey(inte))
						n++;
				}
				double score = 1.0 * n / Math.sqrt(n1 * n2);
				if (n1 > 2 && n2 > 2 && score > 0) {
					String key1 = w1.key.substring(0, w1.key.indexOf("/"));// 获取词条的值
					String type1 = w1.key.substring(w1.key.indexOf("/") + 1);// 获取词条的类型
					String key2 = w2.key.substring(0, w2.key.indexOf("/"));// 获取词条的值
					String type2 = w2.key.substring(w2.key.indexOf("/") + 1);// 获取词条的类型
					DecimalFormat df3 = new DecimalFormat("0.000000");
					String value = df3.format(score);
					EEValue eevalue = new EEValue();
					eevalue.setOrginEntityID(GetIndexFromKey(key1));
					eevalue.setDestEntityID(GetIndexFromKey(key2));
					eevalue.setValue(value);
					reltt.add(eevalue);
				}
			}
		}
		relEE.setTopicID(nCount);
		relEE.setReltt(reltt);
		relee.add(relEE);

		entityList.clear();
	}

	public int GetIndexFromKey(String strTmp) {
		int index = 0;
		for (; index < vocabulary.size(); index++) {
			String str = vocabulary.get(index);
			int endindex = str.indexOf("/");
			str = str.substring(0, endindex);
			if (str.equals(strTmp))
				return index;
		}
		return -1;

	}

	public int transfer(Hashtable<String, Word> ht, TreeSet<Word> ts) {
		Enumeration<String> en = ht.keys();
		int sum = 0;
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			Word w = (Word) ht.get(key);
			ts.add(w);
			sum += w.value;
		}
		return sum;
	}

	public static void main(String[] args) throws IOException, InterruptedException {

	}
}
