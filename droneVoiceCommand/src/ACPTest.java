
public class ACPTest {
	public static void main(String[] args) {
		Dataset datasetRef = new Dataset("D:\\User\\Bureau\\Travail\\Workspaces\\JAVA\\DTWStudent\\test_res\\audio\\non_bruite\\Ref", true);
		Dataset datasetExp = new Dataset("D:\\User\\Bureau\\Travail\\Workspaces\\JAVA\\DTWStudent\\test_res\\audio\\non_bruite\\M01", false);
		
		System.out.println("With ACP, k decendant");
		myKPPV kppvAcp = new myKPPV();
		String[] colorLabel=kppvAcp.kppv(datasetRef, datasetExp, 3);
		for(int i=0; i<colorLabel.length; i++) {
			System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
		}
		
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
