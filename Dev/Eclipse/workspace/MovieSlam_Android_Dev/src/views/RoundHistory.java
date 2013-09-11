package views;

import models.Config;
import models.User;
import tools.AdvElement;
import tools.ResponseDelegate;
import tools.XmlRequestHandler;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.R.layout;
import com.example.movieslam_android_dev.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;

public class RoundHistory extends Activity implements ResponseDelegate, Config {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.round_history);
		
		// get round detail from backend
		Bundle b_in = getIntent().getExtras();
		
		new XmlRequestHandler(this, BASE_URL+"/service/getRoundDetail.php?game_id="+b_in.getString("game_id")+"&user_id="+User.get_uid()).execute();
		Log.d("debug", BASE_URL+"/service/getRoundDetail.php?game_id="+b_in.getString("game_id")+"&user_id="+User.get_uid());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.round_history, menu);
		return true;
	}

	@Override
	public void responseLoaded(String response) {
		
		// generate round details
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TableLayout round_info_wrapper = (TableLayout) findViewById(R.id.round_info_wrapper);
		for (int i = 0; i < 5; i++){
			
			View player_challenge_cell = layoutInflater.inflate(R.layout.round_info_cell, round_info_wrapper, false);
			round_info_wrapper.addView(player_challenge_cell);
		}
		
	}

}
