package text.analysis.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月10日 下午2:01:33
 */
@Component
public class SegUtil {
	NlpAnalysis nlpAnalysis;

	@Autowired
	ConvertUtil convertUtil;

	@SuppressWarnings("restriction")
	@PostConstruct
	public void init() {
		nlpAnalysis = new NlpAnalysis();
	}

	public Result parseText(String text) {
		if (GenericValidator.isBlankOrNull(text)) {
			return new Result(new ArrayList<Term>());
		}

		return nlpAnalysis.parseStr(text);
	}

	/**
	 * 
	 * <li>文本分词</li>
	 *
	 * @param text
	 *            待分词文本
	 * @param isContainPos
	 *            是否返回词性
	 * @return
	 */
	public String segText(String text, boolean isContainPos) {
		Result result = parseText(text);
		StringBuffer parseResult = new StringBuffer();
		Iterator<Term> wordIterator = result.iterator();

		if (isContainPos) {
			while (wordIterator.hasNext()) {
				Term itemTerm = wordIterator.next();
				parseResult.append(MessageFormat.format(ConstantUtil.WORD_FORMAT, itemTerm.getName(),
						convertUtil.convertPos(itemTerm.getNatureStr())));

				parseResult.append(ConstantUtil.WORD_SPLIT);
			}
		} else {
			while (wordIterator.hasNext()) {
				parseResult.append(wordIterator.next().getName());
				parseResult.append(ConstantUtil.WORD_SPLIT);
			}
		}

		return parseResult.toString();
	}

	/**
	 * 
	 * <li>抽取文本关键词</li>
	 *
	 * @param title
	 *            文本标题
	 * @param content
	 *            文本正文
	 * @param nK
	 *            抽取的关键词个数
	 * @return
	 */
	public String extraKeywords(String title, String content, int nK) {
		if (nK == 0) {
			return "";
		}

		KeyWordComputer kwc = new KeyWordComputer(nK);
		title = GenericValidator.isBlankOrNull(title) ? "" : title;
		content = GenericValidator.isBlankOrNull(content) ? "" : content;

		Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
		Iterator<Keyword> keywordIterator = result.iterator();

		StringBuffer resultBuffer = new StringBuffer();

		while (keywordIterator.hasNext()) {
			Keyword itemWord = keywordIterator.next();
			resultBuffer.append(itemWord.getName());
			resultBuffer.append(ConstantUtil.WORD_SPLIT);
		}

		return resultBuffer.toString();
	}

	/**
	 * 
	 * <li>从文本中抽取地点</li>
	 *
	 * @param text
	 * @return
	 */
	public String extraLOC(String text) {
		Result result = parseText(text);
		Iterator<Term> wordIterator = result.iterator();

		Set<String> locSet = new HashSet<String>();

		while (wordIterator.hasNext()) {
			Term itemTerm = wordIterator.next();

			if ("ns".equals(itemTerm.getNatureStr())) {
				locSet.add(itemTerm.getName());
			}
		}

		StringBuffer parseResult = new StringBuffer();

		Iterator<String> locIterator = locSet.iterator();
		while (locIterator.hasNext()) {
			String loc = locIterator.next();
			parseResult.append(loc);
			parseResult.append(ConstantUtil.WORD_SPLIT);
		}

		return parseResult.toString();
	}

	/**
	 * 对新闻内容进行分词并过滤掉单个词和停用词
	 * 
	 * @param m_content
	 * @return
	 */
	public String getKeywords(String m_content) {
		String allTokens = "";
		String[] allWords = m_content.trim().split(" ");

		for (int j = 0; j < allWords.length; j++) {
			int pos1 = allWords[j].lastIndexOf("/");

			if (pos1 > 0) {
				String type = allWords[j].substring(pos1 + 1);
				String key = allWords[j].substring(0, pos1).replaceAll(" ", "").replaceAll("　", "");

				String pattern = "[a-zA-Z]{1,}";
				Pattern p = Pattern.compile(pattern);
				Matcher m = p.matcher(key);
				boolean b = m.matches();

				if (b) {
					continue;
				}

				String pattern1 = "[0-9]{1,}";
				Pattern p1 = Pattern.compile(pattern1);
				Matcher m1 = p1.matcher(key);
				boolean b1 = m1.matches();

				if (b1) {
					continue;
				}

				int cut = key.indexOf("/?");
				if (cut > -1)
					key = key.substring(cut + 2);

				if (type.matches("u|c|p|w|d|r|f|z|q|o|mq")) {
					continue;
				}

				if (type.matches("NUM|TIM")) {
					continue;
				}

				if (key.length() == 1 /* || StopWordsFilter.isStopWord(key) */) {
					continue;
				}

				if (type.equals("LOC") && (key.endsWith("市") || key.endsWith("省"))) {
					key = key.substring(0, key.length() - 1);
				}

				if (type.equals("nt")) {
					type = "ORG";
				}

				if (type.equals("ns")) {
					type = "LOC";
				}

				if (type.equals("nr")) {
					type = "PER";
				}

				if (!key.contains("(")) {
					allTokens = allTokens + key + "/" + type + " ";
				}

			}
		}
		return allTokens;
	}
}
