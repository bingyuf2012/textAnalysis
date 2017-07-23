package text.searchSDK.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
	public static String THREAD_DIR = CommonUtil.class.getResource("/")
			.getFile();
	public static String CONFIG_FILENAME = "config.properties";
	public static String FILENAME = THREAD_DIR + CONFIG_FILENAME;
	public static String NOISE_WORD = "noiseword.txt";
	public static String SITE_FILENAME = "siteweights.properties";
	public static String WEIGHT_FILENAME = THREAD_DIR + SITE_FILENAME;

	/**
	 * @功能：配置路径字符串处理
	 * @return
	 */
	public static String processPath(String dir) {
		return dir.replaceAll("%20", " ");
	}

	/**
	 * @功能：根据传入参数的不同，返回相应的配置文件属性值
	 * @param args
	 * @return
	 */
	public static String getPropertiesValue(String args) {
		Properties propertie = new Properties();
		String configdir = processPath(FILENAME);

		String value = "";
		try {
			FileInputStream inputFile = new FileInputStream(configdir);
			propertie.load(inputFile);

			if (propertie.containsKey(args)) {
				value = propertie.getProperty(args).trim();
				// System.out.println("value --- " + args + ":" + value);
			}
			inputFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 得到 txt 文件路径
	 * 
	 * @return
	 */
	public static String getTxtFilePath() {
		return getPropertiesValue("txtdata");
	}

	public static String getSqlFilePath() {
		return getPropertiesValue("sqldata");
	}

	public static String getOutputDataDir() {
		return getPropertiesValue("output");
	}

	/**
	 * 得到测试数据的路径
	 * 
	 * @param property
	 * @return
	 */
	public static String getTestData() {
		return getPropertiesValue("testdata");
	}

	/**
	 * @功能：读取 siteweights.properties文件中的所有站点及权重
	 */
	@SuppressWarnings("unchecked")
	public static Map readProperties() {
		String configdir = processPath(WEIGHT_FILENAME);
		Properties props = new Properties();
		Map map = new HashMap<String, String>();
		try {
			InputStream ips = new BufferedInputStream(new FileInputStream(
					configdir));
			props.load(ips);
			Enumeration enums = props.propertyNames();
			while (enums.hasMoreElements()) {
				String key = (String) enums.nextElement();
				String value = props.getProperty(key);
				// System.out.println(key + "=" + value);
				map.put(key, value);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * @功能：获取百度搜索的页数
	 * @return
	 */
	public static int getBPageCount() {
		int pagecount = 40;

		if (!getPropertiesValue("pagecount").equals("")) {
			pagecount = Integer.parseInt(getPropertiesValue("pagecount"));
		}
		return pagecount;
	}

	public static String getDBHost() {
		return getPropertiesValue("dbhost");
	}

	public static String getDBPort() {
		return getPropertiesValue("dbport");
	}

	public static String getDBUsername() {
		return getPropertiesValue("dbusername");
	}

	public static String getDBPassword() {
		return getPropertiesValue("dbpassword");
	}

	/**
	 * @功能：获取谷歌搜索的页数
	 * @return
	 */
	public static int getGPageCount() {
		int pagecount = 2;

		if (!getPropertiesValue("gpagecount").equals("")) {
			pagecount = Integer.parseInt(getPropertiesValue("gpagecount"));
		}
		return pagecount;
	}

	/**
	 * @功能：获取搜索的摘要的页数
	 * @return
	 */
	public static int getSPageCount() {
		int pagecount = 5;

		if (!getPropertiesValue("summarypages").equals("")) {
			pagecount = Integer.parseInt(getPropertiesValue("summarypages"));
		}
		return pagecount;
	}

	/**
	 * @功能：获取分类个数
	 * @return
	 */
	public static int getTopicNum() {
		int topicnum = 8;

		if (!getPropertiesValue("topicnum").equals("")) {
			topicnum = Integer.parseInt(getPropertiesValue("topicnum"));
		}
		return topicnum;
	}

	public static int getTopicNum(int docNum) {
		int TopicNum = 0;

		if (docNum > 0 && docNum < 30)
			TopicNum = 2;
		else if (docNum >= 30 && docNum < 200)
			TopicNum = 4;
		else if (docNum >= 200 && docNum < 500)
			TopicNum = 6;
		else if (docNum >= 500 && docNum < 1000)
			TopicNum = 8;
		// else if (docNum >= 1000 && docNum < 1500)
		else if (docNum >= 1000 && docNum < 2000)
			TopicNum = 10;
		// else if (docNum >= 1500 && docNum < 2000) {
		// TopicNum = 15;
		// } else if (docNum >= 2000) {
		// TopicNum = 20;
		// }
		else if (docNum >= 2000)
			TopicNum = 15;
		return TopicNum;
	}

	/**
	 * @功能：获取每个分类的label数
	 * @return
	 */
	public static int getTopicLabelNum() {
		int topiclabelnum = 10;

		if (!getPropertiesValue("topiclabelnum").equals("")) {
			topiclabelnum = Integer
					.parseInt(getPropertiesValue("topiclabelnum"));
		}
		return topiclabelnum;
	}

	/**
	 * @功能：获取搜索时间间隔
	 * @return
	 */
	public static long getTinterval() {
		long interval = 2 * 60;

		if (!getPropertiesValue("tinterval").equals("")) {
			String intervalstr = getPropertiesValue("tinterval");
			String[] intervalarr = intervalstr.split(",");
			interval = Long.parseLong(intervalarr[getIndexNum(intervalarr)]);
		}
		return interval;
	}

	/**
	 * 得到前 100 个实体
	 * 
	 * @return
	 */
	public static int getTop100Entity() {
		return Integer.parseInt(getPropertiesValue("topentity"));
	}

	/**
	 * @功能：从数组中获取一个随机下标
	 * @param array
	 * @return
	 */
	public static int getIndexNum(String[] array) {
		Random random = new Random();
		// 获取随机下标
		return random.nextInt(array.length);
	}

	/**
	 * @功能：获取干扰词的路径
	 * @return
	 */
	public static String getNoiseword() {
		String noiseword = "D:\\Workspaces\\.metadata\\.me_tcat\\webapps\\SmartSearch\\noiseword\\noiseword.txt";

		if (!getPropertiesValue("noiseword").equals("")) {
			noiseword = getPropertiesValue("noiseword") + "\\" + NOISE_WORD;
		}
		return noiseword;
	}

	/**
	 * @功能：读取 lore 的url 地址
	 * @return
	 */
	public static String readServerurl() {
		String loreurl = "";
		if (!getPropertiesValue("loreurl").equals("")) {
			loreurl = getPropertiesValue("loreurl");
		}
		return loreurl;
	}

	/**
	 * @功能: 获取下载的绝对路径
	 * @return
	 */
	public static String reaDownpathAbs() {
		String downloadpath = THREAD_DIR + "/downloadFiles/LDA";
		if (!getPropertiesValue("downloadpath").equals("")) {
			downloadpath = getPropertiesValue("downloadpath");
		}
		return downloadpath;
	}

	/**
	 * @功能：获取下载文件的相对路径
	 * @return
	 */
	public static String readDownpathRel() {
		String relativepath = "downloadFiles/LDA";
		if (!getPropertiesValue("relativepath").equals("")) {
			relativepath = getPropertiesValue("relativepath");
		}
		return relativepath;
	}

	/**
	 * @功能：获取控制台信息输出文件路径
	 * @return
	 */
	public static String readOutputDir() {
		String output = "D:/output.txt";
		if (!getPropertiesValue("output").equals("")) {
			output = getPropertiesValue("output");
		}
		return output;
	}

	/**
	 * @功能：设定在线系统工程名字
	 * @return
	 */
	public static String readInlineProgressName() {
		String inline = "inline";
		if (!getPropertiesValue("inline").equals("")) {
			inline = getPropertiesValue("inline");
		}
		return inline;
	}

	/**
	 * @功能：设定离线系统工程名字
	 * @return
	 */
	public static String readOfflineProgressName() {
		String offline = "offline";
		if (!getPropertiesValue("offline").equals("")) {
			offline = getPropertiesValue("offline");
		}
		return offline;
	}

	/**
	 * @功能:获取最大的下载进程数
	 * @return a string
	 */

	public static int getMaxTasksNum() {
		int maxNum = 10;
		if (!getPropertiesValue("MaxTasksNum").equals("")) {
			maxNum = Integer.parseInt(getPropertiesValue("MaxTasksNum"));
		}
		return maxNum;
	}

	/**
	 * @功能: 获取最小的下载进程数
	 * @return a string
	 */

	public static int getMinTasksNum() {
		int minNum = 0;
		if (!getPropertiesValue("MinTasksNum").equals("")) {
			minNum = Integer.parseInt(getPropertiesValue("MinTasksNum"));
		}

		return minNum;
	}

	/**
	 * @功能：获取 文件 byte
	 * 
	 */
	public static int getByte() {
		int bytes = 1000;

		if (!getPropertiesValue("byte").equals("")) {
			bytes = Integer.parseInt(getPropertiesValue("byte"));
		}
		return bytes;
	}

	/**
	 * @功能：获取 topic前N个词
	 * 
	 */
	public static int getTopwords() {
		int topwords = 50;

		if (!getPropertiesValue("topwords").equals("")) {
			topwords = Integer.parseInt(getPropertiesValue("topwords"));
		}
		return topwords;
	}

	public static int getTopKeyWords() {
		int topwords = 20;

		if (!getPropertiesValue("kwtopwords").equals("")) {
			topwords = Integer.parseInt(getPropertiesValue("kwtopwords"));
		}
		return topwords;
	}

	/**
	 * @功能：according to the node name in the configuration file to obtain the
	 *               corresponding node property value
	 * @param key
	 * @return a string
	 */
	/*
	 * public static String readValue(String key) { Properties props = new
	 * Properties(); // System.out.println(filepath); try { InputStream in = new
	 * BufferedInputStream(new FileInputStream( filepath)); props.load(in);
	 * String value = props.getProperty(key); // System.out.println(key+value);
	 * return value; } catch (Exception e) { e.printStackTrace(); return null; }
	 * }
	 */
	/**
	 * @功能：a property name and property value, the value and the name is written
	 *       to the configuration fil
	 * @param parameterName
	 * @param parameterValue
	 */
	/*
	 * public synchronized static void writeProperties(String parameterName,
	 * String parameterValue) { Properties prop = new Properties(); try {
	 * InputStream fis = new FileInputStream(filepath); // 从输入流中读取属性列表（键和元素对）
	 * prop.load(fis); // 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。 //
	 * 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。 OutputStream fos = new
	 * FileOutputStream(filepath); prop.setProperty(parameterName,
	 * parameterValue); // 以适合使用 load 方法加载到 Properties 表中的格式， // 将此 Properties
	 * 表中的属性列表（键和元素对）写入输出流 prop.store(fos, "Update '" + parameterName +
	 * "' value"); } catch (IOException e) { System.err.println("Visit " +
	 * filepath + " for updating " + parameterName + " value error"); } }
	 */

	/**
	 * @功能：获取timestamp 类型数据的最小值
	 * @return a string
	 */
	@SuppressWarnings("deprecation")
	public static String getMinTime() {
		Timestamp st = new Timestamp(0);
		st.setSeconds(1);
		return st.toString().split("\\.")[0];
	}

	/**
	 * @功能：get a certain format Timestamp from new Date
	 * @param date
	 * @return a Timestamp
	 */
	public static Timestamp toTimestamp(Date date) {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df1.format(date);
		Timestamp ts = Timestamp.valueOf(time);
		return ts;
	}

	/**
	 * @功能：get a certain format Timestamp from new Date
	 * @param date
	 * @return a String
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String time = df1.format(date);
		return time;
	}

	/**
	 * @功能：obtained from a date specified format string
	 * @param date
	 * @return a string
	 */
	public static String getNowTime(Date date) {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df1.format(date);
		Timestamp ts = Timestamp.valueOf(time);
		return ts.toString().split("\\.")[0];
	}

	/**
	 * @功能：Split a string in accordance with the specified character and
	 *           credited to your list collection.
	 * @param str
	 * @return a list
	 */
	public static List<String> getStringlist(String str) {
		List<String> list = new ArrayList<String>();
		StringTokenizer token = new StringTokenizer(str, ".");

		while (token.hasMoreElements()) {
			list.add(token.nextToken());
		}

		return list;
	}

	/**
	 * @功能：过滤html标签
	 * @param inputString
	 * @return
	 */
	public static String SplitHtml(String inputString) {
		// 含html标签的字符串
		String htmlStr = inputString;
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>}
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			// 定义HTML标签的正则表达式
			String regEx_html = "<[^>]+>";

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			// 过滤script标签
			htmlStr = m_script.replaceAll("");

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			// 过滤style标签
			htmlStr = m_style.replaceAll("");

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			// 过滤html标签
			htmlStr = m_html.replaceAll("");
			textStr = htmlStr;
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		// 返回文本字符串
		return textStr;
	}

	/**
	 * @功能：转换Double格式
	 * @param format
	 * @param figures
	 * @return
	 */
	public static String formatDouble(String format, Double figures) {
		DecimalFormat df = new DecimalFormat(format);
		return df.format(figures);
	}

	/**
	 * @功能：获取ZIP文件大小
	 * @param filedir
	 * @return
	 */
	public static String getFileSize(String filedir) {
		File f = new File(filedir);
		String filesize = "";
		if (f.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(f);
				int bytes = getByte();
				Double filesizereal = (double) ((double) fis.available()
						/ bytes / bytes);

				if (filesizereal < 1) {
					filesize = formatDouble("0.00", filesizereal * bytes) + "K";
				} else {
					filesize = formatDouble("0.00", filesizereal) + "M";
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			filesize = "0 K";
		}

		return filesize;
	}

	/**
	 * java中正则表达式提取字符串中日期实现代码
	 * 
	 * @param text
	 *            待提取的字符串
	 * @return 返回日期
	 * @author: bing
	 * @Createtime: June 05, 2013
	 */
	@SuppressWarnings("unchecked")
	public static String getTime(String text) {
		String dateStr = text.replaceAll("r?n", " ");
		try {
			List matches = null;
			Pattern p = Pattern.compile(
					"(\\d{1,4}[-|\\/]\\d{1,2}[-|\\/]\\d{1,2})",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			Matcher matcher = p.matcher(dateStr);
			if (matcher.find() && matcher.groupCount() >= 1) {
				matches = new ArrayList();
				for (int i = 1; i <= matcher.groupCount(); i++) {
					String temp = matcher.group(i);
					matches.add(temp);
				}
			} else {
				matches = Collections.EMPTY_LIST;
			}

			if (matches.size() > 0) {
				return ((String) matches.get(0)).trim();
			} else {
				return formatDate(new java.util.Date());
			}
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @功能：从配置文件中读取所有网页类型
	 * @return
	 */
	public static String getUrlType() {
		String urltype = "网页";
		if (!getPropertiesValue("urltype").equals("")) {
			urltype = getPropertiesValue("urltype");
		}
		return urltype;
	}

	/**
	 * @功能：将 抓取的 html 中的特殊字符去除掉，让 json以正确格式显示
	 * @param str
	 * @return
	 */
	public static String filterHtmlChar(String str) {
		return str.replaceAll("\"", "").replaceAll("'", "")
				.replaceAll("/>", "").replaceAll("\\\\", "")
				.replaceAll("/", "").replaceAll("\b", "").replaceAll("\f", "")
				.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "")
				.replaceAll("&nbsp;", "").replaceAll("&ldquo;", "“")
				.replaceAll("&rdquo;", "”").replaceAll("&middot;", ".");
	}

	public static void main(String[] args) throws IOException {
		// System.out.println(getTinterval());
		// System.out.println(formatDate(new java.util.Date()));

		// String test = "国外飞翼式无人机技术特点分析--《飞航导弹》2012年04期 ";

		Map map = readProperties();

		String value = map.get("baidu.com").toString();

		System.out.println(value);
	}

}
