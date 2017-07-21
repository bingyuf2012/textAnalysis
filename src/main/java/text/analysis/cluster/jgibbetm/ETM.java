package text.analysis.cluster.jgibbetm;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午5:20:58
 */

public class ETM {
	public Model est(String filetokename, String outputdir, int docNum) {
		Timer timer = new Timer("ETM");
		ETMCmdOption option = new ETMCmdOption();
		CmdLineParser parser = new CmdLineParser(option);
		int topicNum = CommonUtil.getTopicNum(docNum);
		// int topicNum = 2;
		int topicword = CommonUtil.getTopwords();
		int top100entity = CommonUtil.getTop100Entity();
		// String outputdir = CommonUtil.getOutputDataDir();
		// parser
		// .parseArgument(new String[] { "-est", "-beta1", "0.1",
		// "-beta2", "0.1", "-ntopics", "10", "-netopics", "10",
		// "-twords", "20", "-tentities", "20", "-dir",
		// ".\\perp\\", "-dfile", "test1.txt" });
		// parser.parseArgument(new String[] { "-est", "-beta1", "0.1",
		// "-beta2",
		// "0.1", "-ntopics", "" + topicNum, "-netopics", "" + topicNum,
		// "-twords", "" + topicword, "-tentities", "" + top100entity,
		// "-dir", outputdir, "-dfile", "新华社.txt" });
		try {
//			parser.parseArgument(new String[] { "-est", "-beta1", "0.1",
//					"-beta2", "0.1", "-ntopics", "" + topicNum, "-netopics",
//					"" + topicNum, "-twords", "" + topicword, "-tentities",
//					"" + top100entity, "-dir", outputdir, "-dfile",
//					filetokename });
			
			parser.parseArgument(new String[] { "-est", "-beta1", "0.1",
					"-beta2", "0.01", "-ntopics", "" + topicNum, "-netopics",
					"" + topicNum, "-twords", "" + topicword, "-tentities",
					"" + top100entity, "-dir", outputdir, "-dfile",
					filetokename });
			
		} catch (CmdLineException e) {
			e.printStackTrace();
		}

		/**
		 * -inf -alpha 10 -beta1 0.1 -beta2 0.1 -gamma 10 -ntopics 5 -netopics
		 * 10 -twords 20 -tentities 20 -dir .\test2\ -dfile t2.txt
		 */
		option.niters = Constant.ITERATOR_NUM;

		timer.start();
		Estimator estimator = new Estimator();
		estimator.init(option);

		timer.getTime();
		return estimator.estimate();
	}

	public static void inf() throws CmdLineException, IOException {

		Timer timer = new Timer("ETM");
		ETMCmdOption option = new ETMCmdOption();
		CmdLineParser parser = new CmdLineParser(option);
		/** Warning： train.txt ******************/
		parser.parseArgument(new String[] { "-inf", "-beta1", "0.1", "-beta2",
				"0.1", "-ntopics", "20", "-netopics", "15", "-twords", "20",
				"-tentities", "20", "-dir", ".\\perp\\", "-dfile",
				"智利8.8级地震_train1.txt" });

		//				
		//				
		// option = new ETMCmdOption();
		// option.KWords = 5;
		// option.KEntities = 5;
		// option.est = false;
		// option.inf = true;
		// option.alpha = 50.0 / 5;
		// option.beta1 = 0.1;
		// option.beta2 = 0.1;
		// option.gamma = 50.0/ 5 ;
		// option.tentities = 10;
		// option.twords = 10;
		// option.dir = "test2/";
		// option.dfile = "t2.txt";
		option.niters = 100;
		option.modelName = "model-final";
		ETMInference eInference = new ETMInference();
		eInference.init(option);
		option.dfile = "智利8.8级地震_test1.txt";
		Model newModel = eInference.inference();
		newModel.saveModel("model-inf");
		PrintConsole.PrintLog("", Perplexity.calPerp(newModel,
				eInference.trnModel));

	}

	public static void showHelp(CmdLineParser parser) {
		System.out.println("ETM [options ...] [arguments...]");
		parser.printUsage(System.out);
	}

}
