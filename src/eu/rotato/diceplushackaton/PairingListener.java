package eu.rotato.diceplushackaton;

import us.dicepl.android.sdk.BluetoothManipulator;
import us.dicepl.android.sdk.DiceConnectionListener;
import us.dicepl.android.sdk.DiceController;
import us.dicepl.android.sdk.DiceScanningListener;
import us.dicepl.android.sdk.Die;
import android.widget.Toast;

// Testzone!

// /Testzone!

public final class PairingListener implements DiceConnectionListener, DiceScanningListener {
	
	private final static PairingListener instance = new PairingListener();
	public static PairingListener getInstance(){
		return instance;
	}
	

	private static final int[] developerKey = new int[] {0x83, 0xed, 0x60, 0x0e, 0x5d, 0x31, 0x8f, 0xe7};
	static private Die dices[];
	private int nowPairing;
	//private int secondOne;
	private boolean found;
	private PairingActivity parentActivity;
	
	private void toast(final String text){
		parentActivity.runOnUiThread(new Runnable(){

			@Override
			public void run()
			{
				Toast.makeText(parentActivity.getApplication(), text, Toast.LENGTH_SHORT).show();
			}
			
		});
		
	}
	
	public Die[] getDices(){
		return dices;
	}
	
	private PairingListener(){};
	
	public PairingListener(PairingActivity activity) {
		// TODO Auto-generated constructor stub
		
		nowPairing = -1;
		parentActivity = activity;
		dices = new Die[2];

		DiceController.initiate(developerKey);
        BluetoothManipulator.initiate(parentActivity);
        
        // When connecting to DICE+ you get two responses: a good one and a bad one ;)
        DiceController.registerDiceConnectionListener(this);
        // Listen to all the state occurring during the discovering process of DICE+
        BluetoothManipulator.registerDiceScanningListener(this);
     // Attaching to DICE+ events that we subscribed to.
        //DiceController.registerDiceResponseListener(this);
	}
	
	public boolean isReady(){
		return dices[0]!=null && dices[1]!=null;
	}
	
	public void pairPlayers(final int playerId){
		// TODO
		//nowPairing++;
		nowPairing=playerId;
		if(dices[nowPairing] != null) DiceController.disconnectDie(dices[nowPairing]);
		found = false;
		toast("Pairing...");
		parentActivity.setButText("Scanning..."+nowPairing);
		parentActivity.setEnabled(false);
		BluetoothManipulator.startScan();
	}
	
	public void stopPairing(){
		toast("Pairing stopped.");
		BluetoothManipulator.cancelScan();
		//parentActivity.setInfo(nowPairing, "?", "Scan", true);
	}
	
	// interfejsy
	
	@Override
	public void onNewDie(Die die) {
		// TODO Auto-generated method stub
		
		if(found) return;
		
		dices[nowPairing]=die;
		found=true;
		toast("New dice"+nowPairing);
        DiceController.connect(die);
	}

	@Override
	public void onScanFailed() {
		// TODO Auto-generated method stub
		toast("Scan failed.");
		parentActivity.setInfo(0, "?");
		parentActivity.setInfo(1, "?");
		//parentActivity.setInfo(nowPairing, "?", "Scan", true);
	}

	@Override
	public void onScanFinished() {
		toast("Scan finished.");
		parentActivity.setButText("Scan");
		parentActivity.setEnabled(true);
		
	}

	@Override
	public void onScanStarted() {
		toast("Scan started!"+nowPairing);
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionEstablished(Die die) {

		BluetoothManipulator.cancelScan();
		int pid = 0;
		if(die!=dices[0]) pid=1;
		parentActivity.setInfo(pid, ""+(pid+1));
		toast("Connection established!"+nowPairing);
		//TODO What we want to subscribe
		DiceController.subscribeRolls(die);
		//DiceController.subscribeMagnetometerReadouts(die);
		DiceController.runFadeAnimation(die, pid+1, 0, 255, 0, 0, 500, 59000);
		DiceController.subscribeOrientationReadouts(die, 500);
		//parentActivity.setInfo(nowPairing,""+(nowPairing+1),"Rescan",true);
	}

	@Override
	public void onConnectionFailed(Die die, Exception arg1) {
		toast("Connection failed!");
		int pid = 0;
		if(die!=dices[0]) pid=1;
		parentActivity.setInfo(pid, "?");
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionLost(Die die) {
		toast("Connection lost!");
		int pid = 0;
		if(die!=dices[0]) pid=1;
		parentActivity.setInfo(pid, "?");
	}
	
	public void identifyDices(){
		if(dices[0] != null) DiceController.runFadeAnimation(dices[0], 1, 0, 255, 0, 144, 0, 60000);	//magenta
		if(dices[1] != null) DiceController.runFadeAnimation(dices[1], 2, 0, 0, 255, 255, 0, 60000);	//cyan
	}
	
}
