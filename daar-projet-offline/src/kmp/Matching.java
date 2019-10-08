package kmp;
import java.util.ArrayList;
import java.util.HashMap;

public class Matching {
	private char[] facteur;
	private int[] retenue;

	private ArrayList<Integer> indices;

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
