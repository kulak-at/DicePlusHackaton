package eu.rotato.diceplushackaton;

import us.dicepl.android.sdk.DiceResponseAdapter;
import us.dicepl.android.sdk.Die;
import us.dicepl.android.sdk.responsedata.MagnetometerData;
import us.dicepl.android.sdk.responsedata.RollData;
import android.widget.Toast;

public class GameListener extends DiceResponseAdapter {
	private PairingListener pl;
	private Die dices[];
	private GameActivity parentActivity;
	
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
	}
	
	public void onMagnetometerReadout(Die die, MagnetometerData readout, Exception exception){
		int pid=0;
		if(die!=dices[0]) pid=1;
		
		//toast("some rot info on dice "+(pid+1));
		
	}
	
	public void onRoll(Die die, RollData readout, Exception exception){
		int pid=0;
		if(die!=dices[0]) pid=1;
		
		final int face = readout.face;
		
		toast("some roll info on dice "+(pid+1)+" with face="+face);
		
	}
	
	

}
