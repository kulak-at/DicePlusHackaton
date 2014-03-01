package eu.rotato.diceplushackaton;

import eu.rotato.diceplushackaton.model.Game;
import android.os.Bundle;
import android.os.PowerManager;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;

public class GameActivity extends Activity {

	Game game = null;
	
	public static int gcd(int p, int q) {
	    if (q == 0) {
	      return p;
	    }
	    return gcd(q, p % q);
	  }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
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
        
        int multiplier = 1;
        
        setContentView(R.layout.lay);
		LinearLayout tl = (LinearLayout)findViewById(R.id.table);
		
		game = new Game(tl, h_c*multiplier, w_c*multiplier, 2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
