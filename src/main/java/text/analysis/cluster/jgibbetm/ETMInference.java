package text.analysis.cluster.jgibbetm;

/**
 * @Copyright© 2017 doumi jz. All Rights Reserved. DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * @author yangruibing
 * @date 2017年7月17日 下午5:22:13
 */

public class ETMInference {
	public Model trnModel;
	public Dictionary globalDict;

	private ETMCmdOption option;

	private Model newModel;
	public int niters = 1000;

	public boolean init(ETMCmdOption option) {
		this.option = option;
		trnModel = new Model();

		if (!trnModel.initEstimatedModel(option)) {

			return false;
		}

		globalDict = trnModel.data.localDict;

		computeTrnTheta();
		computrTrnPhi();
		computrTrnKesai();
		computetrnPhiEntity();

		return true;

	}

	public Model inference(ETMDataset newData) {
		/*PrintConsole.PrintLog("init new model", null);*/
		Model newModel = new Model();

		newModel.initNewModel(option, newData, trnModel);
		this.newModel = newModel;

		/*PrintConsole.PrintLog("Sampling", niters, "iteration for inference!");*/
		for (newModel.liter = 1; newModel.liter <= niters; newModel.liter++) {

			for (int m = 0; m < newModel.M; ++m) {
				for (int n = 0; n < newModel.data.docs[m].words.length; n++) {
					int topic = infSamplingWords(m, n);

					newModel.z[m].set(n, topic);
				}
			}

			int[] a = new int[2];
			for (int m = 0; m < newModel.M; ++m) {
				for (int n = 0; n < newModel.data.docs[m].entities.length; n++) {
					infSamplingEntities(m, n, a);

					int xtopic = a[0];
					int etopic = a[1];

					newModel.zEntity[m].set(n, etopic);
					newModel.x[m].set(n, xtopic);
				}
			}

		}
		/*PrintConsole.PrintLog("Gibbs sampling for inference completed!", null);
		PrintConsole.PrintLog("Saving the inference outputs!", null);*/

		computeNewTheta();
		computeNewPhi();
		computeNewKesai();
		computeNewPhiEntity();
		newModel.liter--;

		return this.newModel;
	}

	public Model inference(String[] strs) throws Exception {
		Model newModel = new Model();

		ETMDataset dataset = ETMDataset.readDataSet(strs, globalDict);

		return inference(dataset);

	}

	public Model inference() {
		newModel = new Model();
		if (!newModel.initNewModel(option, trnModel))
			return null;
		/*PrintConsole.PrintLog("Sampling ", niters, " iteration for inference!");*/

		for (newModel.liter = 1; newModel.liter <= niters; newModel.liter++) {

			for (int m = 0; m < newModel.M; ++m) {// ��forѭ����++m��m++��һ����˼
				for (int n = 0; n < newModel.data.docs[m].words.length; n++) {
					int topic = infSamplingWords(m, n);

					newModel.z[m].set(n, topic);
				}
				/*PrintConsole.PrintLog(".", null);*/
			}
			int[] a = new int[2];
			for (int m = 0; m < newModel.M; ++m) {
				for (int n = 0; n < newModel.data.docs[m].entities.length; n++) {
					infSamplingEntities(m, n, a);

					int xtopic = a[0];
					int etopic = a[1];
					newModel.zEntity[m].set(n, etopic);
					newModel.x[m].set(n, xtopic);
				}
				/*PrintConsole.PrintLog("*", null);*/
			}
			/*PrintConsole.PrintLog("", null);
			PrintConsole.PrintLog("---", null);*/

		}
		/*PrintConsole.PrintLog("Gibbs sampling for inference completed!", null);
		PrintConsole.PrintLog("Saving the inference outputs!", null);*/

		computeNewTheta();
		computeNewPhi();
		computeNewKesai();
		computeNewPhiEntity();
		newModel.liter--;
		// newModel.saveModel(newModel.dfile+"."+newModel.modelName);

		return newModel;

	}

