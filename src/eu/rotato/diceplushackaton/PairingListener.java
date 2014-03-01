package eu.rotato.diceplushackaton;

import us.dicepl.android.sdk.BluetoothManipulator;
import us.dicepl.android.sdk.DiceConnectionListener;
import us.dicepl.android.sdk.DiceController;
import us.dicepl.android.sdk.DiceScanningListener;
import us.dicepl.android.sdk.Die;

public class PairingListener implements DiceConnectionListener,
		DiceScanningListener {
	
	private Die dices[];
	private int nowPairing;
	private int secondOne;
	private boolean found;
	private PairingActivity parentActivity;
	
	public PairingListener(PairingActivity activity) {
		// TODO Auto-generated constructor stub
		found = false;
		parentActivity = activity;
		dices = new Die[2];
	}
	
	public boolean isReady(){
		return dices[0]!=null && dices[1]!=null;
	}
	
	public void pairPlayer(int playerId){
		nowPairing = playerId;
		secondOne = (playerId+1)%2; 
		
		parentActivity.views[nowPairing].setText("...");
		parentActivity.buts[nowPairing].setText("Stop");
		parentActivity.buts[secondOne].setEnabled(false);
		
		
		// Listen to all the state occurring during the discovering process of DICE+
        BluetoothManipulator.registerDiceScanningListener(this);
        // When connecting to DICE+ you get two responses: a good one and a bad one ;)
        DiceController.registerDiceConnectionListener(this);
        
        BluetoothManipulator.startScan();
	}
	
	// interfejsy
	
	@Override
	public void onNewDie(Die die) {
		// TODO Auto-generated method stub
		if(found) return;

        DiceController.connect(die);
        
	}

	@Override
	public void onScanFailed() {
		// TODO Auto-generated method stub
		parentActivity.views[nowPairing].setText("?");
		parentActivity.buts[nowPairing].setText("Scan");
		parentActivity.buts[secondOne].setEnabled(true);

	}

	@Override
	public void onScanFinished() {
		DiceController.unregisterDiceConnectionListener(this);
        BluetoothManipulator.unregisterDiceScanningListener(this);
	}

	@Override
	public void onScanStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionEstablished(Die die) {
		found = true;
		parentActivity.views[nowPairing].setText(""+(nowPairing+1));
		parentActivity.buts[nowPairing].setText("Rescan");
		parentActivity.buts[secondOne].setEnabled(true);
		
		dices[nowPairing] = die;

	}

	@Override
	public void onConnectionFailed(Die arg0, Exception arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionLost(Die arg0) {
		// TODO Auto-generated method stub

	}

}
