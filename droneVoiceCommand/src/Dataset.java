import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.enseeiht.danck.voice_analyzer.Extractor;
import fr.enseeiht.danck.voice_analyzer.MFCC;
import fr.enseeiht.danck.voice_analyzer.WindowMaker;

public class Dataset {
	public ArrayList<Record> records = new ArrayList<>();
	public String path;

	// Fonction permettant de calculer la taille des Fields
	// c'est-à-dire le nombre de MFCC du Field
	private static int FieldLength(String filePath) throws IOException {
		int counter = 0;
		File file = new File(filePath);
		for (String _ : Files.readAllLines(file.toPath(), Charset.defaultCharset())) {
			counter++;
		}
		return 2 * Math.floorDiv(counter, 512);
	}
	
	/// Store a record as a vector 13
	public class Record {
		public double[] data;
		public String path;
		
		public Record(double[] data, String path) {
			assert(data.length == 13);
			this.data = data;
			this.path = path;
		}

		@Override
		public String toString() {
			return "Record [data=" + Arrays.toString(data) + ", path=" + path + "]";
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
			mfccs = new MFCC[FieldLength(path)];
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
	
	public Dataset(String folderPath){
		path = folderPath;
		
		File[] files = new File(folderPath).listFiles();
		for(int i = 0; i < files.length; ++i){
			records.add(processFile(files[i].getAbsolutePath()));
		}
	}

	@Override
	public String toString() {
		return "Dataset [records=" + records + ", path=" + path + "]";
	}
}
