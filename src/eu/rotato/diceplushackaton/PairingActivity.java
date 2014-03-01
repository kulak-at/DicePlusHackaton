package eu.rotato.diceplushackaton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PairingActivity extends Activity {
	
	private Button scanBut1, scanBut2;
	private Button stopBut1, stopBut2;
	private SeekBar seekTime, seekMultiplier, seekPointLimit;
	private TextView views[];
	private TextView textTimeVal, textMultiplierVal, textPointLimitVal;
	private PairingListener pl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_pairing);
		
		views = new TextView[2];
		views[0] = (TextView) this.findViewById(R.id.diceView1);
		views[1] = (TextView) this.findViewById(R.id.diceView2);
		views[0].setText("?");
		views[1].setText("?");
		scanBut1 = (Button) this.findViewById(R.id.scanBut1);
		scanBut2 = (Button) this.findViewById(R.id.scanBut2);
		
		seekTime = (SeekBar) this.findViewById(R.id.seekTime);
		seekMultiplier = (SeekBar) this.findViewById(R.id.seekMultiplier);
		seekPointLimit = (SeekBar) this.findViewById(R.id.seekPointLimit);
		
		textTimeVal = (TextView) this.findViewById(R.id.textTimeVal);
		textTimeVal.setText(""+Global.getTimer()+" s");
		textMultiplierVal = (TextView) this.findViewById(R.id.textMultiplierVal);
		textMultiplierVal.setText(""+Global.getMultiplier()+"x");
		textPointLimitVal = (TextView) this.findViewById(R.id.textPointLimitVal);
		textPointLimitVal.setText(""+Global.getPointsLimit()+" pts");
		
		seekTime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				return;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				return;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				Global.setTimer(progress+30);
				textTimeVal.setText(""+Global.getTimer()+" s");
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! +30
				
			}
		});
		
		seekMultiplier.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				return;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				return;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				Global.setMultiplier(progress+1);
				textMultiplierVal.setText(""+Global.getMultiplier()+"x");
				// TODO Auto-generated method stub
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! +1
				
			}
		});
		
		seekPointLimit.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				return;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				return;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				Global.setPointsLimit(progress+10);
				textPointLimitVal.setText(""+Global.getPointsLimit()+" pts");
				// TODO Auto-generated method stub
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! +10
				
			}
		});
		
		pl = new PairingListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pairing, menu);
		return true;
	}
	
	public void pScan1(View v){
		pl.pairPlayers(0);
	}
	public void pScan2(View v){
		pl.pairPlayers(1);
	}
	
	public void pDisconnect1(View v){
		pl.disconnect(0);
		//pl.stopPairing();
	}
	public void pDisconnect2(View v){
		pl.disconnect(1);
		//pl.stopPairing();
	}
	
	public void identifyDices(View v){
		pl.identifyDices();
	}
	
	public void setEnabled(final boolean enabled){
		this.runOnUiThread(new Runnable(){
			@Override
			public void run()
			{
				scanBut1.setEnabled(enabled);
				scanBut2.setEnabled(enabled);
			}
			
		});
	}
	
	public void setInfo(final int playerId, final String text){
		this.runOnUiThread(new Runnable(){

			@Override
			public void run()
			{
				views[playerId].setText(text);
			}
			
		});
	}
	
	public void setButText(final String text){
		this.runOnUiThread(new Runnable(){

			@Override
			public void run()
			{
				scanBut1.setText(text);
				scanBut2.setText(text);
			}
			
		});
	}
	
	public void goToGame(View view){
		Intent gameIntent = new Intent(PairingActivity.this, GameActivity.class);
		PairingActivity.this.startActivity(gameIntent);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		pDisconnect1(null);
		pDisconnect2(null);
	}
}
