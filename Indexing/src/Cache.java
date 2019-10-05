import static java.util.Comparator.comparingInt;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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

public class Cache {
	
	private HashMap<String, ArrayList<String>> freq = new HashMap<>();
	public static int index = 0;

	public HashMap<String, ArrayList<String>> getFreq() {
		return freq;
	}

	public void listeMots(String chemin) {
		System.out.println("start");
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
						if(m.length()<=2) {
							continue;
						}
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
		System.out.println("computing");
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
		System.out.println("computing");
		HashMap<String, ArrayList<String>> sortedMap = freq.entrySet().stream()
				.sorted(comparingInt(e -> e.getValue().size()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
					throw new AssertionError();
				}, LinkedHashMap::new));

		freq = sortedMap;
		System.out.println("finis, ecriture en cours");
		saveToFile("cache", freq);

	}

	public void printeur() {
		Iterator<String> iterateur = freq.keySet().iterator();
		while (iterateur.hasNext()) {
			String key = iterateur.next();
			System.out.println(key + ": " + freq.get(key));
		}
	}
	
	private void saveToFile(String filename, HashMap<String, ArrayList<String>> freq) {
		
		try {
			while (true) {
				BufferedReader input = new BufferedReader(
						new InputStreamReader(new FileInputStream(filename + index + ".txt")));
				try {
					input.close();
				} catch (IOException e) {
					System.err.println(
							"I/O exception: unable to close " + filename );
				}
				index++;
			}
		} catch (FileNotFoundException e) {
			printToFile(filename + Integer.toString(index) + ".txt", freq);
		}
	}

	private void printToFile(String filename, HashMap<String,ArrayList<String>> freq) {
		try {
			PrintStream output = new PrintStream(new FileOutputStream(filename));
			int x, y;
			for (String s : freq.keySet())
				output.println( (s + ":" + freq.get(s).toString()).toLowerCase());
			output.close();
		} catch (FileNotFoundException e) {
			System.err.println("I/O exception: unable to create " + filename);
		}
	}

	// FILE LOADER
	private ArrayList<Point> readFromFile(String filename) {
		String line;
		String[] coordinates;
		ArrayList<Point> points = new ArrayList<Point>();
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			try {
				while ((line = input.readLine()) != null) {
					coordinates = line.split("\\s+");
					points.add(new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
				}
			} catch (IOException e) {
				System.err.println("Exception: interrupted I/O.");
			} finally {
				try {
					input.close();
				} catch (IOException e) {
					System.err.println("I/O exception: unable to close " + filename);
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Input file not found.");
		}
		return points;
	}

	public static void main(String args[]) {
		Cache cache = new Cache();
		cache.listeMots("/users/nfs/Etu5/3408625/Bureau/S3/DAAR/Indexing/bab1.txt");
//		rt.printeur();
	}

}
