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
 * JGibbsLDA is a free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * JGibbsLDA is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JGibbsLDA; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 */
package text.thu.keg.smartsearch.jgibbetm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dictionary {
	private Logger LOG = LoggerFactory.getLogger(Dictionary.class);

	public class WordDic {
		public Map<String, Integer> word2id;
		public Map<Integer, String> id2word;

		public WordDic() {
			word2id = new HashMap<String, Integer>();
			id2word = new HashMap<Integer, String>();
		}

	}

	public class EntityDic {
		public Map<String, Integer> entity2id;
		public Map<Integer, String> id2entity;

		public EntityDic() {
			entity2id = new HashMap<String, Integer>();
			id2entity = new HashMap<Integer, String>();
		}
	}

	WordDic wordDic;
	EntityDic entityDic;

	// --------------------------------------------------
	// constructors
	// --------------------------------------------------

	public Dictionary() {
		wordDic = new WordDic();
		entityDic = new EntityDic();
	}

	// ---------------------------------------------------
	// get/set methods
	// ---------------------------------------------------

	public WordDic getWordDic() {
		return wordDic;
	}

	public void setWordDic(WordDic wordDic) {
		this.wordDic = wordDic;
	}

	public EntityDic getEntityDic() {
		return entityDic;
	}

	public void setEntityDic(EntityDic entityDic) {
		this.entityDic = entityDic;
	}

	public String getWord(int id) {
		return wordDic.id2word.get(id);
	}

	public String getEntity(int id) {
		return entityDic.id2entity.get(id);
	}

	public Integer getIDW(String word) {
		return wordDic.word2id.get(word);
	}

	public Integer getIDE(String entity) {
		return entityDic.entity2id.get(entity);
	}

	// ----------------------------------------------------
	// checking methods
	// ----------------------------------------------------
	/**
	 * check if this dictionary contains a specified word
	 */
	public boolean containsWord(String word) {
		return wordDic.word2id.containsKey(word);
	}

	public boolean containsWord(int id) {
		return wordDic.id2word.containsKey(id);
	}

	public boolean containsEntity(String entity) {
		return entityDic.entity2id.containsKey(entity);
	}

	public boolean containsEntity(int id) {
		return entityDic.id2entity.containsKey(id);
	}

	// ---------------------------------------------------
	// manupulating methods
	// ---------------------------------------------------
	/**
	 * add a word into this dictionary return the corresponding id
	 */
	public int addWord(String word) {
		if (!containsWord(word)) {
			int id = wordDic.word2id.size();

			wordDic.word2id.put(word, id);
			wordDic.id2word.put(id, word);

			return id;
		} else
			return getIDW(word);
	}

	public int addEntity(String entity) {
		if (!containsEntity(entity)) {
			int id = entityDic.entity2id.size();

			entityDic.entity2id.put(entity, id);
			entityDic.id2entity.put(id, entity);

			return id;
		} else
			return getIDE(entity);
	}

	// ---------------------------------------------------
	// I/O methods
	// ---------------------------------------------------
	/**
	 * read dictionary from file
	 */
	public boolean readWordEntityMap() {
		Iterator<Entry<String, Integer>> wordsIterator = wordDic.word2id.entrySet().iterator();

		while (wordsIterator.hasNext()) {
			Entry<String, Integer> itemWordEntry = wordsIterator.next();

			wordDic.id2word.put(itemWordEntry.getValue(), itemWordEntry.getKey());
			wordDic.word2id.put(itemWordEntry.getKey(), itemWordEntry.getValue());
		}

		Iterator<Entry<String, Integer>> entitysIterator = entityDic.entity2id.entrySet().iterator();

		while (entitysIterator.hasNext()) {
			Entry<String, Integer> itemEntityEntry = entitysIterator.next();

			entityDic.id2entity.put(itemEntityEntry.getValue(), itemEntityEntry.getKey());
			entityDic.entity2id.put(itemEntityEntry.getKey(), itemEntityEntry.getValue());
		}

		return true;
	}

	/*public boolean readWordEntityMap(String wordMapFile, String entityMapFile) {
		try {
			BufferedReader readerw = new BufferedReader(
					new InputStreamReader(new FileInputStream(wordMapFile), ConstantUtil.UTF8));
			BufferedReader readere = new BufferedReader(
					new InputStreamReader(new FileInputStream(entityMapFile), ConstantUtil.UTF8));
			String linew;// word
			String linee;// entity
			// read the number of words
			linew = readerw.readLine();
			linee = readere.readLine();
			int nwords = Integer.parseInt(linew);
			int nentities = Integer.parseInt(linee);
			// read map
			for (int i = 0; i < nwords; ++i) {
				linew = readerw.readLine();
				StringTokenizer tknr = new StringTokenizer(linew, " \t\n\r");
	
				if (tknr.countTokens() != 2)
					continue;
	
				String word = tknr.nextToken();
				String id = tknr.nextToken();
				int intID = Integer.parseInt(id);
	
				wordDic.id2word.put(intID, word);
				wordDic.word2id.put(word, intID);
			}
			for (int i = 0; i < nentities; ++i) {
				linee = readere.readLine();
				StringTokenizer tknr = new StringTokenizer(linee, " \t\n\r");
	
				if (tknr.countTokens() != 2)
					continue;
	
				String entity = tknr.nextToken();
				String id = tknr.nextToken();
				int intID = Integer.parseInt(id);
	
				entityDic.id2entity.put(intID, entity);
				entityDic.entity2id.put(entity, intID);
			}
	
			readerw.close();
			readere.close();
			return true;
		} catch (Exception e) {
			LOG.error("Error while writing map ,error msg = {} ", ExceptionUtils.getFullStackTrace(e));
			return false;
		}
	}
	
	public boolean writeWordMap(String wordMapFile, String entityMapFile) {
		try {
			BufferedWriter writerw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(wordMapFile), ConstantUtil.UTF8));
			BufferedWriter writere = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(entityMapFile), ConstantUtil.UTF8));
	
			Iterator<String> itw = wordDic.word2id.keySet().iterator();
			Iterator<String> ite = entityDic.entity2id.keySet().iterator();
			while (itw.hasNext()) {
				String key = itw.next();
				Integer value = wordDic.word2id.get(key);
	
				writerw.write(key + " " + value + "\r\n");
			}
			while (ite.hasNext()) {
				String key = ite.next();
				Integer value = entityDic.entity2id.get(key);
	
				writere.write(key + " " + value + "\r\n");
			}
	
			writerw.close();
			writere.close();
			return true;
		} catch (Exception e) {
			LOG.error("Error while writing map ,error msg = {} ", ExceptionUtils.getFullStackTrace(e));
			return false;
		}
	}*/
}