package text.thu.keg.smartsearch.jgibbetm;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Perplexity {
	private Logger LOG = LoggerFactory.getLogger(Perplexity.class);

	public double calPerp(Model newModel, Model trnModel) throws IOException {

		double perp = 0;
		double numerator = 0;// ����
		double denominator = 0;// ��ĸ
		double perplexity = 0;// ������ֵ
		double[][] thetaEntity = new double[newModel.data.docs.length][newModel.KEntities];
		for (int i = 0; i < newModel.data.docs.length; i++) {
			for (int j = 0; j < newModel.KEntities; j++) {
				for (int k = 0; k < newModel.KWords; k++) {
					thetaEntity[i][j] += newModel.theta[i][k] * trnModel.kesai[k][j];
				}
			}
		}

		for (int m = 0; m < newModel.data.docs.length; m++) {
			int Ndw = newModel.data.docs[m].words.length;
			int Nde = newModel.data.docs[m].entities.length;
			LOG.info("Ndw = {} , Nde = {} ", Ndw, Nde);
			double log_p_wd = 0.0;

			for (int n = 0; n < Ndw; n++) {

				int w = newModel.data.docs[m].words[n];
				double p = 0.0;
				for (int k = 0; k < newModel.KWords; k++) {

					p += trnModel.phi[k][w] * newModel.theta[m][k];

				}
				log_p_wd += Math.log(p);

			}
			for (int n = 0; n < Nde; n++) {

				int e = newModel.data.docs[m].entities[n];
				double p = 0;

				for (int k = 0; k < newModel.KEntities; k++) {

					p += trnModel.phiEntity[k][e] * thetaEntity[m][k];
				}
				log_p_wd += Math.log(p);
			}
			perp += log_p_wd;
		}
		numerator = perp;
		for (int m = 0; m < newModel.data.docs.length; m++) {
			denominator += newModel.data.docs[m].length;
		}
		perplexity = Math.exp(-numerator / denominator);
		return perplexity;

	}
}
