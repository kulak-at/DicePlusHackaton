package eu.rotato.diceplushackaton;

import us.dicepl.android.sdk.BluetoothManipulator;
import us.dicepl.android.sdk.DiceConnectionListener;
import us.dicepl.android.sdk.DiceController;
import us.dicepl.android.sdk.DiceScanningListener;
import us.dicepl.android.sdk.Die;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import eu.rotato.diceplushackaton.R;
import eu.rotato.diceplushackaton.dice.AnimationHelper;
import eu.rotato.diceplushackaton.dice.DiceInteraction;

public class LearningAPIActivity extends Activity {

	private static final int[] developerKey = new int[] {0x83, 0xed, 0x60, 0x0e, 0x5d, 0x31, 0x8f, 0xe7};
	private static final String TAG = "DICEPlus";	
	private Die dicePlus;
	
	private DiceInteraction diceInteraction;
	
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

        	diceInteraction = new DiceInteraction(LearningAPIActivity.this, dicePlus);
        	DiceController.registerDiceResponseListener(diceInteraction);
        	
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
        	if (diceInteraction != null)
        		DiceController.registerDiceResponseListener(diceInteraction);

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
        DiceController.unregisterDiceResponseListener(diceInteraction);

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
	
	public void animateCyan(View view){
		if (dicePlus == null)
			return;
		AnimationHelper ah = new AnimationHelper(dicePlus);
		//ah.showOneColor(android.graphics.Color.CYAN);
		ah.makeSomeParty();
	}
	
	public void animateMagenta(View view){
		if (dicePlus == null)
			return;
		EditText et = (EditText) findViewById(R.id.sides);
		AnimationHelper ah = new AnimationHelper(dicePlus);
		ah.showColorOnSides(Color.MAGENTA, et.getText().toString());
	}
}
