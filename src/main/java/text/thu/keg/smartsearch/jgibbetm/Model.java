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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import text.analysis.utils.ConstantUtil;

public class Model {
	private Logger LOG = LoggerFactory.getLogger(Model.class);

	// ---------------------------------------------------------------
	// Class Variables
	// ---------------------------------------------------------------

	public static String tassignSuffix; // suffix for topic assignment file
	public static String tEAssignSuffix; // //suffix for entity topic assignment
	// file
	public static String xEAssignSuffix;
	public static String thetaSuffix; // suffix for theta (topic - document
	// distribution) file
	public static String kesaiSuffix;
	public static String phiSuffix; // suffix for phi file (topic - word
	// distribution) file
	public static String phiEntitySuffix;
	public static String othersSuffix; // suffix for containing other parameters
	public static String twordsSuffix; // suffix for file containing
	// words-per-topics
	public static String tdocsSuffix;
	public static String tentitiesSuffix;

	// ---------------------------------------------------------------
	// Model Parameters and Variables
	// ---------------------------------------------------------------

	public String wordMapFile; // file that contain word to id map
	public String entityMapFile;
	public String trainlogFile; // training log file

	public String dir;
	public String dfile;
	public String modelName;
	public int modelStatus; // see ConstantUtils class for status of model
	public ETMDataset data; // link to a dataset

	public int M; // dataset size (i.e., number of docs)
	public int VWords; // vocabulary size
	public int VEntities;
	public int KWords; // number of topics
	public int KEntities;
	public double alpha, beta1, beta2, gamma; // LDA hyperparameters
	public int niters; // number of Gibbs sampling iteration
	public int liter; // the iteration at which the model was saved
	public int savestep; // saving period
	public int twords; // print out top words per each topic
	public int tdocs;
	public int tentities;
	public int withrawdata;

	// Estimated/Inferenced parameters
	public double[][] theta; // theta: document - topic distributions, size M x
	// KWords
	public double[][] kesai; // kesai: document - entity topic distributions,
	// size KWords x KEntities
	public double[][] phi; // phi: topic-word distributions, size KWords x
	// VWords
	public double[][] phiEntity;

	// Temp variables while sampling
	public Vector<Integer>[] z; // topic assignments for words, size M x
	// doc.wordsize()
	public Vector<Integer>[] zEntity;// entity assignments for entities,size M x
	// doc.entitysize()
	public Vector<Integer>[] x;// size M x doc.entitysize()
	protected int[][] nw; // nw[i][j]: number of instances of word/term i
	// assigned to topic j, size VWords x KWords
	protected int[][] ne; // number of ent.....�����⣬��ȡʵ��������������ĵ���
	protected int[][] nd; // nd[i][j]: number of words in document i assigned to
	// topic j, size M x KWords
	protected int[][] nde;
	protected int[] nwsum; // nwsum[j]: total number of words assigned to topic
	// j, size KWords
	protected int[] nesum;
	protected int[] ndsum; // ndsum[i]: total number of words in document i,
	// size M
	protected int[] ndesum;
	protected int[][] nz; // nz[i][j]: number of instances of entity i assigned
	// to super topic j,size VEntities x KWords
	protected int[] nzsum; // nz[j]:total number of entities assigned to super
	// topic i,size KWords
	protected int[][] ndz; // ndz[i][j] number of entities in document i
	// assigned to super topic j, size M x KWords
	protected int[][] nzz; // nzz[i][j] number of entity topic j assigned to
	// super topic i
	protected int[] nzzsum;// nzz[i] number of entity topics assigned to super
	// topic i

	// temp variables for sampling
	protected double[] p;
	protected double[][] pEntity; // zEntities

	// protected double [] pWE;//zWords for entities

	// ---------------------------------------------------------------
	// Constructors
	// ---------------------------------------------------------------

	public Model() {
		setDefaultValues();
	}

	/**
	 * Set default values for variables
	 */
	public void setDefaultValues() {
		wordMapFile = ConstantUtil.WORD_MAP;
		entityMapFile = ConstantUtil.ENTITY_MAP;
		trainlogFile = ConstantUtil.TRAIN_LOG;
		tassignSuffix = ConstantUtil.SUFFIX_TASSIGN;
		tEAssignSuffix = ConstantUtil.SUFFIX_TEASSIGN;
		xEAssignSuffix = ConstantUtil.SUFFIX_XEASSIGN;
		thetaSuffix = ConstantUtil.SUFFIX_THETA;
		kesaiSuffix = ConstantUtil.SUFFIX_KESAI;

		phiSuffix = ConstantUtil.SUFFIX_PHI;
		phiEntitySuffix = ConstantUtil.SUFFIX_PHIENTITY;
		othersSuffix = ConstantUtil.SUFFIX_OTHERS;
		twordsSuffix = ConstantUtil.SUFFIX_TWORDS;
		tdocsSuffix = ConstantUtil.SUFFIX_TDOCS;
		tentitiesSuffix = ConstantUtil.SUFFIX_TENTITIES;

		dir = ConstantUtil.DIR;
		dfile = ConstantUtil.TRNDOCS_DAT;
		modelName = ConstantUtil.MODEL_NAME;
		modelStatus = ConstantUtil.MODEL_STATUS_UNKNOWN;

		M = 0;
		VWords = 0;
		VEntities = 0;
		KWords = 100;
		KEntities = 200;
		alpha = 50.0 / KWords;
		beta1 = 0.1;
		beta2 = 0.1;
		gamma = 50.0 / KEntities;
		niters = 2000;
		liter = 0;

		z = null;
		zEntity = null;
		nw = null;
		ne = null;
		nde = null;
		nd = null;
		ndsum = null;
		ndesum = null;
		nz = null;
		nzsum = null;
		theta = null;
		phi = null;
		phiEntity = null;
		kesai = null;
		x = null;
		twords = 0;
		tdocs = 0;
		tentities = 0;
	}

