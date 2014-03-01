package eu.rotato.diceplushackaton;

import eu.rotato.diceplushackaton.model.Game;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;

public class GameActivity extends Activity {

	Game game = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lay);
		LinearLayout tl = (LinearLayout)findViewById(R.id.table);
		
		game = new Game(tl, 10, 10, 2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
