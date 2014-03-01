package eu.rotato.diceplushackaton;

import us.dicepl.android.sdk.DiceController;
import us.dicepl.android.sdk.DiceResponseAdapter;
import us.dicepl.android.sdk.Die;
import us.dicepl.android.sdk.responsedata.MagnetometerData;
import us.dicepl.android.sdk.responsedata.OrientationData;
import us.dicepl.android.sdk.responsedata.RollData;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;
import eu.rotato.diceplushackaton.dice.AnimationHelper;
import eu.rotato.diceplushackaton.model.DiceData;
import eu.rotato.diceplushackaton.model.Game;
import eu.rotato.diceplushackaton.model.Player;

public class GameListener extends DiceResponseAdapter {
	private PairingListener pl;
	private Die dices[];
	private long playerTime[];
	private DiceData[] diceData;
	private AnimationHelper[] animationHelpers;
	private GameActivity parentActivity;
	
	private static final long TIMESTAMP_TRESHOLD = 20;
	private static final int ROLL_TRESHOLD = 40;
	
	private void toast(final String text){
		parentActivity.runOnUiThread(new Runnable(){

			@Override
			public void run()
			{
				Toast.makeText(parentActivity.getApplication(), text, Toast.LENGTH_SHORT).show();
			}
			
		});
		
	}
	
	public GameListener(GameActivity ga) {
		parentActivity = ga;
		// TODO Auto-generated constructor stub
		pl = PairingListener.getInstance();
		dices = pl.getDices();
		diceData = new DiceData[dices.length];
		playerTime = new long[dices.length];
		animationHelpers = new AnimationHelper[dices.length];
		
			for (int i = 0; i < dices.length; i++)
			{
				diceData[i] = new DiceData();
				animationHelpers[i] = new AnimationHelper(dices[i]);
			}
	}
	
	public void onMagnetometerReadout(Die die, MagnetometerData readout, Exception exception)
	{
		int pid=0;
		if(die!=dices[0]) pid=1;
		
		//toast("some rot info on dice "+(pid+1));
		
	}
	
	public void onRoll(Die die, RollData readout, Exception exception)
	{
		int pid=0;
		if(die!=dices[0]) pid=1;
		
		DiceData currentDiceData = diceData[pid];
		
		final int face = readout.face;
		
		currentDiceData.setCurrentFace(face);
		currentDiceData.setBaseYaw(currentDiceData.getCurrentYaw());
        
        switch (face % 3)
        {
        	case 0:
        		currentDiceData.resultColor = Color.RED;
        		break;
        		
        	case 2:
        		currentDiceData.resultColor = Color.GREEN;
        		break;
        		
        	case 1:
        		currentDiceData.resultColor = Color.BLUE;
        		break;
        }
        
        int r = Color.red(currentDiceData.resultColor);
        int g = Color.green(currentDiceData.resultColor);
        int b = Color.blue(currentDiceData.resultColor);
        
        DiceController.runBlinkAnimation(die, 63, 0, r, g, b, 200, 230, 1);
		
		toast("some roll info on dice "+(pid+1)+" with face="+face);
		
	}
	
	@Override
	public void onOrientationReadout(Die die, OrientationData readout, Exception ex)
	{	
		int pid=0;
		if(die!=dices[0]) pid=1;
		
		DiceData currentDiceData = diceData[pid];
		
		int roll = readout.roll;
		int rollDiff = Math.abs(roll - currentDiceData.getPreviousRoll());
		currentDiceData.setPreviousRoll(roll);
		
			if (rollDiff >= ROLL_TRESHOLD)
			{
				Log.v("gunwo", "Ignoring yaw!");
				return;
			}
		
		Log.d("gunwo", "Dice: " + pid + " roll value: " + roll);
		
		currentDiceData.setCurrentYaw(readout.yaw);
		
			if (currentDiceData.currentFace != 0)
				updateColor(pid);
	}

	
	private void updateColor(final int pid)
	{
		Game game = Global.getGame();
		if(game == null)
			return;
		
		int player_r = game.getPlayerR(pid);
		int player_g = game.getPlayerG(pid);
		int player_b = game.getPlayerB(pid);
		
		DiceData currentDiceData = diceData[pid];
		
		int localBaseYaw = currentDiceData.getBaseYaw();
		int localCurrentYaw = currentDiceData.getCurrentYaw();
		int difference = Math.abs(localBaseYaw - localCurrentYaw);
		
		
			if (localCurrentYaw < localBaseYaw)
				difference *= -1;
		
			int colorDiff = difference % 256;
		
//			if (Math.abs(colorDiff) < COLOR_TRESHOLD)
//				return;
		
			if(System.currentTimeMillis() - playerTime[pid] < TIMESTAMP_TRESHOLD)
				return;
			
			 playerTime[pid] = System.currentTimeMillis();
			
			int newComponent = 255 + colorDiff;
			
				if (newComponent < 0)
					newComponent = 0;
			
				if (newComponent > 255)
					newComponent = 255;
			
			int newColor = 0;
			Log.i("game", "Player " + pid + ": Component: " + newComponent);
			
			switch (currentDiceData.getResultColor())
			{
				case Color.RED:
					newColor = Color.argb(255, newComponent, 0, 0);
					player_g = newComponent;
					break;
			
				case Color.BLUE:
					newColor = Color.argb(255, 0, 0, newComponent);
					player_b = newComponent;
					break;
					
				case Color.GREEN:
					newColor = Color.argb(255, 0, newComponent, 0);
					player_g = newComponent;
					break;
			}
			final int f_r = player_r;
			final int f_g = player_g;
			final int f_b = player_b;
			parentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Global.getGame().changePlayerColor(pid, f_r, f_g, f_b);
					
				}
			});
			
		Log.d("mazurek", "Dice: " + pid + " component value: " + newComponent);

		animationHelpers[pid].showColorOnSides(newColor, currentDiceData.currentFace + "");
	}
}
