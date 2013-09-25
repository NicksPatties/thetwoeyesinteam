package views;

import tools.AdvRDAdjuster;
import models.Gameplay;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.movieslam_android_dev.R;

public class GenreSelection extends Activity {
	private boolean screenAdjusted = false;

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
		
		// check for promo genre
		Button promo_btn = (Button)findViewById(R.id.promo_btn);
		if (Gameplay.get_promo() == 1){			
			promo_btn.setText(Gameplay.get_promo_name());
		}else{
			promo_btn.setVisibility(View.GONE);
		}
	}
	
	public void gotoGamePlayPage(View view){
		Gameplay.setChallID("0");
		Gameplay.setChallType("self");
		Bundle b_in = getIntent().getExtras();		
		Intent intent = new Intent(getApplicationContext(), ReadyToPlayPage.class);
		Bundle b_out = new Bundle();
		b_out.putString("target_source_type", b_in.getString("target_source_type"));
		b_out.putString("target_id", b_in.getString("target_id"));
		b_out.putString("target_genre", (String) view.getTag());
		Gameplay.setGenre((String) view.getTag());
		intent.putExtras(b_out);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.genre_selection, menu);		
		return true;
	}

}
