package text.searchSDK.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateFormat {
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

	/**
	 * @功能：获取timestamp 类型数据的最小值
	 * @return a string
	 */
	@SuppressWarnings("deprecation")
	public String getMinTime() {
		Timestamp st = new Timestamp(0);
		st.setSeconds(1);
		return st.toString().split("\\.")[0];
	}

	/**
	 * @功能：get a certain format Timestamp from new Date
	 * @param date
	 * @return a Timestamp
	 */
	public Timestamp toTimestamp(Date date) {
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
	public String formatDate(Date date) {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String time = df1.format(date);
		return time;
	}

	/**
	 * 将一个 类似于 2013-1-1 的时间字符串转换成 2013-01-01
	 * 
	 * @param time
	 * @return a String
	 */
	@SuppressWarnings("deprecation")
	public String formatDateString(String time) {
		// PrintConsole.PrintLog("Current Date --->", new java.util.Date());
		String timestr = "";
		if (time.length() == 10) {
			timestr = time;
		} else {
			try {
				String[] tmp = time.split("-");
				if (tmp[1].length() == 1) {
					tmp[1] = "0" + tmp[1];
				}

				if (tmp[2].length() == 1) {
					tmp[2] = "0" + tmp[2];
				}

				if (tmp[1].equals("00") || tmp[2].equals("00")
						|| (tmp[0].length() != 4)) {
					// timestr = formatDateString(new java.util.Date()
					// .toLocaleString().split(" ")[0]);
					timestr = df.format(new Date());
				} else {
					timestr = tmp[0] + "-" + tmp[1] + "-" + tmp[2];
				}
			} catch (Exception e) {
				// timestr = formatDateString(new java.util.Date()
				// .toLocaleString().split(" ")[0]);
				timestr = df.format(new Date());
				// System.out.println(df.format(new Date()));
			}

		}
		return timestr;
	}

	/**
	 * @功能：obtained from a date specified format string
	 * @param date
	 * @return a string
	 */
	public String getNowTime(Date date) {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df1.format(date);
		Timestamp ts = Timestamp.valueOf(time);
		return ts.toString().split("\\.")[0];
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
	public String getTime(String text) {
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

	public Date getDate(String time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		try {
			date = df.parse(time);
		} catch (Exception ex) {
			PrintConsole.PrintLog("ex.getMessage()", ex.getMessage());
		}
		return date;
	}

	/**
	 * 获得任务预计时间
	 * 
	 * @param ratio
	 *            当前任务比率
	 * @param startime
	 *            任务开始时间
	 * @return
	 */
	public String getEstimate(String ratio, String startime) {
		Double ratiodouble = Double.parseDouble(ratio);

		if (ratiodouble == 0.00) {
			return "1小时";
		} else if (ratiodouble == 1.00) {
			return "0 秒";
		} else {
			Date start = getDate(startime);
			Date end = new Date();

			long difftime = end.getTime() - start.getTime();

			PrintConsole.PrintLog("difftime", difftime);
		}

		return "";
	}

	public static void main(String[] args) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
	}
}
