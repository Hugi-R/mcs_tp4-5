import java.io.IOException;

import fr.enseeiht.danck.voice_analyzer.defaults.DTWHelperDefault;

public class FullTest {

	public static void main(String[] args) {
		String ref = "Tests/test_1/ref";
		String hyp = "Tests/test_1/hyp";
		
		fullTest(ref, hyp);

	}
	
	
	public static void fullTest(String ref, String hyp){
		Dataset datasetRef = new Dataset(ref, true);
		Dataset datasetExp = new Dataset(hyp, false);
		final int KMIN = 1;
		final int KMAX = 6;
		//double[][] summary = new double[4][KMAX-KMIN];
		
		for(int k = KMIN; k < KMAX; ++k){
			System.out.println("\nKPPV with ACP, k = "+k);
			myKPPV kppvAcp = new myKPPV();
			String[] colorLabel=kppvAcp.kppv(datasetRef, datasetExp, k);
			for(int i=0; i<colorLabel.length; i++) {
				System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
			}
			ACPTest.confusionMatrix(datasetRef, datasetExp, colorLabel);
			
			System.out.println("\nKPPV without ACP, k = "+k);
			myKPPV kppvNoAcp = new myKPPV(false, false);
			colorLabel=kppvNoAcp.kppv(datasetRef, datasetExp, k);
			for(int i=0; i<colorLabel.length; i++) {
				System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
			}
			ACPTest.confusionMatrix(datasetRef, datasetExp, colorLabel);
			
			System.out.println("\nKPPV with ACP, k augmente, k = "+k);
			myKPPV kppvAcpA = new myKPPV(true,true);
			colorLabel=kppvAcpA .kppv(datasetRef, datasetExp, k);
			for(int i=0; i<colorLabel.length; i++) {
				System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
			}
			ACPTest.confusionMatrix(datasetRef, datasetExp, colorLabel);
			
			System.out.println("\nKPPV without ACP, k augmente, k = "+k);
			myKPPV kppvNoAcpA = new myKPPV(false, true);
			colorLabel=kppvNoAcpA.kppv(datasetRef, datasetExp, k);
			for(int i=0; i<colorLabel.length; i++) {
				System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
			}
			ACPTest.confusionMatrix(datasetRef, datasetExp, colorLabel);
		}
		
		System.out.println("\nDTW");
		myDTWtest mdt = new myDTWtest(new DTWHelperDefault(), new myMFCCdistance());
		try {
			mdt.matriceConfusion(hyp, ref);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("End.");
	}
		

}
