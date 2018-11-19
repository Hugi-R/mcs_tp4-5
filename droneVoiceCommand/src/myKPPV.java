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
		double x=Math.pow((abs(vectorT[0])-abs(vectorA[0])), 2);
		double y=Math.pow((abs(vectorT[1])-abs(vectorA[1])), 2);
		double z=Math.pow((abs(vectorT[2])-abs(vectorA[2])), 2);
		return Math.sqrt(x+y+z);
	}
	
	
	
	public static String[] kppv (Dataset datasetA, Dataset datasetT, int k) {
		 String[] colorTest= new String [datasetT.records.size()] ;
		 for(int i=0; i<datasetT.records.size(); i++) {
			 List <distLabel> distList = new ArrayList <> ();
			 double[] test=datasetT.records.get(i).acp;
			 //System.out.println("Point:"+datasetT.records.get(i).label());
			 for(int j=0; j<datasetA.records.size(); j++) {
				 double[] a = datasetA.records.get(j).acp;
				 double dist=abs(distance3D(test,a));
				 //System.out.println(datasetA.records.get(j).label()+":"+dist);
				 distList.add(new distLabel(dist,datasetA.records.get(j).label()));
			 }
			 String meilleur = whatIsBest(k, distList);
			 colorTest[i]=meilleur;
		 }
		 return colorTest;
	}
	
	public static String[] kppvDouble (double[] dataA, String[] labelA , double[] dataT, int k) {
		 String[] colorTest= new String [dataT.length] ;
		 for(int i=0; i<dataT.length; i++) {
			 List <distLabel> distList = new ArrayList <> ();
			 double test=dataT[i];
			 for(int j=0; j<dataA.length; j++) {
				 double a = dataA[j];
				 double dist=abs(test-a);
				 distList.add(new distLabel(dist,labelA[j]));
			 }
			 //System.out.println("Point " + test +":");
			 String meilleur = whatIsBest(k, distList);
			 colorTest[i]=meilleur;
		 }
		 return colorTest;
	}



	private static String whatIsBest(int k, List<distLabel> distList) {
		Map <String,Integer> colorAp = new HashMap<>();
		for(int n=0;n<distList.size();n++) {
			 for(int j=0;j<distList.size()-n-1;j++) {
				 if(distList.get(j).dist>distList.get(j+1).dist) {
					 distLabel change = distList.get(j);
					 distList.set(j, distList.get(j+1));
					 distList.set(j+1, change);
				 }
			 }
		 }
		 //Affichage list
		 for(int i=0;i<distList.size();i++) {
			 distLabel x = distList.get(i);
			 //System.out.println(x.dist + ": label " + x.label);
		 }
		 boolean egalite;
		 String meilleur;
		 int max;
		 int kIntern=k;
		 do {
			 //System.out.println("For k=" + kIntern);
			 for(int j=0;j<kIntern;j++) {
				 distLabel var=distList.get(j);
				 //System.out.println(var.dist + ": label " + var.label);
				 if(!colorAp.containsKey(var.label)) {
					 colorAp.put(var.label, 1);
				 }else {
					 colorAp.replace(var.label, colorAp.get(var.label)+1);
				 }
			 }
			 meilleur="";
			 max=0;
			 egalite=false;
			 //System.out.println("With " + kIntern + " points, the point is close to:");
			 for(Map.Entry<String, Integer> entry : colorAp.entrySet()) {
				 String key = entry.getKey();
				 int value = entry.getValue();
				 //System.out.println(value + " with the label " + key);
				 if(max<=value) {
					 egalite=(max==value);
					 meilleur=key;
					 max=value;
				 }
			 }
			 if(egalite) {
				 kIntern--;
				 colorAp.clear();
			 }
		}while (egalite && kIntern>0);
		System.out.println("meilleur=" + meilleur);
		return meilleur;
	}

	/*public static void main(String[] args) {
		double[] testA= {9.9,8,11,6,16,2,1,5,18};
		String[] labelA= {"a","b","c","b","c","b","a","a","c"};
		double[] testT= {10,3,12};
		String[] labelT= kppvDouble(testA, labelA, testT, 3);
		for(int i=0 ; i<labelT.length; i++) {
			System.out.println("The number " + testT[i] + " is recognised as the label " + labelT[i]);
		}
	}*/
	
	
}