	protected int infSamplingWords(int m, int n) {

		int topic = newModel.z[m].get(n);
		int _w = newModel.data.docs[m].words[n];
		int w = newModel.data.lid2gidW.get(_w);

		newModel.nw[_w][topic] -= 1;
		newModel.nd[m][topic] -= 1;
		newModel.nwsum[topic] -= 1;
		newModel.ndsum[m] -= 1;

		double Vbeta1 = trnModel.VWords * newModel.beta1;
		double Kalpha = trnModel.KWords * newModel.alpha;

		for (int k = 0; k < newModel.KWords; k++) {
			newModel.p[k] = (trnModel.nw[w][k] + newModel.nw[_w][k] + newModel.beta1)
					/ (trnModel.nwsum[k] + newModel.nwsum[k] + Vbeta1) * (newModel.nd[m][k] + newModel.alpha)
					/ (newModel.ndsum[m] + Kalpha);
		}

		for (int k = 1; k < newModel.KWords; k++) {
			newModel.p[k] += newModel.p[k - 1];
		}

		double u = Math.random() * newModel.p[newModel.KWords - 1]; // ���ѡ��һ��u

		for (topic = 1; topic < newModel.KWords; topic++) {
			if (newModel.p[topic] > u)
				break;
		}
		topic--;// ��Ϊ������ѭ����topic�����һ�λ��г���ȥ��++���⣬��˼�ȥ

		newModel.nw[_w][topic] += 1;
		newModel.nd[m][topic] += 1;
		newModel.nwsum[topic] += 1;
		newModel.ndsum[m] += 1;
		return topic;

	}

	public void infSamplingEntities(int m, int n, int[] a) {

		int etopic = newModel.zEntity[m].get(n);
		int xtopic = newModel.x[m].get(n);
		int _e = newModel.data.docs[m].entities[n];
		int e = newModel.data.lid2gidE.get(_e);

		newModel.ne[_e][etopic] -= 1;
		newModel.nde[m][etopic] -= 1;
		newModel.nesum[etopic] -= 1;
		newModel.ndesum[m] -= 1;
		newModel.nz[_e][xtopic] -= 1;
		newModel.ndz[m][xtopic] -= 1;
		newModel.nzsum[xtopic] -= 1;
		newModel.nzz[xtopic][etopic] -= 1;
		newModel.nzzsum[xtopic] -= 1;

		double Vbeta2 = trnModel.VEntities * newModel.beta2;

		// ��鷢��,newModel��ne�ڴ����ú�δˢ�£����ˢ��һ��
		// int VEntities=newModel.data.globalDict.entityDic.entity2id.size();
		// int KEntities=newModel.KEntities;
		// newModel.ne = new int[VEntities][KEntities];
		// for (int w = 0; w < VEntities; w++){
		// for (int k = 0; k < KEntities; k++){
		// newModel.ne[w][k] = 0;
		// }
		// }
		// for (int m_tmp = 0; m_tmp < newModel.data.M; m_tmp++){
		// int N = newModel.data.docs[m_tmp].entities.length;
		// //initilize for z
		// for (int n_tmp = 0; n_tmp < N; n_tmp++){
		// // number of instances of entities assigned to etopic j
		// newModel.ne[newModel.data.docs[m_tmp].entities[n_tmp]][etopic] += 1;
		// }
		// // total number of words in document i
		// }

		for (int kw = 0; kw < newModel.KWords; kw++) {
			for (int k = 0; k < newModel.KEntities; k++) {

				newModel.pEntity[kw][k] = ((newModel.nd[m][kw] * 1.0 + 1) / (newModel.ndsum[m] + trnModel.KWords))
						* (trnModel.ne[e][k] + newModel.ne[_e][k] + newModel.beta2)
						/ (trnModel.nesum[k] + newModel.nesum[k] + Vbeta2) * // ///to
																				// be
																				// changed..............
						(newModel.nzz[kw][k] + trnModel.nzz[kw][k] + newModel.gamma)
						/ (newModel.nzzsum[kw] + trnModel.nzzsum[kw] + newModel.KEntities * newModel.gamma);
				/*PrintConsole.PrintLog("pEntity[kw][k]:", trnModel.pEntity[kw][k]);
				PrintConsole.PrintLog("kw", kw, "k", k, "");*/
				/*if (newModel.pEntity[kw][k] < 0) {
					try {
						PrintConsole.PrintLog(newModel.ne[e][k] + " ", newModel.nd[m][kw]);
						PrintConsole.PrintLog(" " + newModel.ndsum[m], " " + newModel.nzz[kw][k],
								" " + newModel.nzzsum[kw]);
					} catch (Exception e11) {
						PrintConsole.PrintLog("1231231231", null);
					}
				}*/
			}
		}

		for (int kw = 0; kw < newModel.KWords; kw++) {
			if (kw > 0) {
				newModel.pEntity[kw][0] += newModel.pEntity[kw - 1][newModel.KEntities - 1];
			}
			for (int k = 1; k < newModel.KEntities; k++) {
				newModel.pEntity[kw][k] += newModel.pEntity[kw][k - 1];
			}
		}

		double u = Math.random() * newModel.pEntity[newModel.KWords - 1][newModel.KEntities - 1];

		int flag = 1;
		for (xtopic = 0; xtopic < newModel.KWords && flag != 0; xtopic++) {
			for (int etopic_tmp = 0; etopic_tmp < newModel.KEntities; etopic_tmp++) {

				if (newModel.pEntity[xtopic][etopic_tmp] > u) {
					// sample topic w.r.t distribution p
					flag = 0;
					break;
					// goto mark;
				}
			}
		}
		xtopic--;

		newModel.ne[_e][etopic] += 1; // �˴�����Խ�磬ԭ����topic�ܴﵽ���ֵ������ԭ���ǲ������ò�����
		newModel.nde[m][etopic] += 1;
		newModel.nesum[etopic] += 1;
		newModel.ndesum[m] += 1;
		newModel.nz[_e][xtopic] += 1;// ʵ��e�ڴ�������super topic�г��ֵĴ���
		newModel.nzsum[xtopic] += 1;// ������t���ܹ���Ļ���������
		newModel.ndz[m][xtopic] += 1;
		newModel.nzz[xtopic][etopic] += 1;
		newModel.nzzsum[xtopic] += 1;

		a[0] = xtopic;
		a[1] = etopic;
	}

