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
	
	static class distClasse{
		public float dist;
		public String classe;
		public distClasse (float dist, String classe) {
			this.dist=dist;
			this.classe=classe;
		}
	}
	
	private static float abs (float i) {
		if (i<0) return -i;
		return i;
	}
	
	
	
	public static String[] kppv (float [] dataA, String [] labelA, float [] dataT, int k) {
		 String[] colorTest= new String [dataT.length] ;
		 for(int i=0; i<dataT.length; i++) {
			 Map <String,Integer> colorAp = new HashMap<>();
			 List <distClasse> distList = new ArrayList <> ();
			 float test=dataT[i];
			 for(int j=k; j<dataA.length; j++) {
				 float a = dataA[j];
				 float dist=abs(test-a);
				 distList.add(new distClasse(dist,labelA[j]));
			 }
			 
			 String meilleur = whatIsBest(k, colorAp, distList);
			 colorTest[i]=meilleur;
		 }
		 return colorTest;
	}



	private static String whatIsBest(int k, Map<String, Integer> colorAp, List<distClasse> distList) {
		for(int n=0;n<k;n++) {
			 for(int j=n;j<distList.size()-1;j++) {
				 if(distList.get(j).dist<distList.get(j+1).dist) {
					 distClasse change = distList.get(j);
					 distList.set(j, distList.get(j+1));
					 distList.set(j+1, change);
				 }
			 }
		 }
		 for(int j=0;j<k;j++) {
			 distClasse var=distList.get(j);
			 if(!colorAp.containsKey(var.classe)) {
				 colorAp.put(var.classe, 1);
			 }else {
				 colorAp.replace(var.classe, colorAp.get(var.classe)+1);
			 }
		 }
		 String meilleur="";
		 int max=0;
		 for(Map.Entry<String, Integer> entry : colorAp.entrySet()) {
			 String key = entry.getKey();
			 int value = entry.getValue();
			 if(max<value) {
				 meilleur=key;
				 max=value;
			 }
		 }
		return meilleur;
	}
	
	
	public static void main (String[] args) {
		float [] dataA = {9,8,11,6,16,2,1,5,18};
		String [] labelA = {"a","b","c","b","c","b","a","a","c"};
		float [] dataT = {10,3,12,7};
		int k=4;
		String [] labelT = kppv(dataA,labelA,dataT,k);
		for(int i=0;i<dataT.length;i++) {
			System.out.println(dataT[i] + " is recognised as " + labelT[i]);
		}
	}
	
}
