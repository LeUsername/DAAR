package automate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import tools.Etoile;

public class AutomateBuilder {

	public static Automate unitaire(int t) {
		Automate a = new Automate(0, 1, 2, 1);
		a.addTransition(0, 1, t);
		return a;
	}

	public static Automate union(Automate a1, Automate a2) {
		int start = 0;
		int end = a1.getNbStates() + a2.getNbStates() + 1;
		int nbStates = a1.getNbStates() + a2.getNbStates() + 2;
		int nbTransitions = a1.getNbTransitions() + a2.getNbTransitions();
		Automate res = new Automate(start, end, nbStates, nbTransitions);

		for (int i = 0; i < a1.getNbStates(); i++) {
			for (int j = 0; j < Automate.NB_TRANSITIONS; j++) {
				if (a1.getAutom()[i][j] != -1) {
					res.addTransition(i + 1, a1.getAutom()[i][j] + 1, j);
				}
			}
		}
		for (int i = 0; i < a2.getNbStates(); i++) {
			for (int j = 0; j < Automate.NB_TRANSITIONS; j++) {
				if (a2.getAutom()[i][j] != -1) {
					res.addTransition(i + a1.getNbStates() + 1, a2.getAutom()[i][j] + a1.getNbStates() + 1, j);
				}
			}
		}
		for (int i = 0; i < a1.getEpsilon().length; i++) {
			for (int j = 0; j < a1.getEpsilon()[0].length; j++) {
				if (a1.getEpsilon()[i][j] == 1) {
					res.addEpsilon(i + 1, j + 1);
				}
			}
		}
		for (int i = 0; i < a2.getEpsilon().length; i++) {
			for (int j = 0; j < a2.getEpsilon()[0].length; j++) {
				if (a2.getEpsilon()[i][j] == 1) {
					res.addEpsilon(i + a1.getNbStates() + 1, j + a1.getNbStates() + 1);
				}
			}
		}

		res.addEpsilon(res.getStart(), a1.getStart() + 1);
		res.addEpsilon(a1.getEnd() + 1, res.getEnd());

		res.addEpsilon(res.getStart(), a2.getStart() + a1.getNbStates() + 1);
		res.addEpsilon(a2.getEnd() + a1.getNbStates() + 1, res.getEnd());

		ArrayList<Etoile> etoile = new ArrayList<Etoile>();
		for (Etoile e : a1.getEtoile()) {
			etoile.add(new Etoile(e.x + 1, e.y + 1));
		}
		for (Etoile e : a2.getEtoile()) {
			etoile.add(new Etoile(e.x + a1.getNbStates() + 1, e.y + a1.getNbStates() + 1));
		}
		res.setEtoile(etoile);

		return res;
	}

	public static Automate concat(Automate a1, Automate a2) {
		int start = a1.getStart();
		int end = a1.getNbStates() + a2.getEnd();
		int nbStates = a1.getNbStates() + a2.getNbStates();
		int nbTransitions = a1.getNbTransitions() + a2.getNbTransitions();
		Automate res = new Automate(start, end, nbStates, nbTransitions);

		for (int i = 0; i < a1.getNbStates(); i++) {
			for (int j = 0; j < Automate.NB_TRANSITIONS; j++) {
				res.addTransition(i, a1.getAutom()[i][j], j);
			}
		}
		for (int i = a1.getNbStates(); i < res.getNbStates(); i++) {
			for (int j = 0; j < Automate.NB_TRANSITIONS; j++) {
				if (a2.getAutom()[i - a1.getNbStates()][j] != -1) {
					res.addTransition(i, a2.getAutom()[i - a1.getNbStates()][j] + a1.getNbStates(), j);
				}
			}
		}
		for (int i = 0; i < a1.getEpsilon().length; i++) {
			for (int j = 0; j < a1.getEpsilon()[0].length; j++) {
				if (a1.getEpsilon()[i][j] == 1) {
					res.addEpsilon(i, j);
				}
			}
		}

		for (int i = 0; i < a2.getEpsilon().length; i++) {
			for (int j = 0; j < a2.getEpsilon()[0].length; j++) {
				if (a2.getEpsilon()[i][j] == 1) {
					res.addEpsilon(i + a1.getNbStates(), j + a1.getNbStates());
				}
			}
		}
		res.addEpsilon(a1.getEnd(), a2.getStart() + a1.getNbStates());

		ArrayList<Etoile> etoile = new ArrayList<Etoile>();
		for (Etoile e : a1.getEtoile()) {
			etoile.add(new Etoile(e.x, e.y));
		}
		for (Etoile e : a2.getEtoile()) {
			etoile.add(new Etoile(e.x + a1.getNbStates(), e.y + a1.getNbStates()));
		}
		res.setEtoile(etoile);

		return res;
	}

