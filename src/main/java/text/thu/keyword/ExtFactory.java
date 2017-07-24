package text.thu.keyword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import text.analyse.etmutility.ETMCluster;
import text.analyse.struct.lda.TopWords;
import text.analysis.utils.SegUtil;
import text.thu.keyword.extract.Extract;
import text.thu.keyword.model.News;
import text.thu.keyword.model.NewsSet;
import text.thu.keyword.model.Token;

@Component
public class ExtFactory {
	private Logger LOG = LoggerFactory.getLogger(ExtFactory.class);

	public NewsSet newsSet;
	@Autowired
	Extract ext;

	@Autowired
	SegUtil segUtil;

	public void loadFiles(String in_path) {
		File dir = new File(in_path);
		newsSet = new NewsSet();

		if (dir.isDirectory()) {
			String[] files = dir.list();
			for (int i = 0; i < files.length; i++) {
				String fileName = files[i];
				// System.out.println(dir.getPath()+"\\"+fileName);
				File newsFile = new File(dir.getPath() + "\\" + fileName);
				if (newsFile.exists()) {
					try {
						BufferedReader buffer = new BufferedReader(
								new InputStreamReader(new FileInputStream(newsFile)));
						String line = buffer.readLine();
						News news = new News(fileName);
						news.headLine = line.trim();
						news.content = "";
						while ((line = buffer.readLine()) != null) {
							news.content += line.trim() + "\n";
						}
						newsSet.addNews(news);
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}

			}
		}
	}

	public NewsSet loadLDAphi(String in_path) {
		NewsSet newsSet = new NewsSet();
		File newsFile = new File(in_path);
		String line = "";
		if (newsFile.exists()) {
			try {
				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(new FileInputStream(newsFile), "utf-8"));
				line = buffer.readLine();
				String headLine = "";
				String content = "";
				int nTitle = 0;

				while ((line = buffer.readLine()) != null) {
					if (line.contains("Topic")) {
						// title = line;
						continue;
					}

					if (!line.equals("#####")) {
						int nIndex = 0;
						// if(line.contains(" ")){
						// nIndex = line.indexOf(" ");
						// headLine += line.substring(0, nIndex).trim() ;
						// content +=
						// line.substring(nIndex+2,line.length()).trim();
						// }

						if (line.contains("@@@@")) {
							// nIndex = line.indexOf("@@@@");
							// String tempheadline = line.substring(0,
							// nIndex).trim();

							// headLine += line.substring(0, nIndex);
							// content +=
							// line.substring(nIndex+4,line.length()).trim();
							// content +=
							// line.substring(nIndex+2,line.length());
							String[] newsinfo = line.split("@@@@");
							ETMCluster etmCluster = new ETMCluster();
							headLine += etmCluster.GetKeywords(segUtil.segText(newsinfo[0], true)) + " ";
							content += etmCluster.GetKeywords(segUtil.segText(newsinfo[1], true)) + " ";

						} else {
							headLine += line;
							content += line;
						}
					} else {
						News news = new News(String.valueOf(nTitle));
						nTitle++;

						news.headLine = headLine;
						news.content = content;

						// System.out.println(news.headLine+"========"+news.content);

						newsSet.addNews(news);

						headLine = "";
						content = "";
					}

				}

			} catch (IOException ioe) {
				System.out.println(line);
				ioe.printStackTrace();
			}
		}
		return newsSet;
	}

