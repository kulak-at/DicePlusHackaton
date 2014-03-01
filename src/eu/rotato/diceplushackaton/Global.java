package eu.rotato.diceplushackaton;

import eu.rotato.diceplushackaton.model.Game;

public class Global {
	static private int multiplier = 1;
	static private int threshold = 50;
	static private Game game = null;
	
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
	
	static public Game getGame() {
		return game;
	}
	
	static public void setGame(Game g) {
		game = g;
	}
}
