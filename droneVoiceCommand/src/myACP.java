import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class myACP {
	public static void main(String[] args) {
		Dataset dataset = new Dataset("C:/Users/Hugo/Documents/mcs_tp4-5/droneVoiceCommand/Tests/test_1/hypothese");
		Matrix mat = dataset.toMatrix();
		mat.print(9, 6);		
	}
	
	/*public void scatterMatrix(Matrix mat){
		
		for(mat.getRowDimension()){
			
		}
	}*/
	
	public Matrix meanVector(Matrix mat){
		double[] meanVec = new double[mat.getColumnDimension()];
		for(int i = 0; i < mat.getRowDimension(); ++i){
			
		}
		return mat;
	}
}
