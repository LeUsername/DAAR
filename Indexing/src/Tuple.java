import java.util.ArrayList;

public class Tuple {
	ArrayList<String> occurences;
	String mot;

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
