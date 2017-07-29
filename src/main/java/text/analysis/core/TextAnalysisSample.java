package text.analysis.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import text.analyse.common.utils.properties.SpringContextUtil;
import text.analyse.etmutility.ETMCluster;
import text.analyse.struct.etm.AllTopicsETM;
import text.analysis.utils.SegUtil;
import text.searchSDK.model.WebSearchResult;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月28日 下午3:04:39
 */

public class TextAnalysisSample {
	public static void main(String[] args) {
		if (args == null || args.length < 2) {
			return;
		}

		FileReaderUtil fileReaderUtil = new FileReaderUtil();
		SegUtil segUtil = SpringContextUtil.getBean(SegUtil.class);

		String curDir = System.getProperty("user.dir") + File.separator;

		String result = "";
		switch (args[0]) {
		// 分词
		case "0":
			String segNewDir = curDir + args[1];
			WebSearchResult segNew = fileReaderUtil.readNewsFromFile(segNewDir);
			result = segUtil.segText(segNew.getContent(), Boolean.valueOf(args[2]));
			fileReaderUtil.writeResultToFile(segNewDir, "segText", result);
			return;
		// 抽取关键词
		case "1":
			String keywrodDir = curDir + args[1];
			WebSearchResult extraNew = fileReaderUtil.readNewsFromFile(keywrodDir);
			result = segUtil.extraKeywords(extraNew.getTitle(), extraNew.getContent(), Integer.valueOf(args[2]));
			fileReaderUtil.writeResultToFile(keywrodDir, "extraKeyword", result);
			break;
		// 聚类分析
		case "2":
			String clusterDir = curDir + args[1];
			File[] newsFileList = new File(clusterDir).listFiles();
			List<WebSearchResult> newsList = new ArrayList<WebSearchResult>();

			for (File itemFile : newsFileList) {
				if (itemFile.isDirectory()) {
					continue;
				}

				newsList.add(fileReaderUtil.readNewsFromFile(itemFile));
			}

			ETMCluster eTMCluster = SpringContextUtil.getBean(ETMCluster.class);
			AllTopicsETM clusterResult = eTMCluster.runLDA("", newsList, clusterDir + "/temp/", 0, 0);
			fileReaderUtil.writeResultToFile(clusterDir + "/temp/", "clusterResult",
					JSONObject.toJSONString(clusterResult));
			break;
		case "3":
			String locDir = curDir + args[1];
			WebSearchResult extraLOC = fileReaderUtil.readNewsFromFile(locDir);
			String locResult = segUtil.extraLOC(extraLOC.getContent());
			fileReaderUtil.writeResultToFile(locDir, "extraLOC", locResult);
			break;
		default:
			break;
		}
	}
}
