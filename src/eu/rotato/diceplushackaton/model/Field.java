package eu.rotato.diceplushackaton.model;

import java.util.Random;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import eu.rotato.diceplushackaton.R;

public class Field {
	
	private ImageView view = null;
	private int color_r = 0;
	private int color_g = 0;
	private int color_b = 0;
	private int occupied = -1;
	GradientDrawable drw = null;
	LayerDrawable whole = null;
	
	public Field(ImageView w) {
		view = w;
		drw = (GradientDrawable)view.getResources().getDrawable(R.drawable.field_outline);
		Drawable[] layers = new Drawable[1];
		layers[0] = drw;
        whole = new LayerDrawable(layers);
        
		w.setImageDrawable(whole);
		drw.setStroke(0, Color.BLACK);

		this.hide();
		
		this.changeColor(color_r, color_g, color_b);
		
		w.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Random rand = new Random();
				Field.this.changeColor(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
			}
		});
	}
	
	public void drawCross(){
		Drawable[] layers = new Drawable[2];
		layers[0] = drw;
		layers[1] = view.getResources().getDrawable(R.drawable.eyes2);
        whole = new LayerDrawable(layers);
		view.setImageDrawable(whole);
	}
	
	public void unDrawCross(){
		Drawable[] layers = new Drawable[1];
		layers[0] = drw;
		whole = new LayerDrawable(layers);
		view.setImageDrawable(whole);
	}
	
	public void changeColor(int r, int g, int b) {
		this.color_r = r;
		this.color_g = g;
		this.color_b = b;
		
//		ColorDrawable cd = view.getResources().getDrawable(R.drawable.field_shape);
		drw.setColor(Color.rgb(r, g, b));
		view.setImageDrawable(whole);
	}
	
	public void setOccupied(int occupied) {
		this.occupied = occupied;
		drw.setStroke(5, Color.BLACK);
		view.setImageDrawable(whole);
		this.show();
	}
	
	public boolean isOccupied() {
		return this.occupied >= 0;
	}
	
	public int whoOccupies() {
		return this.occupied;
	}
	
	public int getColorR() {
		return color_r;
	}
	
	public int getColorG() {
		return color_g;
	}
	
	public int getColorB() {
		return color_b;
	}
	
	private int getRandomColor() {
		Random rand = new Random();
		int r = rand.nextInt(256);
		int g = rand.nextInt(256);
		int b = rand.nextInt(256);
		return Color.rgb(r, g, b);
	}
	
	public void hide() {
		view.setVisibility(View.INVISIBLE);
	}
	
	public void show() {
		view.setVisibility(View.VISIBLE);
	}
}
