import java.util.ArrayList;
import java.util.Set;

public class EtatsCommuns {
	private Integer etatOrigine;
	private Set<Integer> nouveauxEtats;

	public EtatsCommuns(Integer etatOrigine, Set<Integer> nouveauxEtats) {
		this.etatOrigine = etatOrigine;
		this.nouveauxEtats = nouveauxEtats;
	}

	public Integer getEtatOrigine() {
		return etatOrigine;
	}

	public void setEtatOrigine(Integer etatOrigine) {
		this.etatOrigine = etatOrigine;
	}

	public Set<Integer> getNouveauxEtats() {
		return nouveauxEtats;
	}

	public void addNouveauxEtats(Integer nouveauEtat) {
		this.nouveauxEtats.add(nouveauEtat);
	}
}
