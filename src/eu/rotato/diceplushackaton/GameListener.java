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

public class GameListener extends DiceResponseAdapter {
	private PairingListener pl;
	private Die dices[];
	private long playerTime[];
	private DiceData[] diceData;
	private AnimationHelper[] animationHelpers;
	private GameActivity parentActivity;
	
	private static final int COLOR_TRESHOLD = 30;
	private static final long TIMESTAMP_TRESHOLD = 10;
	private static final int ROLL_TRESHOLD = 40;
	
	private static boolean ignoreYaw = true;
	
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
        
        ignoreYaw = false;
		
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
		
			if (rollDiff >= ROLL_TRESHOLD && ignoreYaw == false)
			{
				Log.v("gunwo", "Ignore yaw.");
				ignoreYaw = true;
			}
			
			if (ignoreYaw)
				return;
		
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
			
			if(System.currentTimeMillis() - playerTime[pid] < TIMESTAMP_TRESHOLD)
				return;
			
		playerTime[pid] = System.currentTimeMillis();
		
		int player_r = game.getPlayerR(pid);
//		Log.i("mazurek", "RED: " + player_r);
		int player_g = game.getPlayerG(pid);
//		Log.i("mazurek", "GREEN: " + player_g);
		int player_b = game.getPlayerB(pid);
//		Log.i("mazurek", "BLUE: " + player_b);
		
		DiceData currentDiceData = diceData[pid];
		
		int localBaseYaw = currentDiceData.getBaseYaw();
		int localCurrentYaw = currentDiceData.getCurrentYaw();
		int difference = localCurrentYaw - localBaseYaw;
		
		double diffToRGB = ((difference + 180) / 360d) * 256;
		Log.i("mazurek", "DIFF TO RGB: " + diffToRGB);
		int diff = (int) Math.floor(diffToRGB);
		Log.i("mazurek", "DIFF: " + diff);
		
		int newColor = 0;
		int newComponent = 0;
		
			switch (currentDiceData.getResultColor())
			{
				case Color.RED:
					newComponent = checkDiff(diff);
					
					newColor = Color.argb(255, newComponent, 0, 0);
					player_r = newComponent;
					break;
			
				case Color.BLUE:
					newComponent = checkDiff(diff);
					
					newColor = Color.argb(255, 0, 0, newComponent);
					player_b = newComponent;
					break;
					
				case Color.GREEN:
					newComponent = checkDiff(diff);
					
					newColor = Color.argb(255, 0, newComponent, 0);
					player_g = newComponent;
					break;
			}
			
			Log.i("mazurek", "Player " + pid + ": Component: " + newComponent);
			
			final int f_r = player_r;
			final int f_g = player_g;
			final int f_b = player_b;
			
			parentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Global.getGame().changePlayerColor(pid, f_r, f_g, f_b);
					
				}
			});

		animationHelpers[pid].showColorOnSides(newColor, currentDiceData.currentFace + "");
	}
	
	private int checkDiff(int diff)
	{
		Log.e("mazurek", (diff) + "");
		
		if ((diff) > 255)
			return 255;
		
		else if ((diff) < 0)
			return 0;

		return diff;
	}
}
