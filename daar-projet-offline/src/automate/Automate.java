package automate;

import java.util.ArrayList;

import tools.Etoile;

/**
 * Classe qui represente un automate dont les transitions correspondent aux
 * lettres.
 * 
 * @author 3408625
 *
 */
public class Automate {

	static final int NB_TRANSITIONS = 256;

	// Etat de depart
	private int start = -1;

	// Etat de fin
	private int end = -1;

	// Nb d'etats dans l'automate
	private int nbStates = 0;

	// Nb de transitions dans l'automate
	private int nbTransitions = 0;

	// representation de l'automate : autom[i][t] = j : de l'etat i on peut aller a
	// j avec la transition t si j est a -1 la transition n'est pas possible
	private int[][] autom;

	// representation de toutes les epsilons transitions
	private int[][] epsilon;

	// toutes les étoiles de l'automate
	private ArrayList<Etoile> etoile = new ArrayList<>();

	public Automate(int s, int e, int nbS, int nbT) {
		start = s;
		end = e;
		nbStates = nbS;
		nbTransitions = nbT;

		autom = new int[nbStates][NB_TRANSITIONS];

		for (int i = 0; i < nbStates; i++) {
			for (int j = 0; j < NB_TRANSITIONS; j++) {
				autom[i][j] = -1;
			}
		}

		epsilon = new int[nbStates][nbStates];

		for (int i = 0; i < nbStates; i++) {
			for (int j = 0; j < nbStates; j++) {
				epsilon[i][j] = 0;
			}
		}

	}

	public void modifEps(int i, int j) {
		epsilon[i][j] = 0;
	}

	public void addTransition(int a, int b, int t) {
		autom[a][t] = b;
	}

	public void addEpsilon(int a, int b) {
		epsilon[a][b] = 1;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getNbStates() {
		return nbStates;
	}

	public void setNbStates(int nbStates) {
		this.nbStates = nbStates;
	}

	public int getNbTransitions() {
		return nbTransitions;
	}

	public void setNbTransitions(int nbTransitions) {
		this.nbTransitions = nbTransitions;
	}

	public int[][] getAutom() {
		return autom;
	}

	public void setAutom(int[][] autom) {
		this.autom = autom;
	}

	public int[][] getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(int[][] epsilon) {
		this.epsilon = epsilon;
	}

	public ArrayList<Etoile> getEtoile() {
		return etoile;
	}

	public void setEtoile(ArrayList<Etoile> etoile) {
		this.etoile = etoile;
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

	public void afficheEpsilon() {
		System.out.println("transitions epislon :");
		for (int i = 0; i < nbStates; i++) {
			for (int j = 0; j < nbStates; j++) {
				if (epsilon[i][j] != 0) {
					System.out.print(i + " ");
					System.out.print(j + " ");
					System.out.println(epsilon[i][j]);
				}
			}
		}
	}
}