	protected void computeNewTheta() {
		// TODO Auto-generated method stub
		for (int m = 0; m < newModel.M; m++) {
			for (int k = 0; k < newModel.KWords; k++) {
				newModel.theta[m][k] = (newModel.nd[m][k] + newModel.alpha)
						/ (newModel.ndsum[m] + newModel.KWords * newModel.alpha);
			}
		}
	}

	protected void computeNewPhi() {
		// TODO Auto-generated method stub
		for (int k = 0; k < newModel.KWords; k++) {
			for (int w = 0; w < newModel.VWords; w++) {
				newModel.phi[k][w] = (newModel.nw[w][k] + newModel.beta1)
						/ (newModel.nwsum[k] + newModel.VWords * newModel.beta1);
			}
		}
	}

	protected void computeNewKesai() {
		// TODO Auto-generated method stub
		for (int k1 = 0; k1 < newModel.KWords; k1++) {
			for (int k2 = 0; k2 < newModel.KEntities; k2++) {
				newModel.kesai[k1][k2] = (newModel.nzz[k1][k2] + newModel.gamma)
						/ (newModel.nzzsum[k1] + newModel.KEntities * newModel.gamma);
			}
		}
	}

	protected void computeNewPhiEntity() {
		// TODO Auto-generated method stub
		for (int k = 0; k < newModel.KEntities; k++) {
			for (int e = 0; e < newModel.VEntities; e++) {
				newModel.phiEntity[k][e] = (newModel.ne[e][k] + newModel.beta2)
						/ (newModel.nesum[k] + newModel.VEntities * newModel.beta2);
			}
		}

	}

	protected void computeTrnTheta() {
		// TODO Auto-generated method stub
		for (int m = 0; m < trnModel.M; m++) {
			for (int k = 0; k < trnModel.KWords; k++) {
				trnModel.theta[m][k] = (trnModel.nd[m][k] + trnModel.alpha)
						/ (trnModel.ndsum[m] + trnModel.KWords * trnModel.alpha);
			}
		}

	}

	protected void computrTrnPhi() {
		// TODO Auto-generated method stub
		for (int k = 0; k < trnModel.KWords; k++) {
			for (int w = 0; w < trnModel.VWords; w++) {
				trnModel.phi[k][w] = (trnModel.nw[w][k] + trnModel.beta1)
						/ (trnModel.nwsum[k] + trnModel.VWords * trnModel.beta1);
			}
		}
	}

	protected void computrTrnKesai() {
		// TODO Auto-generated method stub
		for (int k1 = 0; k1 < trnModel.KWords; k1++) {
			for (int k2 = 0; k2 < trnModel.KEntities; k2++) {
				trnModel.kesai[k1][k2] = (trnModel.nzz[k1][k2] + trnModel.gamma)
						/ (trnModel.nzzsum[k1] + trnModel.KEntities * trnModel.gamma);
			}
		}
	}

	protected void computetrnPhiEntity() {
		// TODO Auto-generated method stub
		for (int k = 0; k < trnModel.KEntities; k++) {
			for (int e = 0; e < trnModel.VEntities; e++) {
				trnModel.phiEntity[k][e] = (trnModel.ne[e][k] + trnModel.beta2)
						/ (trnModel.nesum[k] + trnModel.VEntities * trnModel.beta2);
			}
		}
	}
}
