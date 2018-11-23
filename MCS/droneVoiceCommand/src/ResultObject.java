import java.util.Map;

public class ResultObject {
	private Map<String, Map<String, Integer>> confusion;
	private Dataset ref;
	private Dataset hyp;
	private String[] resultLabels;
	private double successRate;
	private double failureRate;

	public ResultObject(Dataset ref, Dataset hyp, String[] resultLabels){
		this.ref = ref;
		this.hyp = hyp;
		this.resultLabels = resultLabels;
		this.confusion = ACPTest.confusionMatrix(ref, hyp, resultLabels);
		processRate();
	}
	
	private void processRate(){
		int good = 0;
		int bad = 0;
		for (String ki : confusion.keySet()) {
			for (String kj : confusion.get(ki).keySet()) {
				if(ki.equals(kj)){
					good += confusion.get(ki).get(kj);
				} else {
					bad += confusion.get(ki).get(kj);
				}
			}
		}
		successRate = ((double)good)/(good+bad);
		failureRate = ((double)bad)/(good+bad);
	}

	public double getSuccessRate() {
		return successRate;
	}

	public double getFailureRate() {
		return failureRate;
	}

	public Map<String, Map<String, Integer>> getConfusion() {
		return confusion;
	}

	public Dataset getRef() {
		return ref;
	}

	public Dataset getHyp() {
		return hyp;
	}

	public String[] getResultLabels() {
		return resultLabels;
	}
	
	
}
