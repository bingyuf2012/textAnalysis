package text.thu.keyword.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.HashSet;
import java.util.Hashtable;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class DAO {
	protected static String termPathUni;
	protected static String termPathBi;
	protected static String termPathTri;

	public static String weblogPath;

	public static String stopWord1Path;
	public static String stopWord2Path;
	public static String stopWordPath;
	public static String singleWordPath;

	public static Hashtable tempterms = null;
	public static Hashtable termsUni;
	public static Hashtable termsBi;
	public static Hashtable termsTri;

	public static Set stop1Set;
	public static Set stop2Set;
	public static Set stopSet;
	public static Set singleWordSet;

	public static long uniTermSum = 0l;
	public static long biTermSum = 0l;
	public static long triTermSum = 0l;

	public static long termAllSum = 0l;
	static Hashtable termAll = null;
	static Hashtable term_to_id = null;

	public static ResourceBundle bundle = ResourceBundle.getBundle(
			"config", Locale.ENGLISH);

	static {
		termPathUni = bundle.getString("news.ICTUniterm");
		termPathBi = bundle.getString("news.ICTBiterm");
		termPathTri = bundle.getString("news.ICTTriterm");

		stopWord1Path = bundle.getString("news.stopword1");
		stopWord2Path = bundle.getString("news.stopword2");
		stopWordPath = bundle.getString("news.stopword");
		singleWordPath = bundle.getString("news.singleword");

		weblogPath = bundle.getString("news.weblogPath");

		loadTerm();
	}

	public static void loadTerm() {
		termsUni = new Hashtable();
		termsBi = new Hashtable();
		termsTri = new Hashtable();

		// 存放分类或聚类时的特征信息
		termAll = new Hashtable();
		term_to_id = new Hashtable();

		uniTermSum = loadTerm(termPathUni, termsUni);
		biTermSum = loadTerm(termPathBi, termsBi);
		triTermSum = loadTerm(termPathTri, termsTri);

		stop1Set = new HashSet();
		stop2Set = new HashSet();
		stopSet = new HashSet();
		singleWordSet = new HashSet();

		loadTerm(stopWord1Path, stop1Set);
		loadTerm(stopWord2Path, stop2Set);
		loadTerm(stopWordPath, stopSet);
		loadTerm(singleWordPath, singleWordSet);
	}

	public static void loadTerm(String termPath, Set hs) {
		File termFile = new File(termPath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(termFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				hs.add(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 从相应的文件里统计各种文法词的总词频
	public static long loadTerm(String termPath, Hashtable terms) {
		long termSum = 0l;
		File termFile = new File(termPath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(termFile));
			String line = null;
			String[] sec = null;
			while ((line = br.readLine()) != null) {
				sec = line.split("\\s");
				if (sec.length != 3) {
					continue;
				}
				Integer weight = new Integer(sec[2]);
				if (terms == null)
					terms = new Hashtable();

				terms.put(sec[1], weight);
				termSum += Long.parseLong(sec[2]);

				termAll.put(sec[1], weight);
				termAllSum += Long.parseLong(sec[2]);
				// 存放该信息便于查找每一个词所对应的id号
				if (!term_to_id.containsKey(sec[1])) {
					term_to_id.put(sec[1], term_to_id.size() + 1);
				}

			}
			// termSum = new Integer(sec[0]).intValue();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return termSum;
		// if (terms == null) System.out.println("===");
	}

	// 获取该词的特征ID号
	public static int getID(String word) {
		int id = -1;
		if (term_to_id.containsKey(word)) {
			id = (Integer) DAO.term_to_id.get(word);
		} else {
			termAllSum += 1;
			// 存放该信息便于查找每一个词所对应的id号
			term_to_id.put(word, term_to_id.size() + 1);
			id = term_to_id.size();
		}

		return id;
	}

	// 根据id获取相应的单词
	public static String getWord(int id) {
		String word = null;

		Enumeration enkey = DAO.term_to_id.keys();
		while (enkey.hasMoreElements()) {
			String key = (String) enkey.nextElement();
			if (DAO.term_to_id.get(key).equals(id)) {
				word = key;
			}
		}
		return word;
	}

	// 获取某词的词频，在计算该词的特征权重时使用
	public static int getTF(String word) {
		int tf = -1;
		if (termAll.containsKey(word)) {
			tf = (Integer) DAO.termAll.get(word);
		} else {
			tf = 1;
		}

		return tf;
	}

	public static void outputlog(String title, String content, String keywords) {
		File termFile = new File(weblogPath);

		try {
			RandomAccessFile randomWritefile = new RandomAccessFile(termFile,
					"rw");
			long address = termFile.length();
			randomWritefile.seek(address);
			randomWritefile.writeBytes("\r\n");
			byte[] b = ("headline:" + title).getBytes("GBK");
			randomWritefile.write(b);
			randomWritefile.writeBytes("\r\n");
			b = (content).getBytes("GBK");
			randomWritefile.write(b);
			randomWritefile.writeBytes("\r\n");
			b = ("keyword:" + keywords).getBytes("GBK");
			randomWritefile.write(b);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}
}
