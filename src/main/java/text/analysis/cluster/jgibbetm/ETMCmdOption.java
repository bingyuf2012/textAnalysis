package text.analysis.cluster.jgibbetm;

import org.kohsuke.args4j.*;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午5:22:51
 */

public class ETMCmdOption {
	@Option(name = "-est", usage = "Specify whether we want to estimate model from scratch")
	public boolean est = false;

	@Option(name = "-estc", usage = "Specify whether we want to continue the last estimation")
	public boolean estc = false;

	@Option(name = "-inf", usage = "Specify whether we want to do inference")
	public boolean inf = true;

	@Option(name = "-dir", usage = "Specify directory")
	public String dir = "";

	@Option(name = "-dfile", usage = "Specify data file")
	public String dfile = "";

	@Option(name = "-model", usage = "Specify the model name")
	public String modelName = "";

	@Option(name = "-alpha", usage = "Specify alpha")
	public double alpha = -1.0;

	@Option(name = "-beta1", usage = "Specify beta1")
	public double beta1 = -1.0;

	@Option(name = "-beta2", usage = "Specify beta2")
	public double beta2 = -1.0;

	@Option(name = "-gamma", usage = "Specify gamma")
	public double gamma = -1.0;

	@Option(name = "-ntopics", usage = "Specify the number of topics")
	public int KWords = 100;

	@Option(name = "-netopics", usage = "Specify the number of entity topics")
	public int KEntities = 200;

	@Option(name = "-niters", usage = "Specify the number of iterations")
	public int niters = 1000;

	@Option(name = "-savestep", usage = "Specify the number of steps to save the model since the last save")
	public int savestep = 1000;

	@Option(name = "-twords", usage = "Specify the number of most likely words to be printed for each topic")
	public int twords = 100;

	@Option(name = "-tdocs", usage = "Specify the number of most likely docs to be printed for each topic")
	public int tdocs = 10;

	@Option(name = "-tentities", usage = "Specify the number of most likely entities to be printed for each topic")
	public int tentities = 100;

	@Option(name = "-withrawdata", usage = "Specify whether we include raw data in the input")
	public boolean withrawdata = false;

	@Option(name = "-wordmap", usage = "Specify the wordmap file")
	public String wordMapFileName = Constant.WORD_MAP;

	@Option(name = "-entitymap", usage = "Specify the entitymap file")
	public String entityMapFileName = Constant.ENTITY_MAP;
}