	public static Automate etoile(Automate a1) {
		int start = 0;
		int end = a1.getNbStates() + 1;
		int nbStates = a1.getNbStates() + 2;
		Automate res = new Automate(start, end, nbStates, a1.getNbTransitions());

		for (int i = 0; i < a1.getNbStates(); i++) {
			for (int j = 0; j < Automate.NB_TRANSITIONS; j++) {
				if (a1.getAutom()[i][j] != -1) {
					res.addTransition(i + 1, a1.getAutom()[i][j] + 1, j);
				}
			}
		}
		for (int i = 0; i < a1.getEpsilon().length; i++) {
			for (int j = 0; j < a1.getEpsilon()[0].length; j++) {
				if (a1.getEpsilon()[i][j] == 1) {
					res.addEpsilon(i + 1, j + 1);
				}
			}
		}
		res.addEpsilon(start, a1.getStart() + 1);
		res.addEpsilon(a1.getEnd() + 1, end);
		res.addEpsilon(start, end);
		res.addEpsilon(a1.getEnd() + 1, a1.getStart() + 1);

		ArrayList<Etoile> etoile = new ArrayList<Etoile>();
		etoile.add(new Etoile(start + 1, end - 1));
		res.setEtoile(etoile);

		return res;
	}

	public static Automate binaire(char operateur, String expression) {
		int cptParenthese = 1;
		StringBuilder trucAGauche = new StringBuilder();
		StringBuilder trucADroite = new StringBuilder();
		boolean virgule = false;
		char courant = '\u0000'; // Valeur null pour char
		for (int i = 2; i < expression.length(); i++) {
			courant = expression.charAt(i);
			if (courant == '(') {
				cptParenthese++;
			} else if (courant == ')') {
				cptParenthese--;
				if (cptParenthese == 0) { // Si on en est à la parenthèse de fin on sort
					break;
				}
			}
			if (courant == ',' && cptParenthese == 1) {
				virgule = true;
				continue;
			}
			if (!virgule) { // Si on n'a pas encore vu la virgule de séparation, on est dans le fils de
							// gauche
				trucAGauche.append(courant);
			} else {
				trucADroite.append(courant);
			}
		}
		if (expression.charAt(0) == '|') {
			return union(conversion(trucAGauche.toString()), conversion(trucADroite.toString()));
		} else if (expression.charAt(0) == '.') {
			return concat(conversion(trucAGauche.toString()), conversion(trucADroite.toString()));
		} else {
			return null; // NE DOIT PAS RENTRER DANS CE CAS
		}
	}

	public static Automate conversion(String expression) {
		switch (expression.charAt(0)) {
		case '|':
			return binaire('|', expression);
		case '.':
			return binaire('.', expression);
		case '*':
			int cptParenthese = 1;
			StringBuilder truc = new StringBuilder();
			char courant = '\u0000'; // Valeur null pour char
			for (int i = 2; i < expression.length(); i++) {
				courant = expression.charAt(i);
				if (courant == '(') {
					cptParenthese++;
				} else if (courant == ')') {
					cptParenthese--;
					if (cptParenthese == 0) { // Si on en est à la parenthèse de fin on sort
						break;
					}
				}
				truc.append(courant);
			}
			return etoile(conversion(truc.toString()));
		default:
			return unitaire((int) expression.charAt(0));
		}
	}

	public static AutomateDeterministe determinise(Automate a) {

		ArrayList<ArrayList<Integer>> etats = new ArrayList<>();
		ArrayList<Integer> etat = new ArrayList<>();
		ArrayList<Integer> etatDejaTraites = new ArrayList<>();

		for (int i = 0; i < a.getNbStates(); i++) {
			if (!etatDejaTraites.contains(i)) {
				eps(a, etat, i);
				etats.add(etat);
				for (Integer e : etat) {
					etatDejaTraites.add(e);
				}
				etat = new ArrayList<Integer>();
			}
		}
		// for (ArrayList<Integer> e : etats) {
		// for (Integer i : e) {
		// System.out.print(i);
		// }
		// System.out.println("---");
		// }
		AutomateDeterministe res = new AutomateDeterministe(etats, a);
		return res;
	}

