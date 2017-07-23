package text.searchSDK.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlSplit {
	/**
	 * @功能：过滤html标签
	 * @param inputString
	 * @return
	 */
	public String SplitHtml(String inputString) {
		// 含html标签的字符串
		String htmlStr = inputString;
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>}
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			// 定义HTML标签的正则表达式
			String regEx_html = "<[^>]+>";

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			// 过滤script标签
			htmlStr = m_script.replaceAll("");

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			// 过滤style标签
			htmlStr = m_style.replaceAll("");

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			// 过滤html标签
			htmlStr = m_html.replaceAll("");
			textStr = htmlStr;
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		// 返回文本字符串
		return textStr;
	}

	/**
	 * @功能：将 抓取的 html 中的特殊字符去除掉，让 json以正确格式显示
	 * @param str
	 * @return
	 */
	public String filterHtmlChar(String str) {
		return str.replace("\"", "").replace("'", "").replace("/>", "")
				.replace("\\\\", "").replace("/", "").replace("\b", "")
				.replaceAll("\f", "").replace("\n", "").replace("\r", "")
				.replaceAll("\t", "").replace("<b>|</b>", "");
	}

	public String filterHtmlChar_old(String str) {
		return str.replaceAll("\"", "").replaceAll("'", "")
				.replaceAll("/>", "").replaceAll("\\\\", "")
				.replaceAll("/", "").replaceAll("\b", "").replaceAll("\f", "")
				.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "")
				.replaceAll("<b>|</b>", "");
	}

	public String JsonCharFilter(String sourceStr) {
		sourceStr = sourceStr.replace("\\", "");
		sourceStr = sourceStr.replace("\b", "");
		sourceStr = sourceStr.replace("\t", "");
		sourceStr = sourceStr.replace("\n", "");
		sourceStr = sourceStr.replace("\n", "");
		sourceStr = sourceStr.replace("\f", "");
		sourceStr = sourceStr.replace("\r", "");
		return sourceStr.replace("\"", "");
	}

	public String JsonCharFilter_old(String sourceStr) {
		sourceStr = sourceStr.replace("\\", "\\\\");
		sourceStr = sourceStr.replace("\b", "\\\b");
		sourceStr = sourceStr.replace("\t", "\\\t");
		sourceStr = sourceStr.replace("\n", "\\\n");
		sourceStr = sourceStr.replace("\n", "\\\n");
		sourceStr = sourceStr.replace("\f", "\\\f");
		sourceStr = sourceStr.replace("\r", "\\\r");
		return sourceStr.replace("\"", "\\\"");
	}
	
	public static void main(String[] args){
		String str = "人民网北京12月22日电 中央纪委监察部网站今日发布消息，中国人民政治协商会议第十二届全国委员会副主席、中共中央统战部部长令计划涉嫌严重违纪，目前正接受组织调查。至此，十八大后全国已有59名省部级及以上官员落马，包括今年被宣布查处的副国级及以上官员4人、省部级官员37人，十八大至2013年底被宣布查处的省部级官员18人。今年被宣布查处的省部级及以上官员数量已超过去年的2倍。[br]今年被宣布查处的4名副国级及以上官员为:周永康、徐才厚、苏荣、令计划。[br]今年被宣布查处的37名省部级官员为:冀文林、祝作利、金道铭、沈培平、姚木根、谷俊山、申维辰、宋林、毛小兵、谭栖伟、王帅廷、阳宝华、赵智勇、杜善学、令政策、万庆良、谭力、韩先聪、张田欣、武长顺、陈铁新、陈川平、聂春玉、白云、白恩培、任润厚、孙兆学、潘逸阳、秦玉海、何家成、赵少麟、杨金山、梁滨、隋凤富、朱明国、王敏、韩学键。[br]十八大至2013年底被宣布查处的18名省部级官员为:李春城、衣俊卿、刘铁男、倪发科、郭永祥、王素毅、李达球、蒋洁敏、季建业、廖少华、陈柏槐、郭有明、陈安众、付晓光、童名谦、李东生、杨刚、李崇禧。[br]十八大至今其他落马的重要官员还有许杰、齐平景、王永春、戴春宁等，这4人被调查前的职务分别为国家信访局副局长、中国外文局副局长、中国石油天然气集团公司副总经理、中国出口信用保险公司副总经理。[br]十八大后落马副国级及以上官员(共4人)[br]姓名[br]职位[br]被调查报道时间[br]原因[br]处理情况[br]1[br][img]http://img1.cache.netease.com/catchpic/7/73/731387AE39DA57AF3677E0A74FECF1DA.jpg[/img][br]苏荣[br]中国人民政治协商会议第十二届全国委员会副主席[br]2014年6月14日[br]报道[br]涉嫌严重违纪违法[br]被免职[br](6月25日报道)[br]被罢免全国人大代表职务[br](9月25日报道)[br]2[br][img]http://img1.cache.netease.com/catchpic/5/59/59E5E5E879EEBFA0F4ED7332E017A2A6.jpg[/img][br]徐才厚[br]中央军事委员会原副主席[br]--[br]涉嫌受贿犯罪[br]被开除党籍[br](6月30日报道)[br]3[br]周永康[br]十七届中央政治局委员、常委[br]7月29日报道[br]严重违纪[br](12月6日报道)[br]4[br][img]http://img1.cache.netease.com/catchpic/B/B8/B8945AE7B405F5FADAF4D3F1A679F34A.jpg[/img][br]令计划[br]中国人民政治协商会议第十二届全国委员会副主席、中共中央统战部部长[br]12月22日报道[br]涉嫌严重违纪[br]正接受组织调查(12月22日报道)[br]2014年以来落马省部级官员(共37人)[br][img]http://img1.cache.netease.com/catchpic/A/AF/AF71F886AAF150DF92D266E6043853D1.jpg[/img][br]冀文林[br]海南省[br]副省长[br]2月18日[br]严重违纪违法[br](2月20日、[br]3月27日报道)[br]被“双开”[br](7月2日报道)[br][img]http://img1.cache.netease.com/catchpic/F/FE/FEA1A0671E034A714FD79FFABDAE7821.jpg[/img][br]祝作利[br]陕西政协[br]副主席[br]2月19日[br](2月20日[br]报道)[br]被撤委员资格[br](2月27日[br][img]http://img1.cache.netease.com/catchpic/C/CC/CC8BEFC94B51C63FA0BA087FF10F7FB1.jpg[/img][br]金道铭[br]山西省人大常委会[br]副主任[br]2月27日[br](3月1日报道)[br]被撤销职务[br](3月2日报道)[br][img]http://img1.cache.netease.com/catchpic/0/01/01F6A728B512D87891CD2A2287B41B8D.jpg[/img][br]沈培平[br]云南省[br]3月9日[br](3月12日、[br]3月28日报道)[br]5[br][img]http://img1.cache.netease.com/catchpic/C/C5/C5B217E4EFC752C22CDAC17D4F0BF7E4.jpg[/img][br]姚木根[br]江西省[br]3月22日报道[br](3月28日、4月11日报道)[br]6[br][img]http://img1.cache.netease.com/catchpic/B/BC/BCA7C1B83FDB433A7B3FCA7DE5383998.jpg[/img][br]谷俊山[br]解放军总后勤部原副部长[br]涉嫌贪污、受贿、挪用公款、滥用职权[br]被提起公诉[br](3月31日报道)[br]7[br][img]http://img1.cache.netease.com/catchpic/D/DB/DBEABCD7A7F26B973980CD4C9C477EE5.jpg[/img][br]申维辰[br]中国科协[br]党组书记、常务副主席[br]4月12日报道[br](4月17日报道)[br]8[br][img]http://img1.cache.netease.com/catchpic/3/35/3565A75300E144BCB13C86F3D928C122.jpg[/img][br]宋林[br]华润集团[br]董事长、党委书记[br]4月17日报道[br](4月19日、4月23日报道)[br]9[br][img]http://img1.cache.netease.com/catchpic/B/BF/BFF9F5E202495518288F2849ABC198F3.jpg[/img][br]毛小兵[br]青海省委常委、西宁市委书记[br]4月25日报道[br](4月28日报道)[br](7月16日报道)[br]10[br][img]http://img1.cache.netease.com/catchpic/C/CE/CE88C7DB33FFD7ACC85A971510C257A9.jpg[/img][br]谭栖伟[br]重庆市人大常委会副主任[br]5月3日报道[br](5月6日报道)[br]11[br][img]http://img1.cache.netease.com/catchpic/9/96/96482E5B34FFBE312F3FD3D02B621628.jpg[/img][br]王帅廷[br]香港中旅(集团)有限公司副董事长、总经理、党委副书记[br]5月16日报道[br]涉嫌在华润集团工作期间严重违纪违法[br](6月26日报道)[br]12[br][img]http://img1.cache.netease.com/catchpic/3/37/378237D3D6D97D7393FBF879D674E079.jpg[/img][br]阳宝华[br]湖南省政协原党组副书记、副主席[br]5月26日报道[br](7月15日报道)[br]13[br][img]http://img1.cache.netease.com/catchpic/7/76/76C9E1EA98F089E9734543805FFC49A1.jpg[/img][br]赵智勇[br]江西省委常委、委员[br](6月3日报道)[br]被开除党籍，降为科员待遇[br]14[br][img]http://img1.cache.netease.com/catchpic/9/9C/9C037D777005740E0FD5A5A3EAE84DA8.jpg[/img][br]杜善学[br]山西省委常委、副省长[br]6月19日报道[br](6月23日报道)[br]15[br][img]http://img1.cache.netease.com/catchpic/0/0B/0B213677E8B56DAA7EBAFD9C8278B8B1.jpg[/img][br]令政策[br]山西省政协副主席[br]16[br][img]http://img1.cache.netease.com/catchpic/8/85/85B274425F8EFD078298CA1D17064312.jpg[/img][br]万庆良[br]广东省委常委、广州市委书记[br]6月27日[br](10月9日报道)[br]17[br][img]http://img1.cache.netease.com/catchpic/4/4C/4CA38106CCD552CAAAF3CBD8D3F0874E.jpg[/img][br]谭力[br]海南省委常委、副省长[br]7月8日报道[br](7月10日报道)[br]被开除党籍并被立案侦查[br](9月30日报道)[br]18[br][img]http://img1.cache.netease.com/catchpic/0/0A/0A187E861E740CE3D034FE04AB05B917.jpg[/img][br]韩先聪[br]安徽省政协副主席[br]7月12日报道[br](7月18日报道)[br]19[br]张田欣[br]云南省委常委、昆明市委书记[br](7月12日报道)[br]被开除党籍，降为副处级[br]20[br][img]http://img1.cache.netease.com/catchpic/2/2B/2BEF22753C00FEC0306C82CC123FCA4E.jpg[/img][br]武长顺[br]天津市政协副主席、公安局局长[br]7月20日报道[br](7月24日报道)[br]21[br][img]http://img1.cache.netease.com/catchpic/0/09/0907B4EFD151CE005EFF2A1CE9F3DAD6.jpg[/img][br]陈铁新[br]辽宁省政协副主席[br]7月24日报道[br](7月30日报道)[br]22[br][img]http://img1.cache.netease.com/catchpic/8/87/871D84E81E524FB4A47A34806E1145CD.jpg[/img][br]陈川平[br]山西省委常委、太原市委书记[br]8月23日报道[br](8月29日报道)[br]23[br][img]http://img1.cache.netease.com/catchpic/B/B4/B489A44B52107F870611EBD86A5B1875.jpg[/img][br]聂春玉[br]山西省委常委、秘书长[br]24[br][img]http://img1.cache.netease.com/catchpic/8/8A/8A192846491538970153F22C94E298A1.jpg[/img][br]白云[br]山西省委常委、统战部部长[br]8月29日报道[br](9月2日报道)[br]被撤销政协委员资格[br](10月29日报道)[br]25[br][img]http://img1.cache.netease.com/catchpic/F/F5/F59DC374791457E5198F3F2DAA5BB324.jpg[/img][br]白恩培[br]全国人大环资委副主任、原云南省委书记[br](9月3日报道)[br]26[br][img]http://img1.cache.netease.com/catchpic/1/1C/1C0708E8562564EE77139108FF26A497.jpg[/img][br]任润厚[br]山西省副省长[br]27[br][img]http://img1.cache.netease.com/catchpic/9/93/93660105227FD9813210D5511B919B83.jpg[/img][br]孙兆学[br]中国铝业公司总经理[br]9月15日报道[br]28[br][img]http://img1.cache.netease.com/catchpic/a_/AD/AD34D54A1056D8D31FA732161229A9BA.jpg[/img][br]潘逸阳[br]内蒙古自治区党委常委、自治区政府副主席[br]9月17日报道[br](9月21日报道)[br]29[br][img]http://img1.cache.netease.com/catchpic/7/76/763214B4924826500EEDB78D34F93C4D.jpg[/img][br]秦玉海[br]河南省人大常委会党组书记、副主任[br]9月21日报道[br](9月26日报道)[br]30[br][img]http://img1.cache.netease.com/catchpic/7/75/7565FB8A4C4FE40C4F8BAF797B7D0949.jpg[/img][br]何家成[br]国家行政学院常务副院长[br]10月11日报道[br](10月13日报道)[br]31[br]赵少麟[br]江苏省委原常委、秘书长[br]正接受组织调查[br]32[br][img]http://img1.cache.netease.com/catchpic/C/CC/CCA79BB908BAF78A95D50380A52A5588.jpg[/img][br]杨金山[br]成都军区副司令员[br]10月23日报道[br]开除党籍[br]33[br][img]http://img1.cache.netease.com/catchpic/7/7F/7F4D69971CC2AA24937F2A7D960DB6F9.jpg[/img][br]梁滨[br]河北省委常委、组织部部长[br]11月20日报道[br](11月25日报道)[br]34[br][img]http://img1.cache.netease.com/catchpic/4/48/4874DA239E3BDC54960982D748617A4A.jpg[/img][br]隋凤富[br]黑龙江人大常委会副主任[br]11月27日报道[br](12月3日报道)[br]35[br][img]http://img1.cache.netease.com/catchpic/1/1F/1F4766DF5549239ABCCE6A5E57E043F8.jpg[/img][br]朱明国[br]广东省政协主席[br]11月28日报道[br]36[br][img]http://img1.cache.netease.com/catchpic/B/BC/BCE51B490A475028A55AD28364E86EA6.jpg[/img][br]王敏[br]山东省委常委、济南市委书记[br]12月18日报道[br]37[br][img]http://img1.cache.netease.com/catchpic/1/1F/1F41D34C2C50C4C41928726E1C289F4B.jpg[/img][br]韩学键[br]黑龙江省委常委、大庆市委书记[br]注:统计截至2014年12月22日。按以上官员落马的公开报道时间排序。[br]·十八大至2013年底被调查的省部级官员(共18人)[br]本文来源:人民网[br]十八大至2013年底被调查的省部级官员(共18人)[br][img]http://img1.cache.netease.com/catchpic/3/3C/3C2F61DD8F8C4B138B1AF0056307650E.jpg[/img][br]杨刚";
	
		String str2 = str.toLowerCase().replaceAll("\\[img\\].*?\\[/img\\]","").replaceAll("\\[br\\].*?\\[/br\\]","").replace("[br]", "");
		
		System.out.println(str2);
		System.out.println(str2.length());
	}
}
