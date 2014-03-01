package eu.rotato.diceplushackaton;

public class Global {
	static private int multiplier = 1;
	static private int threshold = 50;
	
	static public int getMultiplier() {
		return multiplier;
	}
	
	static public void setMultiplier(int m) {
		if(m > 0)
			multiplier = m;
	}
	
	static public int getThreshold() {
		return threshold;
	}
}
