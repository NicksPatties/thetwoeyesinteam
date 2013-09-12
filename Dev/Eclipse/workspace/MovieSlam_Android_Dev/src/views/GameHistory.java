package views;

import models.Config;
import models.User;
import tools.ResponseDelegate;
import tools.XmlRequestHandler;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.R.layout;
import com.example.movieslam_android_dev.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GameHistory extends Activity implements ResponseDelegate, Config {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_history);
		
		// get round detail from backend
		Bundle b_in = getIntent().getExtras();
		new XmlRequestHandler(this, BASE_URL+"/service/getPastGameplay.php?user_id="+User.get_uid()+"&player_id="+b_in.getString("player_id")).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_history, menu);
		return true;
	}

	@Override
	public void responseLoaded(String response) {
		// TODO Auto-generated method stub
		
	}

}
