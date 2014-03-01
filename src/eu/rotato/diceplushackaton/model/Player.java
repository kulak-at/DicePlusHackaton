package eu.rotato.diceplushackaton.model;

import java.util.Vector;

import android.graphics.Color;

public class Player {
	
	Vector<Field> fields = null;
	int color_r = 0;
	int color_g = 0;
	int color_b = 0;
	int nr = 0;
	int points = 0;
	
	public Player(int nr) {
		fields = new Vector<Field>();
		this.nr = nr;
	}
	
	public boolean occupyField(Field field) {
		if(field.isOccupied() && field.whoOccupies() != nr)
			return false;
		fields.add(field);
		field.setOccupied(this.nr);
		points++;
		return true;
	}
	
	public void setColor(int r, int g, int b) {
		color_r = r;
		color_g = g;
		color_b = b;
		this.redraw();
	}
	
	public void redraw() {
		for(Field field : fields) {
			field.changeColor(color_r, color_g, color_b);
		}
	}
	
	public int getPoints() {
		return points;
	}
}
