
public class ACPTest {
	public static void main(String[] args) {
		Dataset datasetRef = new Dataset("Tests/test_1/reference", true);
		Dataset datasetExp = new Dataset("Tests/test_1/hypothese", false);
		
		System.out.println("With ACP");
		myKPPV kppvAcp = new myKPPV();
		String[] colorLabel=kppvAcp.kppv(datasetRef, datasetExp, 3);
		for(int i=0; i<colorLabel.length; i++) {
			System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
		}
		
		System.out.println("Without ACP");
		myKPPV kppvNoAcp = new myKPPV(false);
		colorLabel=kppvNoAcp.kppv(datasetRef, datasetExp, 3);
		for(int i=0; i<colorLabel.length; i++) {
			System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
		}
	}
}
