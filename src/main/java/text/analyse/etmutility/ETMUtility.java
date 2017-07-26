package text.analyse.etmutility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import text.analyse.common.utils.properties.SpringContextUtil;
import text.analyse.struct.etm.TECountModel;
import text.analyse.struct.lda.AllEntity;
import text.analyse.struct.lda.EEValue;
import text.analyse.struct.lda.NewsDetail;
import text.analyse.struct.lda.RelationEE;
import text.analyse.struct.lda.RelationLocTE;
import text.analyse.struct.lda.RelationOrgTE;
import text.analyse.struct.lda.RelationPerTE;
import text.analyse.struct.lda.RelationTE;
import text.analyse.struct.lda.RelationTT;
import text.analyse.struct.lda.TopWords;
import text.analyse.struct.lda.TopicSet;
import text.analyse.struct.lda.WordStructSet;
import text.analyse.utility.Utility;
import text.analyse.utility.Word;
import text.analysis.utils.CommonUtil;
import text.analysis.utils.ConstantUtil;
import text.thu.keg.smartsearch.jgibbetm.ETMDataset;

public class ETMUtility extends Utility {
	private Logger LOG = LoggerFactory.getLogger(ETMUtility.class);

	CommonUtil commonUtil = SpringContextUtil.getBean(CommonUtil.class);

	public ETMUtility(String filename) {
		super(filename);
	}

	/*public String filexEAssignall = ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_XEASSIGN;*/
	public String filetEAssignall = ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_TEASSIGN;
	public String filentityword = ConstantUtil.ENTITY_MAP;
	public String filekesai = ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_KESAI;
	public String filephientityall = ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_PHIENTITY;
	public String filetopwordsall = ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_TWORDS;
	public String filetheta = ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_THETA;

	public AllEntity GetEntityUsrDef(List entitylist) {
		AllEntity entity = new AllEntity();
		List<Integer> pers = new ArrayList<Integer>();
		List<Integer> locs = new ArrayList<Integer>();
		List<Integer> orgs = new ArrayList<Integer>();

		ArrayList<String> All_Entitys = new ArrayList<String>();
		for (int j = 0; j < entitylist.size(); j++) {
			Word word = (Word) entitylist.get(j);
			// PrintConsole.PrintLog(word.type, word.key,j+"");
			All_Entitys.add(word.key);
			if (word.type.equals("LOC")) {
				// All_Entitys.add(word.key);
				locs.add(j);
			} else if (word.type.equals("PER")) {
				// All_Entitys.add(word.key);
				pers.add(j);
			} else if (word.type.equals("ORG")) {
				// All_Entitys.add(word.key);
				orgs.add(j);
			}
		}

		entity.setLocs(locs);
		entity.setOrgs(orgs);
		entity.setPers(pers);

		entity.setAll_Entitys(All_Entitys);
		return entity;
	}

