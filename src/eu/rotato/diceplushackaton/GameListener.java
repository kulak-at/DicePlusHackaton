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
		
		int currentVal = 0;
		Game gejm = Global.getGame();
        
        switch (face % 3)
        {
        	case 0:
        		currentDiceData.resultColor = Color.RED;
        		currentVal = gejm.getPlayerR(pid);
        		break;
        		
        	case 2:
        		currentDiceData.resultColor = Color.GREEN;
        		currentVal = gejm.getPlayerG(pid);
        		break;
        		
        	case 1:
        		currentDiceData.resultColor = Color.BLUE;
        		currentVal = gejm.getPlayerB(pid);
        		break;
        }
        Log.i("kulak_gunwo", "CURRENT VAL: " + currentVal);
        currentDiceData.setColorVal(currentVal);
        
        int r = Color.red(currentDiceData.resultColor);
        int g = Color.green(currentDiceData.resultColor);
        int b = Color.blue(currentDiceData.resultColor);
        
        DiceController.runBlinkAnimation(die, 63, 0, r, g, b, 200, 230, 1);
        
        currentDiceData.setIgnoreYaw(false);
		
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
		
			if (rollDiff >= ROLL_TRESHOLD && !currentDiceData.isIgnoreYaw())
			{
				Log.v("gunwo", "Ignore yaw.");
				currentDiceData.setIgnoreYaw(true);
			}
			
			if (currentDiceData.isIgnoreYaw())
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
		
		int localBaseYaw = currentDiceData.getBaseYaw() + 180;
		int localCurrentYaw = currentDiceData.getCurrentYaw() + 180;
		int difference = localCurrentYaw - localBaseYaw;
		
		float par_color = currentDiceData.getColorVal() / 256.0f;
		float par_yaw = (5.0f - par_color + (localCurrentYaw / 360.0f) - (localBaseYaw / 360.f)) % 1;
		
		int color = (int)(par_yaw * 255);
		
		Log.i("kulak_gunwo", "par_color: " + par_color);
		Log.i("kulak_gunwo", "par_yaw: " + par_yaw);
		
		
		
		Log.i("kulak_gunwo", "localBaseYaw: " + localBaseYaw + ", localCurrentYaw:" + localCurrentYaw);
		
//		int diffToRGB = checkDiff( (int)Math.floor((difference + 180) / 360d) * 256);
		
//		Log.i("kulak_gunwo", "diff2rgb: " + diffToRGB);
		
		// przesuwamy
//		diffToRGB -= currentDiceData.getColorVal();
		
//		diffToRGB = (256+diffToRGB) % 256;
//		Log.i("kulak_gunwo", "after convert: " + diffToRGB);
		
		
//		Log.i("mazurek", "DIFF TO RGB: " + diffToRGB);
//		int diff = (int) Math.floor(diffToRGB);
//		Log.i("mazurek", "DIFF: " + diff);
		
		int newColor = 0;
		int newComponent = 0;
		
			switch (currentDiceData.getResultColor())
			{
				case Color.RED:
					
					newColor = Color.argb(255, color, 0, 0);
					player_r = color;
					break;
			
				case Color.BLUE:
					
					newColor = Color.argb(255, 0, 0, color);
					player_b = color;
					break;
					
				case Color.GREEN:
					
					newColor = Color.argb(255, 0, color, 0);
					player_g = color;
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
