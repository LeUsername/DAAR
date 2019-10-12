package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import automate.Automate;
import automate.AutomateBuilder;
import automate.AutomateDeterministe;
import automate.RegEx;
import automate.RegExTree;
import kmp.Matching;
import radixtree.RadixTree;
import tools.Tuple;

/**
 * Classe qui represente notre clone de egrep.
 * 
 * @author 3408625
 *
 */
public class EGrepClone {

	static boolean automate = true;
	static boolean kmp = true;

	/**
	 * Variables nous permettant d'afficher en couleur les match. Ne fonctionne pas
	 * sur la console Eclipse
	 */
	static String BLACK = "\u001B[0m";
	static String AUTRE = "\u001B[103m";

	static String chemin = "files/";

	static boolean isRegExp(String s) {
		for (int i = 1; i < s.length(); i++) {
			if (s.charAt(i) == '.' && s.charAt(i - 1) != '\\') {
				return true;
			}
		}
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '|' || s.charAt(i) == '*') {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String motif = args[0];
		String fileName = args[1];
		// System.out.print(">> entrez le motif a rechercher : ");
		// String motif = scanner.next();
		// System.out.println("\n");
		// scanner.reset();
		// System.out.print(">> entrez le nom du fichier (filename.txt) : ");
		// String fileName = scanner.next();
		fileName = chemin + fileName;

		/*
		 * Si le motif rechercher est une regex nous utilisons la methode des automates
		 * pour le rechercher sinon si c'est juste une concatenation de caracteres
		 * alphanumeriques nous utilisons l'algorithme de KMP
		 */
		if (automate) {
			RegEx regEx = new RegEx();
			regEx.setRegEx(motif);
			RegExTree regExTree = null;
			try {
				regExTree = regEx.parse();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.out.print("\nThe regExp Tree is : ");
			System.out.println(regExTree.toString());
			AutomateBuilder ab = new AutomateBuilder();
			Automate gerp = ab.conversion(regExTree.toString());
			AutomateDeterministe grep = ab.determinise(gerp);
			try {
				Instant start = Instant.now();
				String ligne = null;
				String[] mots = null;
				FileReader f = new FileReader(fileName);
				BufferedReader b = new BufferedReader(f);
				while ((ligne = b.readLine()) != null) {
					mots = ligne.split(" ");
					ArrayList<String> matched = new ArrayList<>();
					for (String m : mots) {
						if (m == "") {
							continue;
						}

						if (ab.monGrep(grep, m)) {
							matched.add(m);
						}
					}
					if (matched.size() == 0) {
						continue;
					}
					for (String m : mots) {
						if (matched.contains(m)) {
							System.out.print(AUTRE + m);
						} else {
							System.out.print(BLACK + m);
						}
						System.out.print(BLACK + " ");
					}
					System.out.println();
				}
				Instant end = Instant.now();
				System.out.println(Duration.between(start, end));
				b.close();
			} catch (FileNotFoundException e) {
				System.out.println("Impossible de trouver le fichier");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				scanner.close();
			}
		} else if (kmp) {
			Instant start = Instant.now();
			List<String> lines = Collections.emptyList();
			try {
				lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String line : lines) {
				Matching m = new Matching(line, motif);
				m.match();
				ArrayList<Integer> ind = m.getIndices();
				if (ind.size() == 0) {
					continue;
				} else {
					for (String m2 : line.split(" ")) {
						if (m2.contains(motif)) {
							for (int i = 0; i < m2.length(); i++) {
								if (m2.charAt(i) != motif.charAt(0)) {
									System.out.print(BLACK + m2.charAt(i));
								} else {
									System.out.print(AUTRE + motif);
									i += motif.length() - 1;
								}
							}
						} else {
							System.out.print(BLACK + m2);
						}
						System.out.print(BLACK + " ");
					}
					System.out.println();
				}
			}
			Instant end = Instant.now();
			System.out.println(Duration.between(start, end));
		} else {
			RadixTree racine = new RadixTree("");
			racine.build(fileName);
			String w = motif;
			Instant start = Instant.now();
			Tuple res = racine.search(w);
			if (res.occurences.size() == 0) {
				System.out.println("this word never appears");
			} else {
				System.out.println("the word " + w + " appears at : [line,ind] ");
				System.out.println(res.occurences);

			}
			FileReader fileReader = null;
			try {
				fileReader = new FileReader(fileName);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int i = 0;
			String ligne = null;
			String courant = null;
			ArrayList<String> occ = res.occurences;
			try {
				while (occ.size() > 0 && (ligne = bufferedReader.readLine()) != null) {
					i++;
					if (courant == null) {
						courant = occ.remove(0).split(",")[0];
					}
					if (String.valueOf(i).equals(courant)) {
						System.out.println(ligne);
						courant = null;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Instant end = Instant.now();
			System.out.println(Duration.between(start, end));
		}
	}
}
