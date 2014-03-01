package eu.rotato.diceplushackaton;

import us.dicepl.android.sdk.DiceController;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import eu.rotato.diceplushackaton.model.Game;

public class GameActivity extends Activity {

	Game game = null;
	private GameListener gl;
	//private PairingListener pl;
	
	public static int gcd(int p, int q) {
	    if (q == 0) {
	      return p;
	    }
	    return gcd(q, p % q);
	  }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//pl = PairingListener.getInstance();
		//pl.setGameActivity(this);
		
		gl = new GameListener(this);
		DiceController.registerDiceResponseListener(gl);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
         
        DisplayMetrics display = getResources().getDisplayMetrics();
		
        int width = display.widthPixels;
        int height = display.heightPixels;
        
        int gg = gcd(width, height);
        
        int w_c = width / gg;
        int h_c = height / gg;
        if(w_c > 20 || h_c > 20) {
        	w_c /= 5;
        	h_c /= 5;
        }
        
        int multiplier = Global.getMultiplier();
        int timer = Global.getTimer();
        int points_limit = Global.getPointsLimit();
        
        setContentView(R.layout.lay);
		LinearLayout tl = (LinearLayout)findViewById(R.id.table);
		RelativeLayout pv = (RelativeLayout)findViewById(R.id.pointz);
		View points_board = findViewById(R.id.results);
		
		game = new Game(tl, pv, points_board, h_c*multiplier, w_c*multiplier, 2, timer, points_limit);
		Global.setGame(game);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		DiceController.unregisterDiceResponseListener(gl);
	}
}
