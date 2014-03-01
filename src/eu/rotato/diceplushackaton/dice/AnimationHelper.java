package eu.rotato.diceplushackaton.dice;

import java.util.Random;

import us.dicepl.android.sdk.DiceController;
import us.dicepl.android.sdk.Die;
import android.graphics.Color;

public class AnimationHelper {
	private Die die;
	public AnimationHelper(Die die){
		this.die = die;
	}
	
	public int convertSides(String sides){
		int res = 0;
		for(int i = 0; i < sides.length(); i++){
			res += 1 << (sides.charAt(i) - '1');
		}
		return res;
	}
	
	public void showColorOnMask(int standardColor, int mask){
		int red = android.graphics.Color.red(standardColor);
		int green = android.graphics.Color.green(standardColor);
		int blue = android.graphics.Color.blue(standardColor);
		DiceController.runFadeAnimation(die, mask, 1, red, green, blue, 100, 500, 1);
	}
	
	public void showOneColor(int standardColor){
		int red = android.graphics.Color.red(standardColor);
		int green = android.graphics.Color.green(standardColor);
		int blue = android.graphics.Color.blue(standardColor);
		DiceController.runFadeAnimation(die, 63, 0, red, green, blue, 0, 1000, 1);
	}
	
	public void showColorOnSides(int standardColor, String sides){
		showColorOnMask(standardColor, convertSides(sides));
	}
	
	public void makeSomeParty(){
		int colors[] = {Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.WHITE, Color.GRAY, Color.BLACK};
		Random r = new Random();
		for(int i = 0; i < 120; i++){
			int col = colors[r.nextInt(colors.length)];
			showOneColor(col);
		}
	}
}
