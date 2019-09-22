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
		int[][] tmp = new int[nbStates][NB_TRANSITIONS];
		for (int i = 0; i < nbStates; i++) {
			for (int j = 0; j < NB_TRANSITIONS; j++) {
				autom[i][j] = -1;
				tmp[i][j] = -1;
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
		int etatCourant = 0;
		for (ArrayList<Integer> e : etats) {
			for (Integer ancienEtat : e) {
				correspondance.putIfAbsent(ancienEtat, etatCourant);
			}
			etatCourant++;
		}
		Iterator<Integer> iterateur = correspondance.keySet().iterator();
		while (iterateur.hasNext()) {
			Integer ancienEtat = iterateur.next();
			for (int j = 0; j < NB_TRANSITIONS; j++) {
				if (a.getAutom()[ancienEtat][j] != -1) {
					autom[correspondance.get(ancienEtat)][j] = correspondance.get(a.getAutom()[ancienEtat][j]);
					/*
					 * Les �tats finaux de l'automate d�terminis� sont tous les nouveaux �tats qui
					 * ont �t� cr��s depuis l'�tat final de l'ancien automate
					 */
					if (ancienEtat == a.getEnd()) {
						end.add(correspondance.get(ancienEtat));
					}
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
