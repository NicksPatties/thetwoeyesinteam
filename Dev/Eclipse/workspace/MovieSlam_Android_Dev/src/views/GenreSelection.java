package views;

import models.Gameplay;
import models.Round;
import tools.AdvActivityStarter;
import tools.AdvRDAdjuster;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.movieslam_android_dev.R;

@SuppressWarnings("serial")
public class GenreSelection extends Activity {
	private boolean screenAdjusted = false;
	private Round round;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (!screenAdjusted){
			screenAdjusted = true;
	        AdvRDAdjuster.setScale(findViewById(R.id.genre_selection_wrapper), findViewById(R.id.main_wrapper));
	        AdvRDAdjuster.adjust(findViewById(R.id.genre_selection_wrapper));
		}
		super.onWindowFocusChanged(hasFocus);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.genre_selection);
		round = (Round) getIntent().getSerializableExtra("round_info");
		
		// check for promo genre
		Button promo_btn = (Button)findViewById(R.id.promo_btn);
		if (Gameplay.get_promo() == 1){			
			promo_btn.setText(Gameplay.get_promo_name());
		}else{
			promo_btn.setVisibility(View.GONE);
		}		
	}
	
	public void gotoGamePlayPage(View view){
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
		WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		
		round.challenge_id = 0;
		round.genre = (String) view.getTag();
		new AdvActivityStarter(this, ReadyToPlayPage.class, 0, round).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.genre_selection, menu);		
		return true;
	}

}
