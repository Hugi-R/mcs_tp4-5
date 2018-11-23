import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ACPTest {
	
	private static List<String> constrLabel (Dataset exp) {
		List<String> tab = new ArrayList<>();
		for(int i=0;i<exp.records.size();i++) {
			String str = exp.records.get(i).label();
			if(!tab.contains(str)) {
				tab.add(str);
			}
		}
		return tab;
	}
	
	public static Map<String, Map<String, Integer>> confusionMatrix (Dataset ref, Dataset exp, String[] colorLabel) {
		Map<String, Map<String, Integer>> confusion = new HashMap<>();
		List<String> labelTab = constrLabel(ref);
		creationMatrix(confusion, labelTab);
		for(int i=0;i<exp.records.size();i++) {
			String e = exp.records.get(i).label();
			String c = colorLabel[i];
			int temp = confusion.get(e).get(c);
			temp++;
			confusion.get(e).put(c, temp);
		}
		return confusion;
	}

	private static void creationMatrix(Map<String, Map<String, Integer>> confusion, List<String> labelTab) {
		for(int i=0;i<labelTab.size();i++) {
			String x = labelTab.get(i);
			confusion.put(x, new HashMap<String, Integer>());
			for(int j=0;j<labelTab.size();j++) {
				String y = labelTab.get(j);
				confusion.get(x).put(y, 0);
			}
		}
	}
	
	public static void main(String[] args) {
		Dataset datasetRef = new Dataset("D:\\User\\Bureau\\Travail\\Workspaces\\JAVA\\DTWStudent\\test_res\\audio\\non_bruite\\Ref", true);
		Dataset datasetExp = new Dataset("D:\\User\\Bureau\\Travail\\Workspaces\\JAVA\\DTWStudent\\test_res\\audio\\non_bruite\\M01", false);
		
		System.out.println("With ACP, k decendant");
		myKPPV kppvAcp = new myKPPV();
		String[] colorLabel=kppvAcp.kppv(datasetRef, datasetExp, 3);
		for(int i=0; i<colorLabel.length; i++) {
			System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
		}
		confusionMatrix(datasetRef,datasetExp,colorLabel);
		
		System.out.println("Without ACP");
		myKPPV kppvNoAcp = new myKPPV(false,false);
		colorLabel=kppvNoAcp.kppv(datasetRef, datasetExp, 3);
		for(int i=0; i<colorLabel.length; i++) {
			System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
		}
		
		System.out.println("With ACP, k augmente");
		myKPPV kppvAcpA = new myKPPV(true,true);
		colorLabel=kppvAcpA .kppv(datasetRef, datasetExp, 3);
		for(int i=0; i<colorLabel.length; i++) {
			System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
		}
	}
}
