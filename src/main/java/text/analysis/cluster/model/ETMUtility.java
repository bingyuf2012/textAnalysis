package text.analysis.cluster.model;

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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import text.analysis.utils.ConstantUtil;
import text.analysis.utils.SegUtil;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午5:44:37
 */

public class ETMUtility extends Utility {
	@Autowired
	SegUtil segUtil;

	public ETMUtility(String filename) {
		super(filename);

		Entity_EQU_Map = new HashMap<String, Integer>();
		Entity_TEC_Map = new HashMap<String, Integer>();
		Entity_PRO_Map = new HashMap<String, Integer>();

		Total_Entity_EQU_Map = new HashMap<String, Integer>();
		Total_Entity_TEC_Map = new HashMap<String, Integer>();
		Total_Entity_PRO_Map = new HashMap<String, Integer>();
	}

	// 记录专题中的用户自定义三种实体，每开始一个专题清空
	public HashMap<String, Integer> Entity_EQU_Map;
	public HashMap<String, Integer> Entity_TEC_Map;
	public HashMap<String, Integer> Entity_PRO_Map;

	// 记录所有专题中的用户自定义三种实体
	public HashMap<String, Integer> Total_Entity_EQU_Map;
	public HashMap<String, Integer> Total_Entity_TEC_Map;
	public HashMap<String, Integer> Total_Entity_PRO_Map;
	public String filexEAssignall = ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_XEASSIGN;
	public String filetEAssignall = ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_TEASSIGN;
	public String filentityword = ConstantUtil.ENTITY_MAP;
	public String filekesai = ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_KESAI;
	public String filephientityall = ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_PHIENTITY;
	// ArrayList<String> All_Entitys = new ArrayList<String>();
	public String filetopwordsall = ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_TWORDS;
	public String filetheta = ConstantUtil.MODEL_NAME + ConstantUtil.SUFFIX_THETA;
	/*public static int TOP_WORD_NUM = CommonUtil.getTopwords();*/
	public static int TOP_WORD_NUM = 20;

