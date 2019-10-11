package automate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import tools.Etoile;

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
		 * L'�tat initial correspond forc�ment au premier �tat �tant donn� qu'on
		 * (re)nomme � chaque cr�ation d'automate de mani�re � avoir 0 en premier �tat
		 */
		start = etats.get(0);
		/*
		 * Correspondance anciens �tats - nouveaux �tats
		 */
		Map<Integer, Integer> correspondance = new HashMap<Integer, Integer>();
		boolean appartientEtoile = false;
		int etatCourant = 0;
		boolean ajoutFait = false;

		/*
		 * Dictionnaire permettant de retrouver les �tats de l'ancien automate qui sont
		 * pr�sents dans plusieurs �tats du nouvel automate d�terministe
		 */
		Map<Integer, Set<Integer>> etatsCommuns = new HashMap<>();

		for (ArrayList<Integer> e : etats) {
			for (Integer ancienEtat : e) {
				/*
				 * On verifie si l'etat fait partie d'une etoile et peut donc etre group� avec
				 * les autres �tats de l'�toile
				 */
				for (Etoile t : a.getEtoile()) {
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
		while (iterateur.hasNext()) {
			Integer ancienEtat = iterateur.next();
			if (ancienEtat == a.getEnd()) {
				/*
				 * Les �tats finaux de l'automate d�terminis� sont tous les nouveaux �tats qui
				 * ont �t� cr��s depuis l'�tat final de l'ancien automate
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