	private static void eps(Automate a, ArrayList<Integer> etat, int d) {
		etat.add(d);
		for (int j = 0; j < a.getNbStates(); j++) {
			if (a.getEpsilon()[d][j] == 1) {
				eps(a, etat, j);
			}
		}
	}

	private static boolean lecture() {
		String ligne = null;
		String[] mots = null;
		int cpt = 0;

		Scanner scanner = new Scanner(System.in);
		System.out.print("  >> Please enter a regEx: ");
		String string = scanner.next();
		RegEx regEx = new RegEx();
		regEx.setRegEx(string);
		RegExTree regExTree = null;
		try {
			regExTree = regEx.parse();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.print("  >> The regExp Tree is : ");
		System.out.println(regExTree.toString());

		Automate gerp = conversion(regExTree.toString());
		AutomateDeterministe grep = determinise(gerp);

		try {
			FileReader f = new FileReader("gutenberg.txt");
			BufferedReader b = new BufferedReader(f);
			while ((ligne = b.readLine()) != null) {
				mots = ligne.split(" ");
				for (String m : mots) {
					if (m == "") {
						continue;
					}
					if (monGrep(grep, m)) {
						cpt++;
						System.out.println(m);
					}
				}
			}
			b.close();
		} catch (FileNotFoundException e) {
			System.out.println("Impossible de trouver le fichier");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		System.out.println("I have found " + cpt + " valid words");
		return false;
	}

	public static boolean monGrep(AutomateDeterministe automate, String mot) {
		String mo = mot;
		int etatCourant = automate.getStart().get(0);
		boolean premiereLettre = false;
		ArrayList<Integer> transitionsDepuisEtatInitial = new ArrayList<>();
		ArrayList<Integer> transitionsDejaAppliquees = new ArrayList<>();
		
		for (int i = 0; i < Automate.NB_TRANSITIONS; i++) {
			if (automate.getAutom()[etatCourant][i] != -1) {
				transitionsDepuisEtatInitial.add(i);
			}
		}
		int j = 0;
		while (j < mo.length()) {
			if (!transitionsDepuisEtatInitial.contains(Integer.valueOf(((int) mo.charAt(j)) % 256))
					&& !premiereLettre) {
				transitionsDejaAppliquees.clear();
				j++;
				continue;
			} else {
				premiereLettre = true;
			}
			if ((int) mo.charAt(j) == 65279) {
				premiereLettre = false;
				j++;
				continue;
			}

			etatCourant = automate.getAutom()[etatCourant][((int) mo.charAt(j)) % 256];
			if (etatCourant == -1) {
				mo = mo.substring(1, mo.length());
				j = 0;
				premiereLettre = false;
				etatCourant = automate.getStart().get(0);
			} else {
				transitionsDejaAppliquees.add(((int) mo.charAt(j)) % 256);
				if (automate.getEnd().contains(etatCourant)) {
					return true;
				}
			}
			j++;
		}
//		for (int i = 0; i < mot.length(); i++) {
//			if (!transitionsDepuisEtatInitial.contains(Integer.valueOf(((int) mot.charAt(i)) % 256))
//					&& !premiereLettre) {
//				transitionsDejaAppliquees.clear();
//				continue;
//			} else {
//				premiereLettre = true;
//			}
//			if ((int) mot.charAt(i) == 65279) {
//				premiereLettre = false;
//				continue;
//			}
//
//			etatCourant = automate.getAutom()[etatCourant][((int) mot.charAt(i)) % 256];
//			if (etatCourant == -1) {
//				i--;
//				premiereLettre = false;
//				etatCourant = automate.getStart().get(0);
//			} else {
//				transitionsDejaAppliquees.add(((int) mot.charAt(i)) % 256);
//				if (automate.getEnd().contains(etatCourant)) {
//					return true;
//				}
//			}
//		}
		return automate.getEnd().contains(etatCourant);
	}

	// public static void main(String arg[]) {
	// System.out.println(lecture());
	// }
}
