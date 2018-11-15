import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */

/**
 * @author StevenIP
 *
 */
public class myKPPV {
	
	static class distLabel{
		public double dist;
		public String label;
		public distLabel (double dist, String label) {
			this.dist=dist;
			this.label=label;
		}
	}
	
	private static double abs (double i) {
		if (i<0) return -i;
		return i;
	}
	
	private static double distance3D (double [] vectorT, double [] vectorA) {
		double x=Math.pow((vectorA[0]-vectorT[0]), 2);
		double y=Math.pow((vectorA[1]-vectorT[1]), 2);
		double z=Math.pow((vectorA[2]-vectorT[2]), 2);
		return Math.sqrt(x+y+z);
	}
	
	
	
	public static String[] kppv (Dataset datasetA, Dataset datasetT, int k) {
		 String[] colorTest= new String [datasetT.records.size()] ;
		 for(int i=0; i<datasetT.records.size(); i++) {
			 Map <String,Integer> colorAp = new HashMap<>();
			 List <distLabel> distList = new ArrayList <> ();
			 double[] test=datasetT.records.get(i).acp;
			 for(int j=0; j<datasetA.records.size(); j++) {
				 double[] a = datasetA.records.get(j).acp;
				 double dist=abs(distance3D(test,a));
				 distList.add(new distLabel(dist,datasetA.records.get(j).path));
			 }
			 System.out.println("Point " + test +":");
			 String meilleur = whatIsBest(k, colorAp, distList);
			 colorTest[i]=meilleur;
		 }
		 return colorTest;
	}



	private static String whatIsBest(int k, Map<String, Integer> colorAp, List<distLabel> distList) {
		for(int n=0;n<distList.size();n++) {
			 for(int j=n;j<distList.size()-1;j++) {
				 if(distList.get(j).dist<distList.get(j+1).dist) {
					 distLabel change = distList.get(j);
					 distList.set(j, distList.get(j+1));
					 distList.set(j+1, change);
				 }
			 }
		 }
		 boolean egalite;
		 String meilleur;
		 int max;
		 int kIntern=k;
		 do {
			 for(int j=0;j<kIntern;j++) {
				 distLabel var=distList.get(j);
				 if(!colorAp.containsKey(var.label)) {
					 colorAp.put(var.label, 1);
				 }else {
					 colorAp.replace(var.label, colorAp.get(var.label)+1);
				 }
			 }
			 meilleur="";
			 max=0;
			 egalite=false;
			 System.out.println("With " + kIntern + " points, the point is close to:");
			 for(Map.Entry<String, Integer> entry : colorAp.entrySet()) {
				 String key = entry.getKey();
				 int value = entry.getValue();
				 System.out.println(value + " with the label " + key);
				 if(max<=value) {
					 egalite=(max==value);
					 meilleur=key;
					 max=value;
				 }
			 }
			 if(egalite) {
				 kIntern++;
				 colorAp.clear();
			 }
		}while (egalite && kIntern<distList.size());
		return meilleur;
	}
	
	//test double 
	/*public static void main (String[] args) {
		double [] dataA = {9,8,11,6,16,2,1,5,18};
		String [] labelA = {"a","b","c","b","c","b","a","a","c"};
		double [] dataT = {10,3,12,7};
		int k=4;
		String [] labelT = kppv(dataA,labelA,dataT,k);
		System.out.println();
		for(int i=0;i<dataT.length;i++) {
			System.out.println(dataT[i] + " is recognised as " + labelT[i]);
		}
	}*/
	
}
