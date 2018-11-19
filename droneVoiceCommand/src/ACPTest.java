
public class ACPTest {
	public static void main(String[] args) {
		Dataset datasetRef = new Dataset("D:\\User\\Bureau\\Travail\\Workspaces\\JAVA\\DTWStudent\\test_res\\audio\\non_bruite\\Ref");
		/*for(Dataset.Record r : datasetRef.records){
			System.out.println(r.label());
			System.out.println(r.acp[0] + " " + r.acp[1] + " " + r.acp[2]);
		}
		System.out.println();*/
		Dataset datasetExp = new Dataset("D:\\User\\Bureau\\Travail\\Workspaces\\JAVA\\DTWStudent\\test_res\\audio\\non_bruite\\M01");
		String[] colorLabel=myKPPV.kppv(datasetRef, datasetExp, 3);
		for(int i=0; i<colorLabel.length; i++) {
			System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
		}
	}
}
