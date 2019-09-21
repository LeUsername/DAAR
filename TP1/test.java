import java.util.ArrayList;
import java.util.Scanner;

public class test {

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

	public static void determinise(Automate a) {
//		Map<ArrayList<Integer>, Integer> etats = new HashMap<>();
//		ArrayList<Integer> etat = new ArrayList<>();
//		Integer nouveauIdentifiant = 0;
//
//		for (int i = 0; i < a.getNbStates(); i++) {
//			eps(a, etat, i);
//			Iterator<ArrayList<Integer>> iterateur = etats.keySet().iterator();
//			if (etats.size() > 0) {
//				while (iterateur.hasNext()) {
//					ArrayList<Integer> e = iterateur.next();
//					if (!e.containsAll(etat)) {
//						etats.put(etat, nouveauIdentifiant);
//						nouveauIdentifiant++;
//						break;
//					}
//				}
//			} else {
//				etats.put(etat, nouveauIdentifiant);
//				nouveauIdentifiant++;
//			}
//			System.out.println(etat);
//			etat = new ArrayList<Integer>();
//		}
//		Iterator<ArrayList<Integer>> iterateur = etats.keySet().iterator();
//		while (iterateur.hasNext()) {
//			ArrayList<Integer> e = iterateur.next();
//			for (Integer i : e) {
//				System.out.print(i);
//			}
//			System.out.println("---");
//		}
		ArrayList<ArrayList<Integer>> etats = new ArrayList<>();
		ArrayList<Integer> etat = new ArrayList<>();

		for (int i = 0; i < a.getNbStates(); i++) {
			eps(a, etat, i);
			if (etats.size() > 0) {
				for (ArrayList<Integer> e : etats) {
					if (!e.containsAll(etat)) {
						if (etat.get(0) == 5) {
							System.out.println(etat);
							System.out.println(e);
						}
						etats.add(etat);
						break;
					}
				}
			} else {
				etats.add(etat);
			}
			System.out.println(etat);
			etat = new ArrayList<Integer>();
		}

		for (ArrayList<Integer> e : etats) {

			for (Integer i : e) {
				System.out.print(i);
			}
			System.out.println("---");
		}
	}

	private static void eps(Automate a, ArrayList<Integer> etat, int d) {
		etat.add(d);
		for (int j = 0; j < a.getNbStates(); j++) {
			if (a.getEpsilon()[d][j] == 1) {
				a.modifEps(d, j);
				eps(a, etat, j);
			}
		}

	}

	private static String regExTree;

	public static void main(String arg[]) {
		if (arg.length != 0) {
			regExTree = arg[0];
		} else {
			Scanner scanner = new Scanner(System.in);
			System.out.print("  >> Please enter a regEx Tree: ");
			regExTree = scanner.next();
		}
		System.out.println("---Voici les transitions de votre automate---");
		Automate test = conversion(regExTree);
		test.affiche();
		System.out.println("-------");
		test.afficheEpsilon();
		System.out.println("-------");
		determinise(test);

	}
}