	public void readTokenToNews(String tokendir) {
		BufferedReader reader = null;
		File file = new File(tokendir);

		newsSet = new NewsSet();

		try {
			if (file.exists()) {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

				String line = "";
				int flag = 0;
				StringBuffer headline = new StringBuffer();
				StringBuffer content = new StringBuffer();
				while ((line = reader.readLine()) != null) {
					String flagstr = "Topic " + flag + " size";
					if (line.contains(":") && line.split(":")[0].equals(flagstr)) {
						// if(flag != 0){
						News news = new News("Topic " + flag);
						news.headLine = headline.toString();
						news.content = content.toString();
						System.out.println("headline : " + headline.toString());
						// System.out.println("content : " +
						// content.toString());
						headline = new StringBuffer();
						content = new StringBuffer();
						newsSet.addNews(news);
						// }
						flag++;
						continue;
					} else {
						String[] newsinfo = line.split("@@@@");
						if (newsinfo.length != 2) {
							headline.append(line);
							content.append(line);
						} else {
							headline.append(newsinfo[0]);
							content.append(newsinfo[1]);
						}
					}
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<TopWords> outputKeywors(NewsSet newsSet) {
		ArrayList<TopWords> topWordsArrayList = new ArrayList<TopWords>();

		LOG.info("抽取关键词，NewsSet.size = {} ", newsSet.newsArray.size());

		for (int i = 0; i < newsSet.newsArray.size(); i++) {
			News news = (News) newsSet.newsArray.get(i);

			LOG.info("抽取关键词， News index = {} ... ", i);

			if (news == null)
				continue;

			// PrintConsole.PrintLog("Title", news.headLine);
			// PrintConsole.PrintLog("Content", news.content);

			// 对该新闻进行关键词抽取
			ext.getKeyWord(news);
		}

		LOG.info("新闻关键词抽取完毕，进行关键词格式化....");

		for (int i = 0; i < newsSet.newsArray.size(); i++) {
			News news = (News) newsSet.newsArray.get(i);
			// String sb = news.name + ": ";
			String sb = "";

			// 关键词读取示例
			for (int j = 0; j < news.keywords.length && news.keywords[j] != null; j++) {
				Token t = (Token) news.keywords[j];
				// System.out.println(t.word+" "+t.score);
				// sb += t.word + "; ";
				// sb += t.word.substring(0,t.word.indexOf("/")) + " ";
				if (t.word.contains("/")) {
					sb += t.word.substring(0, t.word.indexOf("/"));
				} else {
					sb += t.word;
				}
				sb += " ";
			}

			// sb = sb.substring(0,sb.lastIndexOf(";")) + "\n";
			TopWords topWords = new TopWords();
			topWords.setTopicID(i);
			topWords.setLabelWords(sb);
			topWordsArrayList.add(topWords);
		}
		return topWordsArrayList;
	}

	public void outFiles(String out_path) {
		File dir = new File(out_path);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}

		FileWriter f;
		try {
			String str = "guanjianci_answer.txt";
			String filename = out_path + "\\" + str;
			f = new FileWriter(filename);

			for (int i = 0; i < newsSet.newsArray.size(); i++) {
				News news = (News) newsSet.newsArray.get(i);
				String sb = news.name + ": ";

				// 关键词读取示例
				for (int j = 0; j < news.keywords.length && news.keywords[j] != null; j++) {
					Token t = (Token) news.keywords[j];
					// System.out.println(t.word+" "+t.score);
					sb += t.word + "; ";
				}

				sb = sb.substring(0, sb.lastIndexOf(";")) + "\n";
				f.write(sb);
			}

			f.flush();
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void testExtract(String in_Dir, String out_Dir) {
		// loadFiles(in_Dir);
		loadLDAphi(in_Dir);
		for (int i = 0; i < newsSet.newsArray.size(); i++) {
			News news = (News) newsSet.newsArray.get(i);

			if (news == null)
				continue;

			// System.out.println("*****************************************: "
			// + i);
			// System.out.println("news.headLine:" + news.headLine);
			// System.out.println("news.content:" + news.content);

			// 对该新闻进行关键词抽取
			ext.getKeyWord(news);

			// 关键词读取示例
			for (int j = 0; j < news.keywords.length && news.keywords[j] != null; j++) {
				Token tok = (Token) news.keywords[j];
				// if(tok.word.length()==1)
				// System.out.println(tok.word+" "+tok.score);
			}

			// 特征提取示例
			// if(news.keyTs!=null){
			// Iterator iter = news.keyTs.iterator();
			// while (iter.hasNext()){
			// Token tok = (Token) iter.next();
			// System.out.println(tok.word+"--------"+tok.score);
			// }
			// }

		}

		outFiles(out_Dir);
	}

	public static void main(String args[]) {
		ExtFactory factory = new ExtFactory();
		// String in_Dir = "news//训练数据";
		// String out_Dir = "news//abc";
		// factory.testExtract(in_Dir, out_Dir);
		// factory.readTokenToNews("D:\\temp\\output\\00282d98-6c51-4ace-80c8-8ce74e03baef\\TokensToNews");
		long start = System.currentTimeMillis();
		NewsSet newsSet = factory.loadLDAphi("D:\\temp\\output\\a5479f54-3d78-4970-9051-9d446c516668\\topDocs.txt");
		factory.outputKeywors(newsSet);
		long end = System.currentTimeMillis();
		System.out.println("时间差(分钟) : " + (end - start) / 60000 + "分钟");
	}
}
