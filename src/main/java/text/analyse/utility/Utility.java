package text.analyse.utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import text.analyse.struct.lda.AllEntity;
import text.analyse.struct.lda.RelationLocTE;
import text.analyse.struct.lda.RelationOrgTE;
import text.analyse.struct.lda.RelationPerTE;
import text.analyse.struct.lda.RelationTE;
import text.analyse.struct.lda.RelationTT;
import text.analyse.struct.lda.TopWords;
import text.searchSDK.util.CommonUtil;
import text.searchSDK.util.Constant;
import text.searchSDK.util.PrintConsole;

public class Utility {
	public int wordNum;
	public double[][] topics;// 装有Phi矩阵
	public WordSet wordset;
	public double[][] similarity;// 计算话题之间的相似度
	public int topicNum;

	// 记录每个专题的3种实体，每开始一个专题清空
	public HashMap<String, Integer> Entity_PER_Map;
	public HashMap<String, Integer> Entity_LOC_Map;
	public HashMap<String, Integer> Entity_ORG_Map;

	// 记录所有专题的3种实体
	public HashMap<String, Integer> Total_Entity_PER_Map;
	public HashMap<String, Integer> Total_Entity_LOC_Map;
	public HashMap<String, Integer> Total_Entity_ORG_Map;

	// 所有实体的集合
	ArrayList<String> All_Entitys = new ArrayList<String>();

