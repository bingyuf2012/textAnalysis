package text.analysis.utils;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午4:18:36
 */

public class CountDFUtil {
	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public static String ToDBC(String input) {

		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);

		return returnString;
	}

	public static String repair(String content) {
		StringBuffer resultBuffer = new StringBuffer();

		int k = content.indexOf("\n");
		while (k > -1) {
			resultBuffer.append(content.substring(0, k));
			if ((k == 0) || (content.charAt(k - 1) == '。')
					|| (content.charAt(k - 1) == '！' || content.charAt(k - 1) == '？')) {
				resultBuffer.append("\n");
			}
			content = content.substring(k + 1);
			k = content.indexOf("\n");
		}
		resultBuffer.append(content);

		return resultBuffer.toString();
	}

}
