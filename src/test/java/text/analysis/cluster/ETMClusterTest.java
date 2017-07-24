package text.analysis.cluster;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import text.analyse.etmutility.ETMCluster;
import text.analysis.core.BaseTest;
import text.searchSDK.model.WebSearchResult;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午7:40:35
 */

public class ETMClusterTest extends BaseTest {
	@Autowired
	ETMCluster eTMCluster;

	@Test
	public void testRunLDA() {
		List<WebSearchResult> list = new ArrayList<WebSearchResult>();

		WebSearchResult w1 = new WebSearchResult();
		w1.setTitle("RPC的基本概念");
		w1.setContent(
				"经过了详细的信息格式、网络IO模型的讲解，并且通过Java RMI的讲解进行了预热。从这篇文章开始我们将进入这个系列博文的另一个重点知识体系的讲解：RPC。在后续的几篇文章中，我们首先讲解RPC的基本概念，一个具体的RPC实现会有哪些基本要素构成，然后我们详细介绍一款典型的RPC框架：Apache Thrift。接下来我们聊聊服务治理和DUBBO服务框架。最后总结一下如何在实际工作中选择合适的RPC框架。");
		w1.setTime("2017-07-17 19:44:00");
		w1.setType("文字");
		w1.setUrl("http://www.baidu.com");
		w1.setWeights("1");

		list.add(w1);

		WebSearchResult w2 = new WebSearchResult();
		w2.setTitle("zookeeper核心原理");
		w2.setContent(
				"我们知道zookeeper主要是为了统一分布式系统中各个节点的工作状态，在资源冲突的情况下协调提供节点资源抢占，提供给每个节点了解整个集群所处状态的途径。这一切的实现都依赖于zookeeper中的事件监听和通知机制。");
		w2.setTime("2017-07-17 19:44:00");
		w2.setType("文字");
		w2.setUrl("http://www.baidu.com");
		w2.setWeights("1");

		list.add(w2);

		WebSearchResult w3 = new WebSearchResult();
		w3.setTitle("Nginx进阶");
		w3.setContent(
				"nginx对返回给浏览器的http response body是可以进行压缩的。虽然需要消耗一点cpu和内存资源，但是想到100KB的数据量可以压缩到60KB甚至更小进行传输，是否有一定的吸引力？这里我的建议是，不要为了节约成本将业务服务和负载层服务放在一台物理服务器上，这样做既影响性能又增加了运维难度。http返回数据进行压缩的功能在很多场景下都实用。");
		w3.setTime("2017-07-17 19:44:00");
		w3.setType("文字");
		w3.setUrl("http://www.baidu.com");
		w3.setWeights("1");

		list.add(w3);

		WebSearchResult w4 = new WebSearchResult();
		w4.setTitle("系统间通信（10）——RPC的基本概念");
		w4.setContent(
				"RPC（Remote Procedure Call Protocol）远程过程调用协议。一个通俗的描述是：客户端在不知道调用细节的情况下，调用存在于远程计算机上的某个对象，就像调用本地应用程序中的对象一样。比较正式的描述是：一种通过网络从远程计算机程序上请求服务，而不需要了解底层网络技术的协议。那么我们至少从这样的描述中挖掘出几个要点。");
		w4.setTime("2017-07-17 19:44:00");
		w4.setType("文字");
		w4.setUrl("http://www.baidu.com");
		w4.setWeights("1");

		list.add(w4);
		
		WebSearchResult w5 = new WebSearchResult();
		w5.setTitle("总理政府工作报告带火的这件事，有了国家规划！");
		w5.setContent(
				"在两会审议、讨论现场，“人工智能”也成为宠儿。3月10日上午，李克强总理来到安徽代表团，全国人大代表、安徽科大讯飞股份有限公司董事长刘庆峰向总理展示公司最新研发的“人工智能”技术产品。对互联网“大佬”身份的代表委员来说，“人工智能”更是不能错过的话题。今年两会，百度董事长兼首席执行官李彦宏、腾讯公司董事会主席马化腾、小米科技董事长雷军就提出不下10份以“人工智能”为主题的提案。总理与“人工智能”的“亲密接触”李克强与“人工智能”这一前沿技术，有过多次“亲密接触”。“问答机器人小度”时间：2015年10月19日地点：全国大众创业万众创新活动周");
		w5.setTime("2017-07-17 19:44:00");
		w5.setType("文字");
		w5.setUrl("http://www.baidu.com");
		w5.setWeights("1");

		list.add(w5);
		
		WebSearchResult w6 = new WebSearchResult();
		w6.setTitle("江苏省高院原院长许前飞被处分 违规出入私人会所");
		w6.setContent(
				"日前，经中共中央批准，中共中央纪委对江苏省高级人民法院原党组书记、院长许前飞严重违纪问题进行了立案审查。经查，许前飞同志身为高级人民法院党组书记、院长，违反政治纪律和政治规矩，应与其关系密切的律师和私营企业主请托，干预和插手具体案件审判工作，以案谋私，严重损害司法公信力和人民法院形象；严重违反中央八项规定精神，违规出入私人会所和打高尔夫球，接受公款宴请；违反组织纪律，违规选拔任用干部，不按规定报告个人有关事项；违反廉洁纪律，收受礼品、礼金。依据《中国共产党纪律处分条例》等有关规定，经中央纪委常委会会议研究并报中共中央批准，决定给予许前飞同志撤销党内职务处分，由最高人民法院给予其撤职处分，降为正局级非领导职务；收缴其违纪所得。许前飞简历许前飞，男，1955年10月生，汉族，河南孟津人，研究生学历，法学博士学位，1976年7月加入中国共产党，1974年10月参加工作。1974年10月，湖北省天门县后港公社知青；1978年03月，武汉大学哲学系哲学专业学习；1982年01月，武汉大学法律系国际法专业硕士研究生；1984年12月，武汉大学国际法学系教师、副主任（其间：1985年09月至1988年07月，武汉大学国际经济法专业博士研究生，1988年10月至1989年09月，美国纽约大学进修）；1988年12月，海南省高级人民法院经济庭副庭长；1990年10月，海南省高级人民法院审判委员会委员、经济庭庭长；1994年07月，海南省洋浦经济开发区中级人民法院院长（副厅级）；1996年11月，海南省海口市中级人民法院院长、党组书记；2003年02月，海南省政府副秘书长、省法制办主任（正厅级，其间：2006年03月至2007年01月，中央党校一年制中青年干部培训班学习）；2007年05月，海南省政府秘书长，省政府办公厅党组书记；2007年12月，云南省高级人民法院党组书记；2008年01月，云南省高级人民法院院长、党组书记（2008年02月二级大法官）；2012年12月，江苏省高级人民法院党组书记；2013年01月，江苏省高级人民法院院长、党组书记。十一届全国人大代表，江苏省十二届人大代表。");
		w6.setTime("2017-07-17 19:44:00");
		w6.setType("文字");
		w6.setUrl("http://www.baidu.com");
		w6.setWeights("1");

		list.add(w6);
		
		
		System.out.println(JSONObject.toJSONString(eTMCluster.runLDA("你好", list, "D:/abc/", 2, 2)));
	}
}
