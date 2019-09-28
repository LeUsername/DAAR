import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AutomateDeterministe {

	static final int NB_TRANSITIONS = 256;

	private ArrayList<Integer> start = new ArrayList<Integer>();
	private Set<Integer> end = new HashSet<Integer>();

	private int nbStates = 0;
	private int nbTransitions = 0;

	private int[][] autom;

	public AutomateDeterministe(ArrayList<ArrayList<Integer>> etats, Automate a) {
		nbStates = etats.size();
		autom = new int[nbStates][NB_TRANSITIONS];

		for (int i = 0; i < nbStates; i++) {
			for (int j = 0; j < NB_TRANSITIONS; j++) {
				autom[i][j] = -1;
			}
		}
		/*
		 * L'état initial correspond forcément au premier état étant donné qu'on
		 * (re)nomme à chaque création d'automate de manière à avoir 0 en premier état
		 */
		start = etats.get(0);
		/*
		 * Correspondance anciens états - nouveaux états
		 */
		Map<Integer, Integer> correspondance = new HashMap<Integer, Integer>();
		boolean appartientEtoile = false;
		int etatCourant = 0;
		boolean ajoutFait = false;

		/*
		 * Dictionnaire permettant de retrouver les états de l'ancien automate qui sont
		 * présents dans plusieurs états du nouvel automate déterministe
		 */
		Map<Integer, Set<Integer>> etatsCommuns = new HashMap<>();

		for (ArrayList<Integer> e : etats) {
			for (Integer ancienEtat : e) {
				/*
				 * On verifie si l'etat fait partie d'une etoile et peut donc etre groupé avec
				 * les autres états de l'étoile
				 */
				for (Tuple t : a.getEtoile()) {
					if (t.x <= ancienEtat && ancienEtat <= t.y) {
						appartientEtoile = true;
						if (correspondance.get(t.x) == null) {
							correspondance.put(t.x, etatCourant);
						}
						correspondance.putIfAbsent(ancienEtat, correspondance.get(t.x));
						break;
					}
				}
				if (!appartientEtoile) {
					if (correspondance.containsKey(ancienEtat)) {
						if (!etatsCommuns.containsKey(ancienEtat)) {
							etatsCommuns.put(ancienEtat, new HashSet<Integer>());
						}
						etatsCommuns.get(ancienEtat).add(etatCourant);
					}
					correspondance.putIfAbsent(ancienEtat, etatCourant);
				} else {
					appartientEtoile = false;
				}
			}
			if (appartientEtoile) {
				appartientEtoile = false;
			}
			etatCourant++;
		}

		Iterator<Integer> iterateur = correspondance.keySet().iterator();
//		for (int i = 0; i < a.getNbStates(); i++) {
//			for (int j = i + 1; j < a.getNbStates(); j++) {
//				for (int t = 0; t < NB_TRANSITIONS; t++) {
//					int avecI = a.getAutom()[i][t];
//					int avecJ = a.getAutom()[j][t];
//					if (avecI == -1 || avecJ == -1) {
//						continue;
//					}
//					if (avecI == avecJ) {
//						System.out.println("dedans hehe");
////						autom[correspondance.get(i)][t] = avecI;
////						autom[correspondance.get(j)][t] = avecI;
//						nbTransitions++;
//					}
//
//				}
//			}
//		}
		while (iterateur.hasNext()) {
			Integer ancienEtat = iterateur.next();
			if (ancienEtat == a.getEnd()) {
				/*
				 * Les états finaux de l'automate déterminisé sont tous les nouveaux états qui
				 * ont été créés depuis l'état final de l'ancien automate
				 */
				end.add(correspondance.get(ancienEtat));
			}
			for (int j = 0; j < NB_TRANSITIONS; j++) {
				if (a.getAutom()[ancienEtat][j] != -1) {
					Set<Integer> listeEtats = etatsCommuns.get(ancienEtat);
					if (listeEtats != null) {
						if (listeEtats.size() >= 1) {
							for (Integer e : etatsCommuns.get(ancienEtat)) {
								autom[e][j] = correspondance.get(a.getAutom()[ancienEtat][j]);
								nbTransitions++;
							}
						}
					}
					autom[correspondance.get(ancienEtat)][j] = correspondance.get(a.getAutom()[ancienEtat][j]);
					nbTransitions++;
				}
			}
		}
//		start = starts;
//		end = ends;
//		nbStates = nbS;
//		nbTransitions = nbT;		
//		autom = new int[nbStates][NB_TRANSITIONS];
//		
//		for (int i = 0; i < nbStates; i++) {
//			for (int j = 0; j < NB_TRANSITIONS; j++) {
//				autom[i][j] = transitions[i][j];
//			}
//		}
	}

	public ArrayList<Integer> getStart() {
		return start;
	}

	public void setStart(ArrayList<Integer> start) {
		this.start = start;
	}

	public Set<Integer> getEnd() {
		return end;
	}

	public void setEnd(Set<Integer> end) {
		this.end = end;
	}

	public int[][] getAutom() {
		return autom;
	}

	public void affiche() {
		System.out.println("transitions :");
		for (int i = 0; i < nbStates; i++) {
			for (int j = 0; j < NB_TRANSITIONS; j++) {
				if (autom[i][j] != -1) {
					System.out.print(i + " ");
					System.out.print(j + " ");
					System.out.println(autom[i][j]);
				}
			}
		}
	}

	public void addTransition(int a, int b, int t) {
		autom[a][t] = b;
	}

}
