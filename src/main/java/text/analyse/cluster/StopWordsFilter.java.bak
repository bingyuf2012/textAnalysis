/**
 * 
 */
package text.analyse.cluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import text.searchSDK.util.Constant;
import text.searchSDK.util.PrintConsole;

/**
 * @author Alex
 * 
 */
public class StopWordsFilter {

	public static final String ICTCLAS_PROP = "ICTCLAS.properties";
	public static final String STOPWORDS_FILE = "stopwords.txt";

	private static List<String> WORDS = new ArrayList<String>();

	static {
		Properties prop = new Properties();
		try {
			prop.load(StopWordsFilter.class.getClassLoader()
					.getResourceAsStream(ICTCLAS_PROP));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String file = prop.getProperty("argu") + File.separator
				+ STOPWORDS_FILE;
		loadStopWords(file);
		PrintConsole.PrintLog("WORDS", WORDS);
	}

	private static void loadStopWords(String file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), Constant.UTF8));
			String line = reader.readLine();
			while (line != null) {
				WORDS.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isStopWord(String word) {
		if (WORDS.contains(word))
			return true;
		else
			return false;
	}

}
