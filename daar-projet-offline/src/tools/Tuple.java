package tools;
import java.util.ArrayList;

/**
 * Classe qui reprente un couple (A,B) où A est un mot et B la liste de toutes ses occurences : (ligne,indice)
 * @author 3408625
 *
 */
public class Tuple {
	public ArrayList<String> occurences;
	public String mot;

	public Tuple() {
		occurences = new ArrayList<>();
		mot = new String();
	}

	public Tuple(String mot) {
		occurences = new ArrayList<>();
		this.mot = mot;
	}

	public Tuple(String mot, ArrayList<String> occurences) {
		this.occurences = occurences;
		this.mot = mot;
	}

}