	/**
	 * @param args
	 */
	public Utility(String filename) {
		wordset = new WordSet();

		Entity_PER_Map = new HashMap<String, Integer>();
		Entity_LOC_Map = new HashMap<String, Integer>();
		Entity_ORG_Map = new HashMap<String, Integer>();

		Total_Entity_PER_Map = new HashMap<String, Integer>();
		Total_Entity_LOC_Map = new HashMap<String, Integer>();
		Total_Entity_ORG_Map = new HashMap<String, Integer>();

		// 获取文件行数
		LineNumberReader reader;
		try {
			reader = new LineNumberReader(new FileReader(filename));
			String lineRead = "";
			int cnt = 0;
			while ((lineRead = reader.readLine()) != null) {

			}
			cnt = reader.getLineNumber();
			reader.close();
			topics = new double[cnt][];
			similarity = new double[cnt][cnt];
			topicNum = cnt;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取PhiMatrix文件
	 * 
	 * @param filename
	 *            phi 矩阵的文件名称
	 */
	public void readPhiMatrix(String filename) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), Constant.UTF8));
			wordNum = 0;
			int index = 0;
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] allWord = line.split(" ");
				if (wordNum == 0)
					wordNum = allWord.length;
				topics[index] = new double[wordNum];
				for (int i = 0; i < wordNum; i++) {
					topics[index][i] = Double.valueOf(allWord[i]).doubleValue();
				}
				index++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取wordmap.txt文件
	 * 
	 * @param wordFile
	 *            wordmap 文件名
	 */
	public void loadWord(String wordFile) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(wordFile), Constant.UTF8));
			String line = "";

			// read word number
			int num = Integer.valueOf((line = br.readLine()).trim()).intValue();
			PrintConsole.PrintLog("该专题的词条个数：", num);
			while ((line = br.readLine()) != null) {
				String[] allWord = line.split(" ");
				Word word = new Word();
				word.id = Integer.valueOf(allWord[1]).intValue();

				word.key = allWord[0].substring(0, allWord[0].indexOf("/")).trim();
				word.type = allWord[0].substring(allWord[0].indexOf("/") + 1, allWord[0].length()).trim();
				wordset.addWord(word);
				// PrintConsole.PrintLog("id :", word.id, "key :", word.key,
				// "type :" + word.type);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<RelationTT> topic_similarity() {
		ArrayList<RelationTT> reltt = new ArrayList<RelationTT>();
		computeSimilarity();
		try {
			for (int i = 0; i < topicNum; i++) {
				for (int j = i + 1; j < topicNum; j++) {
					DecimalFormat df = new DecimalFormat("0.000000");
					String value = df.format(similarity[i][j]);
					RelationTT relTT = new RelationTT();
					relTT.setOrigTopicID(i);
					relTT.setDestTopicID(j);
					relTT.setRelationValue(Double.valueOf(value));
					reltt.add(relTT);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reltt;

	}

	public void computeSimilarity() {
		for (int i = 0; i < topicNum; i++) {
			for (int j = i + 1; j < topicNum; j++) {
				double simi = 0;
				simi = simiByCos(i, j);
				similarity[i][j] = simi;
			}
		}
	}

	/**
	 * 计算两个话题之间的相似度
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public double simiByCos(int i, int j) {
		double result = 0;
		double res1 = 0, res2 = 0;

		if ((topics[i] != null) && (topics[j] != null)) {
			for (int p = 0; p < wordNum; p++) {
				res1 += Math.pow(topics[i][p], 2);
				result += topics[i][p] * topics[j][p];
			}
			for (int p = 0; p < wordNum; p++) {
				res2 += Math.pow(topics[j][p], 2);
			}
			try {
				result = (double) (result / (Math.sqrt(res1) * Math.sqrt(res2)));
			} catch (Exception exp) {
				PrintConsole.PrintLog("similarity compute error!", null);
			}
		}
		return result;
	}

	public ArrayList<TopWords> topic_lable_Candicate(int num) {
		ArrayList<TopWords> topWords = new ArrayList<TopWords>();

		num = new CommonUtil().getTopwords();

		int index = 0;
		try {
			for (int i = 0; i < topicNum; i++) {
				TopWords tops = new TopWords();
				tops.setTopicID(i);

				TreeSet<Word> ts = new TreeSet<Word>();
				for (int j = 0; j < wordNum; j++) {
					ts.add(new Word(j, topics[i][j]));
				}
				index++;
				Iterator<Word> it = ts.iterator();
				int count = 0;
				String key = "";
				// 通过迭代排序得到排序前num的词

				// while (it.hasNext() && (count < num)) {
				while (it.hasNext() && (count < num)) {
					Word word = (Word) it.next();
					key += ((Word) wordset.getWord(word.id)).key + "/" + ((Word) wordset.getWord(word.id)).type + "/"
							+ word.value + " ";// 利用wordnum的index得到key值
					// key += ((Word) wordset.getWord(word.id)).key + " ";
					count++;
				}

				tops.setLabelWords(key);
				topWords.add(tops);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return topWords;
	}

	public AllEntity GetEntity() {
		AllEntity entity = new AllEntity();
		// 清空三种实体映射表，开始一个新的专题
		Entity_PER_Map.clear();
		Entity_LOC_Map.clear();
		Entity_ORG_Map.clear();
		int countPER = 0;
		int countLOC = 0;
		int countORG = 0;
		try {

			ArrayList<Integer> pers = new ArrayList<Integer>();
			ArrayList<Integer> locs = new ArrayList<Integer>();
			ArrayList<Integer> orgs = new ArrayList<Integer>();

			Iterator<Entry<Object, Word>> ite = wordset.wordTable.entrySet().iterator();
			PrintConsole.PrintLog("wordset.wordTable", wordset.wordTable.size());

			int nIndex = 0;
			while (ite.hasNext()) {
				Entry<Object, Word> entry = (Entry<Object, Word>) ite.next();
				Word word = (Word) entry.getValue();
				String pattern = "[0-9]+(.[0-9]+)?";
				Pattern p = Pattern.compile(pattern);
				Matcher m = p.matcher(word.key);
				PrintConsole.PrintLog("word.key", word.key, "word.type", word.type, "" + nIndex);
				boolean b = m.matches();
				if (b == false) {
					if (word.type.equals("nr") || word.type.equals("PER")) {
						if (!Total_Entity_PER_Map.containsKey(word.key))// 如果Total_Entity_PER_Map不含有该实体则存储
						{
							Entity_PER_Map.put(word.key, countPER);
							Total_Entity_PER_Map.put(word.key, countPER);
							All_Entitys.add(word.key);

							pers.add(nIndex);
							nIndex++;

							countPER++;
						} else {
							Entity_PER_Map.put(word.key, Total_Entity_PER_Map.get(word.key));
						}
						// pers.add(word.key);

					} else if (word.type.equals("ns") || word.type.equals("LOC")) {
						if (!Total_Entity_LOC_Map.containsKey(word.key)) {
							Entity_LOC_Map.put(word.key, countLOC);
							Total_Entity_LOC_Map.put(word.key, countLOC);
							All_Entitys.add(word.key);

							locs.add(nIndex);
							nIndex++;

							countLOC++;
						} else {
							Entity_LOC_Map.put(word.key, Total_Entity_LOC_Map.get(word.key));
						}
						// locs.add(word.key);
					} else if (word.type.equals("nt") || word.type.equals("ORG")) {
						if (!Total_Entity_ORG_Map.containsKey(word.key)) {
							Entity_ORG_Map.put(word.key, countORG);
							Total_Entity_ORG_Map.put(word.key, countORG);
							All_Entitys.add(word.key);

							orgs.add(nIndex);
							nIndex++;

							countORG++;
						} else {
							Entity_ORG_Map.put(word.key, Total_Entity_ORG_Map.get(word.key));
						}
						// orgs.add(word.key);
					}
				}
			}
			entity.setLocs(locs);
			entity.setOrgs(orgs);
			entity.setPers(pers);
			entity.setAll_Entitys(All_Entitys);

			PrintConsole.PrintLog("LOC", entity.getLocs());
		} catch (Exception e) {
			e.printStackTrace();
		}

		PrintConsole.PrintLog("LOC", entity.getLocs());

		return entity;
	}

	public RelationTE saveTop100_entity_topic_Relation() throws Exception {
		RelationTE relte = new RelationTE();
		ArrayList<RelationPerTE> relPerTe = new ArrayList<RelationPerTE>();
		ArrayList<RelationLocTE> relLocTe = new ArrayList<RelationLocTE>();
		ArrayList<RelationOrgTE> relOrgTe = new ArrayList<RelationOrgTE>();

		for (int i = 0; i < topicNum; i++) {
			int pnum = 0, onum = 0, lnum = 0;
			ArrayList<Word> relations = new ArrayList<Word>();
			for (int j = 0; j < topics[i].length; j++) {
				Word word = new Word(j, topics[i][j]);
				relations.add(word);
			}
			System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
			Collections.sort(relations);

			for (int j = 0; j < relations.size(); j++) {
				Word tea = relations.get(j);
				Word word = wordset.getWord(tea.id);

				try {
					if (word.type.equals("nr") && pnum < 100) {
						RelationPerTE perTE = new RelationPerTE();

						pnum++;
						String relation = "";
						DecimalFormat df = new DecimalFormat("0.000000");
						relation = df.format(topics[i][word.id]);

						perTE.setID(GetIDFromEntityName(word.key));
						perTE.setTopicID(i);
						perTE.setValue(relation);
						relPerTe.add(perTE);

					} else if (word.type.equals("ns") && lnum < 100) {
						RelationLocTE locTE = new RelationLocTE();

						lnum++;
						String relation = "";

						// if(topics[i][word.id]<tea) return;
						DecimalFormat df = new DecimalFormat("0.000000");
						relation = df.format(topics[i][word.id]);

						locTE.setID(GetIDFromEntityName(word.key));
						locTE.setTopicID(i);
						locTE.setValue(relation);
						relLocTe.add(locTE);

					} else if (word.type.equals("nt") && onum < 100) {
						RelationOrgTE orgTE = new RelationOrgTE();

						onum++;
						String relation = "";
						DecimalFormat df = new DecimalFormat("0.000000");
						relation = df.format(topics[i][word.id]);

						orgTE.setID(GetIDFromEntityName(word.key));
						orgTE.setTopicID(i);
						orgTE.setValue(relation);
						relOrgTe.add(orgTE);
					}
					if (pnum >= 100 && onum >= 100 && lnum >= 100)
						break;

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		relte.setRelLocTe(relLocTe);
		relte.setRelOrgTe(relOrgTe);
		relte.setRelPerTe(relPerTe);

		return relte;

	}

	public int GetIDFromEntityName(String name) {
		for (int i = 0; i < All_Entitys.size(); i++) {
			if (All_Entitys.get(i).equals(name))
				return i;
		}
		return -1;
	}

}
