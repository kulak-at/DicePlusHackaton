package eu.rotato.diceplushackaton;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PairingActivity extends Activity {
	
	private boolean p1Ready;
	private boolean p2Ready;
	public Button buts[];
	public TextView views[];
	private Button accBut;
	private PairingListener pl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pairing);
		p1Ready = p2Ready = false;
		views = new TextView[2];
		views[0] = (TextView) this.findViewById(R.id.diceView1);
		views[1] = (TextView) this.findViewById(R.id.diceView2);
		buts = new Button[2];
		buts[0] = (Button) this.findViewById(R.id.scanBut1);
		buts[1] = (Button) this.findViewById(R.id.scanBut2);
		accBut = (Button) this.findViewById(R.id.acceptButton);
		pl = new PairingListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pairing, menu);
		return true;
	}
	
	public void pScan(int playerId){
		pl.pairPlayer(playerId);
	}
	
	public void p1Scan(View view){
		pScan(0);
	}
	
	public void p2Scan(View view){
		pScan(1);
	}

}
