package kmp;

public class Main {

	public static void main(String[] args) {
		Matching m = new Matching("/users/nfs/Etu5/3408625/Bureau/S3/DAAR/daar-projet-offline/bab1.txt", "Sargon");
		System.out.print("Word find at i = ");
		m.match();
		System.out.println(m.getIndices());

	}

}
