import java.io.IOException;

import fr.enseeiht.danck.voice_analyzer.defaults.DTWHelperDefault;

public class FullTest {

	public static void main(String[] args) {
		String ref = "Tests/test_4/ref";
		String hyp = "Tests/test_4/hyp";
		
		fullTest(ref, hyp);

	}
	
	
	public static void fullTest(String ref, String hyp){
		Dataset datasetRef = new Dataset(ref, true);
		Dataset datasetExp = new Dataset(hyp, false);
		final int KMIN = 1;
		final int KMAX = 8;
		ResultObject[][] summary = new ResultObject[4][KMAX-KMIN];
		ResultObject ro;
		
		for(int k = KMIN; k < KMAX && k < datasetRef.records.size(); ++k){
			System.out.println("\nKPPV with ACP, k descend, k = "+k);
			myKPPV kppvAcp = new myKPPV();
			String[] colorLabel=kppvAcp.kppv(datasetRef, datasetExp, k);
			for(int i=0; i<colorLabel.length; i++) {
				System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
			}
			ro = new ResultObject(datasetRef, datasetExp, colorLabel);
			myDTWtest.printConfusion(ro.getConfusion());
			summary[0][k-KMIN] = ro;
			
			System.out.println("\nKPPV without ACP, k descend, k = "+k);
			myKPPV kppvNoAcp = new myKPPV(false, false);
			colorLabel=kppvNoAcp.kppv(datasetRef, datasetExp, k);
			for(int i=0; i<colorLabel.length; i++) {
				System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
			}
			ro = new ResultObject(datasetRef, datasetExp, colorLabel);
			myDTWtest.printConfusion(ro.getConfusion());
			summary[1][k-KMIN] = ro;
			
			System.out.println("\nKPPV with ACP, k augmente, k = "+k);
			myKPPV kppvAcpA = new myKPPV(true,true);
			colorLabel=kppvAcpA .kppv(datasetRef, datasetExp, k);
			for(int i=0; i<colorLabel.length; i++) {
				System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
			}
			ro = new ResultObject(datasetRef, datasetExp, colorLabel);
			myDTWtest.printConfusion(ro.getConfusion());
			summary[2][k-KMIN] = ro;
			
			System.out.println("\nKPPV without ACP, k augmente, k = "+k);
			myKPPV kppvNoAcpA = new myKPPV(false, true);
			colorLabel=kppvNoAcpA.kppv(datasetRef, datasetExp, k);
			for(int i=0; i<colorLabel.length; i++) {
				System.out.println(datasetExp.records.get(i).path + " have the label : " + colorLabel[i]);
			}
			ro = new ResultObject(datasetRef, datasetExp, colorLabel);
			myDTWtest.printConfusion(ro.getConfusion());
			summary[3][k-KMIN] = ro;
		}
		
		System.out.println("\nSummary KPPV failure rate :");
		System.out.println("   ACP k descend \t noACP k descend \t ACP k augment \t\t noACP k augment");
		for(int j = 0; j < KMAX-KMIN && j < (datasetRef.records.size()-KMIN); ++j){
			System.out.println("k="+(j+KMIN)+" "+summary[0][j].getFailureRate()+"\t"+summary[1][j].getFailureRate()+"\t"+summary[2][j].getFailureRate()+"\t"+summary[3][j].getFailureRate());
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
