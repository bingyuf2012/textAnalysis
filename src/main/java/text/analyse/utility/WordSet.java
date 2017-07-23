package text.analyse.utility;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class WordSet {

	public HashMap<Object, Word> wordTable = new HashMap<Object, Word>();

	public void addWord(Word word) {
		if (wordTable == null) {
			wordTable = new HashMap<Object, Word>();
		} else {
			if (this.wordTable.get(word.id) == null) {
				this.wordTable.put(word.id, word);
			}
		}
	}

	public void removeWord(Word word) {
		if (wordTable != null) {
			wordTable.remove(word.id);
		}
	}

	public HashMap<Object, Word> getTokenHashtable() {
		return wordTable;
	}

	public Word getWord(int id) {
		if (wordTable == null) {
			return null;
		} else {
			return (Word) wordTable.get(id);
		}
	}

	public Word getWord(String key, String type) {
		if (wordTable == null) {
			return null;
		} else {
			boolean flag = false;
			Word w = null;
			Iterator<Entry<Object, Word>> ite = wordTable.entrySet().iterator();
			while (ite.hasNext()) {
				Entry<Object, Word> entry = (Entry<Object, Word>) ite.next();
				w = (Word) entry.getValue();
				if (w.key.equals(key.trim()) && w.type.equals(type.trim())) {
					flag = true;
					break;
				}
			}
			if (flag) {
				return w;
			} else {
				return null;
			}
		}
	}

}
