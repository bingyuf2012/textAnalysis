package text.analysis.utils;

import java.text.MessageFormat;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
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

	public String parseText(String text) {
		Result result = nlpAnalysis.parseStr(text);
		StringBuffer parseResult = new StringBuffer();
		Iterator<Term> wordIterator = result.iterator();
		while (wordIterator.hasNext()) {
			Term itemTerm = wordIterator.next();
			parseResult.append(MessageFormat.format(ConstantUtil.WORD_FORMAT, itemTerm.getName(),
					convertUtil.convertPos(itemTerm.getNatureStr())));

			parseResult.append(ConstantUtil.WORD_SPLIT);
		}

		return parseResult.toString();
	}

	public String parseText(String text, boolean isContainPos) {
		Result result = nlpAnalysis.parseStr(text);
		StringBuffer parseResult = new StringBuffer();
		Iterator<Term> wordIterator = result.iterator();
		while (wordIterator.hasNext()) {
			parseResult.append(wordIterator.next().getName());
			parseResult.append(ConstantUtil.WORD_SPLIT);
		}

		return parseResult.toString();
	}
}
