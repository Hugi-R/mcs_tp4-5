import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author StevenIP
 *
 */
public class myKPPV {
	private boolean useAcp;

	public myKPPV(boolean useAcp) {
		this.useAcp = useAcp;
	}

	public myKPPV() {
		this.useAcp = true;
	}

	static class distLabel {
		public double dist;
		public String label;

		public distLabel(double dist, String label) {
			this.dist = dist;
			this.label = label;
		}
	}

	private double distance(double[] a, double[] b) {
		// distance euclidienne
		double sum = 0;
		for (int i = 0; i < a.length; i++)
			sum += Math.pow(a[i] - b[i], 2);
		return Math.sqrt(sum);
	}

	public String[] kppv(Dataset datasetA, Dataset datasetT, int k) {
		String[] colorTest = new String[datasetT.records.size()];
		for (int i = 0; i < datasetT.records.size(); i++) {
			List<distLabel> distList = new ArrayList<>();
			double[] test = useAcp ? datasetA.toDatasetBase(datasetT.records.get(i).toMatrix())
					: datasetT.records.get(i).data;
			for (int j = 0; j < datasetA.records.size(); j++) {
				double[] a = useAcp ? datasetA.records.get(j).acp : datasetA.records.get(j).data;
				double dist = distance(test, a);
				distList.add(new distLabel(dist, datasetA.records.get(j).label()));
			}
			String meilleur = whatIsBest(k, distList);
			colorTest[i] = meilleur;
		}
		return colorTest;
	}

	private String whatIsBest(int k, List<distLabel> distList) {
		Map<String, Integer> colorAp = new HashMap<>();
		for (int n = 0; n < distList.size(); n++) {
			for (int j = 0; j < distList.size() - n - 1; j++) {
				if (distList.get(j).dist > distList.get(j + 1).dist) {
					distLabel change = distList.get(j);
					distList.set(j, distList.get(j + 1));
					distList.set(j + 1, change);
				}
			}
		}
		boolean egalite;
		String meilleur;
		int max;
		int kIntern = k;
		do {
			for (int j = 0; j < kIntern; j++) {
				distLabel var = distList.get(j);
				if (!colorAp.containsKey(var.label)) {
					colorAp.put(var.label, 1);
				} else {
					colorAp.replace(var.label, colorAp.get(var.label) + 1);
				}
			}
			meilleur = "";
			max = 0;
			egalite = false;
			for (Map.Entry<String, Integer> entry : colorAp.entrySet()) {
				String key = entry.getKey();
				int value = entry.getValue();
				if (max <= value) {
					egalite = (max == value);
					meilleur = key;
					max = value;
				}
			}
			if (egalite) {
				kIntern--;
				colorAp.clear();
			}
		} while (egalite && kIntern > 0);
		return meilleur;
	}
}
