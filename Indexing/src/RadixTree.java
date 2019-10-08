import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;

public class RadixTree {

	Tuple value;
	ArrayList<RadixTree> fils = new ArrayList<>();

	public RadixTree(Tuple v) {
		value = v;
	}

	public RadixTree(String v) {
		value = new Tuple(v);
	}

	public Tuple getValue() {
		return value;
	}

	public void setValue(Tuple value) {
		this.value = value;
	}

	public ArrayList<RadixTree> getFils() {
		return fils;
	}

	public void setFils(ArrayList<RadixTree> fils) {
		this.fils = fils;
	}

	public void build(String chemin) {
		System.out.println("start");
		String line = null;
		Set<String> ensembleMots = new HashSet<>();

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(chemin);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			try {
				while ((line = bufferedReader.readLine()) != null) {
					String[] mots = line.split("[^A-Za-z'àáâãäåçèéêëìíîïðòóôõöùúûüýÿ'-]");
					for (String m : mots) {
						if (m.length() <= 2) {
							continue;
						}
						ensembleMots.add(m.toLowerCase());
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
			for (String l2 : lines) {
				String line2 = l2.toLowerCase();
				i++;
				Matching m = new Matching(line2, l);
				m.match();
				ArrayList<Integer> ind = m.getIndices();
				if (ind.size() == 0) {
					continue;
				} else {
					for (Integer b : ind) {
						StringBuilder toWrite = new StringBuilder();
						toWrite.append(i + "," + b);
						ArrayList<String> occurence = new ArrayList<>();
						occurence.add(toWrite.toString());
						Tuple toAdd = new Tuple(l, occurence);
						add(toAdd);
					}
				}
			}
		}
	}

	public void add(Tuple tuple) {
		if (tuple.mot.length() == 0) {
			return;
		}
		String reste = tuple.mot;
		RadixTree rt = null;
		ArrayList<RadixTree> current = fils;
		if (current.size() <= 0) {
			current.add(new RadixTree(tuple));
		} else {
			String prefixe;
			int i = 0;
			while (current.size() > 0 && current.size() > i) {
				rt = current.get(i);
				if (rt.value.mot.equals(reste)) {
					rt.value.occurences.add(tuple.occurences.get(0));
					return;
				} else {
					if (isPrefix(reste, rt.getValue().mot)) {
						prefixe = getPrefix(reste, rt.getValue().mot);
						reste = reste.substring(prefixe.length(), reste.length());
						if (reste.length() == 0) {
							String tmp = rt.value.mot.substring(prefixe.length(), rt.value.mot.length());
							rt.value.mot = prefixe;
							Tuple tupleFils = new Tuple();
							tupleFils.mot = tmp;
							tupleFils.occurences = rt.value.occurences;
							RadixTree rtFils = new RadixTree(tupleFils);
							rtFils.setFils(rt.fils);
							ArrayList<RadixTree> nouveauFils = new ArrayList<>();
							nouveauFils.add(rtFils);
							rt.setFils(nouveauFils);
							return;
						} else {
							current = rt.getFils();
							i = 0;
						}
					} else {
						i++;
					}
				}
			}
			Tuple tupleFils = new Tuple();
			tupleFils.mot = reste;
			tupleFils.occurences = tuple.occurences;
			RadixTree rtFils = new RadixTree(tupleFils);
			current.add(rtFils);
		}
	}

	public Tuple search(String r) {
		if (r.length() == 0) {
			return new Tuple();
		}
		String reste = r.toLowerCase();
		RadixTree rt;
		ArrayList<RadixTree> current = fils;
		if (current.size() <= 0) {
			return new Tuple();
		}
		String prefixe;
		int i = 0;
		while (current.size() > 0 && current.size() > i) {
			rt = current.get(i);
			if (rt.value.mot.equals(reste)) {
				return rt.value;
			} else {
				if (isPrefix(reste, rt.getValue().mot)) {
					prefixe = getPrefix(reste, rt.getValue().mot);
					reste = reste.substring(prefixe.length(), reste.length());
					if (reste.length() == 0) {
						return new Tuple();
					} else {
						current = rt.getFils();
						i = 0;
					}
				} else {
					i++;
				}
			}
		}
		return new Tuple();
	}

	public boolean isPrefix(String s1, String s2) {
		return s1.charAt(0) == s2.charAt(0);
	}

	public String getPrefix(String s1, String s2) {
		StringBuilder res = new StringBuilder();
		int i = 0;
		if (s1.length() > s2.length()) {
			while (i < s2.length() && s1.charAt(i) == s2.charAt(i)) {
				res.append(s1.charAt(i));
				i++;
			}
			return res.toString();
		} else {
			if (s1.length() < s2.length()) {
				while (i < s1.length() && s1.charAt(i) == s2.charAt(i)) {
					res.append(s1.charAt(i));
					i++;
				}
				return res.toString();
			} else {
				while (i < s2.length() && s1.charAt(i) == s2.charAt(i)) {
					res.append(s1.charAt(i));
					i++;
				}
				return res.toString();
			}
		}
	}

	public static void main(String args[]) {

		String fileName = "bab1.txt";
		try (Scanner scanner = new Scanner(System.in)) {
			RadixTree racine = new RadixTree("");
			racine.build(fileName);
			String[] tab = fileName.split("/");
			while (true) {
				System.out.print("Enter a word to find in " + tab[tab.length - 1]+" >>> ");
				String w = scanner.nextLine();
				Tuple res = racine.search(w);
				if (res.occurences.size() == 0) {
					System.out.println("this world never appears");
				} else {
					System.out.println("the word " + w + " appears at : [line,ind] ");
					System.out.println(res.occurences);
				}

			}

		}
	}

}
