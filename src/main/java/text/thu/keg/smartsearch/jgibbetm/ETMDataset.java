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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import text.analysis.utils.ConstantUtil;

public class ETMDataset {
	private Logger LOG = LoggerFactory.getLogger(ETMDataset.class);

	public static int WORDSNUM = 0;
	public static int ENTITESNUM = 0;
	// ---------------------------------------------------------------
	// Instance Variables
	// ---------------------------------------------------------------
	public static Dictionary localDict; // local dictionary

	public Document[] docs; // a list of documents
	public static String[] documents;
	public int M; // number of documents
	public int VWords; // number of words
	public int VEntities; // number of entities

	// map from local coordinates (id) to global ones
	// null if the global dictionary is not set
	public Map<Integer, Integer> lid2gidW;
	public Map<Integer, Integer> lid2gidE;
	// link to a global dictionary (optional), null for train data, not null for
	// test data
	public Dictionary globalDict;

	// --------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------
	public ETMDataset() {
		localDict = new Dictionary();

		M = 0;
		VWords = 0;
		VEntities = 0;
		docs = null;

		globalDict = null;

		lid2gidW = null;
		lid2gidE = null;
	}

	public ETMDataset(int M) {
		this.M = M;
		localDict = new Dictionary();

		VWords = 0;
		VEntities = 0;
		docs = new Document[M];

		globalDict = null;

		lid2gidW = null;
		lid2gidE = null;
	}

	public ETMDataset(int M, Dictionary globalDict) {
		this.M = M;
		localDict = new Dictionary();

		VWords = 0;
		VEntities = 0;
		docs = new Document[M];

		this.globalDict = globalDict;

		lid2gidW = new HashMap<Integer, Integer>();
		lid2gidE = new HashMap<Integer, Integer>();
	}

	// -------------------------------------------------------------
	// Public Instance Methods
	// -------------------------------------------------------------
	/**
	 * set the document at the index idx if idx is greater than 0 and less than
	 * M
	 * 
	 * @param doc
	 *            document to be set
	 * @param idx
	 *            index in the document array
	 */
	public void setDoc(Document doc, int idx) {
		if (0 <= idx && idx < M) {
			docs[idx] = doc;
		}
	}

	/**
	 * set the document at the index idx if idx is greater than 0 and less than
	 * M
	 * 
	 * @param str
	 *            string contains doc
	 * @param idx
	 *            index in the document array
	 */
	public void setDoc(String str, int idx) throws Exception {
		if (0 <= idx && idx < M) {
			// System.out.println("line :" + idx + " M : " + M);
			String[] totalwords = str.trim().split(" ");

			Pattern pe = Pattern.compile("/[A-Z]+");
			// Pattern pw = Pattern.compile("/[a-z]+");
			Vector<String> words = new Vector<String>();
			Vector<String> entities = new Vector<String>();
			for (int i = 0, j = 0, k = 0; i < totalwords.length; i++) {
				Matcher matchere = pe.matcher(totalwords[i]);
				// Matcher matcherw=pw.matcher(totalwords[i]);
				if (matchere.find()) {
					// System.out.println("entity");
					/*String tempe = totalwords[i];*/
					// String [] tempe = totalwords[i].split("/");
					entities.add(j, totalwords[i]);
					// System.out.println(j);
					// System.out.println(tempe[0]);
					j++;
				} else {
					/*String[] tempw = totalwords[i].split("/");
					words.add(k, tempw[0]);*/
					
					words.add(k, totalwords[i]);
					
					k++;
				}

			}
			// System.out.println("words : " + words.size());
			// System.out.println("Entites : " + entities.size());
			WORDSNUM += words.size();
			ENTITESNUM += entities.size();

			Vector<Integer> idsWord = new Vector<Integer>();
			Vector<Integer> idsEntity = new Vector<Integer>();
			for (String word : words) {
				// �Ѵʶ�Ӧ������������
				int _id = localDict.wordDic.word2id.size();// �õ������size

				if (localDict.containsWord(word)) {

					_id = localDict.getIDW(word);// ����Ѿ����ڣ��õ����

				}
				if (globalDict != null) {
					// get the global id
					Integer id = globalDict.getIDW(word);
					// System.out.println(id);

					if (id != null) {
						localDict.addWord(word);

						lid2gidW.put(_id, id);
						idsWord.add(_id);
					} else { // not in global dictionary
						// do nothing currently
					}
				} else {
					localDict.addWord(word);
					idsWord.add(_id);
				}

			}

			// for entity
			for (String entity : entities) {
				int _id = localDict.entityDic.entity2id.size();

				if (localDict.containsEntity(entity)) {

					_id = localDict.getIDE(entity);
					// System.out.println("*****Already contain Entity"+_id);

				}
				// System.out.println("*****add Entity"+_id);
				if (globalDict != null) {
					// get the global id
					Integer id = globalDict.getIDE(entity);
					// System.out.println(id);

					if (id != null) {
						localDict.addEntity(entity);

						lid2gidE.put(_id, id);
						idsEntity.add(_id);
					} else { // not in global dictionary
						// do nothing currently
					}
				} else {
					localDict.addEntity(entity);
					idsEntity.add(_id);
				}

				// System.out.println("*****Entity"+_id);

			}
			Document doc = new Document(idsWord, idsEntity, str);
			docs[idx] = doc;
			VWords = localDict.wordDic.word2id.size();
			VEntities = localDict.entityDic.entity2id.size();
			// System.out.println(doc);

		}

	}

