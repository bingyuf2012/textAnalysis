package text.analysis.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import text.analysis.utils.ConstantUtil;
import text.searchSDK.model.WebSearchResult;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月28日 下午3:13:55
 */

public class FileReaderUtil {
	public WebSearchResult readNewsFromFile(File file) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), ConstantUtil.UTF8));

			StringBuffer contentBuffer = new StringBuffer();

			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				contentBuffer.append(line);
			}

			WebSearchResult webSearchResult = new WebSearchResult();

			webSearchResult.setTitle(file.getName());
			webSearchResult.setContent(contentBuffer.toString());
			return webSearchResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public WebSearchResult readNewsFromFile(String fileName) {
		BufferedReader bufferedReader = null;
		try {
			File newsFile = new File(fileName);
			bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(newsFile), ConstantUtil.UTF8));

			StringBuffer contentBuffer = new StringBuffer();

			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				contentBuffer.append(line);
			}

			WebSearchResult webSearchResult = new WebSearchResult();

			webSearchResult.setTitle(newsFile.getName());
			webSearchResult.setContent(contentBuffer.toString());
			return webSearchResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void writeResultToFile(String fileName, String flag, String result) {
		File sourceFile = new File(fileName.replaceAll(".txt", "__") + flag + ".txt");

		PrintWriter pwResult = null;
		try {
			pwResult = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sourceFile), ConstantUtil.UTF8)));

			pwResult.write(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pwResult.close();
		}
	}
}
