import java.util.ArrayList;

public class AutomateDeterministe {

	static final int NB_TRANSITIONS = 256;

	private ArrayList<Integer> start = new ArrayList<Integer>();
	private ArrayList<Integer> end = new ArrayList<Integer>();
	
	private int nbStates = 0;
	private int nbTransitions = 0;

	private int[][] autom;

	public AutomateDeterministe(ArrayList<Integer> starts,ArrayList<Integer> ends, int nbS, int nbT,int[][] transitions) {
		start = starts;
		end = ends;
		nbStates = nbS;
		nbTransitions = nbT;		
		autom = new int[nbStates][NB_TRANSITIONS];
		
		for (int i = 0; i < nbStates; i++) {
			for (int j = 0; j < NB_TRANSITIONS; j++) {
				autom[i][j] = transitions[i][j];
			}
		}
	}

	public void addTransition(int a, int b, int t) {
		autom[a][t] = b;
	}

}
