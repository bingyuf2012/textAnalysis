package text.searchSDK.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import text.analyse.common.utils.properties.Context;
import text.analyse.common.utils.properties.Keys;

@Component
public class CommonUtil {
	@Autowired
	Context context;

	public int getTopicNum(int docNum) {
		int TopicNum = 0;

		if (docNum > 0 && docNum < 30)
			TopicNum = 2;
		else if (docNum >= 30 && docNum < 200)
			TopicNum = 4;
		else if (docNum >= 200 && docNum < 500)
			TopicNum = 6;
		else if (docNum >= 500 && docNum < 1000)
			TopicNum = 8;
		else if (docNum >= 1000 && docNum < 2000)
			TopicNum = 10;
		else if (docNum >= 2000)
			TopicNum = 15;
		return TopicNum;
	}

	/**
	 * @功能：获取每个分类的label数
	 * @return
	 */
	public int getTopicLabelNum() {
		return GenericValidator.isBlankOrNull(context.getString(Keys.TOPICLABELNUM)) ? 10
				: context.getIntValue(Keys.TOPICLABELNUM);
	}

	/**
	 * 得到前 100 个实体
	 * 
	 * @return
	 */
	public int getTop100Entity() {
		return context.getIntValue(Keys.TOPENTITY);
	}

	/**
	 * @功能：获取 topic前N个词
	 * 
	 */
	public int getTopwords() {
		return GenericValidator.isBlankOrNull(context.getString(Keys.TOPWORDS)) ? 50
				: context.getIntValue(Keys.TOPWORDS);
	}

	public int getTopKeyWords() {
		return GenericValidator.isBlankOrNull(context.getString(Keys.KWTOPWORDS)) ? 20
				: context.getIntValue(Keys.KWTOPWORDS);
	}

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
			Pattern p = Pattern.compile("(\\d{1,4}[-|\\/]\\d{1,2}[-|\\/]\\d{1,2})",
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
	 * @功能：将 抓取的 html 中的特殊字符去除掉，让 json以正确格式显示
	 * @param str
	 * @return
	 */
	public static String filterHtmlChar(String str) {
		return str.replaceAll("\"", "").replaceAll("'", "").replaceAll("/>", "").replaceAll("\\\\", "")
				.replaceAll("/", "").replaceAll("\b", "").replaceAll("\f", "").replaceAll("\n", "").replaceAll("\r", "")
				.replaceAll("\t", "").replaceAll("&nbsp;", "").replaceAll("&ldquo;", "“").replaceAll("&rdquo;", "”")
				.replaceAll("&middot;", ".");
	}
}
