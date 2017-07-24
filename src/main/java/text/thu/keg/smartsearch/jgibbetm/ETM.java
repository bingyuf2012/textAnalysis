/*
 * Copyright (C) 2007 by
 * 
 * 	Xuan-Hieu Phan
 *	hieuxuan@ecei.tohoku.ac.jp or pxhieu@gmail.com
 * 	Graduate School of Information Sciences
 * 	Tohoku University
 * 
 *  Cam-Tu Nguyen
 *  ncamtu@gmail.com
 *  College of Technology
 *  Vietnam National University, Hanoi
 *
 * JGibbsETM is a free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * JGibbsETM is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JGibbsETM; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 */

package text.thu.keg.smartsearch.jgibbetm;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import text.searchSDK.util.CommonUtil;
import text.searchSDK.util.Constant;
import text.thu.keg.smartsearch.timer.Timer;

@Component
public class ETM {
	@Autowired
	CommonUtil commonUtil;

	public Model est(String filetokename, String outputdir, int docNum) {
		Timer timer = new Timer("ETM");
		ETMCmdOption option = new ETMCmdOption();
		CmdLineParser parser = new CmdLineParser(option);
		try {
			parser.parseArgument(new String[] { "-est", "-beta1", "0.1", "-beta2", "0.01", "-ntopics",
					"" + commonUtil.getTopicNum(docNum), "-netopics", "" + commonUtil.getTopicNum(docNum), "-twords",
					"" + commonUtil.getTopwords(), "-tentities", "" + commonUtil.getTop100Entity(), "-dir", outputdir,
					"-dfile", filetokename });
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

	/*public static void inf() throws CmdLineException, IOException {
		Timer timer = new Timer("ETM");
		ETMCmdOption option = new ETMCmdOption();
		CmdLineParser parser = new CmdLineParser(option);
		parser.parseArgument(new String[] { "-inf", "-beta1", "0.1", "-beta2", "0.1", "-ntopics", "20", "-netopics",
				"15", "-twords", "20", "-tentities", "20", "-dir", ".\\perp\\", "-dfile", "����8.8������_train1.txt" });
	
		option.niters = 100;
		option.modelName = "model-final";
		ETMInference eInference = new ETMInference();
		eInference.init(option);
		option.dfile = "����8.8������_test1.txt";
		Model newModel = eInference.inference();
		newModel.saveModel("model-inf");
		PrintConsole.PrintLog("", Perplexity.calPerp(newModel, eInference.trnModel));
	
	}
	
	public static void showHelp(CmdLineParser parser) {
		System.out.println("ETM [options ...] [arguments...]");
		parser.printUsage(System.out);
	}*/

}
