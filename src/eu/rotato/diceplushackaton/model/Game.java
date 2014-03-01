package eu.rotato.diceplushackaton.model;

import java.util.Random;
import java.util.Vector;

import eu.rotato.diceplushackaton.Global;

import android.graphics.Color;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Game {
	LinearLayout table = null;
	int rows, cols = 0;
	int players_count = 2;
	Field fields[][];
	Vector<Player> players = null;
	
	class Pos {
		int x,y;
	}
	
	Vector<Pos> players_pos = null;
	
	Random rand = null;
	
	public Game(LinearLayout table, int rows, int cols, int players_count) {
		this.rand = new Random();
		this.table = table;
		this.rows = rows;
		this.cols = cols;
		this.players_count = players_count;
		
		this.fields = new Field[this.rows][this.cols];
		for(int i=0;i<this.rows;i++) {
			LinearLayout.LayoutParams lparam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
			
			LinearLayout row = new LinearLayout(table.getContext());
			row.setLayoutParams(lparam);
			row.setOrientation(LinearLayout.HORIZONTAL);
			table.addView(row);
			for(int j=0;j<this.cols;j++) {
				ImageView field_view = new ImageView(table.getContext());
				LinearLayout.LayoutParams lparam2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
				field_view.setLayoutParams(lparam2);
				row.addView(field_view);
				Field field = new Field(field_view);
				Random rand = new Random();
				field.changeColor(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
				this.fields[i][j] = field;
				
				final int p_x = i;
				final int p_y = j;
				
				field_view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Log.i("game", "Pos: " + p_x + ", " + p_y);
						Field field = Game.this.fields[p_x][p_y];
						Game.this.changePlayerColor(0, field.getColorR(), field.getColorG(), field.getColorB());
					}
				});
			}
		}
		
		this.players = new Vector<Player>();
		this.players_pos = new Vector<Game.Pos>();
		for(int i=0;i<this.players_count;i++) {
			Player player = new Player(i);
			this.players.add(player);
			Pos pos = new Pos();
			this.players_pos.add(pos);
			
			// setting player starting point
			Field curr = null;
			if(i == 0) {
				curr = fields[0][0];
//				fields[0][0].setOccupied(i);
				pos.x = 0;
				pos.y = 0;
				
			} else if(i == 1) {
				curr = fields[this.rows - 1][this.cols - 1];
				pos.x = this.rows - 1;
				pos.y = this.cols - 1;
			} else if(i == 2) {
				curr = fields[this.rows - 1][0];
				pos.x = this.rows - 1;
				pos.y = 0;
			} else {
				curr = fields[0][this.cols - 1];
				pos.x = 0;
				pos.y = this.cols - 1;
			}
			player.occupyField(curr);
			this.showSiblings(pos.x, pos.y);
		}
	}
	
	private int generateColor(int i, int j) {
		int r = rand.nextInt(256);
		int g = rand.nextInt(256);
		int b = rand.nextInt(256);
		return Color.rgb(r, g, b);
	}
	
	public void changePlayerColor(int player_id, int r, int g, int b) {
		Player player = this.players.get(player_id);
		
		player.setColor(r, g, b);
		
		int x = this.players_pos.get(player_id).x;
		int y = this.players_pos.get(player_id).y;
		
		checkField(player_id, x+1, y, r, g, b);
		checkField(player_id, x-1, y, r, g, b);
		checkField(player_id, x, y+1, r, g, b);
		checkField(player_id, x, y-1, r, g, b);
		
	}
	
	public int getPlayerR(int player_id) {
		return this.players.get(player_id).color_r;
	}
	
	public int getPlayerG(int player_id) {
		return this.players.get(player_id).color_g;
	}
	
	public int getPlayerB(int player_id) {
		return this.players.get(player_id).color_b;
	}
	
	private void checkField(int player_id, int x,int y, int r,int g,int b) {
		try {

			if(getColorDiff(x, y, r, g, b) < Global.getThreshold() && !this.fields[x][y].isOccupied()) {
				Log.i("game", "Occupying field: " + x + ", " + y + " (Player " + player_id + ")");
				
				// changing
//				this.fields[x][y].setOccupied(player_id);
				this.players.get(player_id).occupyField(this.fields[x][y]);
				
				
				Pos p = this.players_pos.get(player_id);
				
				hideSiblings(p.x, p.y);
				showSiblings(x, y);
				show(x, y);
				
				p.x = x;
				p.y = y;
				this.players.get(player_id).redraw();
				
			}
			
		} catch(Exception e) { }
	}
	
	private void hideSiblings(int x, int y) {
		hide(x+1, y);
		hide(x-1, y);
		hide(x, y+1);
		hide(x,y-1);
	}
	
	private void showSiblings(int x, int y) {
		show(x+1, y);
		show(x-1, y);
		show(x, y+1);
		show(x, y-1);
	}
	
	private void hide(int x, int y) {
		if(x < 0 || y < 0 || x >= this.rows || y >= this.cols || this.fields[x][y].isOccupied())
			return;
		
		this.fields[x][y].hide();
	}
	
	private void show(int x, int y) {
		if(x < 0 || y < 0 || x >= this.rows || y >= this.cols)
			return;
		this.fields[x][y].show();
	}
	
	private int getColorDiff(int x, int y, int r, int g, int b) throws Exception {
		if(x < 0 || y < 0 || x >= this.rows || y >= this.cols)
			throw new Exception();
		Field field = this.fields[x][y];
		return Math.abs(field.getColorR() - r) + Math.abs(field.getColorG() - g) + Math.abs(field.getColorB() - b);
	}
	
}
