package eu.rotato.diceplushackaton;

import us.dicepl.android.sdk.DiceController;
import us.dicepl.android.sdk.Die;
import android.util.Log;

public class AnimationHelper {
	private Die die;
	AnimationHelper(Die die){
		this.die = die;
	}
	
	public int convertSides(String sides){
		int res = 0;
		for(int i = 0; i < sides.length(); i++){
			res += 1 << (sides.charAt(i) - '1');
		}
		return res;
	}
	
	public void showOneColor(int standardColor){
		int red = android.graphics.Color.red(standardColor);
		int green = android.graphics.Color.green(standardColor);
		int blue = android.graphics.Color.blue(standardColor);
		DiceController.runFadeAnimation(die, 63, 0, red, green, blue, 0, 1000, 1);
	}
	
	public void showColorOnSides(int standardColor, String sides){
		int red = android.graphics.Color.red(standardColor);
		int green = android.graphics.Color.green(standardColor);
		int blue = android.graphics.Color.blue(standardColor);
		DiceController.runFadeAnimation(die, convertSides(sides), 0, red, green, blue, 0, 1000, 1);
	}
}
