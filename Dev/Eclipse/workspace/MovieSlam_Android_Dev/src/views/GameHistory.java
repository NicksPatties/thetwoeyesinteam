package views;

import models.Config;
import models.User;
import tools.AdvElement;
import tools.DownloadImageTask;
import tools.ResponseDelegate;
import tools.XmlRequestHandler;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.movieslam_android_dev.R;

public class GameHistory extends Activity implements ResponseDelegate, Config {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_history);
		
		// get round detail from backend
		Bundle b_in = getIntent().getExtras();
		new XmlRequestHandler(this, BASE_URL+"/service/getPastGameplay.php?user_id="+User.get_uid()+"&player_id="+b_in.getString("player_id")).execute();
		Log.d("debug", BASE_URL+"/service/getPastGameplay.php?user_id="+User.get_uid()+"&player_id="+b_in.getString("player_id"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_history, menu);
		return true;
	}

	@Override
	public void responseLoaded(String response) {
		Bundle b_in = getIntent().getExtras();
		
		// add cells
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TableLayout round_info_wrapper = (TableLayout) findViewById(R.id.game_info_wrapper);		
			
		// parse player challenges board
		AdvElement doc = new AdvElement(response);	
		AdvElement gameplays_e = doc.getElement("gameplays");		
		for (int i = 0; i < gameplays_e.getElementLength("gameplay"); i++){
			AdvElement gameplay_e = gameplays_e.getElement("gameplay", i);
			
			View player_challenge_cell = layoutInflater.inflate(R.layout.game_info_cell, round_info_wrapper, false);
			round_info_wrapper.addView(player_challenge_cell);
			
			TextView game_result_txt = (TextView) player_challenge_cell.findViewById(R.id.game_result_txt);
			int user_won = Integer.parseInt(gameplay_e.getValue("user_won"));
			int player_won = Integer.parseInt(gameplay_e.getValue("player_won"));
			game_result_txt.setText((user_won == -1 ? "-" : Integer.toString(user_won)) +" : "+ (player_won == -1 ? "-" : Integer.toString(player_won)));
			
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(game_result_txt.getMeasuredHeight(), game_result_txt.getMeasuredHeight());
			
			ImageView user_tn = (ImageView) player_challenge_cell.findViewById(R.id.user_tn);
			new DownloadImageTask(user_tn).execute(b_in.getString("user_tn_url"));
			//user_tn.setLayoutParams(layoutParams);
			//user_tn.setLayoutParams(new LayoutParams(game_result_txt.getMeasuredHeight(), game_result_txt.getMeasuredHeight()));
			
			ImageView player_tn = (ImageView) player_challenge_cell.findViewById(R.id.player_tn);
			new DownloadImageTask(player_tn).execute(b_in.getString("player_tn_url"));			
			//player_tn.setLayoutParams(layoutParams);
			//player_tn.setLayoutParams(new LayoutParams(game_result_txt.getMeasuredHeight(), game_result_txt.getMeasuredHeight()));
			
		}
		
	}

}
