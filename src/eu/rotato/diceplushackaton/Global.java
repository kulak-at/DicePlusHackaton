package eu.rotato.diceplushackaton;

import eu.rotato.diceplushackaton.model.Game;

public class Global {
	static private int multiplier = 1;
	static private int threshold = 200;
	static private Game game = null;
	static private int timer = 30;
	static private int points_limit = 100;
	
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
	
	static public int getTimer() {
		return timer;
	}
	
	static public void setTimer(int t) {
		timer = t;
	}
	
	static public int getPointsLimit() {
		return points_limit;
	}
	
	static public void setPointsLimit(int limit) {
		points_limit = limit;
	}
}
