package text.thu.keg.smartsearch.jgibbetm;

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

import java.util.Arrays;
import java.util.Vector;

public class Document {

	// ----------------------------------------------------
	// Instance Variables
	// ----------------------------------------------------
	public int[] totalwords;
	public int[] words;// ���ظ���
	public int[] entities;// ���ظ�ʵ��
	public String rawStr;
	public int length;

	// ----------------------------------------------------
	// Constructors
	// ----------------------------------------------------
	public Document() {
		totalwords = null;
		words = null;
		entities = null;
		rawStr = "";
		length = 0;
	}

	public Document(int length, int wl, int el) {
		this.length = length;
		rawStr = "";
		totalwords = new int[length];
		words = new int[wl];
		entities = new int[el];
	}

	public Document(int length, int wl, int el, int[] totalwords, int[] words,
			int[] entities) {
		this.length = length;
		rawStr = "";

		this.totalwords = new int[length];
		this.words = new int[wl];
		this.entities = new int[el];
		for (int i = 0; i < length; ++i) {
			this.totalwords[i] = totalwords[i];
		}
		for (int i = 0; i < wl; ++i) {
			this.words[i] = words[i];
		}
		for (int i = 0; i < el; ++i) {
			this.entities[i] = entities[i];
		}

	}

	public Document(int length, int wl, int el, int[] totalwords, int[] words,
			int[] entities, String rawStr) {
		this.length = length;

		this.totalwords = new int[length];
		this.words = new int[wl];
		this.entities = new int[el];
		for (int i = 0; i < length; ++i) {
			this.totalwords[i] = totalwords[i];
		}
		for (int i = 0; i < wl; ++i) {
			this.words[i] = words[i];
		}
		for (int i = 0; i < el; ++i) {
			this.entities[i] = entities[i];
		}
		this.rawStr = rawStr;
	}

	public Document(Vector<Integer> words, Vector<Integer> entities) {
		this.length = words.size() + entities.size();
		rawStr = "";
		this.totalwords = new int[length];
		this.words = new int[words.size()];
		this.entities = new int[entities.size()];
		this.totalwords = new int[entities.size()];

		for (int i = 0; i < words.size(); ++i) {
			this.words[i] = words.get(i);
		}
		for (int i = 0; i < entities.size(); ++i) {
			this.entities[i] = entities.get(i);
		}

	}

	public Document(Vector<Integer> words, Vector<Integer> entities,
			String rawStr) {
		this.length = words.size() + entities.size();

		this.totalwords = new int[entities.size()];
		this.rawStr = rawStr;
		this.words = new int[words.size()];
		this.entities = new int[entities.size()];

		for (int i = 0; i < words.size(); ++i) {
			this.words[i] = words.get(i);
		}
		for (int i = 0; i < entities.size(); ++i) {
			this.entities[i] = entities.get(i);
		}
	}

	@Override
	public String toString() {
		return "Document [entities=" + Arrays.toString(entities) + "]";
	}
}
