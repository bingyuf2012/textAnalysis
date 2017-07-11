package text.analysis.utils;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

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
	public String parseText(String text, boolean isContainPos) {
		if (GenericValidator.isBlankOrNull(text)) {
			return "";
		}

		Result result = nlpAnalysis.parseStr(text);
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
}
