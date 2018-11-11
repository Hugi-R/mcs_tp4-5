import fr.enseeiht.danck.voice_analyzer.DTWHelper;
import fr.enseeiht.danck.voice_analyzer.Field;

public class myDTW extends DTWHelper {

	@Override
	public float DTWDistance(Field unknown, Field known) {
		// Methode qui calcule le score de la DTW 
		// entre 2 ensembles de MFCC
		double costSuppr = 1.0;
		double costSubst = 2.0;
		double costInsert = 1.0;
		myMFCCdistance helper = new myMFCCdistance();
		
		//System.out.println("I="+known.getLength()+" J="+unknown.getLength());
		
		double g[][] = new double[known.getLength()+1][unknown.getLength()+1];
		g[0][0] = 0;
		for(int j = 1; j <= unknown.getLength(); j++)
			g[0][j] = Double.POSITIVE_INFINITY;
		
		for(int i = 1; i <= known.getLength(); i++){
			g[i][0] = Double.POSITIVE_INFINITY;
			for(int j = 1; j <= unknown.getLength(); j++){
				float d = helper.distance(known.getMFCC(i-1), unknown.getMFCC(j-1));
				g[i][j] = min3(	g[i-1][j] + costSuppr*d,
								g[i-1][j-1] + costSubst*d,
								g[i][j-1] + costInsert*d	);
			}
		}
		
		return (float)g[known.getLength()][unknown.getLength()]/(known.getLength() + unknown.getLength());
	}
	
	private double min3(double a, double b, double c){
		return Math.min(Math.min(a, b), c);
	}
}
