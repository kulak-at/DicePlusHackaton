package eu.rotato.diceplushackaton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PairingActivity extends Activity {
	
	private Button scanBut1, scanBut2;
	private TextView views[];
	private PairingListener pl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pairing);
		views = new TextView[2];
		views[0] = (TextView) this.findViewById(R.id.diceView1);
		views[1] = (TextView) this.findViewById(R.id.diceView2);
		views[0].setText("?");
		views[1].setText("?");
		scanBut1 = (Button) this.findViewById(R.id.scanBut1);
		scanBut2 = (Button) this.findViewById(R.id.scanBut2);
		
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
		/*scanBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pStop(v);
			}
		});*/
	}
	public void pScan2(View v){
		pl.pairPlayers(1);
	}
	/*
	public void pStop(View v){
		pl.stopPairing();
		scanBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pScan(v);
			}
		});
	}*/
	
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

}