	public AllEntityUsrDef GetEntityUsrDef(List entitylist) {
		AllEntityUsrDef entity = new AllEntityUsrDef();
		ArrayList<Integer> pers = new ArrayList<Integer>();
		ArrayList<Integer> locs = new ArrayList<Integer>();
		ArrayList<Integer> orgs = new ArrayList<Integer>();

		ArrayList<Integer> equs = new ArrayList<Integer>();
		ArrayList<Integer> tecs = new ArrayList<Integer>();
		ArrayList<Integer> pros = new ArrayList<Integer>();
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
			} else if (word.type.equals("EQU")) {
				// All_Entitys.add(word.key);
				equs.add(j);
			} else if (word.type.equals("TEC")) {
				// All_Entitys.add(word.key);
				tecs.add(j);
			} else if (word.type.equals("PRO")) {
				// All_Entitys.add(word.key);
				pros.add(j);
			}
		}

		entity.setLocs(locs);
		entity.setOrgs(orgs);
		entity.setPers(pers);

		entity.setEqus(equs);
		entity.setTecs(tecs);
		entity.setPros(pros);
		entity.setAll_Entitys(All_Entitys);
		return entity;
	}

	/**
	 * 得到各种实体的
	 * 
	 * @return
	 */
	// public AllEntityUsrDef GetEntityUsrDef_old() {
	// AllEntityUsrDef entity = new AllEntityUsrDef();
	// // AllEntity allentity = super.GetEntity();
	//
	// // ArrayList<String> All_Entitys = allentity.getAll_Entitys();
	//
	// // entity.setLocs(allentity.getLocs());
	// // entity.setOrgs(allentity.getOrgs());
	// // entity.setPers(allentity.getPers());
	//
	// // 清空三种实体映射表，开始一个新的专题
	// Entity_PER_Map.clear();
	// Entity_LOC_Map.clear();
	// Entity_ORG_Map.clear();
	// Entity_EQU_Map.clear();
	// Entity_TEC_Map.clear();
	// Entity_PRO_Map.clear();
	//
	// int countPER = 0;
	// int countLOC = 0;
	// int countORG = 0;
	//
	// int countEQU = 0;
	// int countTEC = 0;
	// int countPRO = 0;
	// try {
	// ArrayList<Integer> pers = new ArrayList<Integer>();
	// ArrayList<Integer> locs = new ArrayList<Integer>();
	// ArrayList<Integer> orgs = new ArrayList<Integer>();
	//
	// ArrayList<Integer> equs = new ArrayList<Integer>();
	// ArrayList<Integer> tecs = new ArrayList<Integer>();
	// ArrayList<Integer> pros = new ArrayList<Integer>();
	//
	// Iterator<Entry<Object, Word>> ite = wordset.wordTable.entrySet()
	// .iterator();
	//
	// PrintConsole
	// .PrintLog("wordset.wordTable", wordset.wordTable.size());
	//
	// int nIndex = 0;
	// while (ite.hasNext()) {
	// Entry<Object, Word> entry = (Entry<Object, Word>) ite.next();
	// Word word = (Word) entry.getValue();
	//
	// String pattern = "[0-9]+(.[0-9]+)?";
	// Pattern p = Pattern.compile(pattern);
	// Matcher m = p.matcher(word.key);
	// PrintConsole.PrintLog("word.key", word.key, "word.type",
	// word.type, "" + nIndex);
	// boolean b = m.matches();
	// if (b == false) {
	// if (word.type.equals("nr") || word.type.equals("PER")) {
	// if (!Total_Entity_PER_Map.containsKey(word.key))//
	// 如果Total_Entity_PER_Map不含有该实体则存储
	// {
	// Entity_PER_Map.put(word.key, countPER);
	// Total_Entity_PER_Map.put(word.key, countPER);
	// All_Entitys.add(word.key);
	//
	// pers.add(nIndex);
	// nIndex++;
	// countPER++;
	// } else {
	// Entity_PER_Map.put(word.key, Total_Entity_PER_Map
	// .get(word.key));
	// }
	// // pers.add(word.key);
	//
	// } else if (word.type.equals("ns")
	// || word.type.equals("LOC")) {
	// if (!Total_Entity_LOC_Map.containsKey(word.key)) {
	// Entity_LOC_Map.put(word.key, countLOC);
	// Total_Entity_LOC_Map.put(word.key, countLOC);
	// All_Entitys.add(word.key);
	//
	// locs.add(nIndex);
	// nIndex++;
	//
	// countLOC++;
	// } else {
	// Entity_LOC_Map.put(word.key, Total_Entity_LOC_Map
	// .get(word.key));
	// }
	// // locs.add(word.key);
	// } else if (word.type.equals("nt")
	// || word.type.equals("ORG")) {
	// if (!Total_Entity_ORG_Map.containsKey(word.key)) {
	// Entity_ORG_Map.put(word.key, countORG);
	// Total_Entity_ORG_Map.put(word.key, countORG);
	// All_Entitys.add(word.key);
	//
	// orgs.add(nIndex);
	// nIndex++;
	//
	// countORG++;
	// } else {
	// Entity_ORG_Map.put(word.key, Total_Entity_ORG_Map
	// .get(word.key));
	// }
	// // orgs.add(word.key);
	// } else if (word.type.equals("EQU")) {
	// if (!Total_Entity_EQU_Map.containsKey(word.key)) {
	// Entity_EQU_Map.put(word.key, countEQU);
	// Total_Entity_EQU_Map.put(word.key, countEQU);
	// All_Entitys.add(word.key);
	//
	// equs.add(nIndex);
	// nIndex++;
	//
	// countEQU++;
	// } else {
	// Entity_EQU_Map.put(word.key, Total_Entity_EQU_Map
	// .get(word.key));
	// }
	// } else if (word.type.equals("TEC")) {
	// if (!Total_Entity_TEC_Map.containsKey(word.key)) {
	// Entity_TEC_Map.put(word.key, countTEC);
	// Total_Entity_TEC_Map.put(word.key, countTEC);
	// All_Entitys.add(word.key);
	//
	// tecs.add(nIndex);
	// nIndex++;
	//
	// countTEC++;
	// } else {
	// Entity_TEC_Map.put(word.key, Total_Entity_TEC_Map
	// .get(word.key));
	// }
	// } else if (word.type.equals("PRO")) {
	// if (!Total_Entity_PRO_Map.containsKey(word.key)) {
	// Entity_PRO_Map.put(word.key, countPRO);
	// Total_Entity_PRO_Map.put(word.key, countPRO);
	// All_Entitys.add(word.key);
	//
	// pros.add(nIndex);
	// nIndex++;
	//
	// countPRO++;
	// } else {
	// Entity_PRO_Map.put(word.key, Total_Entity_PRO_Map
	// .get(word.key));
	// }
	// }
	// }
	// }
	// entity.setLocs(locs);
	// entity.setOrgs(orgs);
	// entity.setPers(pers);
	//
	// entity.setEqus(equs);
	// entity.setTecs(tecs);
	// entity.setPros(pros);
	// entity.setAll_Entitys(All_Entitys);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// PrintConsole.PrintLog("entity.getEqus()", entity.getEqus());
	// PrintConsole.PrintLog("entity.getTecs()", entity.getTecs());
	// PrintConsole.PrintLog("entity.getPers()", entity.getPers());
	// PrintConsole.PrintLog("entity.getPros()", entity.getPros());
	// PrintConsole.PrintLog("entity.getLocs()", entity.getLocs());
	// PrintConsole.PrintLog("entity.getOrgs()", entity.getOrgs());
	//
	// return entity;
	// }

	/**
	 * 获取 top 100 个实体
	 * 
	 * @param topiccountMap
	 *            topic 出现次数的Map集合
	 * @param entitycountMap
	 *            实体词出现次数的Map集合
	 * @param entitylist
	 *            所有实体的List集合
	 * @return
	 */
	public RelationETMTE getTERelation(Map<String, Integer> topiccountMap, Map<String, Integer> entitycountMap,
			List<Word> entitylist) {
		RelationETMTE reletmte = new RelationETMTE();

		ArrayList<RelationPerTE> relPerTe = new ArrayList<RelationPerTE>();
		ArrayList<RelationLocTE> relLocTe = new ArrayList<RelationLocTE>();
		ArrayList<RelationOrgTE> relOrgTe = new ArrayList<RelationOrgTE>();

		for (int i = 0; i < topiccountMap.size(); i++) {
			int lnum = 0, onum = 0, penum = 0;
			int eqnum = 0, tnum = 0, pnum = 0;
			int topicnum = Integer.parseInt(topiccountMap.get(i + "").toString());
			for (int j = 0; j < entitylist.size(); j++) {
				String et = entitylist.get(j).id + ":" + i;
				if (entitycountMap.get(et) != null) {
					int count = Integer.parseInt(entitycountMap.get(et).toString());

					double score1 = Double.parseDouble(count + "") / topicnum;
					DecimalFormat df = new DecimalFormat("0.00000");
					String values = df.format(score1);

					if (entitylist.get(j).type.equals("LOC") && lnum < 100) {
						RelationLocTE locTE = new RelationLocTE();
						// locTE.setID(entitylist.get(j).id);
						locTE.setID(j);
						locTE.setTopicID(i);
						locTE.setValue(values);
						// PrintConsole.PrintLog("LOC", entitylist.get(j).id, ""
						// + i, values, entitylist.get(j).key);
						lnum++;
						relLocTe.add(locTE);
					} else if (entitylist.get(j).type.equals("PER") && penum < 100) {
						RelationPerTE perTE = new RelationPerTE();
						// perTE.setID(entitylist.get(j).id);
						perTE.setID(j);
						perTE.setTopicID(i);
						perTE.setValue(values);
						// PrintConsole.PrintLog("PER", entitylist.get(j).id, ""
						// + i, values, entitylist.get(j).key);
						penum++;
						relPerTe.add(perTE);
					} else if (entitylist.get(j).type.equals("ORG") && onum < 100) {
						RelationOrgTE orgTE = new RelationOrgTE();
						// orgTE.setID(entitylist.get(j).id);
						orgTE.setID(j);
						orgTE.setTopicID(i);
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
				} else {
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
		List<Word> wordlist = new ArrayList<Word>();
		BufferedReader br = null;

		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream(outputdir + filentityword), ConstantUtil.UTF8));
			String line = "";

			while ((line = br.readLine()) != null) {
				if (line.equals("") || line.split(" ").length != 2) {
					continue;
				} else {
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
	}

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
	 * 获取model-final.xEAssign 中每个topic对应实体信息
	 * 
	 * @param filename
	 *            model-final.xEAssign 的路径
	 * @return
	 */
	// public Map<Integer, TECountModel> getXeCountMap(String filename) {
	// return getTeCountMap(filename);
	// }

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

		System.out.println("releeMap.size() :" + releeMap.size());
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
				} else {
					// label += line.trim().split("/")[0] + " ";
					if (line.trim().split(" ").length > 1) {
						// label += line.trim().split(" ")[0] + "/"
						// + line.trim().split(" ")[1] + " ";
						label += line.trim().split(" ")[0] + " ";
						// value += line.trim().split(" ")[1] + " ";
						valueList.add(line.trim().split(" ")[1]);
					}

					num++;
					if (num % TOP_WORD_NUM == 0) {
						TopWords topWords = new TopWords();
						topWords.setTopicID(flag);
						/*String labelsplit = split.splitString(label);*/
						String labelsplit = segUtil.segText(label, true);
						/*PrintConsole.PrintLog("labelsplit.length()", labelsplit.split("  ").length,
								"valueList.size()", valueList.size(), "");*/
						String[] lsArray = labelsplit.split("  ");
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

					if (num % TOP_WORD_NUM == 0) {
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
				if (!newLine.equals("")) {
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
					// System.out.println("t+id+max" + topics[id] + " " + id +
					// " "
					// + max);
				} else {
					Map<Integer, Double> tprob = new HashMap<Integer, Double>();
					tprob.put(id, max);
					tdp.put(topics[id], tprob);
					// System.out.println("t+id+max" + topics[id] + " " + id +
					// " "
					// + max);
				}
				line = br.readLine();
				id++;
			}
			// System.out.println(tdp);

			linef = brf.readLine();
			int index = 0;
			while (linef != null) {
				// System.out.println(index);
				documents[index] = linef;
				linef = brf.readLine();
				index++;
			}

			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputdir + ConstantUtil.TOP_DOCS), ConstantUtil.UTF8)));
			Map<Integer, ArrayList<WordStructSet>> worMap = getWordStructSetMap(outputdir);
			for (int i = 0; i < numTopics; i++) {
				ArrayList<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(
						tdp.get(i).entrySet());

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
					// pw.write(newsDetail.get(c).title + "@@@@"
					// + newsDetail.get(c).content + "\n");
					pw.write(newDocuments[list.get(c).getKey()].title + "@@@@"
							+ newDocuments[list.get(c).getKey()].content + "\n");
					// tnewsDetail.add(newsDetail.get(c));
					tnewsDetail.add(newDocuments[list.get(c).getKey()]);
				}
				pw.write("#####\n");
				topicSet.setLabelName(i);
				// ArrayList<WordStructSet> speIssueSet = getWordStructSet(
				// outputdir, i);
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