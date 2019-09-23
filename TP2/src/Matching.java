import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Matching {
	private char[] facteur;
	private int[] retenue;

	private String texte = "";

	public Matching(String chemin, String facteur) {
		String line = null;
		try {
			FileReader fileReader = new FileReader(chemin);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				texte = texte + line;
				texte = texte + "\n";
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + chemin + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + chemin + "'");
		}

		this.facteur = new char[facteur.length()];
		for (int i = 0; i < facteur.length(); i++) {
			this.facteur[i] = facteur.charAt(i);
		}
		this.retenue = new int[facteur.length() + 1];
		computeRetenue();
		
	}

	private void computeRetenue() {
		retenue[0] = -1;
		int tmp = 0;
		for (int i = 1; i < retenue.length - 1; i++) {
			if (facteur[i] == facteur[0]) {
				retenue[i] = -1;
				tmp = i;
			} else {
				retenue[i] = 0;
			}
		}
		int a = 0;
		int b = 0;
		int c = 0;
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

	public int match() {
		int i = 0;
		int j = 0;
		while (i < texte.length()) {
			if (j == facteur.length) {
				return i - facteur.length;
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
			return i - facteur.length;
		} else {
			return -1;
		}
	}
}
