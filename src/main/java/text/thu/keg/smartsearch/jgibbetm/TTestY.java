package text.thu.keg.smartsearch.jgibbetm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import text.searchSDK.util.CommonUtil;
import text.searchSDK.util.Constant;
import text.searchSDK.util.PrintConsole;

public class TTestY {
	@SuppressWarnings( { "unused", "rawtypes" })
	public static void main(String[] args) throws IOException {
		PrintConsole.PrintLog(new String(""
				+ Double.parseDouble("6.084606975640946E-4")), null);
		List<String> twords = getList(CommonUtil.getOutputDataDir()
				+ "/model-final.twords");
		// List<String> tentities = getList("./perp/model-final.tentities");
		// List<String> kesai = getList("./perp/model-final.kesai");
		List<String> tentities = getList(CommonUtil.getOutputDataDir()
				+ "/model-final.tentities");
		List<String> kesai = getList(CommonUtil.getOutputDataDir()
				+ "/model-final.kesai");
		double minValue = 0.15;
		int maxWordCount = 20;// 每个主题最多输出5个词
		int maxEntiesWordCount = 20;// 每个实体主题最多输出5个词
		LinkedHashMap<String, LinkedHashMap<String, String>> twords_map = formatTWords(twords);
		LinkedHashMap<String, LinkedHashMap<String, String>> tentities_map = formatTentities(tentities);
		List<LinkedHashMap<String, String>> tentities_list = formatTentities_list(tentities);
		List<List<String>> parseKeSai = parseKesai(kesai, minValue);

		String result = "";
		int ccc = 0;// 当前是第几个主题
		for (Entry<String, LinkedHashMap<String, String>> l : twords_map
				.entrySet()) {
			result += l.getKey() + "  (";// 输出标题
			LinkedHashMap<String, String> words = l.getValue();
			int count = 0;
			for (Entry<String, String> e : words.entrySet()) {
				if (count >= maxWordCount)
					break;// 如果数量超过5个，那么不输出
				result += "" + (e.getKey()).trim() + "  ";// +e.getValue();//输出标题的word1,word2...................
				count++;
			}
			result += ")\n";
			// 输出实体主题
			// for()

			// 获取当前主题对应的0th:5-3-6-1-3

			List<String> 主题对应实体主题的顺序 = parseKeSai.get(ccc);
			// 对实体主题列表进行输出
			for (int ii = 0; ii < 主题对应实体主题的顺序.size(); ii++) {
				String 顺序 = 主题对应实体主题的顺序.get(ii);// 应该输出第几个实体主题
				result += "        entityTopic" + 顺序 + "(";
				// 从tentities_list中获取LinkedHashMap<String,String>
				LinkedHashMap<String, String> tentities_list_one = tentities_list
						.get(Integer.parseInt(顺序) - 1);
				int count_2 = 0;
				for (Entry<String, String> e : tentities_list_one.entrySet()) {
					if (count_2 >= maxEntiesWordCount)
						break;// 如果数量超过5个，那么不输出
					result += " " + e.getKey() + " ";// +e.getValue()+" ";
					count_2++;
				}
				result += ")\n";
			}
			ccc++;
			result += "\n";
		}
		PrintConsole.PrintLog("result", result);
		PrintConsole.PrintLog("OK", "OK");
	}

	public static List<String> getList(String fileName) throws IOException {
		List<String> list = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(fileName)), Constant.UTF8));
		String line = "";
		while ((line = br.readLine()) != null) {
			list.add(line);
		}
		br.close();
		return list;
	}

	/**
	 * 格式化Twords
	 * 
	 * @param twords
	 * @return
	 */
	public static LinkedHashMap<String, LinkedHashMap<String, String>> formatTWords(
			List<String> twords) {
		LinkedHashMap linkedHashMap = new LinkedHashMap();// 总体Map
		LinkedHashMap linkedHashMap_2st = null;// 临时变量
		for (String s : twords) {
			if (s.startsWith("Topic")) {
				linkedHashMap_2st = new LinkedHashMap();
				linkedHashMap.put(s, linkedHashMap_2st);
			} else {
				linkedHashMap_2st.put((s.split(" "))[0], (s.split(" "))[1]);
			}

		}
		return linkedHashMap;
	}

	/**
	 * 格式化Tentities
	 * 
	 * @param twords
	 * @return
	 */
	public static LinkedHashMap<String, LinkedHashMap<String, String>> formatTentities(
			List<String> twords) {
		LinkedHashMap linkedHashMap = new LinkedHashMap();// 总体Map
		LinkedHashMap linkedHashMap_2st = null;// 临时变量
		for (String s : twords) {
			if (s.startsWith("EntityTopic")) {
				linkedHashMap_2st = new LinkedHashMap();
				linkedHashMap.put(s, linkedHashMap_2st);
			} else {
				try {
					linkedHashMap_2st.put((s.split(" "))[0], (s.split(" "))[1]);
				} catch (Exception e) {
					PrintConsole.PrintLog("------------------", s);
				}
			}
		}
		return linkedHashMap;
	}

	public static List<LinkedHashMap<String, String>> formatTentities_list(
			List<String> twords) {
		List list = new ArrayList();// 总体Map
		LinkedHashMap linkedHashMap_2st = null;// 临时变量
		for (String s : twords) {
			if (s.startsWith("EntityTopic")) {
				linkedHashMap_2st = new LinkedHashMap();
				list.add(linkedHashMap_2st);
			} else {
				try {
					linkedHashMap_2st.put((s.split(" "))[0], (s.split(" "))[1]);
				} catch (Exception e) {
					PrintConsole.PrintLog("------------------", s);
				}
			}
		}
		return list;
	}

	/**
	 * 解析kesai文件，获取的是一个List<List<String>>数据，其含义为，第一层是主题，第二层是实体主题的顺序 如
	 * 0th:5-3-6-1-3 1th:9-1-2-5-3 .............
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	public static List<List<String>> parseKesai(List<String> list,
			double minValue) {

		List<List<String>> result = new ArrayList();

		for (String s : list) {
			String[] ss = s.split(" ");
			List<ERDATA> tmp_list = new ArrayList<ERDATA>();
			int x = 1;
			for (String sss : ss) {

				tmp_list.add(new ERDATA(Double.parseDouble(sss), x));
				// tmp_list.add(sss+""+x);
				x++;
			}
			Collections.sort(tmp_list);
			List listcell = new ArrayList();
			for (ERDATA e : tmp_list) {
				double 实体主题的概率 = e.value;
				if (实体主题的概率 >= minValue)
					listcell.add("" + e.index);
			}
			result.add(listcell);
		}
		return result;
	}
}

class ERDATA implements Comparable {
	double value = 0;// P
	int index = 0;// index

	public ERDATA(double value, int index) {
		super();
		this.value = value;
		this.index = index;
	}

	@Override
	public int compareTo(Object o) {
		ERDATA o2 = (ERDATA) o;
		if (o2.value < this.value)
			return -1;// -1不变
		if (o2.value > this.value)
			return 1;// 1变
		return 0;
	}
}
