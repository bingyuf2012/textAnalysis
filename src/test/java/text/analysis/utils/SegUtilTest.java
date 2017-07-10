package text.analysis.utils;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import text.analysis.core.BaseTest;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月10日 下午2:35:59
 */

public class SegUtilTest extends BaseTest {
	@Autowired
	SegUtil segUtil;

	@Test
	public void testParseText() {
		System.out.println(segUtil.parseText("洪磊发表严正声明:钓鱼台是中国的!中国气象局预测北京将发生大暴雨！在汶川地震后，中国地震局建议人民在空旷地方，搭建帐篷，不要去高楼附近，以防发生意外。"));
	}
}
