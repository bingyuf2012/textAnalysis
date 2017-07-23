package text.analyse.utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import text.searchSDK.util.Constant;
import text.searchSDK.util.PrintConsole;

public class Entity {

	// public int topicNum;
	// public int docNum;
	// public int wordNum;
	// public NewsSet newsSet;
	// public int totalTopicNum;//话题表中的话题总数

	// public HashMap<String,Integer> Entity_PER_Map = new
	// HashMap<String,Integer>();
	// public HashMap<String,Integer> Entity_LOC_Map = new
	// HashMap<String,Integer>();
	// public HashMap<String,Integer> Entity_ORG_Map = new
	// HashMap<String,Integer>();
	/**
	 * @param args
	 */

	public void loadData(String filename) {
		ArrayList dataArray = new ArrayList();
		// filename = filename + "TokensToNews";

		int nCount = 0;
		try {
			String m_inputFile = "d:\\testABC\\TokensToNews";
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(m_inputFile), Constant.UTF8));
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
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

	/*
	 * 以下问ENTITY-ENTITY功能实现，但是没有分别对某个topic下所有新闻的entity关系进行处理，而是对所有news的entity进行
	 * 关联关系运算，有待改进
	 */

	public void dealEntity(ArrayList dataArray, int nCount) {

		// 定义存放PER,LOC和ORG的list
		Hashtable<String, Word> perHT = new Hashtable<String, Word>();
		Hashtable<String, Word> locHT = new Hashtable<String, Word>();
		Hashtable<String, Word> orgHT = new Hashtable<String, Word>();
		List<HotEntity> hotList = new ArrayList<HotEntity>();//

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
				if (!(type.equals("nr") || type.equals("ns") || type
						.equals("nt")))
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
					Word temp2 = (Word) entityList
							.get(entityList.indexOf(tmp1));
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

		PrintConsole.PrintLog("Entity--Entity ", null);
		DecimalFormat df1 = new DecimalFormat("0.000000000000000000");
		PrintConsole.PrintLog("TopicMinnigDistribution ", null);

		// -----计算该topic下所有entity之间的关联大小----------//
		PrintConsole.PrintLog("Entity_Entity_Relation ", null);
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
					// int ID1 = getID(key1, type1);
					// int ID2 = getID(key2, type2);
					DecimalFormat df3 = new DecimalFormat("0.000000");
					String value = df3.format(score);
					PrintConsole.PrintLog("key1 ", key1);
					PrintConsole.PrintLog("key2 ", key2);
					PrintConsole.PrintLog("value ", value);
				}
			}
		}
		entityList.clear();
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

	// public int getID(String Entity_Key,String Type)
	// {
	// if(Type.equals("nr") && Entity_PER_Map.containsKey(Entity_Key))
	// return Entity_PER_Map.get(Entity_Key);
	// else if(Type.equals("ns")&& Entity_LOC_Map.containsKey(Entity_Key))
	// return Entity_LOC_Map.get(Entity_Key);
	// else if(Type.equals("nt") && Entity_ORG_Map.containsKey(Entity_Key))
	// return Entity_ORG_Map.get(Entity_Key);
	// else
	// return -1;
	//		
	// }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Entity entity = new Entity();
		// entity.dealEntity();
		String str = "";
		entity.loadData(str);

	}

}
