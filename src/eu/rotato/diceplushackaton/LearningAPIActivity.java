package eu.rotato.diceplushackaton;

import us.dicepl.android.sdk.BluetoothManipulator;
import us.dicepl.android.sdk.DiceConnectionListener;
import us.dicepl.android.sdk.DiceController;
import us.dicepl.android.sdk.DiceResponseAdapter;
import us.dicepl.android.sdk.DiceResponseListener;
import us.dicepl.android.sdk.DiceScanningListener;
import us.dicepl.android.sdk.Die;
import us.dicepl.android.sdk.responsedata.OrientationData;
import us.dicepl.android.sdk.responsedata.RollData;
import us.dicepl.android.sdk.responsedata.TemperatureData;
import us.dicepl.android.sdk.responsedata.TouchData;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import eu.rotato.diceplushackaton.R;

public class LearningAPIActivity extends Activity {

	private static final int[] developerKey = new int[] {0x83, 0xed, 0x60, 0x0e, 0x5d, 0x31, 0x8f, 0xe7};
	private static final String TAG = "DICEPlus";	
	private Die dicePlus;
	
    DiceScanningListener scanningListener = new DiceScanningListener() {
        @Override
        public void onNewDie(Die die) {
            Log.d(TAG, "New DICE+ found");
            dicePlus = die;
            DiceController.connect(dicePlus);
        }

        @Override
        public void onScanStarted() {
        	Toast.makeText(getApplication(), "scan started", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onScanFailed() {
            Log.d(TAG, "Scan Failed");
            BluetoothManipulator.startScan();
        }

        @Override
        public void onScanFinished() {
            Log.d(TAG, "Scan Finished");

            if(dicePlus == null) {
                BluetoothManipulator.startScan();
            }
        }
    };
    
    DiceConnectionListener connectionListener = new DiceConnectionListener() {
    	
        @Override
        public void onConnectionEstablished(Die die) {
        	LearningAPIActivity.this.runOnUiThread(
        			new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplication(), "connection estabilished", Toast.LENGTH_LONG).show();
						}
					});

            // Signing up for roll events
            DiceController.subscribeRolls(dicePlus);
       //     DiceController.subscribeTemperatureReadouts(dicePlus);
            DiceController.subscribeOrientationReadouts(dicePlus);
            DiceController.subscribeTouchReadouts(dicePlus);
        }

        @Override
        public void onConnectionFailed(Die die, Exception e) {
            Log.d(TAG, "Connection failed", e);
            //myText.setText("disconnected");

            dicePlus = null;

            BluetoothManipulator.startScan();
        }

        @Override
        public void onConnectionLost(Die die) {
            Log.d(TAG, "Connection lost");
            LearningAPIActivity.this.runOnUiThread(
        			new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplication(), "connection estabilished", Toast.LENGTH_LONG).show();
						}
					});

            dicePlus = null;

            BluetoothManipulator.startScan();
        }
    };

    DiceResponseListener responseListener = new DiceResponseAdapter() {
        @Override
        public void onRoll(Die die, RollData rollData, Exception e) {
            super.onRoll(die, rollData, e);
            Log.d(TAG, "on Dice roll");

            final int face = rollData.face;
            LearningAPIActivity.this.runOnUiThread(
        			new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplication(), "rolled " + face, Toast.LENGTH_LONG).show();
						}
					});
        }
        
        public void onTemperatureReadout(Die die, TemperatureData readout, Exception exception){
        	final TemperatureData data = readout;
        	LearningAPIActivity.this.runOnUiThread(
        			new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplication(), "temperature readout: " + 
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
        	
        	LearningAPIActivity.this.runOnUiThread(
        			new Runnable() {
						@Override
						public void run() {
							TextView rollView = (TextView) findViewById(R.id.rollValueText);
							TextView pitchView = (TextView) findViewById(R.id.pitchValueText);
							TextView yawView = (TextView) findViewById(R.id.yawValueText);
							
							rollView.setText(roll + "");
							pitchView.setText(pitch + "");
							yawView.setText(yaw + "");
						}
					});
        	
        	Log.d("mazurek", "onOrientationReadout");
        	Log.d("mazurek", "Roll value: " + roll);
        	Log.d("mazurek", "Pitch value: " + pitch);
        	Log.d("mazurek", "Yaw value: " + yaw);
        }
        
        @Override
        public void onTouchReadout(Die die, TouchData readout, Exception exception)
        {
        	Log.d("mazurek", "onTouchReadout");
        	
        	final int current_state_mask = readout.current_state_mask;
        	int change_mask = readout.change_mask;
        	
        	LearningAPIActivity.this.runOnUiThread(
        			new Runnable() {
						@Override
						public void run() {
							TextView currentMask = (TextView) findViewById(R.id.touchMaskValueText);
							
							currentMask.setText(current_state_mask + "");
						}
					});
        	
        	Log.d("mazurek", "current state mask: " + current_state_mask);
        	Log.d("mazurek", "change mask: " + change_mask);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_api);
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");

        // Initiating
        BluetoothManipulator.initiate(this);
        DiceController.initiate(developerKey);

        // Listen to all the state occurring during the discovering process of DICE+
        BluetoothManipulator.registerDiceScanningListener(scanningListener);

        // When connecting to DICE+ you get two responses: a good one and a bad one ;)
        DiceController.registerDiceConnectionListener(connectionListener);

        // Attaching to DICE+ events that we subscribed to.
        DiceController.registerDiceResponseListener(responseListener);

        // Scan for a DICE+
        BluetoothManipulator.startScan();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop");

        // Unregister all the listeners
        DiceController.unregisterDiceConnectionListener(connectionListener);
        BluetoothManipulator.unregisterDiceScanningListener(scanningListener);
        DiceController.unregisterDiceResponseListener(responseListener);

        DiceController.disconnectDie(dicePlus);
        dicePlus = null;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void rescan(View view){
		if (dicePlus != null)
			DiceController.disconnectDie(dicePlus);
		dicePlus = null;
		BluetoothManipulator.startScan();
	}
	
	public void animate(View view){
		if (dicePlus == null)
			return;
		DiceController.runFadeAnimation(dicePlus, 12, 3, 200, 200, 200, 1000, 1000, 10);
		//DiceController.runStandardAnimation(dicePlus, 10, 1, LedAnimationType.ANIMATION_ROLL_FAILED);
	}

}