	// ---------------------------------------------------------------
	// I/O methods
	// ---------------------------------------------------------------

	/**
	 * read a dataset from a stream, create new dictionary
	 * 
	 * @return dataset if success and null otherwise
	 */
	public ETMDataset readDataSet(String filename) {
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(filename), ConstantUtil.UTF8));

			LOG.info(filename, "");
			ETMDataset data = readDataSet(reader);

			reader.close();
			return data;
		} catch (Exception e) {
			System.out.println("Read Dataset Error: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * read a dataset from a file with a preknown vocabulary
	 * 
	 * @param filename
	 *            file from which we read dataset
	 * @param dict
	 *            the dictionary
	 * @return dataset if success and null otherwise
	 */
	public ETMDataset readDataSet(String filename, Dictionary dict) {
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(filename), ConstantUtil.UTF8));
			ETMDataset data = readDataSet(reader, dict);

			reader.close();
			return data;
		} catch (Exception e) {
			System.out.println("Read Dataset Error: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * read a dataset from a stream, create new dictionary
	 * 
	 * @return dataset if success and null otherwise
	 */
	public ETMDataset readDataSet(BufferedReader reader) {
		try {
			// read number of document
			String line;
			line = reader.readLine();
			int M = Integer.parseInt(line);
			documents = new String[M];

			ETMDataset data = new ETMDataset(M);
			for (int i = 0; i < M; ++i) {
				line = reader.readLine();
				data.setDoc(line, i);
				documents[i] = line;
			}
			LOG.info("words = {} , entites = {} . ", WORDSNUM, ENTITESNUM);
			return data;
		} catch (Exception e) {
			System.out.println("Read Dataset Error: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * read a dataset from a stream with respect to a specified dictionary
	 * 
	 * @param reader
	 *            stream from which we read dataset
	 * @param dict
	 *            the dictionary
	 * @return dataset if success and null otherwise
	 */
	public ETMDataset readDataSet(BufferedReader reader, Dictionary dict) {
		try {
			// read number of document
			String line;
			line = reader.readLine();
			int M = Integer.parseInt(line);
			LOG.info("NewM = {} ", M);

			ETMDataset data = new ETMDataset(M, dict);
			for (int i = 0; i < M; ++i) {
				line = reader.readLine();

				data.setDoc(line, i);
			}

			return data;
		} catch (Exception e) {
			System.out.println("Read Dataset Error: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * read a dataset from a string, create new dictionary
	 * 
	 * @param str
	 *            String from which we get the dataset, documents are seperated
	 *            by newline character
	 * @return dataset if success and null otherwise
	 */
	public static ETMDataset readDataSet(String[] strs) throws Exception {
		ETMDataset data = new ETMDataset(strs.length);

		for (int i = 0; i < strs.length; ++i) {
			data.setDoc(strs[i], i);
		}
		return data;
	}

	/**
	 * read a dataset from a string with respect to a specified dictionary
	 * 
	 * @param str
	 *            String from which we get the dataset, documents are seperated
	 *            by newline character
	 * @param dict
	 *            the dictionary
	 * @return dataset if success and null otherwise
	 */
	public ETMDataset readDataSet(String[] strs, Dictionary dict) throws Exception {
		LOG.info("readDataset...");
		ETMDataset data = new ETMDataset(strs.length, dict);

		for (int i = 0; i < strs.length; ++i) {
			LOG.info("set doc = {}", i);
			data.setDoc(strs[i], i);
		}
		return data;
	}
}
