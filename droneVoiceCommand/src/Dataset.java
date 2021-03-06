import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

import fr.enseeiht.danck.voice_analyzer.Extractor;
import fr.enseeiht.danck.voice_analyzer.MFCC;
import fr.enseeiht.danck.voice_analyzer.WindowMaker;

public class Dataset {
	public List<Record> records = new ArrayList<>();
	public String path;
	public boolean isAcp;
	private RealMatrix base;

	// Fonction permettant de calculer la taille des Fields
	// c'est-�-dire le nombre de MFCC du Field
	private static int fieldLength(String filePath) throws IOException {
		int counter = 0;
		File file = new File(filePath);
		for (@SuppressWarnings("unused") String s : Files.readAllLines(file.toPath(), Charset.defaultCharset())) {
			counter++;
		}
		return 2 * Math.floorDiv(counter, 512);
	}
	
	/// Store a record as a vector 13
	public class Record {
		public static final int length = 13;
		public double[] data;
		public double[] acp = null;
		public String path;
		
		public Record(double[] data, String path) {
			assert(data.length == length);
			this.data = data;
			this.path = path;
		}
		
		public RealMatrix toMatrix(){
			
			double[][] mat = new double[1][length];
			mat[0] = data;
			return new BlockRealMatrix(mat);
		}

		@Override
		public String toString() {
			return "Record [data=" + Arrays.toString(data) + ", path=" + path + "]";
		}
		
		public String label(){
			Path s = Paths.get(path);
			String name = s.toFile().getName();
			Pattern p = Pattern.compile("[MFH]\\d+_([a-z]+).wav.csv");
			Matcher m = p.matcher(name);
			if (!m.matches()) {
				System.err.println("Regex failled");
				System.exit(1);
			}
			return m.group(1);
		}
	}
	
	/// get the mean vector 13 of a file
	public Record processFile(String path){
		Extractor extractor = Extractor.getExtractor();
		MFCC[] mfccs;
		try{
			// Read file
			List<String> files = new ArrayList<>();
			files.add(path);
			WindowMaker windowMaker = new MultipleFileWindowMaker(files);
	
			// Get MFCC from file
			mfccs = new MFCC[fieldLength(path)];
			for (int i = 0; i < mfccs.length; i++) {
				mfccs[i] = extractor.nextMFCC(windowMaker);
			}
		} catch (Exception e) {
			System.err.println("ERROR : Cannot read "+path+" : "+e.getMessage());
			return null;
		}
		
		double[] data = new double[13]; // Java init at 0, nice !
		for(MFCC mfcc : mfccs){
			for(int i = 0; i < mfcc.getLength(); ++i){
				data[i] += mfcc.getCoef(i);
			}
		}
		
		for(int i = 0; i < data.length; ++i){
			data[i] /= mfccs.length;
		}
		
		return new Record(data, path);
	}
	
	public Dataset(String folderPath, boolean doAcp){
		path = folderPath;
		isAcp = doAcp;
		
		File[] files = new File(folderPath).listFiles();
		for(int i = 0; i < files.length; ++i){
			records.add(processFile(files[i].getAbsolutePath()));
		}
		if(isAcp)
			computeACP(3);
	}
	public Dataset(String folderPath){
		this(folderPath, false);
	}
	
	public RealMatrix toMatrix(){
		double[][] matrix = new double[records.size()][Record.length];
		for(int i = 0; i < records.size(); ++i){
			matrix[i] = records.get(i).data;
		}
		return new BlockRealMatrix(matrix);
	}

	@Override
	public String toString() {
		return "Dataset [records=" + records + ", path=" + path + "]";
	}
	
	public double[] toDatasetBase(RealMatrix m){
		assert isAcp : "This dataset did not compute ACP !";
		return m.multiply(base).getRow(0);
	}
	
	private void computeACP(int dimension){
		assert isAcp : "Trying to compute ACP on a dataset that shoudn't !";
		RealMatrix m = this.toMatrix();
		Covariance cov = new Covariance(m);
		
		EigenDecomposition eig = new EigenDecomposition(cov.getCovarianceMatrix());
		
		double[][] w_tmp = new double[3][13];
		for(int i = 0; i < 3; ++i){
			w_tmp[i] = eig.getEigenvector(i).toArray();
		}
		RealMatrix w = new BlockRealMatrix(w_tmp);
		
		base = w.transpose();
		for(Record r : records){
			RealMatrix y = r.toMatrix().multiply(base);
			r.acp = y.getData()[0];
		}
	}
}
