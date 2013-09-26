package views;

import models.Config;
import models.User;
import tools.AdvElement;
import tools.AdvImageLoader;
import tools.AdvRDAdjuster;
import tools.AdvResponseDelegate;
import tools.AdvRequestHandler;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.movieslam_android_dev.R;

public class GameHistory extends Activity implements AdvResponseDelegate, Config {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_history);
		AdvRDAdjuster.adjust(findViewById(R.id.game_history_wrapper));
		
		// get round detail from backend
		Bundle b_in = getIntent().getExtras();
		new AdvRequestHandler(this, BASE_URL+"/service/getPastGameplay.php?user_id="+User.get_uid()+"&player_id="+b_in.getString("player_id")).execute();
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
		TableLayout game_history_table = (TableLayout) findViewById(R.id.game_history_table);		
			
		// parse player challenges board
		AdvElement doc = new AdvElement(response);	
		AdvElement gameplays_e = doc.getElement("gameplays");		
		for (int i = 0; i < gameplays_e.getElementLength("gameplay"); i++){
			AdvElement gameplay_e = gameplays_e.getElement("gameplay", i);
			
			View player_challenge_cell = layoutInflater.inflate(R.layout.game_info_cell, game_history_table, false);
			game_history_table.addView(player_challenge_cell);
			/*
			if (i == 0){
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)player_challenge_cell.getLayoutParams();
				params.setMargins(0, 160, 0, 0);
				player_challenge_cell.setLayoutParams(params);
			}*/
			
			TextView game_result_txt = (TextView) player_challenge_cell.findViewById(R.id.game_result_txt);
			int user_won = Integer.parseInt(gameplay_e.getValue("user_won"));
			int player_won = Integer.parseInt(gameplay_e.getValue("player_won"));
			game_result_txt.setText((user_won == -1 ? "-" : Integer.toString(user_won)) +" : "+ (player_won == -1 ? "-" : Integer.toString(player_won)));
			
			ImageView user_tn = (ImageView) player_challenge_cell.findViewById(R.id.user_tn);
			new AdvImageLoader(user_tn).execute(b_in.getString("user_tn_url"));
			
			ImageView player_tn = (ImageView) player_challenge_cell.findViewById(R.id.player_tn);
			new AdvImageLoader(player_tn).execute(b_in.getString("player_tn_url"));			
			
		}
		
		AdvRDAdjuster.adjust(findViewById(R.id.game_history_table));
		
	}

}
