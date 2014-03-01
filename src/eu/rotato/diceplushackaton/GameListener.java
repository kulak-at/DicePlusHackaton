package eu.rotato.diceplushackaton;

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

public class GameListener extends DiceResponseAdapter {
	private PairingListener pl;
	private Die dices[];
	private DiceData[] diceData;
	private AnimationHelper[] animationHelpers;
	private GameActivity parentActivity;
	
	private static final int COLOR_TRESHOLD = 30;
	
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
		
			for (int i = 0; i < dices.length; i++)
			{
				diceData[i] = new DiceData();
			}
			
		animationHelpers = new AnimationHelper[dices.length];
		
			for (int i = 0; i < dices.length; i++)
			{
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
		
		toast("some roll info on dice "+(pid+1)+" with face="+face);
		
	}
	
	@Override
	public void onOrientationReadout(Die die, OrientationData readout, Exception ex)
	{	
		int pid=0;
		if(die!=dices[0]) pid=1;
		
		DiceData currentDiceData = diceData[pid];
		
		currentDiceData.setCurrentYaw(readout.yaw);
		
			if (currentDiceData.currentFace != 0)
				updateColor(pid);
	}

	
	private void updateColor(int pid)
	{
		DiceData currentDiceData = diceData[pid];
		
		int localBaseYaw = currentDiceData.getBaseYaw();
		int localCurrentYaw = currentDiceData.getCurrentYaw();
		int difference = Math.abs(localBaseYaw - localCurrentYaw);
		
			if (localCurrentYaw < localBaseYaw)
				difference *= -1;
		
		int colorDiff = difference % 256;
		
			if (Math.abs(colorDiff) < COLOR_TRESHOLD)
				return;
			
			int newComponent = 255 + colorDiff;
			
				if (newComponent < 0)
					newComponent = 0;
			
				if (newComponent > 255)
					newComponent = 255;
			
			int newColor = 0;
			
			switch (currentDiceData.getResultColor())
			{
				case Color.RED:
					newColor = Color.argb(255, newComponent, 0, 0);
					break;
			
				case Color.BLUE:
					newColor = Color.argb(255, 0, 0, newComponent);
					break;
					
				case Color.GREEN:
					newColor = Color.argb(255, 0, newComponent, 0);
					break;
			}
			
		Log.d("mazurek", "Dice: " + pid + " component value: " + newComponent);

		animationHelpers[pid].showColorOnSides(newColor, currentDiceData.currentFace + "");
	}
}
