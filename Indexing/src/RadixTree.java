import static java.util.Comparator.comparingInt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RadixTree {
	private HashMap<String, ArrayList<String>> freq = new HashMap<>();

	public HashMap<String, ArrayList<String>> getFreq() {
		return freq;
	}

	public void listeMots(String chemin) {
		String line = null;
		Set<String> ensembleMots = new HashSet<>();

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(chemin);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			try {
				while ((line = bufferedReader.readLine()) != null) {
					String[] mots = line.split("[^0-9A-Za-z'àáâãäåçèéêëìíîïðòóôõöùúûüýÿ'-]");
					for (String m : mots) {
						ensembleMots.add(m);
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			fileReader.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(chemin), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int i = 0;

		for (String l : ensembleMots) {
			i = 0;
			for (String line2 : lines) {
				i++;
				Matching m = new Matching(line2, l);
				m.match();
				ArrayList<Integer> ind = m.getIndices();
				if (ind.size() == 0) {
					continue;
				} else {
					for (Integer b : ind) {
						StringBuilder toWrite = new StringBuilder();
						if (freq.containsKey(l)) {
							toWrite.append(i + "," + b);
							freq.get(l).add(toWrite.toString());
						} else {
							toWrite.append(i + "," + b);
							freq.put(l, new ArrayList<>());
							freq.get(l).add(toWrite.toString());
						}
					}
				}
			}
		}
		HashMap<String, ArrayList<String>> sortedMap = freq.entrySet().stream()
				.sorted(comparingInt(e -> e.getValue().size()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
					throw new AssertionError();
				}, LinkedHashMap::new));

		freq = sortedMap;

	}

	public void printeur() {
		Iterator<String> iterateur = freq.keySet().iterator();
		while (iterateur.hasNext()) {
			String key = iterateur.next();
			System.out.println(key + ": " + freq.get(key));
		}
	}

	public static void main(String args[]) {
		RadixTree rt = new RadixTree();
		rt.listeMots("/users/nfs/Etu5/3408625/Bureau/S3/DAAR/Indexing/bab1.txt");
		rt.printeur();
	}

}
