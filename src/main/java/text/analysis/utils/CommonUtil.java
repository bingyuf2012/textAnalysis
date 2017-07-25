package text.analysis.utils;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import text.analyse.common.utils.properties.Context;
import text.analyse.common.utils.properties.Keys;

@Component
public class CommonUtil {
	@Autowired
	Context context;

	public int getTopicNum(int docNum) {
		int TopicNum = 0;

		if (docNum > 0 && docNum < 30)
			TopicNum = 2;
		else if (docNum >= 30 && docNum < 200)
			TopicNum = 4;
		else if (docNum >= 200 && docNum < 500)
			TopicNum = 6;
		else if (docNum >= 500 && docNum < 1000)
			TopicNum = 8;
		else if (docNum >= 1000 && docNum < 2000)
			TopicNum = 10;
		else if (docNum >= 2000)
			TopicNum = 15;
		return TopicNum;
	}

	/**
	 * @功能：获取每个分类的label数
	 * @return
	 */
	public int getTopicLabelNum() {
		return GenericValidator.isBlankOrNull(context.getString(Keys.TOPICLABELNUM)) ? 10
				: context.getIntValue(Keys.TOPICLABELNUM);
	}

	/**
	 * 得到前 100 个实体
	 * 
	 * @return
	 */
	public int getTop100Entity() {
		return context.getIntValue(Keys.TOPENTITY);
	}

	/**
	 * @功能：获取 topic前N个词
	 * 
	 */
	public int getTopwords() {
		return GenericValidator.isBlankOrNull(context.getString(Keys.TOPWORDS)) ? 50
				: context.getIntValue(Keys.TOPWORDS);
	}

	public int getTopKeyWords() {
		return GenericValidator.isBlankOrNull(context.getString(Keys.KWTOPWORDS)) ? 20
				: context.getIntValue(Keys.KWTOPWORDS);
	}

}