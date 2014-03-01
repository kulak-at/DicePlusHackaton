package eu.rotato.diceplushackaton.dice;

import us.dicepl.android.sdk.DiceResponseAdapter;
import us.dicepl.android.sdk.Die;
import us.dicepl.android.sdk.responsedata.OrientationData;
import us.dicepl.android.sdk.responsedata.RollData;
import us.dicepl.android.sdk.responsedata.TemperatureData;
import us.dicepl.android.sdk.responsedata.TouchData;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import eu.rotato.diceplushackaton.R;

public class DiceInteraction extends DiceResponseAdapter
{
	private static Activity activity;
	private static Die dicePlus;
	
	private int currentFace;
	private int currentYaw;
	private int baseYaw;
	private int resultColor;
	
	private static final int COLOR_TRESHOLD = 30;
	private static final String TAG = "DICEPlus";
	
	public DiceInteraction(Activity activity, Die dice)
	{
		this.activity = activity;
		dicePlus = dice;
	}
	
        @Override
        public void onRoll(Die die, RollData rollData, Exception e) {
            super.onRoll(die, rollData, e);
            Log.d(TAG, "on Dice roll");

            final int face = rollData.face;
            currentFace = face;
            baseYaw = currentYaw;
            
            switch (face % 3)
            {
            	case 0:
            		resultColor = Color.RED;
            		break;
            		
            	case 2:
            		resultColor = Color.GREEN;
            		break;
            		
            	case 1:
            		resultColor = Color.BLUE;
            		break;	
            }
            
            activity.runOnUiThread(
        			new Runnable() {
						@Override
						public void run() {
							Toast.makeText(activity, "rolled " + face, Toast.LENGTH_LONG).show();
						}
					});
        }
        
        @Override
        public void onTemperatureReadout(Die die, TemperatureData readout, Exception exception){
        	final TemperatureData data = readout;
        	activity.runOnUiThread(
        			new Runnable() {
						@Override
						public void run() {
							Toast.makeText(activity, "temperature readout: " + 
						data.temperature + "C, timestamp: " + data.timestamp, Toast.LENGTH_LONG).show();
						}
					});
        }
        
        @Override
        public void onOrientationReadout(Die die, OrientationData data, Exception ex)
        {
        	final int roll = data.roll;
        	final int pitch = data.pitch;
        	final int yaw = data.yaw;
        	currentYaw = yaw;
        	
        		if (currentFace != 0)
        			updateColor();
        	
        	activity.runOnUiThread(
        			new Runnable() {
						@Override
						public void run() {
							TextView rollView = (TextView) activity.findViewById(R.id.rollValueText);
							TextView pitchView = (TextView) activity.findViewById(R.id.pitchValueText);
							TextView yawView = (TextView) activity.findViewById(R.id.yawValueText);
							
							rollView.setText(roll + "");
							pitchView.setText(pitch + "");
							yawView.setText(yaw + "");
						}
					});
        }
        
        @Override
        public void onTouchReadout(Die die, TouchData readout, Exception exception)
        {
        	final int current_state_mask = readout.current_state_mask;
        	int change_mask = readout.change_mask;
        	final Die d1 = die;
        	activity.runOnUiThread(
        			new Runnable() {
						@Override
						public void run() {
							TextView currentMask = (TextView) activity.findViewById(R.id.touchMaskValueText);
							
							currentMask.setText(current_state_mask + "");
						}
					});

       // 	AnimationHelper ah = new AnimationHelper(d1);
			
		//	ah.showColorOnMask(Color.BLUE, current_state_mask);
        	
        	Log.d("mazurek", "current state mask: " + current_state_mask);
        	Log.d("mazurek", "change mask: " + change_mask);
        }
    
    public void updateColor()
	{
		int localBaseYaw = baseYaw;
		int localCurrentYaw = currentYaw;
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
			
			switch (resultColor)
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
			
		Log.d("mazurek", newComponent + "");
		
		AnimationHelper ah = new AnimationHelper(dicePlus);
		ah.showColorOnSides(newColor, currentFace + "");
	}
}
