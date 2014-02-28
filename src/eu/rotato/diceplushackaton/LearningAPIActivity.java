package eu.rotato.diceplushackaton;

import us.dicepl.android.sdk.BluetoothManipulator;
import us.dicepl.android.sdk.DiceConnectionListener;
import us.dicepl.android.sdk.DiceController;
import us.dicepl.android.sdk.DiceResponseAdapter;
import us.dicepl.android.sdk.DiceResponseListener;
import us.dicepl.android.sdk.DiceScanningListener;
import us.dicepl.android.sdk.Die;
import us.dicepl.android.sdk.responsedata.RollData;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

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
        	Toast.makeText(getApplication(), "scan started", Toast.LENGTH_LONG).show();
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
        }

        @Override
        public void onConnectionFailed(Die die, Exception e) {
            Log.d(TAG, "Connection failed", e);

            dicePlus = null;

            BluetoothManipulator.startScan();
        }

        @Override
        public void onConnectionLost(Die die) {
            Log.d(TAG, "Connection lost");

            dicePlus = null;

            BluetoothManipulator.startScan();
        }
    };

    DiceResponseListener responseListener = new DiceResponseAdapter() {
        @Override
        public void onRoll(Die die, RollData rollData, Exception e) {
            super.onRoll(die, rollData, e);

            final int face = rollData.face;
            LearningAPIActivity.this.runOnUiThread(
        			new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplication(), "rolled " + face, Toast.LENGTH_LONG).show();
						}
					});
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

}
