package text.thu.keyword.extract;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import text.searchSDK.util.CommonUtil;
import text.thu.keyword.dao.DAO;
import text.thu.keyword.model.News;
import text.thu.keyword.model.Token;

public class Extract {

	// public static SpliterOperator ict = SpliterOperator.getInstance();
	public static int KEYNUM = CommonUtil.getTopKeyWords();
	public String id;
	public String headLine;
	public String content;

	public Hashtable uniTokens;// 值元素是Token对象
	public Hashtable biTokens;// 值元素是Token对象
	public Hashtable triTokens;// 值元素是Token对象
	public Hashtable delTokens;// 值元素是Token对象, not completeness sure
	public Hashtable headTokens;
	public Hashtable btTokens;

	public Hashtable results;

	public TreeSet uniTs;
	public TreeSet biTs;
	public TreeSet triTs;
	public TreeSet headTs;

	public boolean completeness;

	public String headLineICT;
	public String firstPara;

	public int tfSum = 0;
	public boolean bigram = true;
	public boolean trigram = true;
	public boolean find_newword = true;
	public boolean final_process = true;

	public double t1 = 0.99;
	public double t2 = 2.3;
	public double t3 = 2.3;
	public double t4 = 0.01;
	public double t5 = 1.0;
	public double t6 = 0.95;
	public double t7 = 0.85;

