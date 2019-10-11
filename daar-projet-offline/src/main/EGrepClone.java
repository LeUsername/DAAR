package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

public class EGrepClone {

	static String BLACK = "\u001B[0m";
	static String AUTRE = "\u001B[103m";

	static String chemin = "files/";

	static boolean isRegExp(String s) {
		for (int i = 1; i < s.length(); i++) {
			if (s.charAt(i) == '.' && s.charAt(i-1) != '\\') {
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
		System.out.print(">> entrez le motif a rechercher :  ");
		String motif = scanner.next();
		System.out.println("\n");
		scanner.reset();
		System.out.print(">> entrez le nom du fichier (filename.txt) :  ");
		String fileName = scanner.next();
		fileName = chemin + fileName;

		if (isRegExp(motif)) {

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
				b.close();
			} catch (FileNotFoundException e) {
				System.out.println("Impossible de trouver le fichier");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				scanner.close();
			}
		} else

		{
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
		}
	}
}
