package automate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import tools.Etoile;

/**
 * Classe qui represente un automate determinise et minimise.
 * 
 * @author 3408625
 */
public class AutomateDeterministe {
	static final int NB_TRANSITIONS = 256;

	// les états de départ
	private ArrayList<Integer> start = new ArrayList<Integer>();

	// les états finaux
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
		 * L'etat initial correspond forcement au premier etat etant donne qu'on
		 * (re)nomme tous les etats a chaque creation d'automate de maniere a avoir 0 en
		 * premier etat
		 */
		start = etats.get(0);
		/*
		 * Correspondance anciens etats - nouveaux etats
		 */
		Map<Integer, Integer> correspondance = new HashMap<Integer, Integer>();
		boolean appartientEtoile = false;
		int etatCourant = 0;
		boolean ajoutFait = false;

		/*
		 * Dictionnaire permettant de retrouver les etats de l'ancien automate qui sont
		 * presents dans plusieurs etats du nouvel automate deterministe
		 */
		Map<Integer, Set<Integer>> etatsCommuns = new HashMap<>();

		for (ArrayList<Integer> e : etats) {
			for (Integer ancienEtat : e) {
				/*
				 * On verifie si l'etat fait partie d'une etoile et peut donc etre groupe avec
				 * les autres etats de l'etoile
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
				 * Les etats finaux de l'automate determinise sont tous les nouveaux etats qui
				 * ont ete crees depuis l'etat final de l'ancien automate
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
