package text.searchSDK.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintConsole {
	/**
	 * 打印后台日志
	 * 
	 * @param printContent
	 *            需要打印的提示信息
	 * @param param
	 *            需要打印的变量名
	 */
	@SuppressWarnings("deprecation")
	public static void PrintLog(String printContent, Object param) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		if (Constant.ISDEBUG) {
			StringBuffer sb = new StringBuffer();

			StackTraceElement[] stacks = new Throwable().getStackTrace();
			sb
					.append("time :")
					.append(df.format(new Date()))
					.append("---ClassName:")
					.append(stacks[1].getClassName())
					.append(" -- Method:")
					.append(stacks[1].getMethodName())
					.append(" -- Line:")
					.append(stacks[1].getLineNumber())
					.append(" -- ")
					.append(printContent)
					.append(" :")
					.append(param)
					.append(
							"\n********************************************************");

			System.out.println(sb.toString());
		}
	}

	/**
	 * 打印后台日志
	 * 
	 * @param printContent
	 *            需要打印的提示信息
	 * @param param
	 *            需要打印的变量名
	 */
	@SuppressWarnings("deprecation")
	public static void PrintLog(String printContent1, Object param,
			String printContent2) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		if (Constant.ISDEBUG) {
			StringBuffer sb = new StringBuffer();
			StackTraceElement[] stacks = new Throwable().getStackTrace();
			sb
					.append("time :")
					.append(df.format(new Date()))
					.append("---ClassName:")
					.append(stacks[1].getClassName())
					.append(" -- Method:")
					.append(stacks[1].getMethodName())
					.append(" -- Line:")
					.append(stacks[1].getLineNumber())
					.append(" -- ")
					.append(printContent1)
					.append(" :")
					.append(param)
					.append(" ")
					.append(printContent2)
					.append(
							"\n********************************************************");

			System.out.println(sb.toString());
		}
	}

	/**
	 * 打印后台日志
	 * 
	 * @param printContent
	 *            需要打印的提示信息
	 * @param param
	 *            需要打印的变量名
	 */
	@SuppressWarnings("deprecation")
	public static void PrintLog(String printContent1, Object param1,
			String printContent2, Object param2, String printContent3) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		if (Constant.ISDEBUG) {
			StringBuffer sb = new StringBuffer();

			StackTraceElement[] stacks = new Throwable().getStackTrace();
			sb
					.append("time :")
					.append(df.format(new Date()))
					.append("---ClassName:")
					.append(stacks[1].getClassName())
					.append(" -- Method:")
					.append(stacks[1].getMethodName())
					.append(" -- Line:")
					.append(stacks[1].getLineNumber())
					.append(" -- ")
					.append(printContent1)
					.append(" ")
					.append(param1)
					.append(" ")
					.append(printContent2)
					.append(" ")
					.append(param2)
					.append(" ")
					.append(printContent3)
					.append(
							"\n********************************************************");

			System.out.println(sb.toString());
		}
	}

	/**
	 * 打印后台日志
	 * 
	 * @param printContent
	 *            需要打印的提示信息
	 * @param param
	 *            需要打印的变量名
	 */
	@SuppressWarnings("deprecation")
	public static void PrintLog(String printContent1, Object param1,
			String printContent2, Object param2, String printContent3,
			Object param3, String printContent4) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		if (Constant.ISDEBUG) {
			StringBuffer sb = new StringBuffer();

			StackTraceElement[] stacks = new Throwable().getStackTrace();
			sb
					.append("time :")
					.append(df.format(new Date()))
					.append("---ClassName:")
					.append(stacks[1].getClassName())
					.append(" -- Method:")
					.append(stacks[1].getMethodName())
					.append(" -- Line:")
					.append(stacks[1].getLineNumber())
					.append(" -- ")
					.append(printContent1)
					.append(" ")
					.append(param1)
					.append(" ")
					.append(printContent2)
					.append(" ")
					.append(param2)
					.append(" ")
					.append(printContent3)
					.append(" ")
					.append(param3)
					.append(" ")
					.append(printContent4)
					.append(
							"\n********************************************************");

			System.out.println(sb.toString());
		}
	}

	public static void main(String[] args) {

	}
}