	/**
	 * /** 获取 top 100 个实体
	 * 
	 * @param topiccountMap
	 *            topic 出现次数的Map集合
	 * @param entitycountMap
	 *            实体词出现次数的Map集合
	 * @param entitylist
	 *            所有实体的List集合
	 * @return
	 */
	public RelationTE getTERelation(Map<String, Integer> topiccountMap, Map<String, Integer> entitycountMap,
			List<Word> entitylist) {
		RelationTE reletmte = new RelationTE();

		List<RelationPerTE> relPerTe = new ArrayList<RelationPerTE>();
		List<RelationLocTE> relLocTe = new ArrayList<RelationLocTE>();
		List<RelationOrgTE> relOrgTe = new ArrayList<RelationOrgTE>();

		Iterator<Entry<String, Integer>> topicCountMapIterator = topiccountMap.entrySet().iterator();

		while (topicCountMapIterator.hasNext()) {
			Entry<String, Integer> itemEntry = topicCountMapIterator.next();
			int topicnum = itemEntry.getValue();

			int lnum = 0, onum = 0, penum = 0;
			for (int j = 0; j < entitylist.size(); j++) {
				String et = entitylist.get(j).id + ":" + itemEntry.getKey();
				if (entitycountMap.get(et) != null) {
					int count = Integer.parseInt(entitycountMap.get(et).toString());

					double score1 = Double.parseDouble(count + "") / topicnum;
					DecimalFormat df = new DecimalFormat("0.00000");
					String values = df.format(score1);

					if (entitylist.get(j).type.equals("LOC") && lnum < 100) {
						RelationLocTE locTE = new RelationLocTE();
						// locTE.setID(entitylist.get(j).id);
						locTE.setID(j);
						locTE.setTopicID(Integer.valueOf(itemEntry.getKey()));
						locTE.setValue(values);
						// PrintConsole.PrintLog("LOC", entitylist.get(j).id, ""
						// + i, values, entitylist.get(j).key);
						lnum++;
						relLocTe.add(locTE);
					} else if (entitylist.get(j).type.equals("PER") && penum < 100) {
						RelationPerTE perTE = new RelationPerTE();
						// perTE.setID(entitylist.get(j).id);
						perTE.setID(j);
						perTE.setTopicID(Integer.valueOf(itemEntry.getKey()));
						perTE.setValue(values);
						// PrintConsole.PrintLog("PER", entitylist.get(j).id, ""
						// + i, values, entitylist.get(j).key);
						penum++;
						relPerTe.add(perTE);
					} else if (entitylist.get(j).type.equals("ORG") && onum < 100) {
						RelationOrgTE orgTE = new RelationOrgTE();
						// orgTE.setID(entitylist.get(j).id);
						orgTE.setID(j);
						orgTE.setTopicID(Integer.valueOf(itemEntry.getKey()));
						orgTE.setValue(values);
						// PrintConsole.PrintLog("ORG", entitylist.get(j).id, ""
						// + i, values, entitylist.get(j).key);
						onum++;
						relOrgTe.add(orgTE);
					}
				}
			}
		}
		reletmte.setRelLocTe(relLocTe);
		reletmte.setRelPerTe(relPerTe);
		reletmte.setRelOrgTe(relOrgTe);

		return reletmte;
	}

