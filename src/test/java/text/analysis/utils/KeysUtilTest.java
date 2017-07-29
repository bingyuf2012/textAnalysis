package text.analysis.utils;

import org.junit.Test;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月24日 下午3:16:50
 */

public class KeysUtilTest {
	@Test
	public void testDir() {
		System.out.println(System.getProperty("user.dir"));
	}

	@Test
	public void testToUpperCase() {
		String testStr = "uselabeling,usenoiseword,topicnum,topiclabelnum,byte,output,tinterval,noiseword,urltype,summarypages,gsummarypages,global,proxy,topentity,outputdata,#txtdata,txtdata,userAgent,timeout,baiduserchurl,blocksdiv,titlediv,newsurldiv,contentfontdiv,contentdiv,counting,news.ICTUniterm,news.ICTBiterm,news.ICTTriterm,news.stopword1,news.stopword2,news.stopword,news.singleword,news.weblogPath";

		System.out.println(testStr.toUpperCase());
	}
}
