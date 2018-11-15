
public class ACPTest {
	public static void main(String[] args) {
		Dataset dataset = new Dataset("C:/Users/Hugo/Documents/mcs_tp4-5/droneVoiceCommand/Tests/test_1/hypothese");
		for(Dataset.Record r : dataset.records){
			System.out.println(r.acp[0] + " " + r.acp[1] + " " + r.acp[2]);
		}
	}
}
