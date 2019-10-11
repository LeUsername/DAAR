package kmp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe qui implémente l'algorithme KMP
 * @author 3408625
 *
 */
public class Matching {
	
	/**
	 * Décomposition en char du motif que nous cherchons
	 */
	private char[] facteur;
	
	/**
	 * Un tableau qui nous dit a quel indice de notre motif nous devont retourner si nous ne reconnaissons rien avec ce que nous avons vu jusqu'à present
	 */
	private int[] retenue;

	/**
	 * Si on reconnais plusieurs fois le motif dans le texte on garde les indices de chacun de ses mots
	 */
	private ArrayList<Integer> indices;

	/**
	 * Le texte sur lequel nous cherchons le motif
	 */
	private String texte = "";

	public Matching(String chemin, String facteur) {
		this.texte = chemin;

		this.facteur = new char[facteur.length()];
		for (int i = 0; i < facteur.length(); i++) {
			this.facteur[i] = facteur.charAt(i);
		}
		this.retenue = new int[facteur.length() + 1];
		computeRetenue();
		indices = new ArrayList<>();
	}

	/**
	 * Fonction qui nous permet de calculer correctement la table de retenue
	 */
	private void computeRetenue() {
		if (facteur.length == 0) {
			return;
		}
		if (retenue.length == 0) {
			return;
		}
		retenue[0] = -1;
		for (int i = 1; i < retenue.length - 1; i++) {
			if (facteur[i] == facteur[0]) {
				retenue[i] = -1;
			} else {
				retenue[i] = 0;
			}
		}
		int a = 0;
		int b = 0;
		for (int i = 1; i < retenue.length - 1; i++) {
			if (retenue[i] == -1) {
				continue;
			} else {
				a = i;
				for (int j = 1; j < a; j++) {
					if (facteur[j] != facteur[b]) {
						continue;
					}
					b++;
				}
				retenue[i] = b;
				b = 0;
			}
		}
		/**
		 * On repasse sur le tableau de retenue pour que les memes lettres renvoient a leur premiere occurence
		 */
		HashMap<String, Integer> correspondanceLettresIndice = new HashMap<>();
		correspondanceLettresIndice.put(String.valueOf(facteur[0]), 0);
		for (int i = 1; i < facteur.length; i++) {
			if (retenue[i] != -1) {
				if (correspondanceLettresIndice.get(String.valueOf(facteur[i])) == null) {
					correspondanceLettresIndice.put(String.valueOf(facteur[i]), i);
				} else {
						retenue[i] = retenue[correspondanceLettresIndice.get(String.valueOf(facteur[i]))];
				}
			}
		}

		retenue[retenue.length - 1] = 0;
	}

	/**
	 * L'algorithme de KMP qui fonctionne avec le tableau de retenue 
	 */
	public void match() {
		if (facteur.length == 0) {
			return;
		}
		if (retenue.length == 0) {
			return;
		}
		int i = 0;
		int j = 0;
		while (i < texte.length()) {
			if (j == facteur.length) {
				indices.add(i - facteur.length);
				i++;
				j = 0;
			}
			if (i >= texte.length()) {
				i = texte.length() - 1;
			}
			if (texte.charAt(i) == facteur[j]) {
				i++;
				j++;
			} else {
				if (retenue[j] == -1) {
					i++;
					j = 0;
				} else {
					j = retenue[j];
				}
			}
		}
		if (j == facteur.length) {
			indices.add(i - facteur.length);
		}
	}

	public ArrayList<Integer> getIndices() {
		return indices;
	}
}
