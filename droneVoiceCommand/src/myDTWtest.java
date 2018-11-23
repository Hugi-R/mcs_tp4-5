import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.enseeiht.danck.voice_analyzer.DTWHelper;
import fr.enseeiht.danck.voice_analyzer.Extractor;
import fr.enseeiht.danck.voice_analyzer.Field;
import fr.enseeiht.danck.voice_analyzer.MFCC;
import fr.enseeiht.danck.voice_analyzer.MFCCHelper;
import fr.enseeiht.danck.voice_analyzer.WindowMaker;
import fr.enseeiht.danck.voice_analyzer.defaults.DTWHelperDefault;


/* mail prof : jfarinas@irit.fr */

public class myDTWtest {

	//protected static final int MFCCLength = 13;

	// Fonction permettant de calculer la taille des Fields
	// c'est-à-dire le nombre de MFCC du Field
	static int FieldLength(String filePath) throws IOException {
		int counter = 0;
		File file = new File(filePath);
		for (@SuppressWarnings("unused") String line : Files.readAllLines(file.toPath(), Charset.defaultCharset())) {
			counter++;
		}
		return 2 * Math.floorDiv(counter, 512);
	}

	DTWHelper myDTWHelper;
	MFCCHelper myMCCHelper;

	public myDTWtest(DTWHelper myDTWHelper, MFCCHelper myMfccHelper) {
		this.myDTWHelper = myDTWHelper;
		this.myMCCHelper = myMfccHelper;
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		myDTWtest mdt = new myDTWtest(new DTWHelperDefault(), new myMFCCdistance());
		String path = "Tests/"; //add tests in this folder
		String tests[] = {"test_1"}; //add tests to be performed here
		for (String t : tests) {
			mdt.matriceConfusion(path + t +"/hypothese", path + t +"/reference");
			System.out.println("End "+t+".");
		}
	}

	public void matriceConfusion(String data, String truth) throws IOException, InterruptedException {
		Pattern p = Pattern.compile("[MFH]\\d+_([a-z]+).wav.csv");
		File[] datas = new File(data).listFiles();
		File[] truths = new File(truth).listFiles();
		Map<String, Map<String, Integer>> confusion = new HashMap<>();
		for (int i = 0; i < datas.length; ++i) {
			File d = datas[i];
			// System.out.println("Starting "+datas[i].getName());
			String md = getOrdre(p, d.getName());
			if (!confusion.containsKey(md))
				confusion.put(md, new HashMap<>());
			int bestMatch = 0;
			float min = 0;
			for (int j = 0; j < truths.length; ++j) {
				File t = truths[j];
				String mt = getOrdre(p, t.getName());
				if (!confusion.get(md).containsKey(mt))
					confusion.get(md).put(mt, 0);
				if (j == 0) {
					min = distanceCsv(d.getPath(), t.getPath());
				} else {
					float dist = distanceCsv(d.getPath(), t.getPath());
					if (dist < min) {
						min = dist;
						bestMatch = j;
						// System.out.println("j="+j+" min="+min+" dist="+dist+"
						// bestmatch="+bestMatch);
					}
				}
			}
			String mt = getOrdre(p, truths[bestMatch].getName());
			int tmp = confusion.get(md).get(mt);
			confusion.get(md).put(mt, tmp + 1);
			System.out.println(
					datas[i].getName() + " detected as " + truths[bestMatch].getName() + " with a distance of " + min);
		}
		System.out.println(confusion);
		printConfusion(confusion);
	}

	public static String getOrdre(Pattern p, String s) {
		Matcher m = p.matcher(s);
		if (!m.matches()) {
			System.err.println("Regex failled");
			System.exit(1);
		}
		return m.group(1);
	}

	public static double printConfusion(Map<String, Map<String, Integer>> confusion) {
		int good = 0;
		int bad = 0;
		String s = "hyp\\ref, ";
		for (String ki : confusion.get("arretetoi").keySet()) {
			s += ki + " , ";
		}
		s += "\n";
		for (String ki : confusion.keySet()) {
			s += ki + "   \t, ";
			for (String kj : confusion.get(ki).keySet()) {
				s += confusion.get(ki).get(kj) + ", ";
				if(ki.equals(kj)){
					good += confusion.get(ki).get(kj);
				} else {
					bad += confusion.get(ki).get(kj);
				}
			}
			s += "\n";
		}
		float failure = (float)bad/(float)(good+bad);
		System.out.println(s);
		System.out.println("Taux d'echec :"+failure);
		return failure;
	}

	public float distanceCsv(String a, String b) throws IOException, InterruptedException {
		Extractor extractor = Extractor.getExtractor();

		List<String> files = new ArrayList<>();
		files.add(a);
		WindowMaker windowMaker = new MultipleFileWindowMaker(files);

		// Etape 2. Recuperation des MFCC du mot Alpha
		MFCC[] mfccsAlpha = new MFCC[FieldLength(a)];
		for (int i = 0; i < mfccsAlpha.length; i++) {
			mfccsAlpha[i] = extractor.nextMFCC(windowMaker);
		}

		// Etape 3. Construction du Field (ensemble de MFCC) de alpha
		Field alphaField = new Field(/*removeSilence(*/mfccsAlpha);

		// Etape 1. Lecture de Bravo
		files = new ArrayList<>();
		files.add(b);
		windowMaker = new MultipleFileWindowMaker(files);

		// Etape 2. Recuperation des MFCC du mot Bravo
		MFCC[] mfccsBravo = new MFCC[FieldLength(b)];
		for (int i = 0; i < mfccsBravo.length; i++) {
			mfccsBravo[i] = extractor.nextMFCC(windowMaker);

		}

		// Etape 3. Construction du Field (ensemble de MFCC) de Bravo
		Field bravoField = new Field(/*removeSilence(*/mfccsBravo);

		return myDTWHelper.DTWDistance(alphaField, bravoField);
	}
	
	//abandonne
	public MFCC[] removeSilence(MFCC[] mfccs){
		ArrayList<MFCC> res = new ArrayList<>();
		int i = 0;
		int j = 0;
		for(MFCC mfcc : mfccs){
			if(i > 2 && i < mfccs.length-2){
				res.add(mfcc);
				++j;
			}
			++i;
		}
		//System.out.println(res);
		return res.toArray(new MFCC[j]);
	}

}
