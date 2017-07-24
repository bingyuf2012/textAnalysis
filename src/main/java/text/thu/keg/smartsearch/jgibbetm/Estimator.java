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

import java.io.File;

import text.searchSDK.util.PrintConsole;

public class Estimator {
	protected Model trnModel;
	ETMCmdOption option;

	public boolean init(ETMCmdOption option) {
		this.option = option;
		trnModel = new Model();

		if (option.est) {
			if (!trnModel.initNewModel(option))
				return false;
			trnModel.data.localDict.writeWordMap(option.dir + File.separator + option.wordMapFileName,
					option.dir + File.separator + option.entityMapFileName);
		} else if (option.estc) {
			if (!trnModel.initEstimatedModel(option))
				return false;
		}

		return true;
	}

	public Model estimate() {
		PrintConsole.PrintLog("Sampling ", trnModel.niters, " iteration!");

		int lastIter = trnModel.liter;
		for (trnModel.liter = lastIter + 1; trnModel.liter < trnModel.niters + lastIter; trnModel.liter++) {
			// for all z_i
			for (int m = 0; m < trnModel.M; m++) {
				for (int n = 0; n < trnModel.data.docs[m].words.length; n++) {
					// z_i = z[m][n]
					// sample from p(z_i|z_-i, w)
					int topic = samplingWords(m, n);

					trnModel.z[m].set(n, topic);
				} // end for each word
			} // end for each document

			// for all zEntity_i
			int[] a = new int[2];
			for (int m = 0; m < trnModel.M; m++) {
				for (int n = 0; n < trnModel.data.docs[m].entities.length; n++) {
					// zEntity_i = zEntity[m][n]
					// sample from p(zEntity_i|zEntity_-i, w)

					samplingEntities(m, n, a);
					int xtopic = a[0];
					int etopic = a[1];

					trnModel.zEntity[m].set(n, etopic);
					trnModel.x[m].set(n, xtopic);
				} // end for each word
			} // end for each document

			if (option.savestep > 0) {
				if (trnModel.liter % option.savestep == 0) {
					PrintConsole.PrintLog("Saving the model at iteration ", trnModel.liter, " ...");
					computeTheta();
					computePhi();
					computeKesai();
					computePhiEntity();
					trnModel.saveModel("model-" + Conversion.ZeroPad(trnModel.liter, 5));
				}
			}
		} // end iterations
		PrintConsole.PrintLog("Iteration end ............", "");
		PrintConsole.PrintLog("Gibbs sampling completed!\n", "");
		PrintConsole.PrintLog("Saving the final model!\n", "");
		computeTheta();
		computePhi();
		computeKesai();
		computePhiEntity();
		trnModel.liter--;
		trnModel.saveModel("model-final");
		return trnModel;
	}

	/**
	 * Do sampling
	 * 
	 * @param m
	 *            document number
	 * @param n
	 *            word number
	 * @return topic id
	 */
	public int samplingWords(int m, int n) {
		// remove z_i from the count variable
		int topic = trnModel.z[m].get(n);
		int w = trnModel.data.docs[m].words[n];

		trnModel.nw[w][topic] -= 1;
		trnModel.nd[m][topic] -= 1;
		trnModel.nwsum[topic] -= 1;
		trnModel.ndsum[m] -= 1;

		double Vbeta1 = trnModel.VWords * trnModel.beta1;
		double Kalpha = trnModel.KWords * trnModel.alpha;

		// do multinominal sampling via cumulative method
		for (int k = 0; k < trnModel.KWords; k++) {
			trnModel.p[k] = (trnModel.nw[w][k] + trnModel.beta1) / (trnModel.nwsum[k] + Vbeta1)
					* (trnModel.nd[m][k] + trnModel.alpha) / (trnModel.ndsum[m] + Kalpha);
		}

		// cumulate multinomial parameters
		for (int k = 1; k < trnModel.KWords; k++) {
			trnModel.p[k] += trnModel.p[k - 1];
		}
		// scaled sample because of unnormalized p[]
		double u = Math.random() * trnModel.p[trnModel.KWords - 1];

		for (topic = 0; topic < trnModel.KWords; topic++) {
			if (trnModel.p[topic] > u) // sample topic w.r.t distribution p
				break;
		}

		// add newly estimated z_i to count variables
		trnModel.nw[w][topic] += 1;
		trnModel.nd[m][topic] += 1;
		trnModel.nwsum[topic] += 1;
		trnModel.ndsum[m] += 1;

		return topic;
	}

