package kmp;

public class TestKMP {

	public static void main(String[] args) {
		Matching m = new Matching("files/bab1.txt", "Sargon");
		System.out.print("Word find at i = ");
		m.match();
		System.out.println(m.getIndices());
	}

}