	public boolean checkHashtable(String term, Hashtable ht, int tf, boolean del) {
		Enumeration e = ht.keys();
		// double stability = 0.0;
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			Token t = (Token) ht.get(key);

			if ((term.length() > 2) && term.contains(key) && (tf <= t.tf)
					&& (key.length() > 2)) {
				completeness = true;
				if (t.tf > 3) {
					countTf(key, content);
				}
				if (completeness)
					return true;
			}
			if (key.contains(term) && !t.word.contains(term)
					&& t.type.contains("ns_") && term.startsWith("国"))
				return true;
		}
		return false;
	}

	public boolean checkHashtable(String term, Hashtable ht) {
		Enumeration e = ht.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			if (key.contains(term))
				return true;
		}
		return false;
	}

	public boolean checkHashtable(Token term, Hashtable ht) {
		Enumeration e = ht.keys();
		// double stability = 0.0;
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			// if (key.equals("北京奥组委"))
			// System.out.println("klj;l");
			if (key.contains(term.word))
				return true;
			if ((term.word.length() > 2) && term.word.contains(key)
					&& (key.length() > 1)) {
				if (key.length() > 2)
					return true;
				Token t = (Token) ht.get(key);
				if (t.tf >= term.tf) {
					int k = term.word.indexOf(key);
					String theOtherWord = "";
					if (term.word.startsWith(key))
						theOtherWord = term.word.substring(key.length());
					else if (term.word.endsWith(key))
						theOtherWord = term.word.substring(0, k);
					else {
						// if (t.tf > term.tf) return true;
						if (t.type.equals("ns"))
							return true;
						continue;
					}

					int keytf = countTf(key, content);
					t.completeness = completeness;
					int otherword = countTf(theOtherWord, content);

					if (term.word.startsWith(key) || term.word.endsWith(key)) {
						term.stability = (double) term.tf
								/ (keytf + otherword + 2 * term.isHead - term.tf);
						double tTf = Math.pow((double) term.tf + term.isHead
								+ term.inQuotation * 3 + term.bonus
								+ term.firstpara, 3.0);
						// System.out.println(term.word+"\t"+tTf+"\t"+term.stability+"\t"+tfSum+"\t"+tTf*term.stability/tfSum);
						if ((double) tTf * t.stability / tfSum > 0.480) {
							term.del = false;
							return false;
						}
					}

					if ((!t.completeness || !completeness)
							&& (theOtherWord.length() > 1)
							&& (term.tf == otherword + 1)
							&& !DAO.stop1Set.contains(theOtherWord)) {
						term.del = false;
						return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	public boolean modifyHashtable(Token term, Hashtable ht) { // modify the
		// word in ht,
		// the tf,
		// isHead,
		// stability
		if (ht.containsKey(term.word)) {
			Token t = (Token) ht.get(term.word);
			if (t.tf < term.tf)
				t.tf = term.tf;
			t.isHead = 1;
			t.stability = term.stability;
			return true;
		}
		return false;
	}

	public void modifyHashtable(String term, Hashtable ht) {
		if (ht.containsKey(term)) {
			Token t = (Token) ht.get(term);
			t.completeness = false;
		}
	}

	public void addToHash(int isfirst, String key, String type, Hashtable ht,
			int isHead) {
		if (!ht.containsKey(key)) {
			ht.put(key, new Token(key, type, isHead, isfirst));
		} else {
			Token t = (Token) ht.get(key);
			t.tf++;
			if (isHead == 1)
				t.isHead = 1;
			if (isfirst == 1)
				t.firstpara = 1;
			if (t.type.equals("v") && !type.contains("_"))
				t.type = type;

		}
	}

	public void addToHash(int isfirst, String key1, String key2, String type,
			Hashtable ht, int isHead) {
		if (!ht.containsKey(key1)) {
			ht.put(key1, new Token(key2, type, isHead, isfirst));
		} else {
			Token t = (Token) ht.get(key1);
			t.tf++;
			if (isHead == 1)
				t.isHead = 1;
			if (isfirst == 1)
				t.firstpara = 1;
		}
	}

	/*
	 * public void addToHash(String key, String type,Hashtable ht, int isHead,
	 * int inQuotation){ if (!ht.containsKey(key)){ //ht.put(key, new Token(key,
	 * type, isHead, inQuotation)); }else{ Token t = (Token)ht.get(key); t.tf++;
	 * if (isHead==1) t.isHead = 1; if (inQuotation==1) t.inQuotation = 1; } }
	 */

	public int countTf(String key, String text) {
		int tf = 0;
		completeness = true;
		if ((text != null) || (!text.equals(""))) {
			String contentText = text;
			int k = contentText.indexOf(key);
			char cl = '“', cr = '”';
			int clCount = 0, crCount = 0;
			if (k > -1) {
				cl = (k > 0) ? contentText.charAt(k - 1) : ' ';
				cr = (k + key.length() < contentText.length()) ? contentText
						.charAt(k + key.length()) : ' ';
			}
			while (k > -1) {
				tf++;
				if ((k == 0) || contentText.charAt(k - 1) == cl)
					clCount++;
				if ((k + key.length() < contentText.length())
						&& contentText.charAt(k + key.length()) == cr)
					crCount++;
				contentText = contentText.substring(k + key.length());
				k = contentText.indexOf(key);
			}
			if ((tf != 0) && ((tf == clCount) || (tf == crCount))) {
				completeness = false;
				if ((((cl == '的') || (cl == '在') || (cl == '，') || (cl == '。')) && (tf != crCount))
						|| ((cr == '队') && (tf != clCount)))
					completeness = true;
				if (((cl == '“') && (tf == clCount))
						&& ((cr == '”') && (tf == crCount)))
					completeness = true;
				if (((cl == '《') && (tf == clCount))
						&& ((cr == '》') && (tf == crCount)))
					completeness = true;
				if ((cl == '“') && (tf == clCount) && (tf != crCount))
					completeness = true;
				if ((cr == '”') && (tf == crCount) && (tf != clCount))
					completeness = true;
			}
		}

		return tf;
	}

	public int countTf(String key, String text, char cl, char cr) {
		int tf = 0;
		completeness = true;
		if ((text != null) || (!text.equals(""))) {
			String contentText = text;
			int k = contentText.indexOf(key);
			int clCount = 0, crCount = 0;
			while (k > -1) {
				tf++;
				if ((k > 0) && contentText.charAt(k - 1) == cl)
					clCount++;
				if (k + key.length() < contentText.length()) {
					if (contentText.charAt(k + key.length()) == cr)
						crCount++;
					contentText = contentText.substring(k + key.length());
					k = contentText.indexOf(key);
				} else
					k = -1;
			}
			if ((tf != 0) && ((tf == clCount) || (tf == crCount))) {
				completeness = false;
				if ((((cl == '的') || (cl == '在') || (cl == '，') || (cl == '。')) && (tf != crCount))
						|| ((cr == '队') && (tf != clCount)))
					completeness = true;
				if (((cl == '“') && (tf == clCount))
						&& ((cr == '”') && (tf == crCount)))
					completeness = true;
				if (((cl == '《') && (tf == clCount))
						&& ((cr == '》') && (tf == crCount)))
					completeness = true;
				if ((cl == '“') && (tf == clCount) && (tf != crCount))
					completeness = true;
				if ((cr == '”') && (tf == crCount) && (tf != clCount))
					completeness = true;
			}
		}
		return tf;
	}

	public void setQuotation(Hashtable ht, String key) {
		if (ht.containsKey(key)) {
			Token t = (Token) ht.get(key);
			t.inQuotation = 1;
		}
	}

	public static void repair(News news) {
		String content = news.content;
		news.content = "";
		int k = content.indexOf("\n");
		while (k > -1) {
			news.content += content.substring(0, k);
			if ((k == 0)
					|| (content.charAt(k - 1) == '。')
					|| (content.charAt(k - 1) == '！' || content.charAt(k - 1) == '？')) {
				news.content += "\n";
			}
			content = content.substring(k + 1);
			k = content.indexOf("\n");
		}
		news.content += content;
	}

	// 获取一元、二元、三元文法关键词候选集，分别存于uniTokens，biTokens，triTokens三个哈希表中
	public void getToken(String text, int isHead) {
		if ((text == null) || (text.equals("")))
			return;
		// text.replaceAll("我国","中国");
		// if (text.contains("我国")) System.out.println("fdsafasfds");
		String[] texts = text.split("\n"); // 首先根据换行符进行切分
		int fp = 0;
		firstPara = "";

		// 取新闻内容的前20个字符为首段落
		if ((isHead == 0) && (texts.length > 0)) {
			firstPara = texts[fp];
			while ((firstPara.length() < 20) && (texts.length > fp + 1)) {
				fp++;
				firstPara = texts[fp];
			}
		}

		// 从首段落开始，依次对所有段落进行处理
		for (int i = 0; i < texts.length; i++) {
			if (texts[i] == null)
				continue;

			// texts[i] = texts[i].trim().replaceAll("/|／|？|；"," ");
			// //利用正则表达式进行替代
			// texts[i]= texts[i].replaceAll("．{2,}+","．");
			// texts[i]= texts[i].replaceAll("％|%","");
			texts[i] = texts[i].replaceAll(" /x", "");
			if (texts[i].equals(""))
				continue;

			// 检查其内容是否为汉字
			try {// check if chinese word
				byte[] b = texts[i].getBytes("GBK");
				int han = 0;
				for (int index = 0; index < b.length - 1; index++) {
					if ((((b[index] > -88) && (b[index] < -1)) || (b[index] < -95))
							&& (b[index] < 0)) {
						han += 2;
						index++;
					}
				}
				double db = (double) han / b.length;
				// if (db<0.50) continue;
				if (db == 0) {
					if (isHead == 1)
						headLineICT = "";
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// System.out.println(texts[i]);
			// 对该新闻题目或内容段落进行分词处理
			// String str = ict.paragraphProcessing(texts[i]);
			String str = texts[i];
			// String str = texts[i];
			// System.out.println("str : " + str);

			// 如果是标题，则单独处理
			if (isHead == 1)
				headLineICT = str;
			// System.out.println(str);

			int isfirst = 0;
			// 如何不是标题则进行另外处理
			if ((isHead == 0) && (fp == i))
				isfirst = 1;
			// String[] allWords = str.split("  ");
			String[] allWords = str.split(" ");

			for (int j = 0; j < allWords.length; j++) {
				int pos1 = allWords[j].lastIndexOf("/");
				// System.out.println(pos1+allWords[j]);
				// System.out.println(pos1+" "+pos2);
				if (pos1 > 0) {
					String type1 = allWords[j].substring(pos1 + 1).trim();
					String key1 = allWords[j].substring(0, pos1).replaceAll(
							" ", "").replaceAll("　", "").trim();

					int cut = key1.indexOf("/?");
					if (cut > -1)
						key1 = key1.substring(cut + 2).trim();

					// 过滤连词、助词、介词
					if (type1.matches("u|c|p")) {
						if (!delTokens.contains(key1))
							delTokens.put(key1, type1);
						continue;
					}

					// 进行一元文法筛选
					if (type1.matches(".*n[^rg]*|b|ad|a|l|v|j|t|nr")) {
						if (key1.matches("(\\d|[／１２３４５６７８９０]){1,}.*"))
							continue;
						if (type1.equals("nt") && key1.endsWith("队")
								&& (key1.length() > 2)) {
							key1 = key1.substring(0, key1.length() - 1).trim();
						}

						// 过滤不为名词的单字；过滤长度小于2的人名---------是否合适，可以以后进行修改
						if (!((!type1.equals("n") && (key1.length() < 2)) || (type1
								.equals("nr") && (key1.length() < 3)))) {
							// 计算新闻中总的词数目？？？？？？？？？？？？
							tfSum++;
							// 加入一元文法候选词集中
							addToHash(isfirst, key1, type1, uniTokens, isHead);

						}
					}

					// 由于这里会遗漏如“十六大”等关键词，因此进行修改！！！！！！！！！！！！！lijun
					// if
					// (type1.equals("a")&&(key1.equals("大"))&&(j>1)&&allWords[j-1].contains("/m"))
					// {
					if ((type1.equals("a")) && (j > 1)
							&& (allWords[j - 1].contains("/m"))) {
						String temp = allWords[j - 1].substring(0,
								allWords[j - 1].indexOf("/"))
								+ key1;
						addToHash(isfirst, temp, type1, uniTokens, isHead);
						continue;
					}

					if (!bigram)
						continue; // 该语句应该是留给程序测试时使用，目前暂不起作用
					int pos2 = (j + 1 < allWords.length) ? allWords[j + 1]
							.lastIndexOf("/") : 0;
					int pos3 = (j + 2 < allWords.length) ? allWords[j + 2]
							.lastIndexOf("/") : 0;
					if (pos2 > 0) {
						String type2 = allWords[j + 1].substring(pos2 + 1);
						String key2 = allWords[j + 1].substring(0, pos2)
								.replaceAll(" ", "").replaceAll("　", "");

						if (key2.contains("..") || key2.contains("EQU")
								|| key2.contains("/") || key2.contains("-")
								|| key2.contains("LOC")) {
							continue;
						}

						// 检查是否是停用词文本1中的词，是则进行过滤
						if (DAO.stop1Set.contains(key2))
							continue;
						if (type2.matches("q") && (type1.matches("m|t"))) {
							if (!delTokens.contains(key2))
								delTokens.put(key2, type2);
							continue;
						}
						if (type2.equals("nr") && !type1.equals("nr")
								&& (key2.length() == 2)) {
							if ((pos3 > 0)
									&& !allWords[j + 2].substring(pos3 + 1)
											.equals("nr")) {
								tfSum++;
								addToHash(isfirst, key2, type2, uniTokens,
										isHead);
								continue;
							}
						}
						String type = type1 + "_" + type2;
						// if (key1.equals("漂流"))
						// System.out.println("fdasfd");
						if (type
								.matches("(.*n[^rg]*|j|t|s|l)_(.*n[^r]*|j|t)|b_(.*n[^rg]*|j|t)|(a|ag|ad|v)_(vn|n|j)|(v|n)_vg|(vn|n|j|z|v)_(k|ng)|z_n|vd_(v|vn)|nr_nr|(n|v|vn|tg|d|a)_tg")) {
							if (key1.matches("总|的|原|副|相关|一定|主要")
									|| (key2.matches("队") && !(key1
											.equals("国家") || (key1.length() == 1))))
								continue;
							if (type2.equals("ng") && key2.equals("时"))
								continue;
							if (type1.matches("ag") && (key1.length() > 1))
								continue;
							if (type.contains("v_j"))
								continue;
							if (type2.equals("t")
									&& (key2
											.matches("(\\d|[／１２３４５６７８９０]){1,}.*")))
								continue;
							if ((key1.length() < 2)
									&& type1.matches("j|n")
									&& (!(type2.matches("vg|ng|k") || (key2
											.equals("队")))))
								continue;
							if ((key2.length() < 2) && type2.matches("j|vn|t"))
								continue;
							// 进行词的合成操作
							String key = key1 + key2;
							if (type2.equals("k")
									&& !key
											.matches("(.*(症|化|员|式|器|品|家|率|仪|炎|儿|性))|.(业|界)"))
								continue;
							if (type2.equals("tg")) {
								if (!((type1.matches("n|vn|v")
										&& (key1.length() == 2) && key2
										.matches("商|周")) || (key
										.matches("春晚|极夜|平安夜|长春|黄金周"))))
									continue;
								if (key.matches("春晚|极夜|平安夜|长春|黄金周")) {
									type = "n";
									addToHash(isfirst, key, type, uniTokens,
											isHead);
									continue;
								}
							}
							if (uniTokens.containsKey(key))
								addToHash(isfirst, key, type, uniTokens, isHead);
							else {
								if (type.equals("nr_nr")
										&& (key1.length() == 1)
										&& (key2.length() < 3)) {
									// 此处可以合成一些被分词器分开的人名，如：姚明，毛泽东，邓小平等
									addToHash(isfirst, key, key, type,
											biTokens, isHead);
								} else
									// 保存二元文法的组合词
									addToHash(isfirst, key, key1 + "^" + key2,
											type, biTokens, isHead);
							}

							if ((pos3 > 0) && trigram) {
								String type3 = allWords[j + 2]
										.substring(pos3 + 1);
								String key3 = allWords[j + 2]
										.substring(0, pos3).replaceAll(" ", "")
										.replaceAll("　", "");

								if (key3.contains("..") || key3.contains("EQU")
										|| key3.contains("/")
										|| key3.contains("-")
										|| key3.contains("LOC")) {
									continue;
								}

								if (DAO.stop1Set.contains(key3))
									continue;
								type = type1 + "_" + type2 + "_" + type3;
								if (type
										.matches("(.*n[^rg]*|j|t|s)_(.*n[^r]*|j|t)_(.*n[^r]*|j|t)|(b|a)_(.*n[^rg]*|j|t|s)_(.*n[^r]*|j|t)")) {
									if (type.contains("ng_ng"))
										continue;
									if (type3.equals("t")
											&& (key3
													.matches("(\\d|[／１２３４５６７８９０]){1,}.*")))
										continue;
									if ((key3.length() < 2)
											&& type3.equals("j"))
										continue;
									key = key1 + key2 + key3;
									addToHash(isfirst, key, key1 + "^" + key2
											+ "^" + key3, type, triTokens,
											isHead);
								}
							}
						}
					}
				}
			}
		}
	}

	// 获取一元、二元、三元文法关键词候选集，分别存于uniTokens，biTokens，triTokens三个哈希表中
	public void getToken_old(String text, int isHead) {
		if ((text == null) || (text.equals("")))
			return;
		// text.replaceAll("我国","中国");
		// if (text.contains("我国")) System.out.println("fdsafasfds");
		String[] texts = text.split("\n"); // 首先根据换行符进行切分
		int fp = 0;
		firstPara = "";

		// 取新闻内容的前20个字符为首段落
		if ((isHead == 0) && (texts.length > 0)) {
			firstPara = texts[fp];
			while ((firstPara.length() < 20) && (texts.length > fp + 1)) {
				fp++;
				firstPara = texts[fp];
			}
		}

		// 从首段落开始，依次对所有段落进行处理
		for (int i = 0; i < texts.length; i++) {
			if (texts[i] == null)
				continue;

			texts[i] = texts[i].trim().replaceAll("/|／|？|；", " ");
			// 利用正则表达式进行替代
			texts[i] = texts[i].replaceAll("．{2,}+", "．");
			texts[i] = texts[i].replaceAll("％|%", "");
			if (texts[i].equals(""))
				continue;

			// 检查其内容是否为汉字
			try {// check if chinese word
				byte[] b = texts[i].getBytes("GBK");
				int han = 0;
				for (int index = 0; index < b.length - 1; index++) {
					if ((((b[index] > -88) && (b[index] < -1)) || (b[index] < -95))
							&& (b[index] < 0)) {
						han += 2;
						index++;
					}
				}
				double db = (double) han / b.length;
				// if (db<0.50) continue;
				if (db == 0) {
					if (isHead == 1)
						headLineICT = "";
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// System.out.println(texts[i]);
			// 对该新闻题目或内容段落进行分词处理
			// String str = ict.paragraphProcessing(texts[i]);
			String str = texts[i];
			System.out.println("str : " + str);

			// 如果是标题，则单独处理
			if (isHead == 1)
				headLineICT = str;
			// System.out.println(str);

			int isfirst = 0;
			// 如何不是标题则进行另外处理
			if ((isHead == 0) && (fp == i))
				isfirst = 1;
			String[] allWords = str.split("  ");

			for (int j = 0; j < allWords.length; j++) {
				int pos1 = allWords[j].lastIndexOf("/");
				// System.out.println(pos1+allWords[j]);
				// System.out.println(pos1+" "+pos2);
				if (pos1 > 0) {
					String type1 = allWords[j].substring(pos1 + 1).trim();
					String key1 = allWords[j].substring(0, pos1).replaceAll(
							" ", "").replaceAll("　", "").trim();

					int cut = key1.indexOf("/?");
					if (cut > -1)
						key1 = key1.substring(cut + 2).trim();

					// 过滤连词、助词、介词
					if (type1.matches("u|c|p")) {
						if (!delTokens.contains(key1))
							delTokens.put(key1, type1);
						continue;
					}

					// 进行一元文法筛选
					if (type1.matches(".*n[^rg]*|b|ad|a|l|v|j|t|nr")) {
						if (key1.matches("(\\d|[／１２３４５６７８９０]){1,}.*"))
							continue;
						if (type1.equals("nt") && key1.endsWith("队")
								&& (key1.length() > 2)) {
							key1 = key1.substring(0, key1.length() - 1).trim();
						}

						// 过滤不为名词的单字；过滤长度小于2的人名---------是否合适，可以以后进行修改
						if (!((!type1.equals("n") && (key1.length() < 2)) || (type1
								.equals("nr") && (key1.length() < 3)))) {
							// 计算新闻中总的词数目？？？？？？？？？？？？
							tfSum++;
							// 加入一元文法候选词集中
							addToHash(isfirst, key1, type1, uniTokens, isHead);

						}
					}

					// 由于这里会遗漏如“十六大”等关键词，因此进行修改！！！！！！！！！！！！！lijun
					// if
					// (type1.equals("a")&&(key1.equals("大"))&&(j>1)&&allWords[j-1].contains("/m"))
					// {
					if ((type1.equals("a")) && (j > 1)
							&& (allWords[j - 1].contains("/m"))) {
						String temp = allWords[j - 1].substring(0,
								allWords[j - 1].indexOf("/"))
								+ key1;
						addToHash(isfirst, temp, type1, uniTokens, isHead);
						continue;
					}

					if (!bigram)
						continue; // 该语句应该是留给程序测试时使用，目前暂不起作用
					int pos2 = (j + 1 < allWords.length) ? allWords[j + 1]
							.lastIndexOf("/") : 0;
					int pos3 = (j + 2 < allWords.length) ? allWords[j + 2]
							.lastIndexOf("/") : 0;
					if (pos2 > 0) {
						String type2 = allWords[j + 1].substring(pos2 + 1);
						String key2 = allWords[j + 1].substring(0, pos2)
								.replaceAll(" ", "").replaceAll("　", "");

						if (key2.contains("..") || key2.contains("EQU")
								|| key2.contains("/") || key2.contains("-")
								|| key2.contains("LOC")) {
							continue;
						}

						// 检查是否是停用词文本1中的词，是则进行过滤
						if (DAO.stop1Set.contains(key2))
							continue;
						if (type2.matches("q") && (type1.matches("m|t"))) {
							if (!delTokens.contains(key2))
								delTokens.put(key2, type2);
							continue;
						}
						if (type2.equals("nr") && !type1.equals("nr")
								&& (key2.length() == 2)) {
							if ((pos3 > 0)
									&& !allWords[j + 2].substring(pos3 + 1)
											.equals("nr")) {
								tfSum++;
								addToHash(isfirst, key2, type2, uniTokens,
										isHead);
								continue;
							}
						}
						String type = type1 + "_" + type2;
						// if (key1.equals("漂流"))
						// System.out.println("fdasfd");
						if (type
								.matches("(.*n[^rg]*|j|t|s|l)_(.*n[^r]*|j|t)|b_(.*n[^rg]*|j|t)|(a|ag|ad|v)_(vn|n|j)|(v|n)_vg|(vn|n|j|z|v)_(k|ng)|z_n|vd_(v|vn)|nr_nr|(n|v|vn|tg|d|a)_tg")) {
							if (key1.matches("总|的|原|副|相关|一定|主要")
									|| (key2.matches("队") && !(key1
											.equals("国家") || (key1.length() == 1))))
								continue;
							if (type2.equals("ng") && key2.equals("时"))
								continue;
							if (type1.matches("ag") && (key1.length() > 1))
								continue;
							if (type.contains("v_j"))
								continue;
							if (type2.equals("t")
									&& (key2
											.matches("(\\d|[／１２３４５６７８９０]){1,}.*")))
								continue;
							if ((key1.length() < 2)
									&& type1.matches("j|n")
									&& (!(type2.matches("vg|ng|k") || (key2
											.equals("队")))))
								continue;
							if ((key2.length() < 2) && type2.matches("j|vn|t"))
								continue;
							// 进行词的合成操作
							String key = key1 + key2;
							if (type2.equals("k")
									&& !key
											.matches("(.*(症|化|员|式|器|品|家|率|仪|炎|儿|性))|.(业|界)"))
								continue;
							if (type2.equals("tg")) {
								if (!((type1.matches("n|vn|v")
										&& (key1.length() == 2) && key2
										.matches("商|周")) || (key
										.matches("春晚|极夜|平安夜|长春|黄金周"))))
									continue;
								if (key.matches("春晚|极夜|平安夜|长春|黄金周")) {
									type = "n";
									addToHash(isfirst, key, type, uniTokens,
											isHead);
									continue;
								}
							}
							if (uniTokens.containsKey(key))
								addToHash(isfirst, key, type, uniTokens, isHead);
							else {
								if (type.equals("nr_nr")
										&& (key1.length() == 1)
										&& (key2.length() < 3)) {
									// 此处可以合成一些被分词器分开的人名，如：姚明，毛泽东，邓小平等
									addToHash(isfirst, key, key, type,
											biTokens, isHead);
								} else
									// 保存二元文法的组合词
									addToHash(isfirst, key, key1 + "^" + key2,
											type, biTokens, isHead);
							}

							if ((pos3 > 0) && trigram) {
								String type3 = allWords[j + 2]
										.substring(pos3 + 1);
								String key3 = allWords[j + 2]
										.substring(0, pos3).replaceAll(" ", "")
										.replaceAll("　", "");

								if (key3.contains("..") || key3.contains("EQU")
										|| key3.contains("/")
										|| key3.contains("-")
										|| key3.contains("LOC")) {
									continue;
								}

								if (DAO.stop1Set.contains(key3))
									continue;
								type = type1 + "_" + type2 + "_" + type3;
								if (type
										.matches("(.*n[^rg]*|j|t|s)_(.*n[^r]*|j|t)_(.*n[^r]*|j|t)|(b|a)_(.*n[^rg]*|j|t|s)_(.*n[^r]*|j|t)")) {
									if (type.contains("ng_ng"))
										continue;
									if (type3.equals("t")
											&& (key3
													.matches("(\\d|[／１２３４５６７８９０]){1,}.*")))
										continue;
									if ((key3.length() < 2)
											&& type3.equals("j"))
										continue;
									key = key1 + key2 + key3;
									addToHash(isfirst, key, key1 + "^" + key2
											+ "^" + key3, type, triTokens,
											isHead);
								}
							}
						}
					}
				}
			}
		}
	}

	public void getKeyWord(News news) {
		uniTokens = new Hashtable();
		biTokens = new Hashtable();
		triTokens = new Hashtable();
		headTokens = new Hashtable();
		delTokens = new Hashtable();
		btTokens = new Hashtable();

		uniTs = new TreeSet();
		biTs = new TreeSet();
		triTs = new TreeSet();
		// btTs = new TreeSet();

		// Hashtable termsUni = DAO.termsUni;
		// Hashtable termsBi = DAO.termsBi;
		// Hashtable termsTri = DAO.termsTri;
		tfSum = 0;
		// if (termsUni == null) System.out.println("ftft222");
		// repair(news); //修订新闻的内容

		// 进行全角和半角的转换
		headLine = CountDF.ToDBC(news.headLine);
		content = CountDF.ToDBC(news.content);
		// headLine = news.headLine;
		// content = news.content;

		// 去除headLine中被小括号包含的部分
		int t = -2;
		if (headLine.startsWith("（") || headLine.startsWith("(")) {
			t = headLine.indexOf("）");
			if (t == -1)
				t = headLine.indexOf(")");
		}
		if ((t > -1) && (t < 10) && (t + 1 < headLine.length())) {
			headLine = headLine.substring(t + 1);
		}

		// System.out.println("headLine : " + headLine);
		// System.out.println("content : " + content);

		// 创建三个关键词候选集
		getToken(headLine, 1);
		getToken(content, 0);

		// System.out.println(tfSum);
		if (find_newword)
			// 创建新词（被强调引用的词以及专有名词和缩略语）的关键词候选集
			specialDeal(headLine, content);

		// System.out.println("sadfsa"+tfSum);
		if (trigram) {
			countScore(triTokens, DAO.termsTri, triTs, DAO.triTermSum);
			finalDealTri();
		}

		if (bigram) {
			countScore(biTokens, DAO.termsBi, biTs, DAO.biTermSum);
			finalDealBi();
		}

		if (find_newword)
			finalDealUni();

		countScore(uniTokens, DAO.termsUni, uniTs, DAO.uniTermSum);

		// 对四个已经具有特征分值的关键词候选集合进行最后操作，即挑选出最终的抽取结果
		if (final_process)
			finalDeal(news);

	}

	public String wordleft(Token t) {
		String findWord = "";
		String word = t.word.replaceAll("\\^", "");
		if ((t.isHead == 1) && (t.tf == 2))
			return wordLeft(word);

		int k = content.lastIndexOf(findWord + word);
		char c = content.charAt(k - 1);
		findWord = c + findWord;
		while ((countTf(findWord + word, content) == (t.tf - t.isHead))
				&& (c != '“') && (c != '／') && (c != '《') && (c != '　')
				&& (c != ' ')) {
			if (("" + c).matches("(\\d|[／１２３４５６７８９０]){1,}|。|！|：|；|，|、"))
				break;
			if (delTokens.containsKey("" + c))
				return null;
			k--;
			c = content.charAt(k - 1);
			findWord = c + findWord;
			if ((t.isHead == 0) && (findWord.length() > 1)
					&& uniTokens.containsKey(findWord)) {
				findWord = "";
				break;
			}
		}
		if (findWord.equals("") || (findWord.length() < 2))
			return null;
		if ((t.isHead == 1) && (t.tf == 3)
				&& (!headLine.contains(findWord.substring(1))))
			return null;
		return findWord;
	}

	public String wordRight(Token t) {
		String findWord = "";
		String word = t.word.replaceAll("\\^", "");
		if ((t.isHead == 1) && (t.tf == 2)) {
			String right = wordRight(word);
			if ((t.df > 20) && (right.length() < 3))
				return null;
			return right;
		}
		int k = content.indexOf(findWord + word) + word.length();
		char c = content.charAt(k);
		findWord = findWord + c;

		while ((countTf(word + findWord, content) == (t.tf - t.isHead))
				&& (c != '”') && (c != '／') && (c != '》') && (c != '　')
				&& (c != ' ')) {
			if (("" + c).matches("(\\d|[／１２３４５６７８９０]){1,}|。|！|：|；|，|、"))
				break;
			if (delTokens.containsKey("" + c))
				return null;
			k++;
			c = content.charAt(k);
			findWord = findWord + c;
			/*
			 * if
			 * ((t.isHead==0)&&(findWord.length()>1)&&uniTokens.containsKey(findWord
			 * )){ findWord = ""; break; }
			 */
		}
		if (findWord.equals("") || (findWord.length() < 2))
			return null;
		if ((t.isHead == 1)
				&& (t.tf == 3)
				&& (!headLine.contains(findWord.substring(0,
						findWord.length() - 1))))
			return null;
		return findWord;
	}

	public void checkPersonName(Token t) {
		String findWord = t.word.replaceAll("\\^", "");
		String c = content;
		int k = c.indexOf(findWord);
		while (k > -1) {
			int temp = k + findWord.length() + 5;
			if (k + findWord.length() >= c.length())
				break;
			if (temp > c.length())
				temp = c.length();
			String s = c.substring(k + findWord.length(), temp);
			if (s.matches(".*(认为|说|介绍|建议|呼吁|指出|表示).*"))
				t.tf--;
			c = c.substring(k + findWord.length());
			k = c.indexOf(findWord);
		}
		if ((t.tf < 3) && (t.isHead == 0))
			t.tf = 0;
		if (t.completeness = false)
			t.tf = 0;
	}

	public String wordLeft(String word) {
		// System.out.println(id+"\t"+word);
		int k1 = headLine.indexOf(word);
		int k2 = content.indexOf(word);
		if ((k1 < 1) || (k2 < 1))
			return " ";
		char c1 = headLine.charAt(k1 - 1);
		char c2 = content.charAt(k2 - 1);
		String left = "";
		while ((c1 == c2) && (c1 != '“') && (c1 != '／') && (c1 != '《')
				&& (c1 != '　') && (c1 != ' ')) {
			if (("" + c1).matches("(\\d|[／１２３４５６７８９０]){1,}|。|！|：|；|，|、"))
				break;
			if (delTokens.containsKey("" + c1))
				return null;
			left = c1 + left;
			k1--;
			k2--;
			if ((k1 < 1) || (k2 < 1)) {
				c1 = '$';
				break;
			}
			c1 = headLine.charAt(k1 - 1);
			c2 = content.charAt(k2 - 1);
		}
		left = c1 + left;
		// if (left.equals(""+c1)||left.length()<2) return null;
		return left;
	}

	public String wordRight(String word) {
		int k1 = headLine.indexOf(word) + word.length();
		int k2 = content.indexOf(word) + word.length();
		if ((k1 >= headLine.length()) || (k2 >= content.length()))
			return " ";
		char c1 = headLine.charAt(k1);
		char c2 = content.charAt(k2);
		String right = "";
		while ((c1 == c2) && (c1 != '”') && (c1 != '／') && (c1 != '》')
				&& (c1 != '　') && (c1 != ' ')) {
			if (("" + c1).matches("(\\d|[／１２３４５６７８９０]){1,}|。|！|：|；|，|、"))
				break;
			if (delTokens.containsKey("" + c1))
				return null;
			right = right + c1;
			k1++;
			k2++;
			if ((k1 >= headLine.length()) || (k2 >= content.length())) {
				c1 = '$';
				break;
			}
			c1 = headLine.charAt(k1);
			c2 = content.charAt(k2);
		}
		right = right + c1;
		// if (right.equals(""+c1)||right.length()<2) return null;
		return right;
	}

	public String getcompleteness(String word0, Token t) {
		// 注意在countTf(word0,content)函数中对全局变量completeness进行了重新赋值
		t.tf = countTf(word0, content) + t.isHead;
		if ((t.tf - t.isHead == 1) && (t.isHead == 1))
			t.completeness = false;
		else
			t.completeness = completeness;
		if ((!t.completeness) && (t.word.length() == 1))
			return null;
		if ((!t.completeness)
				&& (t.tf - t.isHead > 0)
				&& (!t.type.matches("j|l") && ((t.isHead == 1) || (t.type
						.matches("n|nz|a|nr|v"))))) {
			if ((t.tf - t.isHead > 2)
					&& (!(t.type.contains("_") && (t.tf - t.isHead < 3)))
					&& (!(t.type.matches("a|v") && (t.tf - t.isHead < 6)))) {
				String left = wordleft(t);
				String right = wordRight(t);

				char c1 = ' ';
				char c2 = ' ';
				if ((left != null)
						&& (!uniTokens.containsKey(left.substring(1)))
						&& (left.length() < 5)) {
					c1 = left.charAt(0);
					if ((left.length() > 1)
							&& (!((left.length() == 2) && (left.endsWith("国"))))) {
						left = left.substring(1);
						Matcher m = Pattern.compile("\\p{P}").matcher(left);
						if (!m.find()) {
							t.word = left + t.word;
						}
					}
				}
				if ((right != null)
						&& (!uniTokens.containsKey(right.substring(0, right
								.length() - 1))) && (right.length() < 5)) {
					c2 = right.charAt(right.length() - 1);
					if (right.length() > 1) {
						right = right.substring(0, right.length() - 1);
						if (!right.equals("\n")) {
							Matcher m = Pattern.compile("\\p{P}")
									.matcher(right);
							if (!m.find()) {
								t.word = t.word + right;
							}
						}
					}
				}
				if ((c1 == '“') && (c2 == '”'))
					t.inQuotation = 1;

				if (!word0.equals(t.word.replaceAll("\\^", ""))) {
					String word1 = t.word.replaceAll("\\^", "");
					uniTokens.remove(word0);
					if (btTokens.containsKey(word0)) {
						btTokens.remove(word0);
						btTokens.put(word1, t);// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
					}
					uniTokens.put(word1, t);
					word0 = word1;
				}
			}
		}
		return word0;
	}

	public void regularPlaceName(String word0, Hashtable results, Token t) {
		Token term = (Token) results.get(word0);
		if (term == null)
			term = (Token) results.get(word0 + "省");
		if (term == null)
			term = (Token) results.get(word0 + "市");
		term.tf += t.tf;
		term.isHead += t.isHead;
		term.inQuotation += t.inQuotation;
		term.bonus += t.bonus;
		term.df += t.df;
		term.score = score(term, DAO.uniTermSum, term.df);
		t.word += "*";
	}

	public boolean dealWithOverlap(Hashtable results, Token t, String word0) {
		Enumeration e = results.keys();
		boolean addToResult = true;
		String[] removeword = new String[6];
		int removewordSize = -1;
		double newScore = 0;
		int personName = 0;
		while (e.hasMoreElements()) { // deal with the overlap situation
			String key = (String) e.nextElement();
			Token term = (Token) results.get(key);
			if (key.contains(word0)) {// short after long
				if ((term.inQuotation == 1) || (term.tf >= t.tf)
						|| (word0.length() == 1) || (term.type.equals("ns"))
						|| (term.type.contains("_"))) {
					addToResult = false;
					if (btTokens.containsKey(word0))
						btTokens.remove(word0);
					break;
				}
				// yao
			} else if (word0.contains(key)) {// long after short
				if ((key.length() == 1)
						|| (t.inQuotation == 1)
						|| (t.tf >= term.tf)
						|| term.type.matches("v|ad|a|b")
						|| term.type.contains("_")
						|| ((t.isHead == 1) && (t.tf > 3) && (t.stability > 0.9))) {
					if (!((term.tf > t.tf + 2) && (term.type
							.matches("ns|nz|n|j|t")))) {
						removewordSize++;
						removeword[removewordSize] = key;
						if (btTokens.containsKey(key))
							btTokens.remove(key);
						if (term.score > newScore)
							newScore = term.score;
					}
				} else {
					if (t.type.equals("newword") || t.type.matches(".*_.*_.*")
							|| (t.del && (term.type.equals("ns")))) {
						t.del = true;
						addToResult = false;
						break;
					}
				}
			}
		}
		if (addToResult) {
			if (removewordSize > -1) {
				for (int j = 0; j < removewordSize + 1; j++)
					results.remove(removeword[j]);
				t.score = newScore;
			}
			if (t.type.contains("nr")) {
				personName++;
				// 此处判断是为了避免出现过多的人名
				if (personName >= 3)
					return false;
			}
			results.put(word0, t);
		}
		return true;
	}

	// 此处功能是参照二元文法中词组进行结果中的组合处理
	public void backToBigram(Hashtable results) {// back to bigram
		Iterator j = biTs.iterator();
		int count = 0;
		// while (j.hasNext()&&(count<5)){
		while (j.hasNext() && (count < KEYNUM)) {// modified by lijun
			count++;
			Token tb = (Token) j.next();
			String word1 = tb.word.replaceAll("\\^", "");
			if (uniTokens.containsKey(word1))
				continue;
			String[] word = tb.word.split("\\^");
			if (word.length < 2)
				continue;
			if (results.containsKey(word[0]) && results.containsKey(word[1])) {
				double tTf = Math.pow((double) tb.tf + tb.isHead
						+ tb.inQuotation + tb.bonus + tb.firstpara, 3.0);
				double temp = (double) tTf * tb.stability / tfSum;
				// System.out.println(tb.word+"\t"+temp);
				if ((tb.tf + tb.isHead > 2) && (temp - 0.3 > 0)
						&& (tb.stability - 0.3 > 0)) {
					results.remove(word[0]);
					results.remove(word[1]);
					results.put(word1, tb);
				} else {
					Token tu1 = (Token) results.get(word[0]);
					Token tu2 = (Token) results.get(word[1]);
					if ((tb.tf + tb.isHead > 2) && (tu1.tf == tb.tf)
							&& (tu2.tf > tb.tf)) {
						tu1.word = tb.word;
						tu1.type = tb.type;
						results.remove(word[0]);
						results.put(word1, tu1);
					}
					if ((tb.tf + tb.isHead > 2) && (tu2.tf == tb.tf)
							&& (tu1.tf > tb.tf)) {
						tu2.word = tb.word;
						tu2.type = tb.type;
						results.remove(word[1]);
						results.put(word1, tu2);
					}
				}
			} else if (results.containsKey(word[0])) {
				Token tu = (Token) results.get(word[0]);
				if ((tb.tf == tu.tf) && (word[1].length() == 1)) {
					results.remove(word[0]);
					results.put(word1, tb);
				}
			} else if (results.containsKey(word[1])) {
				Token tu = (Token) results.get(word[1]);
				if ((tb.tf == tu.tf) && (word[0].length() == 1)) {
					results.remove(word[1]);
					results.put(word1, tb);
				}
			}
		}
	}

	public void findPotentialFromBT(TreeSet btTs) { // find potential keyword
		// from btToken and put them
		// into btTS
		Enumeration e = results.elements();
		Set rs = new HashSet();
		while (e.hasMoreElements()) {
			Token ta = (Token) e.nextElement();
			String[] word = ta.word.split("\\^");
			for (int j = 0; j < word.length; j++)
				rs.add(word[j]);
			ta.word = ta.word.replaceAll("\\^", "");
			if (btTokens.containsKey(ta.word)) {
				btTokens.remove(ta.word);
			}
			// resultTs.add(ta);
		}

		e = btTokens.elements();
		while (e.hasMoreElements()) {// find back bigram and trigram
			Token ta = (Token) e.nextElement();
			String[] word = ta.word.split("\\^");
			if (word.length == 2) {
				if (results.containsKey(word[0] + word[1]))
					continue;
				if (results.containsKey(word[0])
						&& results.containsKey(word[1]))
					continue;
				if (checkHashtable(word[0] + word[1], results))
					continue;
				String key = "";
				if (results.containsKey(word[0])) {
					key = word[0];
				} else if (results.containsKey(word[1])) {
					key = word[1];
				}
				if (!key.equals("")) {
					Token tr = (Token) results.get(key);
					if (tr.type.equals("ns"))
						continue;
					if ((ta.isHead == 1) || (ta.tf > 4)) {
						results.remove(key);
						tr.word = word[0] + word[1];
						tr.type = ta.type;
						results.put(tr.word, tr);
						// System.out.println(news.newsid+"\t"+key+"<->"+tr.word+"\tExpand");
					}
					continue;
				}
				if (rs.contains(word[0]) || rs.contains(word[1]))
					continue;
				if (!results.containsKey(word[0])
						&& !results.containsKey(word[1])) {
					if ((ta.isHead == 1) || (ta.tf > 4))
						btTs.add(ta);
				}
			}
			if (word.length == 3) {
				if (results.containsKey(word[0] + word[1] + word[2]))
					continue;
				if (results.containsKey(word[0])
						&& results.containsKey(word[1])
						&& results.containsKey(word[2]))
					continue;
				if (checkHashtable(word[0] + word[1] + word[2], results))
					continue;
				String key = "";
				if (results.containsKey(word[0] + word[1])) {
					key = word[0] + word[1];
				} else if (results.containsKey(word[1] + word[2])) {
					key = word[1] + word[2];
				} else if (results.containsKey(word[0])
						&& !results.containsKey(word[1])
						&& !results.containsKey(word[2])) {
					key = word[0];
				} else if (results.containsKey(word[1])
						&& !results.containsKey(word[0])
						&& !results.containsKey(word[2])) {
					key = word[1];
				} else if (results.containsKey(word[2])
						&& !results.containsKey(word[1])
						&& !results.containsKey(word[0])) {
					key = word[2];
				}
				if (!key.equals("")) {
					Token tr = (Token) results.get(key);
					if (tr.type.equals("ns"))
						continue;
					if ((ta.isHead == 1) || (ta.tf > 4)) {
						results.remove(key);
						tr.word = word[0] + word[1] + word[2];
						tr.type = ta.type;
						results.put(tr.word, tr);
						// System.out.println(news.newsid+"\t"+key+"<->"+tr.word+"\tExpand");
					}
					continue;
				}
				if (rs.contains(word[0]) || rs.contains(word[1])
						|| rs.contains(word[2]))
					continue;
				if (!results.containsKey(word[0])
						&& !results.containsKey(word[1])
						&& !results.containsKey(word[2])) {
					if ((ta.isHead == 1) || (ta.tf > 3))
						btTs.add(ta);
				}
			}
		}
	}

	public void finalDeal(News news) {
		Iterator i = uniTs.iterator();
		results = new Hashtable();
		// while (i.hasNext() && results.size()<5){//get the top five
		while (i.hasNext() && results.size() < KEYNUM) {// modify by lj,
			// 2007-09-26
			// ++++++++++++++++++++++++
			Token t = (Token) i.next();
			// if (t.word.replaceAll("\\^", "").equals("驱车模"))
			String word0 = t.word.replaceAll("\\^", "");

			if (((t.stability < 0.10) || (!t.completeness)) && (t.df < 100)) {
				word0 = getcompleteness(word0, t);
				if (word0 == null)
					continue;
			}

			if (!t.word.contains("^") && (t.isHead == 0) && (t.tf < 3)
					&& (t.df < 20) && (!t.type.matches("nt|ns|nz")))
				continue; // df<20 ,8

			if (t.type.equals("ns")
					&& (t.word.endsWith("市") || t.word.endsWith("省"))
					&& (t.df > 65)) {// df>65,30
				t.word = t.word.substring(0, t.word.length() - 1);
				word0 = word0.substring(0, word0.length() - 1);
			}

			if (word0.length() == 1 && !news.headLine.contains(word0))
				continue;
			if (word0.length() == 1 && !DAO.singleWordSet.contains(word0))
				continue;

			// 如果候选结果中已经存在，则调整对应参数后直接过滤
			if (results.containsKey(word0)
					|| (results.containsKey(word0 + "省") || (results
							.containsKey(word0 + "市")))) {
				regularPlaceName(word0, results, t);
				uniTs.remove(t);
				continue;
			}

			// 对有重复或重叠现象的长短词进行处理，当人名过多时则直接过滤，否则把该词存放于results中去
			if (!dealWithOverlap(results, t, word0))
				continue;

			// if (results.size() == 5) backToBigram(results);
			// 当达到指定的挑选个数时，进行下边的关键词组合处理
			if (results.size() == KEYNUM)
				backToBigram(results);// modify by lj, 2007-09-26
			// ++++++++++++++++++++++++
		}

		TreeSet btTs = new TreeSet();
		// 从标题中筛选关键词
		findPotentialFromBT(btTs);// find potential keyword from btToken and put
		// them into btTS

		Enumeration e = results.elements();
		TreeSet resultTs = new TreeSet();
		while (e.hasMoreElements()) {// sort results
			Token ta = (Token) e.nextElement();
			ta.word = ta.word.replaceAll("\\^", "");
			resultTs.add(ta);
		}
		Iterator it = resultTs.iterator();
		// news.keywords = new Token[5];
		news.keywords = new Token[KEYNUM];// modify by lj, 2007-09-26
		// ++++++++++++++++++++++++

		int count = 0;
		// while (it.hasNext()&&(count<5)){//put results in to News.keywords[]
		while (it.hasNext() && (count < KEYNUM)) {// modify by lj, 2007-09-26
			// ++++++++++++++++++++++++
			Token t = (Token) it.next();
			news.keywords[count] = t;
			count++;
		}

		it = btTs.iterator();
		if (btTs.size() > 0) {// check if potential keywords of btTS can replace
			// News.keywords[4 or 5]
			Token t4 = (Token) it.next();
			int index = news.keywords.length - 1;
			t4.word = t4.word.replaceAll("\\^", "");
			// System.out.println(news.newsid+"\t"+news.keywords[index].word+"<->"+t4.word+"\tReplace");
			// System.out.println(news.keywords[index-1].word+"\t"+news.keywords[index-1].isHead+"\t"+news.keywords[index-1].tf+"\t"+news.keywords[index-1].type);
			// System.out.println(news.keywords[index].word+"\t"+news.keywords[index].isHead+"\t"+news.keywords[index].tf+"\t"+news.keywords[index].type);
			// System.out.println(t4.word+"\t"+t4.isHead+"\t"+t4.tf+"\t"+t4.type);

			if (replace(news.keywords[index], t4)) {
				// System.out.println(news.newsid+"\t"+news.keywords[index].word+"<->"+t4.word+"\tReplace");
				news.keywords[index] = t4;
			} else if (replace(news.keywords[index - 1], t4)) {
				// System.out.println(news.newsid+"\t"+news.keywords[index-1].word+"<->"+t4.word+"\tReplace");
				news.keywords[index - 1] = t4;
			}
		}

		// 进行新闻的特征提取(当进行以关键词抽取为基础进行分类和聚类，以及相似度计算时的特征选取依据uniTs的内容进行！！！！！！！！！)
		news.keyTs = new TreeSet();
		Iterator iter = uniTs.iterator();
		while (iter.hasNext()) {
			Token tok = (Token) iter.next();
			if (tok.score > 0) {
				if (tok.word.contains("^")) {
					tok.word = tok.word.replaceAll("\\^", "");
				}

				news.keyTs.add(tok);
			}
		}
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	}

	public boolean replace(Token oldt, Token newt) {
		double d = (double) oldt.tf / newt.tf;
		if (d > 1.90)
			return false;
		if (newt.isHead > oldt.isHead)
			return true;
		if ((oldt.isHead > newt.isHead) || (oldt.type.contains("_")))
			return false;
		if (oldt.type.matches("vn|v|a|ad"))
			return true;
		if (oldt.type.matches("j") && (oldt.isHead + oldt.tf > 3))
			return false;
		if (newt.word.length() > oldt.word.length())
			return true;
		if (newt.tf + 2 > oldt.tf)
			return true;
		return false;
	}

	public void finalDealUni() {
		int count = 0;
		Iterator i = headTs.iterator();
		while (i.hasNext() && (count < 6)) {
			Token t = (Token) i.next();
			count++;
			if (t.tf < 2)
				break;
			if (t.word.length() > 8)
				continue;
			if ((count > 4) && (t.tf < 6))
				break;
			if ((count > 2) && (t.tf < 4))
				break;

			boolean addToResult = true;
			Enumeration e = uniTokens.keys();

			String[] keySubTf = new String[10];
			int keySubTfSize = -1;
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				if (key.contains(t.word)) {
					addToResult = false;
					break;
				}
				if ((t.word.length() > 2) && t.word.contains(key)
						&& (key.length() > 1)) {
					if ((t.stability > 0.9) && (t.tf > 7)
							&& (t.word.length() > 4)) {
						keySubTfSize++;
						keySubTf[keySubTfSize] = key;
						continue;
					}
					Token uniT = (Token) uniTokens.get(key);
					if (!uniT.completeness)
						continue;
					if ((double) t.tf / uniT.tf < 0.830) {
						if (t.del) {
							addToResult = false;
							break;
						}
					}
					if (DAO.termsUni.containsKey(key)) {
						uniT.df = ((Integer) DAO.termsUni.get(key)).intValue();
						if ((uniT.tf == t.tf) && (uniT.df > 200)) {// >200,120
							addToResult = false;
							break;
						}
					}
					if (uniT.type.equals("newword")
							&& (!t.type.equals("newword"))) {
						addToResult = false;
						break;
					}
					keySubTfSize++;
					keySubTf[keySubTfSize] = key;
				}
			}
			if (addToResult) {
				if (keySubTfSize > -1)
					for (int j = 0; j < keySubTfSize + 1; j++) {
						Token uniT = (Token) uniTokens.get(keySubTf[j]);
						uniT.tf -= t.tf;
						if (uniT.tf == 0)
							uniT.score = 0;
						else {
							if (uniT.word.contains("\\^")) {
								int c1 = uniT.word.indexOf("^");
								int c2 = uniT.word.lastIndexOf("^");
								long termSum = 0l;
								if (c1 == c2)
									termSum = DAO.biTermSum;
								else
									termSum = DAO.triTermSum;
								uniT.score = score(uniT, termSum, uniT.df);
							}
						}
					}
				uniTokens.put(t.word, t);
			}
		}
	}

	public void finalDealBi() {
		Iterator i = biTs.iterator();
		while (i.hasNext()) {
			Token t = (Token) i.next();
			String[] word = t.word.split("\\^");
			if (word.length != 2)
				continue;
			if (t.type.equals("nr_nr")) {
				if (!uniTokens.containsKey(word[0] + word[1]))
					uniTokens.put(word[0] + word[1], t);
				continue;
			}
			double tTf = Math.pow((double) t.tf + t.isHead * 2 + t.inQuotation
					* 3 + t.bonus + t.firstpara, 3.0);
			// System.out.println(t.word+"\t"+tTf+"\t"+t.stability+"\t"+tfSum+"\t"+tTf*t.stability/tfSum);
			/*
			 * for (int j=0; j<2; j++){ if
			 * (uniTokens.containsKey(word[j])&&(word[j].length()>1)){ Token tu
			 * = (Token)uniTokens.get(word[j]); if (((tu.tf == t.tf)&&(tu.tf >
			 * 1))||((double)tTf*t.stability/tfSum > 0.480)){
			 * 
			 * } } }
			 */
			if ((double) tTf * t.stability / tfSum > 0.480) {
				for (int j = 0; j < 2; j++) {
					if (uniTokens.containsKey(word[j])) {
						Token tu = (Token) uniTokens.get(word[j]);
						tu.bonus -= t.tf;
						if (t.tf == tu.tf)
							t.del = false;
						// tu.tf -= t.tf;
						// uniTokens.remove(word[j]);
					}
				}
				if (!uniTokens.containsKey(word[0] + word[1]))
					uniTokens.put(word[0] + word[1], t);
			} else {
				for (int j = 0; j < 2; j++) {
					if (uniTokens.containsKey(word[j])
							&& (word[j].length() > 1)) {
						Token tu = (Token) uniTokens.get(word[j]);
						tu.bonus++;
					}
				}
			}
		}
	}

	public boolean triCheck(Token t) {
		double tTf = Math.pow((double) t.tf + t.isHead * 2 + t.inQuotation * 3
				+ t.firstpara, 3.0);
		if ((double) tTf * t.stability / tfSum < 0.51)
			return false;
		if ((t.word.length() > 5) && (t.stability < 0.8)
				&& (t.inQuotation == 0))
			return false;
		return true;
	}

	public void finalDealTri() {
		Iterator i = triTs.iterator();
		while (i.hasNext()) {
			Token t = (Token) i.next();
			String[] word = t.word.split("\\^");
			if (word.length != 3)
				continue;
			// double tTf =
			// Math.pow((double)t.tf+t.isHead*2+t.inQuotation*3+t.firstpara,3.0);
			// System.out.println(t.word+"\t"+tTf*t.stability/tfSum);
			if (triCheck(t)) {
				for (int j = 0; j < 2; j++) {
					if (biTokens.containsKey(word[j] + word[j + 1])) {
						Token tb = (Token) biTokens.get(word[j] + word[j + 1]);
						tb.bonus -= t.tf;
						if (t.tf == tb.tf)
							t.del = false;
						// biTokens.remove(word[j]+word[j+1]);
					}
				}
				for (int j = 0; j < 3; j++) {
					if (uniTokens.containsKey(word[j])) {
						Token tu = (Token) uniTokens.get(word[j]);
						tu.bonus -= t.tf;
						if (t.tf == tu.tf)
							t.del = false;
						// uniTokens.remove(word[j]);
					}
				}
				uniTokens.put(word[0] + word[1] + word[2], t);
			} else {
				if (biTokens.containsKey(word[1] + word[2])
						&& biTokens.containsKey(word[0] + word[1])) {
					Token tb1 = (Token) biTokens.get(word[0] + word[1]);
					Token tb2 = (Token) biTokens.get(word[1] + word[2]);
					if (tb2.tf > tb1.tf) {
						tb2.bonus++;
						biTokens.remove(word[0] + word[1]);
						continue;
					}
					if ((DAO.termsBi.containsKey(word[0] + word[1]) && !DAO.termsBi
							.containsKey(word[1] + word[2]))
							|| (tb1.tf > tb2.tf)) {
						tb1.bonus++;
						biTokens.remove(word[1] + word[2]);
						continue;
					}
					if (DAO.termsBi.containsKey(word[1] + word[2])
							&& !DAO.termsBi.containsKey(word[0] + word[1])) {
						tb2.bonus++;
						biTokens.remove(word[0] + word[1]);
						continue;
					}
				}
				if (biTokens.containsKey(word[0] + word[1])) {
					Token tb = (Token) biTokens.get(word[0] + word[1]);
					tb.bonus++;
				}
				if (biTokens.containsKey(word[1] + word[2])) {
					Token tb = (Token) biTokens.get(word[1] + word[2]);
					tb.bonus++;
				}
				if (!biTokens.containsKey(word[1] + word[2])
						&& !biTokens.containsKey(word[0] + word[1]))
					for (int k = 0; k < 3; k++) {
						if (uniTokens.containsKey(word[k])
								&& (word[k].length() > 1)) {
							Token tu = (Token) uniTokens.get(word[k]);
							tu.bonus++;
						}
					}
			}
		}
	}

	public boolean containsNr(String word, List l) {
		if (l == null)
			return false;

		for (int i = 0; i < l.size(); i++) {
			String name = (String) l.get(i);
			if (word.contains(name))
				return true;
		}
		return false;
	}

	// 进行未登录词及新词的挖掘操作
	public void specialDeal(String headline, String content) {
		headline = headline.trim().replace(" ", "").replace("　", "");
		content = content.trim();
		if (headline.equals(""))
			return;

		// dealWith Quotation Mark
		String[] dealText1 = headline.split("“");
		if (dealText1.length > 1) {
			for (int i = 1; i < dealText1.length; i++) {
				int j = dealText1[i].indexOf("”");
				if (j > -1) {
					// 解析出被引号所引用的词
					String key = dealText1[i].substring(0, j);
					if ((key.length() > 7) || (key.length() < 2))
						continue;
					if (uniTokens.containsKey(key) || biTokens.containsKey(key)
							|| triTokens.containsKey(key)) {
						// 如果已经在某文法关键词候选集合中，则需要进行标定该词为强调引用
						setQuotation(uniTokens, key);
						setQuotation(biTokens, key);
						setQuotation(triTokens, key);
						continue;
					}
					// 统计该词词频值
					int tf = 1 + countTf(key, content);
					// 只记录词频大于2的强调引用词
					if (tf > 2) {
						// 其中“tiq”表示该词的类型为被强调引用词
						Token t = new Token(key, "tiq", 1, 1, tf);
						if (firstPara.indexOf(key) > -1)
							t.firstpara = 1;
						// 把该词保存至一元文法候选词集合中
						uniTokens.put(key, t);
					} else {
					}
					// System.out.println("tiq"+(double) tf/tfSum);
				}
			}
		}
		dealText1 = headline.split("《");
		if (dealText1.length > 1) {
			for (int i = 1; i < dealText1.length; i++) {
				int j = dealText1[i].indexOf("》");
				if (j > -1) {
					String key = dealText1[i].substring(0, j);
					if (uniTokens.containsKey(key) || biTokens.containsKey(key)
							|| triTokens.containsKey(key)) {
						setQuotation(uniTokens, key);
						setQuotation(biTokens, key);
						setQuotation(triTokens, key);
						continue;
					}
					int tf = 1 + countTf(key, content);
					if ((double) tf / tfSum > 0.02) {
						Token t = new Token(key, "tiq", 1, 1, tf);
						if (firstPara.indexOf(key) > -1)
							t.firstpara = 1;
						uniTokens.put(key, t);
					} else {
					}
					// System.out.println("book"+(double) tf/tfSum);
				}
			}
		}
		dealText1 = headline.split("＂");
		if (dealText1.length > 1) {
			for (int i = 2; i < dealText1.length; i = i + 2) {
				String key = dealText1[i];
				if ((key.length() > 7) || (key.length() < 2))
					continue;
				if (uniTokens.containsKey(key) || biTokens.containsKey(key)
						|| triTokens.containsKey(key)) {
					setQuotation(uniTokens, key);
					setQuotation(biTokens, key);
					setQuotation(triTokens, key);
					continue;
				}
				int tf = 1 + countTf(key, content);
				if (tf > 2) {
					Token t = new Token(key, "tiq", 1, 1, tf);
					if (firstPara.indexOf(key) > -1)
						t.firstpara = 1;
					uniTokens.put(key, t);
				}
			}
		}

		// dealWith special phrase
		// boolean inQuotation = false;
		// headLineICT = headLineICT
		// System.out.println(headLineICT);
		// 挖掘标题中的专有名词和缩略语
		String[] titleWords = headLineICT.split("  ");
		Set headSet = new HashSet();
		List namelist = new ArrayList();
		String type0 = "";
		StringBuffer newHeadline = new StringBuffer();
		for (int j = 0; j < titleWords.length; j++) {
			int pos1 = titleWords[j].lastIndexOf("/");
			if (pos1 > 0) {
				String type1 = titleWords[j].substring(pos1 + 1);
				String key1 = titleWords[j].substring(0, pos1);
				// System.out.println(key1+""+type1);
				titleWords[j] = key1;
				headTokens.put(key1, type1);
				if (type1.matches("f|t|d|r|m"))
					headSet.add(key1);
				if (type1.matches("nr|nx"))
					namelist.add(key1);
				if (type1.matches("w|c") || key1.equals("/")
						|| key1.equals("／")) {
					titleWords[j] = " ";
				}
				if (type0.equals("m")) {
					if (key1.startsWith("万")) {
						titleWords[j - 1] = "";
						if (key1.length() == 1)
							titleWords[j] = " ";
						else
							titleWords[j] = " " + key1.substring(1);
					}
					if (type1.equals("q")) {
						titleWords[j] = " ";
						titleWords[j - 1] = "";
					}
				}
				if (key1.matches("(\\d|[１２３４５６７８９０]){1,}.*"))
					titleWords[j] = " ";
				type0 = type1;
			} else {
				if (titleWords[j].equals(""))
					titleWords[j] = " ";
			}
			if (j > 0)
				newHeadline.append(titleWords[j - 1]);
			if (j == titleWords.length - 1)
				newHeadline.append(titleWords[j]);
		}

		// headTokens = new Hashtable();
		headTs = new TreeSet();
		// System.out.println(newHeadline.toString());
		String[] dealWords = newHeadline.toString().split(" ");
		for (int j = 0; j < dealWords.length; j++) {
			dealWords[j] = dealWords[j].trim().replaceAll("　", "");
			int size = dealWords[j].length();
			if (size < 2)
				continue;
			if (size == 2) {
				if (dealWords[j].contains("队"))
					continue;
				if (headSet.contains(dealWords[j]))
					continue;
				int tf = 1 + countTf(dealWords[j], content);
				if (!completeness) {
					modifyHashtable(dealWords[j], uniTokens);
					modifyHashtable(dealWords[j], biTokens);
					continue;
				} else {
					Token t = new Token(dealWords[j], "newword", 1, 0, tf, 0.99);
					if (modifyHashtable(t, uniTokens)
							|| modifyHashtable(t, biTokens)
							|| modifyHashtable(t, triTokens))
						continue;
					if (checkHashtable(t, uniTokens))
						continue;
					double stability = (double) tf
							/ (countTf(dealWords[j].substring(0, dealWords[j]
									.length() - 1), content)
									+ countTf(dealWords[j].substring(1,
											dealWords[j].length()), content)
									+ 2 - tf);
					if (firstPara.indexOf(dealWords[j]) > -1)
						t.firstpara = 1;
					if (((double) tf / tfSum > 0.021) && (stability > 0.39))
						headTs.add(t);
				}
			} else {
				int[][] tf = new int[size - 1][size + 1];
				for (int i = 0; i < size - 1; i++) {
					for (int k = 0; k < size - 1 - i; k++) {
						if (dealWords[j].substring(k, k + 2 + i).endsWith("队")
								&& (i > 0))
							continue;
						if (headSet.contains(dealWords[j].substring(k, k + 2
								+ i)))
							continue;
						if (((dealWords[j].charAt(k) == '的') || (dealWords[j]
								.charAt(k + 1 + i) == '的'))
								&& headTokens.containsKey("的")) {
							if (((String) headTokens.get("的")).equals("u"))
								continue;
						}
						if ((dealWords[j].charAt(k) == '在')
								&& headTokens.containsKey("在")) {
							if (((String) headTokens.get("在")).equals("p"))
								continue;
						}
						int temp = headline.indexOf(dealWords[j].substring(k, k
								+ 2 + i));
						char c1 = (temp > 0) ? headline.charAt(temp - 1) : ' ';
						char c2 = (temp + 2 + i < headline.length()) ? headline
								.charAt(temp + 2 + i) : ' ';
						// if (dealWords[j].substring(k,k+2+i).equals("突发公共事件"))
						// System.out.println("dfad");
						tf[k][k + 2 + i] = 1 + countTf(dealWords[j].substring(
								k, k + 2 + i), content, c1, c2);
						if (completeness) {
							if (i > 0) {
								if ((double) tf[k][k + 2 + i]
										/ tf[k + 1][k + 2 + i] > 0.74)
									tf[k + 1][k + 2 + i] = 1;
								if ((double) tf[k][k + 2 + i]
										/ tf[k][k + 1 + i] > 0.74)
									tf[k][k + 1 + i] = 1;
							}
						} else
							tf[k][k + 2 + i] = -1;
					}
				}
				for (int i = 0; i < size - 1; i++) {
					int goon = 0;
					for (int k = 0; k < size - 1 - i; k++) {
						if (tf[k][k + 2 + i] == 1)
							goon++;

						if (dealWords[j].substring(k, k + 2 + i).endsWith("队")
								&& (i > 0))
							continue;
						// if (dealWords[j].substring(k,k+2+i).equals("突发公共事件"))
						// System.out.println(dealWords[j].substring(k,k+2+i));
						if (headSet.contains(dealWords[j].substring(k, k + 2
								+ i)))
							continue;
						if (tf[k][k + 2 + i] < 2) {// uncompleteness term,
							// remove from bitokens and
							// tritokens
							modifyHashtable(dealWords[j]
									.substring(k, k + 2 + i), uniTokens);
							modifyHashtable(dealWords[j]
									.substring(k, k + 2 + i), biTokens);
							modifyHashtable(dealWords[j]
									.substring(k, k + 2 + i), triTokens);
							continue;
						}

						Token t = new Token(dealWords[j]
								.substring(k, k + 2 + i), "newword", 1, 0,
								tf[k][k + 2 + i], 0.99);
						if (modifyHashtable(t, uniTokens)
								|| modifyHashtable(t, biTokens)
								|| modifyHashtable(t, triTokens))
							continue;

						if (tf[k][k + 2 + i] < 3)
							continue;

						double stability = (double) tf[k][k + 2 + i]
								/ (countTf(
										dealWords[j].substring(k, k + 1 + i),
										content)
										+ countTf(dealWords[j].substring(k + 1,
												k + 2 + i), content) + 2 - tf[k][k
										+ 2 + i]);
						t.stability = stability;
						if (checkHashtable(t, uniTokens))
							continue;
						if (checkHashtable(
								dealWords[j].substring(k, k + 2 + i), biTokens,
								t.tf, t.del)
								|| checkHashtable(dealWords[j].substring(k, k
										+ 2 + i), triTokens, t.tf, t.del))
							continue;

						if (containsNr(dealWords[j].substring(k, k + 2 + i),
								namelist)
								&& (tf[k][k + 2 + i] > 1))
							t.del = false;

						if (t.del) {
							int totf = (tfSum > 75) ? 4 : 3;
							if (tf[k][k + 2 + i] < totf)
								continue;
							if ((tf[k][k + 2 + i] == totf) && (i == 0))
								continue;
						}

						double tOfstability;
						// double tOfstability2=13;
						if (stability - t.stability > 0.10)
							tOfstability = 0.47;
						else if (i == 0) {
							tOfstability = 0.38;
							// tOfstability2 = 2.1;
						} else if (i == 1) {
							tOfstability = 0.67;
							// tOfstability2 = 2.1;
						} else
							tOfstability = 0.8;

						// if (dealWords[j].substring(k,k+2+i).equals("北京奥组委"))
						// System.out.println("dfad");
						if (firstPara.indexOf(dealWords[j].substring(k, k + 2
								+ i)) > -1)
							t.firstpara = 1;
						if (((double) tf[k][k + 2 + i] / tfSum > 0.021)
								&& (t.stability > tOfstability))
							// 把标题中发现的潜在关键词保存至headTs中去
							headTs.add(t);
					}
					if (goon == size - 1 - i)
						break;
				}
			}
		}
		// if (headTs.size()<2) return;

		/*
		 * for (int j = 0; j < titleWords.length-1; j++) { if
		 * (titleWords[j].equals(" ")||titleWords[j+1].equals(" ")) continue;
		 * String word1 =
		 * titleWords[j].charAt(titleWords[j].length()-1)+titleWords
		 * [j+1].substring(0,1);
		 * 
		 * if(!(biTokens.containsKey(word1)||checkHashtable(word1,uniTokens)||
		 * checkHashtable(word1,biTokens))){ System.out.println(word1); int tf =
		 * 1 + countTf(word1,content); if ((double)tf/tfSum> 0.025)
		 * uniTokens.put(word1, new Token(word1,"newword",1,0,tf)); }
		 * 
		 * String word2 = null; if (titleWords[j].length()>1) word2 =
		 * titleWords[
		 * j].substring(titleWords[j].length()-2,titleWords[j].length(
		 * ))+titleWords[j+1].substring(0,1); else{ if ((j-1>-1)&&
		 * !titleWords[j-1].equals(" ")) word2 =
		 * titleWords[j-1].charAt(titleWords
		 * [j-1].length()-1)+titleWords[j]+titleWords[j+1].substring(0,1); } if
		 * (!((word2 ==
		 * null)||biTokens.containsKey(word2)||triTokens.containsKey
		 * (word2)||checkHashtable
		 * (word2,uniTokens)||checkHashtable(word2,biTokens
		 * )||checkHashtable(word2,triTokens))){ System.out.println(word2); int
		 * tf = 1 + countTf(word2,content); if ((double)tf/tfSum> 0.025)
		 * uniTokens.put(word2, new Token(word2,"newword",1,0,tf)); }
		 * 
		 * if (titleWords[j+1].length()>1){ String word3 =
		 * titleWords[j].charAt(titleWords
		 * [j].length()-1)+titleWords[j+1].substring(0,2); if
		 * (!(biTokens.containsKey
		 * (word3)||checkHashtable(word3,biTokens)||checkHashtable
		 * (word3,uniTokens))){ System.out.println(word3); int tf = 1 +
		 * countTf(word3,content); if ((double)tf/tfSum> 0.025)
		 * uniTokens.put(word3, new Token(word3,"newword",1,0,tf)); } } }
		 */

		/*
		 * for (int i=0; i<headline.length()-1; i++){ if
		 * (headline.charAt(i)=='“'){ while (headline.charAt(i++)!='”'); if (i >
		 * headline.length()-2) return; } String word1 =
		 * headline.substring(i,i+2); if
		 * (!word1.matches(".*(（|）|\\?|：|\\)|\\(|，|．|《|》|、|％).*")){ if
		 * (!(uniTokens
		 * .containsKey(word1)||biTokens.containsKey(word1)||checkHashtable
		 * (word1,uniTokens)||checkHashtable(word1,biTokens))){
		 * 
		 * int tf = 1 + countTf(word1,content); if (tf>3) uniTokens.put(word1,
		 * new Token(word1,"newword",1,0,tf)); } } if (i+2<headline.length()){
		 * String word2 = headline.substring(i,i+3); if
		 * (!word2.matches(".*(（|）|\\?|：|\\)|\\(|，|．|《|》|、|％).*")){ if
		 * (!(uniTokens
		 * .containsKey(word2)||biTokens.containsKey(word2)||checkHashtable
		 * (word2,uniTokens)||checkHashtable(word2,biTokens))){ int tf = 1 +
		 * countTf(word2,content); if (tf>3) uniTokens.put(word2, new
		 * Token(word2,"newword",1,0,tf)); } } } }
		 */
	}

	public double score(Token t, long termSum, int k) {
		String word = t.word.replaceAll("\\^", "");
		double significance = 1;
		if (word.length() > 2)
			significance = Math.log(word.length()) / Math.log(2);
		if (word.length() > 8)
			significance = 3;
		double tf = Math.pow((t.tf + t.bonus), t1)
				* (1 + t.isHead * t2 + t.inQuotation * t3 + t.firstpara * t4 + significance
						* t7);
		double init = Math.log(termSum) * t5 - Math.pow(Math.log(k), t6);

		return tf * init;
	}

	// 进行特征拟合运算，其中ts为各关键词候选集合中各词的最终进行排名集合
	public void countScore(Hashtable newsTokens, Hashtable allTokens,
			TreeSet ts, long termSum) {
		Enumeration e = newsTokens.keys();
		int nrcount = 0;
		// System.out.println("#####################:");
		while (e.hasMoreElements()) {
			String word = (String) e.nextElement();
			// System.out.println("+++++++++++++++++++++"+word);

			// 过滤一些非中文的字符串
			if (word.matches("[┃━─＝＿\\d]{1,}")) {
				newsTokens.remove(word);
				continue;
			}

			Token t = (Token) newsTokens.get(word);
			// String type = t.type;
			// if (word.equals("漂流瓶"))
			// System.out.println("ok1");

			// 如果包含在停用词中则直接过滤
			if (DAO.stop1Set.contains(word) || DAO.stop2Set.contains(word))
				continue;
			if (DAO.stopSet.contains(word))
				continue;

			// 该词类型为非名词且长度小于3时,设置它为完整的
			if (!(t.type.matches("n") && (word.length() < 3))) {
				// 当该词在标题中出现时则设定它的对应特征值
				if ((t.isHead == 0) && (countTf(word, headLine) > 0))
					t.isHead = 1;
				t.tf = countTf(word, content) + t.isHead;
				t.completeness = completeness;
			}

			// +++++++++++++++++++++++++++++++++++++++++++++++++++++++该部分规则过强，因此进行暂时部分注释by
			// lijun
			// bonus为包含该词的所有长词的词频总和
			// if (t.tf +t.bonus+t.isHead<2){ newsTokens.remove(word);
			// continue;}

			// 判断如果为一元文法的词时，进行相应处理
			if (termSum == DAO.uniTermSum) {
				if (t.word.contains("^")) {
					ts.add(t); // 如果该词为复合词时则保存至uniTs中
					if (!t.type.equals("nr_nr")) {
						btTokens.put(word, t);// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
					}

					continue;
				}

				// if ((t.isHead == 0)&&(t.tf +t.bonus<4)&&
				// !t.type.matches("n.*|vn|j|t")) continue;

				if ((word.length() == 1) && (t.tf + t.isHead < 4))
					continue;

				// if
				// (t.type.matches("v|ad")&&(t.isHead*2+t.tf+t.bonus+t.firstpara
				// < 6)){
				// newsTokens.remove(word);
				// continue;
				// }

			} else { // 当该词为非一元文法词时，进行相应处理
				if (t.inQuotation == 0) {
					if (content.contains("《" + word + "》")
							|| content.contains("“" + word + "”")
							|| content.contains("‘" + word + "’"))
						t.inQuotation = 1;
				}
				// if ((t.tf +t.bonus+t.isHead<2)&&(t.inQuotation==0)) {
				// newsTokens.remove(word);
				// continue;
				// }
			}

			// if ((t.tf+t.isHead+t.bonus < 3)&&(((termSum == DAO.uniTermSum) &&
			// !t.type.matches("n.*|vn|j"))||(word.length()<2))){
			// newsTokens.remove(word);
			// continue;
			// }

			if (t.type.contains("nr") && (t.isHead == 0) && (nrcount < 5)) {
				checkPersonName(t);
				nrcount++;
			}

			// if (t.tf<2) {
			// newsTokens.remove(word);
			// continue;
			// }
			// +++++++++++++++++++++++++++++++++++++++++++++++++++++++

			int i = t.word.indexOf("^");
			int j = t.word.lastIndexOf("^");
			// if (word.equals("汽车电子狗"))
			// System.out.println("ok1");
			if (i > -1 && i > 0 && j > 0) {
				if (!t.completeness && (t.inQuotation == 0)
						&& ((i != j) || (t.type.matches(".*(vg|ng|k|tg)"))))
					continue;
				if ((!t.completeness || (t.isHead + t.tf < 5))
						&& (t.inQuotation == 0) && (t.type.contains("v_")))
					continue;

				int temp;
				if (i == j)
					temp = countTf(t.word.substring(0, i), content)
							+ countTf(t.word.substring(i + 1, t.word.length()),
									content) - t.tf;
				else
					temp = countTf(word.substring(0, j - 1), content)
							+ countTf(word.substring(i, word.length()), content)
							- t.tf;
				if (t.isHead == 1)
					temp += 2;
				t.stability = (double) t.tf / temp;
				if (t.stability > 10.0)
					t.stability = 1.0;
			}

			int tf = t.tf + t.isHead * 2 + t.inQuotation + t.bonus
					+ t.firstpara;

			int k = 0;
			// if (allTokens == null) System.out.println("ftft");
			String place = "";
			if ((word.endsWith("市") || word.endsWith("省"))
					&& (word.length() > 1))
				place = word.substring(0, word.length() - 1);

			if (!allTokens.containsKey(word)) {
				if (DAO.termsUni.containsKey(word))
					k = ((Integer) DAO.termsUni.get(word)).intValue();
				if (k == 0) {
					if (t.tf < 2)
						continue;
					k = tf;// k=tf,k=1
				}
			} else {
				k = ((Integer) allTokens.get(word)).intValue();
				if (!place.equals("") && allTokens.containsKey(place))
					k += ((Integer) allTokens.get(place)).intValue();
			}

			if (k == 0)
				continue;

			t.score = score(t, termSum, k);

			// System.out.println(t.word+": type = " + t.type + "  termSum = "+
			// termSum +"  k = "+ k); //to test by lj
			// 此处把经过过滤操作的剩余候选关键词加入到相应的TreeSet中去
			t.df = k;
			ts.add(t); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		}
		// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	}
}