	public void samplingEntities(int m, int n, int[] a) {
		// remove z_i from the count variable
		int etopic = trnModel.zEntity[m].get(n);
		int xtopic = trnModel.x[m].get(n);
		int e = trnModel.data.docs[m].entities[n]; // entities
		String[] s = ETMDataset.localDict.getEntity(e).split("/");

		/*if (s[1].equals("EQU") || s[1].equals("TEC") || s[1].equals("PRO")) {
			trnModel.ne[e][etopic] -= 10;
			trnModel.nde[m][etopic] -= 10;
			trnModel.nesum[etopic] -= 10;
			trnModel.ndesum[m] -= 10;
			trnModel.nz[e][xtopic] -= 10;
			trnModel.ndz[m][xtopic] -= 10;
			trnModel.nzsum[xtopic] -= 10;
			trnModel.nzz[xtopic][etopic] -= 10;
			trnModel.nzzsum[xtopic] -= 10;
		} else {*/
		trnModel.ne[e][etopic] -= 1;
		trnModel.nde[m][etopic] -= 1;
		trnModel.nesum[etopic] -= 1;
		trnModel.ndesum[m] -= 1;
		trnModel.nz[e][xtopic] -= 1;
		trnModel.ndz[m][xtopic] -= 1;
		trnModel.nzsum[xtopic] -= 1;
		trnModel.nzz[xtopic][etopic] -= 1;
		trnModel.nzzsum[xtopic] -= 1;

		/*}*/

		double Vbeta2 = trnModel.VEntities * trnModel.beta2;

		// do multinominal sampling via cumulative method
		for (int kw = 0; kw < trnModel.KWords; kw++) {
			for (int k = 0; k < trnModel.KEntities; k++) {
				trnModel.pEntity[kw][k] = ((trnModel.nd[m][kw] * 1.0 + 1) / (trnModel.ndsum[m] + trnModel.KWords))
						* (trnModel.ne[e][k] + trnModel.beta2) / (trnModel.nesum[k] + Vbeta2) * // ///to
																								// be
																								// changed..............
						(trnModel.nzz[kw][k] + trnModel.gamma)
						/ (trnModel.nzzsum[kw] + trnModel.KEntities * trnModel.gamma);
			}
		}

		// cumulate multinomial parameters
		for (int kw = 0; kw < trnModel.KWords; kw++) {
			if (kw > 0) {
				trnModel.pEntity[kw][0] += trnModel.pEntity[kw - 1][trnModel.KEntities - 1];
			}
			for (int k = 1; k < trnModel.KEntities; k++) {
				trnModel.pEntity[kw][k] += trnModel.pEntity[kw][k - 1];
			}
		}
		// scaled sample because of unnormalized p[]
		double u = Math.random() * trnModel.pEntity[trnModel.KWords - 1][trnModel.KEntities - 1];
		int flag = 1;
		for (xtopic = 0; xtopic < trnModel.KWords && flag != 0; xtopic++) {
			for (etopic = 0; etopic < trnModel.KEntities; etopic++) {

				if (trnModel.pEntity[xtopic][etopic] > u) {
					// sample topic w.r.t distribution p
					flag = 0;
					break;
					// goto mark;
				}
			}
		}
		xtopic--;
		String[] str = ETMDataset.localDict.getEntity(e).split("/");

		/*if (str[1].equals("EQU") || str[1].equals("TEC") || str[1].equals("PRO")) {
			// add newly estimated z_i to count variables
			trnModel.ne[e][etopic] += 10;
			trnModel.nde[m][etopic] += 10;
			trnModel.nesum[etopic] += 10;
			trnModel.ndesum[m] += 10;
			trnModel.nz[e][xtopic] += 10;
			trnModel.nzsum[xtopic] += 10;
			trnModel.ndz[m][xtopic] += 10;
			trnModel.nzz[xtopic][etopic] += 10;
			trnModel.nzzsum[xtopic] += 10;
		} else {*/
		// add newly estimated z_i to count variables
		trnModel.ne[e][etopic] += 1;
		trnModel.nde[m][etopic] += 1;
		trnModel.nesum[etopic] += 1;
		trnModel.ndesum[m] += 1;
		trnModel.nz[e][xtopic] += 1;
		trnModel.nzsum[xtopic] += 1;
		trnModel.ndz[m][xtopic] += 1;
		trnModel.nzz[xtopic][etopic] += 1;
		trnModel.nzzsum[xtopic] += 1;
		/*}*/

		a[0] = xtopic;
		a[1] = etopic;
	}

	public void computeTheta() {
		for (int m = 0; m < trnModel.M; m++) {
			for (int k = 0; k < trnModel.KWords; k++) {
				trnModel.theta[m][k] = (trnModel.nd[m][k] + trnModel.alpha)
						/ (trnModel.ndsum[m] + trnModel.KWords * trnModel.alpha);
			}
		}
	}

	public void computePhi() {
		for (int k = 0; k < trnModel.KWords; k++) {
			for (int w = 0; w < trnModel.VWords; w++) {
				trnModel.phi[k][w] = (trnModel.nw[w][k] + trnModel.beta1)
						/ (trnModel.nwsum[k] + trnModel.VWords * trnModel.beta1);
			}
		}
	}

	public void computeKesai() {
		for (int k1 = 0; k1 < trnModel.KWords; k1++) {
			for (int k2 = 0; k2 < trnModel.KEntities; k2++) {
				trnModel.kesai[k1][k2] = (trnModel.nzz[k1][k2] + trnModel.gamma)
						/ (trnModel.nzzsum[k1] + trnModel.KEntities * trnModel.gamma);
			}
		}
	}

	public void computePhiEntity() {
		for (int k = 0; k < trnModel.KEntities; k++) {
			for (int e = 0; e < trnModel.VEntities; e++) {
				trnModel.phiEntity[k][e] = (trnModel.ne[e][k] + trnModel.beta2)
						/ (trnModel.nesum[k] + trnModel.VEntities * trnModel.beta2);
				// if(k == 0 && e == 0){
				// PrintConsole.PrintLog("trnModel.beta2", trnModel.beta2
				// ,"trnModel.ne[e][k]",trnModel.ne[e][k],"");
				// PrintConsole.PrintLog("trnModel.nesum[k]", trnModel.nesum[k]
				// ,"trnModel.VEntities",trnModel.VEntities,"");
				// PrintConsole.PrintLog("trnModel.beta2", trnModel.beta2
				// ,"trnModel.phiEntity[k][e]",trnModel.phiEntity[k][e],"");
				// }
			}
		}
	}

}
