import java.util.ArrayList;

public class RadixTree {

	String value = "";
	ArrayList<RadixTree> fils = new ArrayList<>();

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ArrayList<RadixTree> getFils() {
		return fils;
	}

	public void setFils(ArrayList<RadixTree> fils) {
		this.fils = fils;
	}

	public RadixTree(String v) {
		value = v;
	}

	public void add(String r) {

	}

	public void remove(String r) {
	}

	public String search(String r) {
		if(r.length()==0) {
			return "le mot n'y est pas";
		}
		if (!isPrefix(r, this.value) ) {
			return "le mot n'y est pas";
		}
		
		String prefixe = getPrefix(r, this.value);
		String reste = r.substring(prefixe.length(), r.length());
		if (reste.length() == 0) {
			return r;
		}
		RadixTree rt = null;
		ArrayList<RadixTree> current = fils;
		if (current.size() > 0) {
			rt = current.get(0);
		} else {
			return "le mot n'y est pas!";
		}
		while (reste.length() > 0 && current.size() > 0) {
			for (int i = 0; i < current.size(); i++) {
				rt = current.get(i);
				if (isPrefix(reste, rt.value)) {
					
					prefixe = getPrefix(reste, rt.value);
					if (prefixe.length() == reste.length()) {
						
						if (prefixe.equals(reste)) {
							reste = "";
						} else {
							return "le mot n'y est pas!!";
						}
						break;
					}
					reste = reste.substring(prefixe.length(), rt.value.length());
					current = rt.fils;
					break;
				}
				else {
					return "le mot n'y est pas!!!";
				}
			}
		}
		if (reste.length() == 0) {
			return r;
		} else {
			return "le mot n'y est pas!!!!";
		}
	}

	public boolean isPrefix(String s1, String s2) {
		return s1.charAt(0) == s2.charAt(0);
	}

	public String getPrefix(String s1, String s2) {
		StringBuilder res = new StringBuilder();
		int i = 0;
		if (s1.length() > s2.length()) {
			while (i < s2.length() && s1.charAt(i) == s2.charAt(i)) {
				res.append(s1.charAt(i));
				i++;
			}
			return res.toString();
		} else {
			if (s1.length() < s2.length()) {
				while (i < s1.length() && s1.charAt(i) == s2.charAt(i)) {
					res.append(s1.charAt(i));
					i++;
				}
				return res.toString();
			} else {
				while (i < s2.length() && s1.charAt(i) == s2.charAt(i)) {
					res.append(s1.charAt(i));
					i++;
				}
				return res.toString();
			}
		}
	}

	public static void main(String args[]) {
		// RadixTree rt = new RadixTree("mabite");
		// System.out.println(rt.isPrefix("ma", "mabite"));
		// System.out.println(rt.getPrefix("mabite", "mab"));
		RadixTree rom = new RadixTree("rom");
		RadixTree an = new RadixTree("an");
		RadixTree ulus = new RadixTree("ulus");
		ArrayList<RadixTree> arbre = new ArrayList<>();
		arbre.add(ulus);
		arbre.add(an);
		rom.setFils(arbre);
		System.out.println(rom.search(""));
	}

}