	/**
	 * 获得所有Entity在Topic下的数量
	 * 
	 * @param filename
	 *            model-final.tEAssign 文件全路径
	 * @return
	 */
	public Map<String, Integer> getEntityCountMap(String outputdir) {
		BufferedReader br = null;
		Map<String, Integer> topicountMap = new HashMap<String, Integer>();
		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream(outputdir + filetEAssignall), ConstantUtil.UTF8));
			String line = "";

			while ((line = br.readLine()) != null) {
				if (line.equals("")) {
					continue;
				}

				String[] entityarray = line.split(" ");

				for (int i = 0; i < entityarray.length; i++) {
					if (!entityarray[i].equals("")) {
						String topiclabel = entityarray[i];
						if (topicountMap.containsKey(topiclabel)) {
							int count = Integer.parseInt(topicountMap.get(topiclabel).toString()) + 1;
							topicountMap.put(topiclabel, count);
						} else {
							topicountMap.put(topiclabel, 1);
						}
					}
				}
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

		return topicountMap;
	}

	/**
	 * 得到词出现次数的Map集合
	 * 
	 * @param filename
	 *            model-final.tEAssign 文件全路径
	 * @return
	 */
	public Map<String, Integer> getTopicCountMap(String outputdir) {
		BufferedReader br = null;
		Map<String, Integer> topicountMap = new HashMap<String, Integer>();
		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream(outputdir + filetEAssignall), ConstantUtil.UTF8));
			String line = "";

			while ((line = br.readLine()) != null) {
				if (line.equals("")) {
					continue;
				} else {
					String[] entityarray = line.split(" ");

					for (int i = 0; i < entityarray.length; i++) {
						if (!entityarray[i].equals("")) {
							String[] topiclabel = entityarray[i].split(":");
							if (topiclabel.length != 2) {
								continue;
							} else {
								String topicmaplabel = topiclabel[1];
								if (topicountMap.containsKey(topicmaplabel)) {
									int count = Integer.parseInt(topicountMap.get(topicmaplabel).toString()) + 1;
									topicountMap.put(topicmaplabel, count);
								} else {
									topicountMap.put(topicmaplabel, 1);
								}
							}
						}
					}
				}
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

		return topicountMap;
	}

	/**
	 * 从entitymap 文件中获取所有实体
	 * 
	 * @param filename
	 *            entitymap 文件的全路径
	 * @return
	 */
	public List<Word> getEntityList(String outputdir) {
		Map<String, Integer> entity2idMap = ETMDataset.localDict.getEntityDic().entity2id;
		Iterator<Entry<String, Integer>> entity2idIterator = entity2idMap.entrySet().iterator();

		List<Word> entityslist = new ArrayList<Word>();
		while (entity2idIterator.hasNext()) {
			Entry<String, Integer> itemEntity = entity2idIterator.next();
			String[] entityInfo = itemEntity.getKey().split("/");

			Word word = new Word(itemEntity.getValue(), entityInfo[0], entityInfo[1]);
			entityslist.add(word);
		}

		Collections.sort(entityslist, Word.comparator);
		return entityslist;
	}
	/*public List<Word> getEntityList(String outputdir) {
		List<Word> wordlist = new ArrayList<Word>();
		BufferedReader br = null;
	
		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream(outputdir + filentityword), ConstantUtil.UTF8));
			String line = "";
	
			while ((line = br.readLine()) != null) {
				if (line.equals("") || line.split(" ").length != 2) {
					continue;
				}
				String wordinfo = line.trim();
				String type = wordinfo.split(" ")[0].split("/")[1].trim();
				String keyword = wordinfo.split(" ")[0].split("/")[0].trim();
				int id = Integer.parseInt(wordinfo.split(" ")[1].trim());
	
				String pattern = "[0-9]+(.[0-9]+)?";
				Pattern p = Pattern.compile(pattern);
				Matcher m = p.matcher(keyword);
				boolean b = m.matches();
	
				if (b == false) {
					Word word = new Word(id, keyword, type);
					wordlist.add(word);
				} else {
					continue;
				}
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
		Collections.sort(wordlist, Word.comparator);
		return wordlist;
	}*/

	/**
	 * 获取model-final.tEAssign 中每个topic对应实体信息
	 * 
	 * @param filename
	 *            model-final.tEAssign 的路径
	 * @return
	 */
	public Map<Integer, TECountModel> getTeCountMap(String filename) {
		BufferedReader br = null;
		Map<Integer, TECountModel> maptecmodelMap = new HashMap<Integer, TECountModel>();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), ConstantUtil.UTF8));
			String line = "";

			Map<String, Integer> allemap = new HashMap<String, Integer>();

			while ((line = br.readLine()) != null) {
				if (line.equals("")) {
					continue;
				} else {
					String[] teinfo = line.split(" ");
					for (int i = 0; i < teinfo.length; i++) {
						if (allemap.get(teinfo[i]) != null) {
							int count = allemap.get(teinfo[i]) + 1;
							allemap.put(teinfo[i], count);
						} else {
							allemap.put(teinfo[i], 1);
						}
					}
				}
			}

			Set<Map.Entry<String, Integer>> entry = allemap.entrySet();
			for (Map.Entry<String, Integer> etm : entry) {
				int etid = Integer.parseInt(etm.getKey().split(":")[1]);
				int eid = Integer.parseInt(etm.getKey().split(":")[0]);
				int ecount = etm.getValue();

				List<Entity> entityList = new ArrayList<Entity>();
				int etcount = ecount;
				if (maptecmodelMap.get(etid) != null) {
					entityList = maptecmodelMap.get(etid).getEntityList();
					Entity entity = new Entity(eid, ecount);
					etcount = maptecmodelMap.get(etid).getCount() + ecount;
					entityList.add(entity);
				} else {
					Entity entity = new Entity(eid, ecount);
					entityList.add(entity);
				}
				TECountModel teCountModel = new TECountModel(etid, entityList, etcount);
				maptecmodelMap.put(etid, teCountModel);
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
		return maptecmodelMap;
	}

	/**
	 * 计算两个 et 之间的关系值
	 * 
	 * @param etList
	 *            model-final.tEAssign 文件中对应 et 下的Entity List集合
	 * @param xeList
	 *            model-final.xEAssign 文件中对应 xe 下的Entity List集合
	 * @return
	 */
	public int computesimilarity(List<Entity> etList, List<Entity> xeList) {
		int count = 0;
		for (int i = 0; i < etList.size(); i++) {
			Entity et = etList.get(i);
			for (int j = 0; j < xeList.size(); j++) {
				Entity xe = xeList.get(j);

				if (et.getId() == xe.getId()) {
					if (et.getTotalNum() <= xe.getTotalNum()) {
						count += et.getTotalNum();
					} else {
						count += xe.getTotalNum();
					}
					break;
				}
			}
		}
		return count;
	}

	/**
	 * 计算Topic 与 Topic 之间的关系
	 * 
	 * @param teMap
	 *            tEAssign 文件的Topic Map集合
	 * @param xeMap
	 *            xEAssign 文件的Topic Map集合
	 * @return
	 */
	public ArrayList<RelationTT> getTTRelation(Map<Integer, TECountModel> teMap1, Map<Integer, TECountModel> teMap2,
			String outputdir) {
		ArrayList<RelationTT> reltt = new ArrayList<RelationTT>();
		double[][] kesai = readKesai(outputdir);
		for (int i = 0; i < topicNum; i++) {
			if (teMap1.get(i) != null) {
				int tecount = teMap1.get(i).getCount();
				List<Entity> et = teMap1.get(i).getEntityList();
				for (int j = i + 1; j < topicNum; j++) {
					if (teMap2.get(j) != null) {
						List<Entity> xe = teMap2.get(j).getEntityList();
						int xecount = computesimilarity(et, xe);
						if (xecount == 0) {
							continue;
						} else {
							DecimalFormat df = new DecimalFormat("0.000000");
							Double values = kesai[i][j] * xecount / tecount;
							String value = df.format(values);
							RelationTT relTT = new RelationTT();
							relTT.setOrigTopicID(i);
							relTT.setDestTopicID(j);
							relTT.setRelationValue(Double.valueOf(value));
							reltt.add(relTT);
						}
					}
				}
			}
		}
		return reltt;
	}

	/**
	 * 读取 kesai 文件
	 * 
	 * @return
	 */
	public double[][] readKesai(String outputdir) {
		BufferedReader br = null;
		double[][] kesai = new double[topicNum][];
		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream(outputdir + filekesai), ConstantUtil.UTF8));
			String line = "";
			int flag = 0;
			while ((line = br.readLine()) != null) {
				String[] lineinfo = line.split(" ");
				kesai[flag] = new double[lineinfo.length];
				for (int i = 0; i < lineinfo.length; i++) {
					double d = Double.parseDouble(lineinfo[i]);
					kesai[flag][i] = d;
				}
				flag++;
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
		return kesai;
	}

	public ArrayList<RelationEE> relEE(String outputdir) {
		int Ke = topicNum;
		Entity entity1 = new Entity(outputdir);
		int num = entity1.getTotalNum();
		Entity entity2 = new Entity(outputdir);
		double[][] pro1 = new double[num][Ke];
		for (int e = 0; e < num; e++) {
			for (int j = 0; j < Ke; j++) {
				pro1[e][j] = 0.0;
			}
		}
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(outputdir + filephientityall), ConstantUtil.UTF8));
			String t_e = br.readLine();
			int k = 0;
			while (t_e != null) {
				String[] tes = t_e.split(" ");
				int i = 0;
				for (String te : tes) {
					// PrintConsole.PrintLog("te", te);
					pro1[i][k] = Double.parseDouble(te);
					i++;
				}
				k++;
				t_e = br.readLine();
			}
		} catch (FileNotFoundException e3) {
			e3.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		double[][] pro2 = new double[Ke][num];
		for (int e = 0; e < num; e++) {
			for (int j = 0; j < Ke; j++) {
				pro2[j][e] = 0.0;
			}
		}
		try {

			for (int e = 0; e < num; e++) {
				int counte = 0;
				BufferedReader in = new BufferedReader(
						new InputStreamReader(new FileInputStream(outputdir + filetEAssignall), ConstantUtil.UTF8));
				String e_t = null;
				while ((e_t = in.readLine()) != null) {
					String[] ets = e_t.split(" ");
					for (String et : ets) {
						String[] ett = et.split(":");
						if (ett[0].equals(Integer.toString(e))) {
							counte++;
							pro2[Integer.parseInt(ett[1])][e] += 1;
						}
					}
				}

				for (int j = 0; j < Ke; j++) {
					pro2[j][e] = pro2[j][e] / (double) counte;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		double[][] pro = new double[num][num];
		for (int e1 = 0; e1 < num; e1++) {
			for (int e2 = 0; e2 < num; e2++) {
				pro[e1][e2] = 0;
			}
		}

		HashMap<String, Double> eep = new HashMap<String, Double>();
		for (int e1 = 0; e1 < num; e1++) {
			for (int e2 = e1 + 1; e2 < num; e2++) {
				for (int k = 0; k < Ke; k++) {
					pro[e1][e2] += 0.5 * pro1[e1][k] * pro2[k][e2] + 0.5 * pro1[e2][k] * pro2[k][e1];
				}
				double minValue = 0.0001;

				// System.out.println(entity1.getName(e1) + "---" +
				// entity2.getName(e2) + pro[e1][e2]);
				if (pro[e1][e2] >= minValue) {
					// PrintConsole.PrintLog(entity1.getName(e1),
					// "---",entity2.getName(e2),"---",pro[e1][e2]+"");
					eep.put(entity1.getName(e1) + "---" + entity2.getName(e2), pro[e1][e2]);
				}

			}
		}

		ArrayList<Entry<String, Double>> l = new ArrayList<Entry<String, Double>>(eep.entrySet());

		Collections.sort(l, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				if (o2.getValue() - o1.getValue() > 0)
					return 1;
				else if (o2.getValue() - o1.getValue() < 0) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		// 获取关系对应的entityid
		List<Word> entitylist = getEntityList(outputdir);
		Map<String, Integer> entityMap = new HashMap<String, Integer>();

		for (int i = 0; i < entitylist.size(); i++) {
			Word word = entitylist.get(i);
			String key = word.key + "/" + word.type;
			int value = word.id;
			entityMap.put(key, value);
		}
		// 把相同的orgin的eerelation保存到Map中
		Map<Integer, List<EEValue>> releeMap = new HashMap<Integer, List<EEValue>>();

		for (Entry<String, Double> e : l) {
			String key = e.getKey();
			String orginame = key.split("---")[0];
			String desname = key.split("---")[1];

			List<EEValue> releelist = new ArrayList<EEValue>();

			if (entityMap.get(desname) != null && entityMap.get(orginame) != null) {
				if (releeMap.get(orginame) == null) {
					EEValue eeval = new EEValue();
					eeval.setID(entityMap.get(orginame));
					eeval.setOrginEntityID(entityMap.get(orginame));
					eeval.setDestEntityID(entityMap.get(desname));
					eeval.setTopicID(entityMap.get(desname));
					eeval.setValue("" + e.getValue());
					releelist.add(eeval);
					releeMap.put(entityMap.get(orginame), releelist);
				} else {
					EEValue eeval = new EEValue();
					eeval.setID(entityMap.get(orginame));
					eeval.setOrginEntityID(entityMap.get(orginame));
					eeval.setDestEntityID(entityMap.get(desname));
					eeval.setTopicID(entityMap.get(desname));
					eeval.setValue("" + e.getValue());
					releelist = releeMap.get(entityMap.get(orginame));
					releelist.add(eeval);
					releeMap.put(entityMap.get(orginame), releelist);
				}
			}
		}

		Map<Integer, TECountModel> tecountMap = getTeCountMap(outputdir + filetEAssignall);

		ArrayList<RelationEE> relee = new ArrayList<RelationEE>();
		Set<Map.Entry<Integer, TECountModel>> entry1 = tecountMap.entrySet();
		for (Map.Entry<Integer, TECountModel> test : entry1) {
			int etid = test.getKey();
			RelationEE rel = new RelationEE();
			TECountModel teCountModel = test.getValue();
			List<Entity> elist = teCountModel.getEntityList();
			ArrayList<EEValue> eelist = new ArrayList<EEValue>();
			for (int i = 0; i < elist.size(); i++) {
				Entity entity = elist.get(i);
				if (releeMap.get(entity.getId()) != null) {
					eelist.addAll(releeMap.get(entity.getId()));
				}
			}
			rel.setTopicID(etid);
			rel.setReltt(eelist);
			relee.add(rel);
		}

		return relee;
	}

	/**
	 * 获取话题 Top word 集合
	 * 
	 * @param outputdir
	 *            输出文件的路径
	 * @return
	 */
	public ArrayList<TopWords> getTopWords(String outputdir) {
		ArrayList<TopWords> topwordslist = new ArrayList<TopWords>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream(outputdir + filetopwordsall), ConstantUtil.UTF8));
			String line = "";
			int flag = 0;
			int num = 0;
			String label = "";
			List<String> valueList = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				String tsngl = "Topic " + flag + "th:";
				if (line.equals(tsngl)) {
					continue;
				}

				if (line.trim().split(" ").length > 1) {
					label += line.trim().split(" ")[0] + " ";
					valueList.add(line.trim().split(" ")[1]);
				}

				num++;
				if (num % commonUtil.getTopwords() == 0) {
					TopWords topWords = new TopWords();
					topWords.setTopicID(flag);
					LOG.info("labelsplit.length() = {} , valueList.size() = {} ",
							label.split(ConstantUtil.WORD_SPLIT).length, valueList.size());
					String[] lsArray = label.split(ConstantUtil.WORD_SPLIT);
					int len = Math.min(lsArray.length, valueList.size());
					String rslabel = "";
					for (int i = 0; i < len; i++) {
						rslabel += lsArray[i] + "/" + valueList.get(i) + " ";
					}
					topWords.setLabelWords(rslabel);
					topwordslist.add(topWords);
					flag++;
					label = "";
					valueList = new ArrayList<String>();
				}
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
		return topwordslist;
	}

	/**
	 * 得到某个专题下的WordStructSet集合
	 * 
	 * @param outputdir
	 *            输出文件路径
	 * @param tid
	 *            topicid
	 * @return
	 */
	public Map<Integer, ArrayList<WordStructSet>> getWordStructSetMap(String outputdir) {
		ArrayList<WordStructSet> topwordslist = new ArrayList<WordStructSet>();
		Map<Integer, ArrayList<WordStructSet>> wordstrMap = new HashMap<Integer, ArrayList<WordStructSet>>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream(outputdir + filetopwordsall), ConstantUtil.UTF8));
			String line = "";
			int flag = 0;
			int num = 0;
			// String label = "";
			while ((line = br.readLine()) != null) {
				String tsngl = "Topic " + flag + "th:";

				// PrintConsole.PrintLog("line", line);
				// PrintConsole.PrintLog("tsngl", tsngl);
				// PrintConsole.PrintLog("line", line,"tsngl", tsngl,"");

				if (line.equals(tsngl)) {
					continue;
				} else {
					String label = line.trim();
					num++;

					if (label.split(" ").length > 1) {
						WordStructSet wordStructSet = new WordStructSet();
						wordStructSet.setStrWord(label.split(" ")[0]);
						wordStructSet.setValue(Double.parseDouble(label.split(" ")[1]));
						topwordslist.add(wordStructSet);
					}

					if (num % commonUtil.getTopwords() == 0) {
						wordstrMap.put(flag, topwordslist);
						topwordslist = new ArrayList<WordStructSet>();
						flag++;
						// label = "";
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return wordstrMap;
	}

	public ArrayList<TopicSet> getTopicSet(String outputdir, List<NewsDetail> newsDetail) {
		BufferedReader br = null;
		BufferedReader brf = null;
		BufferedReader reader = null;
		PrintWriter pw = null;
		ArrayList<TopicSet> topicSets = new ArrayList<TopicSet>();
		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream(outputdir + filetheta), ConstantUtil.UTF8));

			// Token 原文件
			brf = new BufferedReader(new InputStreamReader(
					new FileInputStream(outputdir + ConstantUtil.DOC_TOKEN_FILENAME), ConstantUtil.UTF8));
			// 新闻原文
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(outputdir + ConstantUtil.DOC_FILENAME), ConstantUtil.UTF8));
			Map<Integer, Map<Integer, Double>> tdp = new HashMap<Integer, Map<Integer, Double>>();
			String linef = brf.readLine();
			linef = linef.trim();
			System.out.println("linef" + linef);
			int M = Integer.parseInt(linef);
			String[] documents = new String[M];
			int[] topics = new int[M];
			int numTopics = 0;
			String newLine = "";
			// String[] newDocuments = new String[M];
			NewsDetail[] newDocuments = new NewsDetail[M];
			int cc = 0;

			while ((newLine = reader.readLine()) != null) {
				if (GenericValidator.isBlankOrNull(newLine)) {
					continue;
				}

				String[] newsinfo = newLine.split("@@@@");
				if (newsinfo.length > 2) {
					NewsDetail newsDetail2 = new NewsDetail();

					newsDetail2.setTitle(newsinfo[0]);
					newsDetail2.setContent(newsinfo[1]);
					newsDetail2.setUrl(newsinfo[2]);
					newsDetail2.setTime(newsinfo[3]);
					newsDetail2.setType(newsinfo[4]);
					newsDetail2.setWeights(newsinfo[5]);
					String fresh = newsDetail.get(cc).getFresh();
					newsDetail2.setFresh(fresh);
					newDocuments[cc] = newsDetail2;
					cc++;
				}
			}

			String line = br.readLine();
			int id = 0;
			while (line != null) {
				line.trim();
				String[] strs = line.split(" ");
				numTopics = strs.length;
				double[] pros = new double[strs.length];
				double max = 0.0;

				for (int i = 0; i < pros.length; i++) {
					pros[i] = Double.parseDouble(strs[i]);
					if (pros[i] > max) {
						max = pros[i];
						topics[id] = i;

					}
				}
				if (tdp.containsKey(topics[id])) {
					tdp.get(topics[id]).put(id, max);
				} else {
					Map<Integer, Double> tprob = new HashMap<Integer, Double>();
					tprob.put(id, max);
					tdp.put(topics[id], tprob);
				}
				line = br.readLine();
				id++;
			}

			linef = brf.readLine();
			int index = 0;
			while (linef != null) {
				documents[index] = linef;
				linef = brf.readLine();
				index++;
			}

			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputdir + ConstantUtil.TOP_DOCS), ConstantUtil.UTF8)));
			Map<Integer, ArrayList<WordStructSet>> worMap = getWordStructSetMap(outputdir);
			for (int i = 0; i < numTopics; i++) {
				Set<Entry<Integer, Double>> itemEntry = tdp.get(i).entrySet();

				List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(itemEntry);

				Collections.sort(list, new Comparator<Object>() {
					@SuppressWarnings("unchecked")
					public int compare(Object e1, Object e2) {
						Double v1 = ((Map.Entry<Integer, Double>) e1).getValue();
						Double v2 = ((Map.Entry<Integer, Double>) e2).getValue();
						if (v1 - v2 >= 0) {
							return 1;
						} else
							return -1;
					}
				});
				TopicSet topicSet = new TopicSet();
				pw.write("Topic" + i + ":\n");
				ArrayList<NewsDetail> tnewsDetail = new ArrayList<NewsDetail>();
				for (int c = 0; c < list.size(); c++) {
					pw.write(newDocuments[list.get(c).getKey()].title + "@@@@"
							+ newDocuments[list.get(c).getKey()].content + "\n");
					tnewsDetail.add(newDocuments[list.get(c).getKey()]);
				}
				pw.write("#####\n");
				topicSet.setLabelName(i);
				ArrayList<WordStructSet> speIssueSet = worMap.get(i);
				topicSet.setNewsDetail(tnewsDetail);
				topicSet.setSpeIssueSet(speIssueSet);
				topicSets.add(topicSet);

				int count = 0;
				Vector<Integer> a = new Vector<Integer>();
				for (int j = 0; j < M; j++) {
					if (topics[j] == i) {
						count++;
						a.add(j);
					}
				}
			}

			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return topicSets;
	}
}
