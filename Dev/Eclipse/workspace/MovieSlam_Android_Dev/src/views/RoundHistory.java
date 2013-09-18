package views;

import models.Config;
import models.User;
import tools.AdvElement;
import tools.DownloadImageTask;
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
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.movieslam_android_dev.R;

public class RoundHistory extends Activity implements AdvResponseDelegate, Config {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.round_history);
		
		// get round detail from backend
		Bundle b_in = getIntent().getExtras();		
		new AdvRequestHandler(this, BASE_URL+"/service/getRoundDetail.php?game_id="+b_in.getString("game_id")+"&user_id="+User.get_uid()).execute();
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
		// init root element
		AdvElement doc = new AdvElement(response);
		
		// generate round details
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TableLayout round_info_wrapper = (TableLayout) findViewById(R.id.round_info_wrapper);
		for (int i = 0; i < doc.getElementLength("media"); i++){
			AdvElement media_e = doc.getElement("media", i);
			
			View round_info_cell = layoutInflater.inflate(R.layout.round_info_cell, round_info_wrapper, false);
			round_info_wrapper.addView(round_info_cell);
			
			ImageView movie_tn = (ImageView) round_info_cell.findViewById(R.id.movie_tn);
			new DownloadImageTask(movie_tn).execute(BASE_URL + "/" + media_e.getValue("media_thumbnail"));
			/*
			ImageView user_tn = (ImageView) round_info_cell.findViewById(R.id.round_user_tn);
			new DownloadImageTask(user_tn).execute(BASE_URL + "/" + media_e.getValue("user_tn_url"));
			
			ImageView play_tn = (ImageView) round_info_cell.findViewById(R.id.round_player_tn);
			new DownloadImageTask(play_tn).execute(BASE_URL + "/" + media_e.getValue("player_tn_url"));
			*/
			TextView movie_txt = (TextView) round_info_cell.findViewById(R.id.movie_txt);
			movie_txt.setText(media_e.getValue("media_name"));
		}
		
	}

}