	// ---------------------------------------------------------------
	// I/O Methods
	// ---------------------------------------------------------------
	/**
	 * read other file to get parameters
	 */
	protected boolean readOthersFile(String otherFile) {
		// open file <model>.others to read:

		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(otherFile), ConstantUtil.UTF8));

			String line;
			while ((line = reader.readLine()) != null) {
				StringTokenizer tknr = new StringTokenizer(line, "= \t\r\n");

				int count = tknr.countTokens();
				if (count != 2)
					continue;

				String optstr = tknr.nextToken();
				String optval = tknr.nextToken();

				if (optstr.equalsIgnoreCase("alpha")) {
					alpha = Double.parseDouble(optval);
				} else if (optstr.equalsIgnoreCase("beta1")) {
					beta1 = Double.parseDouble(optval);
				} else if (optstr.equalsIgnoreCase("beta2")) {
					beta2 = Double.parseDouble(optval);
				} else if (optstr.equalsIgnoreCase("gamma")) {
					gamma = Double.parseDouble(optval);
				} else if (optstr.equalsIgnoreCase("ntopics")) {
					KWords = Integer.parseInt(optval);
				} else if (optstr.equalsIgnoreCase("netopics")) {
					KEntities = Integer.parseInt(optval);
				} else if (optstr.equalsIgnoreCase("liter")) {
					liter = Integer.parseInt(optval);
				} else if (optstr.equalsIgnoreCase("nwords")) {
					VWords = Integer.parseInt(optval);
				} else if (optstr.equalsIgnoreCase("nentities")) {
					VEntities = Integer.parseInt(optval);
				} else if (optstr.equalsIgnoreCase("ndocs")) {
					M = Integer.parseInt(optval);
				} else {
					// any more?
				}
			}

			reader.close();
		} catch (Exception e) {
			System.out.println("Error while reading other file:" + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected boolean readTAssignFile(String tassignFile, String tEAssignFile) {
		try {
			int i, j;
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(tassignFile), ConstantUtil.UTF8));
			BufferedReader readerE = new BufferedReader(
					new InputStreamReader(new FileInputStream(tEAssignFile), ConstantUtil.UTF8));// ��entity-topic��
			String line;
			String lineE;
			z = new Vector[M];
			zEntity = new Vector[M];
			// �ڴ˴������datadata = new ETMDataset(M);

			data.VWords = VWords;
			data.VEntities = VEntities;
			for (i = 0; i < M; i++) {
				line = reader.readLine();
				lineE = readerE.readLine();// line of tEAssignFile
				StringTokenizer tknr = new StringTokenizer(line, " \t\r\n");
				StringTokenizer tknrE = new StringTokenizer(lineE, " \t\r\n");
				int length = tknr.countTokens();
				int lengthE = tknrE.countTokens();

				Vector<Integer> words = new Vector<Integer>();
				Vector<Integer> entities = new Vector<Integer>();
				Vector<Integer> topics = new Vector<Integer>();
				Vector<Integer> etopics = new Vector<Integer>();// entity topics

				for (j = 0; j < length; j++) {
					String token = tknr.nextToken();

					StringTokenizer tknr2 = new StringTokenizer(token, ":");
					if (tknr2.countTokens() != 2) {
						LOG.info("Invalid word-topic assignment line");
						return false;
					}

					words.add(Integer.parseInt(tknr2.nextToken()));
					topics.add(Integer.parseInt(tknr2.nextToken()));
				} // end for each topic assignment

				for (j = 0; j < lengthE; j++) {
					String token = tknrE.nextToken();

					StringTokenizer tknr2 = new StringTokenizer(token, ":");
					if (tknr2.countTokens() != 2) {
						LOG.info("Invalid entity-topic assignment line");
						return false;
					}

					entities.add(Integer.parseInt(tknr2.nextToken()));
					etopics.add(Integer.parseInt(tknr2.nextToken()));
				} // end for each topic assignment

				// allocate and add new document to the corpus
				// ??????????????????????????????????/�˴�Ӧ�������ӻ��Ǹ�
				// ��ΪĿǰ��data�е�DOC�Ѿ���ֵ�ˣ�����Ϊ���˴���Ӧ�����ӿ��ң����ӵ��Ǵ����
				// Document doc = new Document(words,entities);
				// data.setDoc(doc, i);

				// assign values for z
				z[i] = new Vector<Integer>();
				zEntity[i] = new Vector<Integer>();
				// �Ĺ�
				for (j = 0; j < length; j++) {
					z[i].add(topics.get(j));
				}
				for (j = 0; j < etopics.size(); j++) {
					zEntity[i].add(etopics.get(j));
				}

			} // end for each doc

			reader.close();
		} catch (Exception e) {
			System.out.println("Error while loading model: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean readXAssignFile(String xEAssignFile) {
		try {
			int i, j;

			BufferedReader readerXE = new BufferedReader(
					new InputStreamReader(new FileInputStream(xEAssignFile), ConstantUtil.UTF8));//

			String lineXE;
			x = new Vector[M];

			data.VWords = VWords;
			data.VEntities = VEntities;
			for (i = 0; i < M; i++) {

				lineXE = readerXE.readLine();// line of tEAssignFile

				StringTokenizer tknrXE = new StringTokenizer(lineXE, " \t\r\n");

				int lengthXE = tknrXE.countTokens();

				Vector<Integer> words = new Vector<Integer>();
				Vector<Integer> entities = new Vector<Integer>();
				Vector<Integer> topics = new Vector<Integer>();
				Vector<Integer> xtopics = new Vector<Integer>();// entity topics

				for (j = 0; j < lengthXE; j++) {
					String token = tknrXE.nextToken();

					StringTokenizer tknr2 = new StringTokenizer(token, ":");
					if (tknr2.countTokens() != 2) {
						LOG.info("Invalid entity-topic assignment line");
						return false;
					}

					entities.add(Integer.parseInt(tknr2.nextToken()));
					xtopics.add(Integer.parseInt(tknr2.nextToken()));
				} // end for each topic assignment

				// allocate and add new document to the corpus

				// assign values for z

				x[i] = new Vector<Integer>();
				for (j = 0; j < xtopics.size(); j++) {
					x[i].add(xtopics.get(j));
				}

			} // end for each doc

			readerXE.close();
		} catch (Exception e) {
			System.out.println("Error while loading model: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean readData() {
		data = new ETMDataset().readDataSet(dir + File.separator + dfile);
		return true;
	}

	/**
	 * load saved model
	 */
	public boolean loadModel() {
		if (!readData()) // ���Ӷ�ȡ��ݲ���
			return false;
		if (!readOthersFile(dir + File.separator + modelName + othersSuffix))
			return false;

		if (!readTAssignFile(dir + File.separator + modelName + tassignSuffix,
				dir + File.separator + modelName + tEAssignSuffix))
			return false;

		if (!readXAssignFile(dir + File.separator + modelName + xEAssignSuffix))
			return false;
		// read dictionary
		Dictionary dict = new Dictionary();
		if (!dict.readWordEntityMap(dir + File.separator + wordMapFile, dir + File.separator + entityMapFile))
			return false;

		data.localDict = dict;
		return true;
	}

	/**
	 * Save word-topic assignments for this model
	 */
	public boolean saveModelTAssign(String filename) {
		int i, j;

		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filename), ConstantUtil.UTF8));

			// write docs with topic assignments for words
			for (i = 0; i < data.M; i++) {
				for (j = 0; j < data.docs[i].words.length; ++j) {
					writer.write(data.docs[i].words[j] + ":" + z[i].get(j) + " ");
				}
				writer.write("\n");
			}

			writer.close();
		} catch (Exception e) {
			System.out.println("Error while saving model tassign: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Save word-topic assignments for this model
	 */
	public boolean saveModelTEAssign(String filename) {
		int i, j;

		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filename), ConstantUtil.UTF8));

			// write docs with topic assignments for words
			for (i = 0; i < data.M; i++) {
				for (j = 0; j < data.docs[i].entities.length; ++j) {
					writer.write(data.docs[i].entities[j] + ":" + zEntity[i].get(j) + " ");
				}
				writer.write("\n");
			}

			writer.close();
		} catch (Exception e) {
			System.out.println("Error while saving model tEassign: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Save word-topic assignments for this model
	 */
	public boolean saveModelXEAssign(String filename) {
		int i, j;

		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filename), ConstantUtil.UTF8));

			// write docs with topic assignments for words
			for (i = 0; i < data.M; i++) {
				for (j = 0; j < data.docs[i].entities.length; ++j) {
					writer.write(data.docs[i].entities[j] + ":" + x[i].get(j) + " ");
				}
				writer.write("\n");
			}

			writer.close();
		} catch (Exception e) {
			System.out.println("Error while saving model tEassign: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Save theta (topic distribution) for this model
	 */
	public boolean saveModelTheta(String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filename), ConstantUtil.UTF8));
			for (int i = 0; i < M; i++) {
				for (int j = 0; j < KWords; j++) {
					writer.write(theta[i][j] + " ");
				}
				writer.write("\n");
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("Error while saving topic distribution file for this model: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Save kesai (entity topic distribution) for this model
	 */
	public boolean saveModelKesai(String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filename), ConstantUtil.UTF8));
			for (int i = 0; i < KWords; i++) {
				for (int j = 0; j < KEntities; j++) {
					writer.write(kesai[i][j] + " ");
				}
				writer.write("\n");
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("Error while saving entity topic distribution file for this model: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Save word-topic distribution
	 */

	public boolean saveModelPhi(String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filename), ConstantUtil.UTF8));

			for (int i = 0; i < KWords; i++) {
				for (int j = 0; j < VWords; j++) {
					writer.write(phi[i][j] + " ");
				}
				writer.write("\n");
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("Error while saving word-topic distribution:" + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Save word-topic distribution
	 */

	public boolean saveModelPhiEntity(String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filename), ConstantUtil.UTF8));

			for (int i = 0; i < KEntities; i++) {
				for (int j = 0; j < VEntities; j++) {
					writer.write(phiEntity[i][j] + " ");
				}
				writer.write("\n");
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("Error while saving entities-etopic distribution:" + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Save other information of this model
	 */
	public boolean saveModelOthers(String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filename), ConstantUtil.UTF8));

			writer.write("alpha=" + alpha + "\n");
			writer.write("beta1=" + beta1 + "\n");
			writer.write("beta2=" + beta2 + "\n");
			writer.write("gamma=" + gamma + "\n");

			writer.write("ntopics=" + KWords + "\n");
			writer.write("netopics=" + KEntities + "\n");
			writer.write("ndocs=" + M + "\n");
			writer.write("nwords=" + VWords + "\n");
			writer.write("nentities=" + VEntities + "\n");
			writer.write("liters=" + liter + "\n");

			writer.close();
		} catch (Exception e) {
			System.out.println("Error while saving model others:" + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Save model the most likely words for each topic
	 */
	public boolean saveModelTwords(String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filename), ConstantUtil.UTF8));

			if (twords > VWords) {
				twords = VWords;
			}

			for (int k = 0; k < KWords; k++) {
				List<Pair> wordsProbsList = new ArrayList<Pair>();
				for (int w = 0; w < VWords; w++) {
					Pair p = new Pair(w, phi[k][w], false);

					wordsProbsList.add(p);
				} // end foreach word

				// print topic
				writer.write("Topic " + k + "th:\n");
				Collections.sort(wordsProbsList);

				for (int i = 0; i < twords; i++) {
					if (data.localDict.containsWord((Integer) wordsProbsList.get(i).first)) {
						String word = data.localDict.getWord((Integer) wordsProbsList.get(i).first);
						// String pattern = "[a-zA-Z]{1,}";
						// Pattern p = Pattern.compile(pattern);
						// Matcher m = p.matcher(word);
						// boolean b = m.matches();
						// if(b == false){
						writer.write("\t" + word + " " + wordsProbsList.get(i).second + "\n");
						// }
						// PrintConsole.PrintLog("word:", word);

					}
				}
			} // end foreach topic

			writer.close();
		} catch (Exception e) {
			System.out.println("Error while saving model twords: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean saveModelTEntities(String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filename), ConstantUtil.UTF8));

			if (tentities > VEntities) {
				tentities = VEntities;
			}

			for (int ke = 0; ke < KEntities; ke++) {
				List<Pair> entitiesProbsList = new ArrayList<Pair>();
				for (int e = 0; e < VEntities; e++) {
					Pair p = new Pair(e, phiEntity[ke][e], false);

					entitiesProbsList.add(p);
				} // end foreach word

				// print topic
				writer.write("EntityTopic " + ke + "th:\n");
				Collections.sort(entitiesProbsList);

				for (int i = 0; i < tentities; i++) {
					if (data.localDict.containsEntity((Integer) entitiesProbsList.get(i).first)) {
						String entity = data.localDict.getEntity((Integer) entitiesProbsList.get(i).first);

						// PrintConsole.PrintLog("entity:", entity);

						writer.write("\t" + entity + " " + entitiesProbsList.get(i).second + "\n");
					}
				}
				writer.flush();
			} // end foreach topic
			writer.flush();
			writer.close();
		} catch (Exception e) {
			System.out.println("Error while saving model entities: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Save model
	 */
	public boolean saveModel(String modelName) {
		if (!saveModelTAssign(dir + File.separator + modelName + tassignSuffix)) {
			return false;
		}

		if (!saveModelTEAssign(dir + File.separator + modelName + tEAssignSuffix)) {
			return false;
		}

		if (!saveModelXEAssign(dir + File.separator + modelName + xEAssignSuffix)) {
			return false;
		}

		if (!saveModelOthers(dir + File.separator + modelName + othersSuffix)) {
			return false;
		}

		if (!saveModelTheta(dir + File.separator + modelName + thetaSuffix)) {
			return false;
		}

		if (!saveModelKesai(dir + File.separator + modelName + kesaiSuffix)) {
			return false;
		}

		if (!saveModelPhi(dir + File.separator + modelName + phiSuffix)) {
			return false;
		}

		if (!saveModelPhiEntity(dir + File.separator + modelName + phiEntitySuffix)) {
			return false;
		}

		if (twords > 0) {
			if (!saveModelTwords(dir + File.separator + modelName + twordsSuffix))
				return false;
		}
		if (tdocs > 0) {
			if (!saveModelTdocs(dir + File.separator + modelName + tdocsSuffix))
				return false;
		}
		if (tentities > 0) {
			if (!saveModelTEntities(dir + File.separator + modelName + tentitiesSuffix))
				return false;
		}
		return true;
	}

	// ---------------------------------------------------------------
	// Init Methods
	// ---------------------------------------------------------------
	/**
	 * initialize the model
	 */
	protected boolean init(ETMCmdOption option) {
		if (option == null) {
			return false;
		}
		modelName = option.modelName;
		LOG.info("modelName = {} ", modelName);
		KWords = option.KWords;
		LOG.info("Kwords = {} ", KWords);
		KEntities = option.KEntities;
		LOG.info("KEntities = {} ", KEntities);
		alpha = option.alpha;
		LOG.info("alpha = {} ", alpha);
		beta1 = option.beta1;
		LOG.info("beta1 = {} ", beta1);
		beta2 = option.beta2;
		LOG.info("beta2 = {} ", beta2);
		gamma = option.gamma;
		LOG.info("gamma = {} ", gamma);
		if (alpha < 0.0)
			alpha = 50.0 / KWords;
		LOG.info("alpha = {} ", alpha);
		if (gamma < 0.0)
			gamma = 50.0 / KEntities;
		LOG.info("gamma = {} ", gamma);
		if (option.beta1 >= 0)
			beta1 = option.beta1;
		LOG.info("beta1 = {} ", beta1);
		if (option.beta2 >= 0)
			beta2 = option.beta2;
		if (option.gamma >= 0)
			gamma = option.gamma;

		niters = option.niters;

		dir = option.dir;
		LOG.info("dir = {} ", dir);
		if (dir.endsWith(File.separator)) {
			dir = dir.substring(0, dir.length() - 1);
			LOG.info("dir = {} ", dir);
		}
		dfile = option.dfile;
		LOG.info("dfile = {} ", dfile);
		twords = option.twords;
		tdocs = option.tdocs;
		LOG.info("twords = {} ", twords);
		tentities = option.tentities;
		LOG.info("tentities = {} ", tentities);
		wordMapFile = option.wordMapFileName;
		LOG.info("wordMapFile = {} ", wordMapFile);
		entityMapFile = option.entityMapFileName;
		LOG.info("entityMapFile = {} ", entityMapFile);

		return true;
	}

	/**
	 * Init parameters for estimation
	 */
	public boolean initNewModel(ETMCmdOption option) {
		if (!init(option))
			return false;

		int m, n, w, e, k, ke;
		LOG.info("KWords = {} ", KWords);
		p = new double[KWords];
		pEntity = new double[KWords][KEntities];//

		data = new ETMDataset().readDataSet(dir + File.separator + dfile);
		if (data == null) {
			LOG.info("Fail to read training data!\n");
			return false;
		}

		// + allocate memory and assign values for variables
		M = data.M;
		VWords = data.VWords;
		VEntities = data.VEntities;
		dir = option.dir;
		savestep = option.savestep;

		// KWords: from command line or default value
		// alpha, beta: from command line or default values
		// niters, savestep: from command line or default values

		nw = new int[VWords][KWords];
		for (w = 0; w < VWords; w++) {
			for (k = 0; k < KWords; k++) {
				nw[w][k] = 0;
			}
		}

		ne = new int[VEntities][KEntities];
		for (w = 0; w < VEntities; w++) {
			for (k = 0; k < KEntities; k++) {
				ne[w][k] = 0;
			}
		}

		nd = new int[M][KWords];
		for (m = 0; m < M; m++) {
			for (k = 0; k < KWords; k++) {
				nd[m][k] = 0;
			}
		}

		nde = new int[M][KEntities];
		for (m = 0; m < M; m++) {
			for (k = 0; k < KEntities; k++) {
				nde[m][k] = 0;
			}
		}

		nwsum = new int[KWords];
		for (k = 0; k < KWords; k++) {
			nwsum[k] = 0;
		}

		nesum = new int[KEntities];
		for (k = 0; k < KEntities; k++) {
			nesum[k] = 0;
		}

		ndsum = new int[M];
		for (m = 0; m < M; m++) {
			ndsum[m] = 0;
		}

		ndesum = new int[M];
		for (m = 0; m < M; m++) {
			ndesum[m] = 0;
		}

		nz = new int[VEntities][KWords];
		for (e = 0; e < VEntities; e++) {
			for (k = 0; k < KWords; k++) {
				nz[e][k] = 0;
			}
		}
		ndz = new int[M][KWords];
		for (m = 0; m < M; m++) {
			for (k = 0; k < KWords; k++) {
				ndz[m][k] = 0;
			}
		}
		nzsum = new int[KWords];
		for (k = 0; k < KWords; k++) {
			nzsum[k] = 0;
		}

		nzz = new int[KWords][KEntities];
		for (k = 0; k < KWords; k++) {
			for (ke = 0; ke < KEntities; ke++) {
				nzz[k][ke] = 0;
			}
		}

		nzzsum = new int[KWords];
		for (k = 0; k < KWords; k++) {
			nzzsum[k] = 0;
		}

		z = new Vector[M];
		for (m = 0; m < data.M; m++) {
			int N = data.docs[m].words.length;
			z[m] = new Vector<Integer>();
			// initilize for z
			for (n = 0; n < N; n++) {
				int topic = (int) Math.floor(Math.random() * KWords);
				z[m].add(topic);

				// number of instances of word assigned to topic j
				nw[data.docs[m].words[n]][topic] += 1;
				// number of words in document i assigned to topic j
				nd[m][topic] += 1;
				// total number of words assigned to topic j
				nwsum[topic] += 1;
			}
			// total number of words in document i
			ndsum[m] = N;
		}

		x = new Vector[M];
		for (m = 0; m < data.M; m++) {
			int N = data.docs[m].entities.length;
			x[m] = new Vector<Integer>();
			for (n = 0; n < N; n++) {
				int xtopic = (int) Math.floor(Math.random() * KWords);
				x[m].add(xtopic);

				String[] s = ETMDataset.localDict.getEntity(data.docs[m].entities[n]).split("/");

				if (s[1].equals("EQU") || s[1].equals("TEC") || s[1].equals("PRO")) {
					// number of instances of word assigned to topic j
					// System.out.println(s[1] + ".............");
					// PrintConsole.PrintLog(s[1], ".............");
					nz[data.docs[m].entities[n]][xtopic] += 10;
					ndz[m][xtopic] += 10;
					nzsum[xtopic] += 10; // total number of entities assigned to
					// super topic j
				} else {

					nz[data.docs[m].entities[n]][xtopic] += 1;
					ndz[m][xtopic] += 1;
					nzsum[xtopic] += 1; // total number of entities assigned to
					// super topic j
				}
			}
			ndesum[m] = N;

		}

		zEntity = new Vector[M];
		for (m = 0; m < data.M; m++) {
			int N = data.docs[m].entities.length;
			zEntity[m] = new Vector<Integer>();
			// initilize for z
			for (n = 0; n < N; n++) {
				int etopic = (int) Math.floor(Math.random() * KEntities);
				zEntity[m].add(etopic);
				String[] s = ETMDataset.localDict.getEntity(data.docs[m].entities[n]).split("/");

				if (s[1].equals("EQU") || s[1].equals("TEC") || s[1].equals("PRO")) {
					// number of instances of word assigned to topic j
					// System.out.println(s[1] + ".............");
					// PrintConsole.PrintLog(s[1], ".............");
					ne[data.docs[m].entities[n]][etopic] += 10;
					// number of words in document i assigned to topic j
					nde[m][etopic] += 10;
					// total number of words assigned to topic j
					nesum[etopic] += 10;
					nzz[x[m].get(n)][etopic] += 10;
					nzzsum[x[m].get(n)] += 10;
				} else {
					// number of instances of entities assigned to etopic j
					ne[data.docs[m].entities[n]][etopic] += 1;
					// number of entities in document i assigned to etopic j
					nde[m][etopic] += 1;
					// total number of entities assigned to etopic j
					nesum[etopic] += 1;
					nzz[x[m].get(n)][etopic] += 1;
					nzzsum[x[m].get(n)] += 1;

				}

			}
			// total number of words in document i
			ndesum[m] = N;

		}

		theta = new double[M][KWords];
		kesai = new double[KWords][KEntities];
		phi = new double[KWords][VWords];
		phiEntity = new double[KEntities][VEntities];

		return true;
	}

	/**
	 * Init parameters for inference
	 * 
	 * @param newData
	 *            DataSet for which we do inference
	 */
	@SuppressWarnings("unchecked")
	public boolean initNewModel(ETMCmdOption option, ETMDataset newData, Model trnModel) {
		if (!init(option))
			return false;

		int m, n, w, e, k, ke;

		KWords = trnModel.KWords;
		KEntities = trnModel.KEntities;
		alpha = trnModel.alpha;
		beta1 = trnModel.beta1;
		beta2 = trnModel.beta2;
		gamma = trnModel.gamma;

		p = new double[KWords];
		pEntity = new double[KWords][KEntities];
		LOG.info("KWords = {} ", KWords);
		LOG.info("KEntities = {} ", KEntities);
		data = newData;
		// + allocate memory and assign values for variables
		M = data.M;
		VWords = data.VWords;
		dir = option.dir;
		savestep = option.savestep;
		VEntities = data.VEntities;
		LOG.info("M = {} ", M);
		LOG.info("VWords = {} ", VWords);
		LOG.info("VEntities = {} ", VEntities);

		// KWords: from command line or default value
		// alpha, beta: from command line or default values
		// niters, savestep: from command line or default values

		nw = new int[VWords][KWords];
		for (w = 0; w < VWords; w++) {
			for (k = 0; k < KWords; k++) {
				nw[w][k] = 0;
			}
		}

		ne = new int[VEntities][KEntities];
		for (w = 0; w < VEntities; w++) {
			for (k = 0; k < KEntities; k++) {
				ne[w][k] = 0;
			}
		}

		nd = new int[M][KWords];
		for (m = 0; m < M; m++) {
			for (k = 0; k < KWords; k++) {
				nd[m][k] = 0;
			}
		}

		nde = new int[M][KEntities];
		for (m = 0; m < M; m++) {
			for (k = 0; k < KEntities; k++) {
				nde[m][k] = 0;
			}
		}

		nwsum = new int[KWords];
		for (k = 0; k < KWords; k++) {
			nwsum[k] = 0;
		}

		nesum = new int[KEntities];
		for (k = 0; k < KEntities; k++) {
			nesum[k] = 0;
		}

		ndsum = new int[M];
		for (m = 0; m < M; m++) {
			ndsum[m] = 0;
		}

		ndesum = new int[M];
		for (m = 0; m < M; m++) {
			ndesum[m] = 0;
		}

		nz = new int[VEntities][KWords];
		for (e = 0; e < VEntities; e++) {
			for (k = 0; k < KWords; k++) {
				nz[e][k] = 0;
			}
		}
		ndz = new int[M][KWords];
		for (m = 0; m < M; m++) {
			for (k = 0; k < KWords; k++) {
				ndz[m][k] = 0;
			}
		}
		nzsum = new int[KWords];
		for (k = 0; k < KWords; k++) {
			nzsum[k] = 0;
		}

		nzz = new int[KWords][KEntities];
		for (k = 0; k < KWords; k++) {
			for (ke = 0; ke < KEntities; ke++) {
				nzz[k][ke] = 0;
			}
		}

		nzzsum = new int[KWords];
		for (k = 0; k < KWords; k++) {
			nzzsum[k] = 0;
		}

		z = new Vector[M];
		for (m = 0; m < data.M; m++) {
			int N = data.docs[m].words.length;
			z[m] = new Vector<Integer>();
			// initilize for z
			for (n = 0; n < N; n++) {
				int topic = (int) Math.floor(Math.random() * KWords);
				z[m].add(topic);

				// number of instances of word assigned to topic j
				nw[data.docs[m].words[n]][topic] += 1;
				// number of words in document i assigned to topic j
				nd[m][topic] += 1;
				// total number of words assigned to topic j
				nwsum[topic] += 1;
			}
			// total number of words in document i
			ndsum[m] = N;
		}

		x = new Vector[M];
		for (m = 0; m < data.M; m++) {
			int N = data.docs[m].entities.length;
			x[m] = new Vector<Integer>();
			for (n = 0; n < N; n++) {
				int xtopic = (int) Math.floor(Math.random() * KWords);
				x[m].add(xtopic);

				String[] s = ETMDataset.localDict.getEntity(data.docs[m].entities[n]).split("/");

				if (s[1].equals("EQU") || s[1].equals("TEC") || s[1].equals("PRO")) {
					// number of instances of word assigned to topic j
					// System.out.println(s[1] + ".............");
					nz[data.docs[m].entities[n]][xtopic] += 10;
					ndz[m][xtopic] += 10;
					nzsum[xtopic] += 10; // total number of entities assigned to
					// super topic j
				} else {

					nz[data.docs[m].entities[n]][xtopic] += 1;
					ndz[m][xtopic] += 1;
					nzsum[xtopic] += 1; // total number of entities assigned to
					// super topic j
				}
			}
			ndesum[m] = N;

		}

		zEntity = new Vector[M];
		for (m = 0; m < data.M; m++) {
			int N = data.docs[m].entities.length;
			zEntity[m] = new Vector<Integer>();
			// initilize for z
			for (n = 0; n < N; n++) {
				int etopic = (int) Math.floor(Math.random() * KEntities);
				zEntity[m].add(etopic);
				String[] s = ETMDataset.localDict.getEntity(data.docs[m].entities[n]).split("/");

				if (s[1].equals("EQU") || s[1].equals("TEC") || s[1].equals("PRO")) {
					// number of instances of word assigned to topic j
					// System.out.println(s[1] + ".............");
					ne[data.docs[m].entities[n]][etopic] += 10;
					// number of words in document i assigned to topic j
					nde[m][etopic] += 10;
					// total number of words assigned to topic j
					nesum[etopic] += 10;
					nzz[x[m].get(n)][etopic] += 10;
					nzzsum[x[m].get(n)] += 10;
				} else {
					// number of instances of entities assigned to etopic j
					ne[data.docs[m].entities[n]][etopic] += 1;
					// number of entities in document i assigned to etopic j
					nde[m][etopic] += 1;
					// total number of entities assigned to etopic j
					nesum[etopic] += 1;
					nzz[x[m].get(n)][etopic] += 1;
					nzzsum[x[m].get(n)] += 1;

				}

			}
			// total number of words in document i
			ndesum[m] = N;

		}

		theta = new double[M][KWords];
		kesai = new double[KWords][KEntities];
		phi = new double[KWords][VWords];
		phiEntity = new double[KEntities][VEntities];

		return true;
	}

	/**
	 * Init parameters for inference reading new dataset from file
	 */
	public boolean initNewModel(ETMCmdOption option, Model trnModel) {
		if (!init(option))
			return false;

		ETMDataset dataset = new ETMDataset().readDataSet(dir + File.separator + dfile, trnModel.data.localDict);
		if (dataset == null) {
			LOG.info("Fail to read dataset!");
			return false;
		}

		return initNewModel(option, dataset, trnModel);
	}

	/**
	 * init parameter for continue estimating or for later inference
	 */
	public boolean initEstimatedModel(ETMCmdOption option) {
		if (!init(option))
			return false;

		int m, n, w, e, k, ke;

		p = new double[KWords];
		pEntity = new double[KWords][KEntities];

		// load model, i.e., read z and trndata
		if (!loadModel()) {
			LOG.info("Fail to load word-topic and entity-etopic assignment file of the model!");
			return false;
		}

		LOG.info("Model loaded: alpha = {} , tbeta1 = {} , tbeta2 = {} , tgamma = {} , tM = {} , tV = {} , tE = {} ",
				alpha, beta1, beta2, gamma, M, VWords, VEntities);
		nw = new int[VWords][KWords];
		for (w = 0; w < VWords; w++) {
			for (k = 0; k < KWords; k++) {
				nw[w][k] = 0;
			}
		}

		ne = new int[VEntities][KEntities];
		for (e = 0; e < VEntities; e++) {
			for (k = 0; k < KEntities; k++) {
				ne[e][k] = 0;
			}
		}
		nz = new int[VEntities][KWords];
		for (e = 0; e < VEntities; e++) {
			for (k = 0; k < KWords; k++) {
				nz[e][k] = 0;
			}
		}
		nd = new int[M][KWords];
		for (m = 0; m < M; m++) {
			for (k = 0; k < KWords; k++) {
				nd[m][k] = 0;
			}
		}
		nde = new int[M][KEntities];
		for (m = 0; m < M; m++) {
			for (k = 0; k < KEntities; k++) {
				nde[m][k] = 0;
			}
		}
		ndz = new int[M][KWords];
		for (m = 0; m < M; m++) {
			for (k = 0; k < KWords; k++) {
				ndz[m][k] = 0;
			}
		}
		nwsum = new int[KWords];
		for (k = 0; k < KWords; k++) {
			nwsum[k] = 0;
		}
		nesum = new int[KEntities];
		for (k = 0; k < KEntities; k++) {
			nesum[k] = 0;
		}
		nzsum = new int[KWords];
		for (k = 0; k < KWords; k++) {
			nzsum[k] = 0;
		}
		ndsum = new int[M];
		for (m = 0; m < M; m++) {
			ndsum[m] = 0;
		}
		ndesum = new int[M];
		for (m = 0; m < M; m++) {
			ndesum[m] = 0;
		}
		nzz = new int[KWords][KEntities];
		for (k = 0; k < KWords; k++) {
			for (ke = 0; ke < KEntities; ke++) {
				nzz[k][ke] = 0;
			}
		}
		nzzsum = new int[KWords];
		for (k = 0; k < KWords; k++) {
			nzzsum[k] = 0;
		}

		for (m = 0; m < data.M; m++) {
			int N = data.docs[m].words.length;
			int NE = data.docs[m].entities.length;
			// assign values for nw, nd, nwsum, and ndsum
			for (n = 0; n < N; n++) {
				w = data.docs[m].words[n];

				int topic = (Integer) z[m].get(n);

				// number of instances of word i assigned to topic j
				nw[w][topic] += 1;
				// number of words in document i assigned to topic j
				nd[m][topic] += 1;
				// total number of words assigned to topic j
				nwsum[topic] += 1;
			}
			// total number of words in document i
			ndsum[m] = N;
			for (n = 0; n < NE; n++) {
				e = data.docs[m].entities[n];
				int etopic = (Integer) zEntity[m].get(n);

				// Q
				int xtopic = (Integer) x[m].get(n);

				String[] s = ETMDataset.localDict.getEntity(data.docs[m].entities[n]).split("/");

				if (s[1].equals("EQU") || s[1].equals("TEC") || s[1].equals("PRO")) {
					nz[e][xtopic] += 10;
					ndz[m][xtopic] += 10;
					nzsum[xtopic] += 10;
					nzz[xtopic][etopic] += 10;
					nzzsum[xtopic] += 10;

					// number of instances of word i assigned to topic j
					ne[e][etopic] += 10;
					// number of words in document i assigned to topic j
					nde[m][etopic] += 10;
					// total number of words assigned to topic j
					nesum[etopic] += 10;
				} else {
					nz[e][xtopic] += 1;
					ndz[m][xtopic] += 1;
					nzsum[xtopic] += 1;
					nzz[xtopic][etopic] += 1;
					nzzsum[xtopic] += 1;

					// number of instances of word i assigned to topic j
					ne[e][etopic] += 1;
					// number of words in document i assigned to topic j
					nde[m][etopic] += 1;
					// total number of words assigned to topic j
					nesum[etopic] += 1;
				}

			}
			// total number of words in document i

		}
		theta = new double[M][KWords];
		kesai = new double[KWords][KEntities];
		phi = new double[KWords][VWords];
		phiEntity = new double[KEntities][VEntities];
		dir = option.dir;
		savestep = option.savestep;
		return true;
	}

	public boolean saveModelTdocs(String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filename), ConstantUtil.UTF8));

			if (tdocs > M) {
				tdocs = M;
			}

			for (int k = 0; k < KWords; k++) {
				List<Pair> docsProbsList = new ArrayList<Pair>();
				for (int d = 0; d < M; d++) {
					Pair p = new Pair(d, theta[d][k], false);

					docsProbsList.add(p);
				} // end foreach word

				// print topic
				// writer.write("Topic " + k + "th:\n");
				writer.write("Topic " + k + " size:" + docsProbsList.size() + "\n");
				Collections.sort(docsProbsList);
				/*
				 * //显示前 N 条记录 for (int i = 0; i < tdocs; i++) {
				 * 
				 * writer.write("\t" + ETMDataset.documents[(Integer)
				 * docsProbsList .get(i).first] + "\n"); }
				 */
				// 将话题下的所有记录的分词结果写到 tdoc 文件中
				for (int i = 0; i < docsProbsList.size(); i++) {
					writer.write(ETMDataset.documents[(Integer) docsProbsList.get(i).first] + "\n");
				}
				writer.write("#####" + "\n");
			} // end foreach topic
			writer.close();
		} catch (Exception e) {
			System.out.println("Error while saving model tdocs: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
