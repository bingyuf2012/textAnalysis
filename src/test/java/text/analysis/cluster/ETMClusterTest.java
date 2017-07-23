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
		w5.setTitle("系统间通信（9）——RPC的基本概念");
		w5.setContent(
				"RPC（Remote Procedure Call Protocol）远程过程调用协议。一个通俗的描述是：客户端在不知道调用细节的情况下，调用存在于远程计算机上的某个对象，就像调用本地应用程序中的对象一样。比较正式的描述是：一种通过网络从远程计算机程序上请求服务，而不需要了解底层网络技术的协议。那么我们至少从这样的描述中挖掘出几个要点。");
		w5.setTime("2017-07-17 19:44:00");
		w5.setType("文字");
		w5.setUrl("http://www.baidu.com");
		w5.setWeights("1");

		list.add(w5);
		
		System.out.println(JSONObject.toJSONString(eTMCluster.runLDA("你好", list, "D:/abc/", 1, 1)));
	}
}
