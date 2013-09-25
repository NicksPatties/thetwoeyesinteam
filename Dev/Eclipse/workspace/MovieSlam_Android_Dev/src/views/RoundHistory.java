package views;

import models.Config;
import models.User;
import tools.AdvButtonListener;
import tools.AdvElement;
import tools.AdvRDAdjuster;
import tools.AdvRequestHandler;
import tools.AdvResponseDelegate;
import tools.AdvImageLoader;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.movieslam_android_dev.R;

public class RoundHistory extends Activity implements AdvResponseDelegate, Config {
	
	private boolean screenAdjusted = false;
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (!screenAdjusted){
			screenAdjusted = true;
	        //AdvRDAdjuster.adjust(findViewById(R.id.round_history_wrapper));
		}
		super.onWindowFocusChanged(hasFocus);
	}

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
		AdvElement user_rounds_e = doc.getElement("user");
		AdvElement player_rounds_e = doc.getElement("player");
		
		// generate round details
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TableLayout round_info_wrapper = (TableLayout) findViewById(R.id.round_info_wrapper);
		for (int i = 0; i < doc.getElementLength("media"); i++){
			AdvElement media_e = doc.getElement("media", i);
			
			View round_info_cell = layoutInflater.inflate(R.layout.round_info_cell, round_info_wrapper, false);
			round_info_wrapper.addView(round_info_cell);
			
			ImageView movie_tn = (ImageView) round_info_cell.findViewById(R.id.movie_tn);
			new AdvImageLoader(movie_tn).execute(BASE_URL + "/" + media_e.getValue("media_thumbnail"));
			
			
			Bundle b_in = getIntent().getExtras();	
			ImageView user_tn = (ImageView) round_info_cell.findViewById(R.id.round_user_tn);
			new AdvImageLoader(user_tn).execute(b_in.getString("user_tn_url"));
			
			TextView round_user_txt = (TextView) round_info_cell.findViewById(R.id.round_user_txt);
			double round_user = Double.parseDouble(user_rounds_e.getValue("elapsed", i));
			if (round_user == -1){
				round_user_txt.setText("WRONG");
				round_user_txt.setTextColor(0xFFFF0000);
			}else{
				round_user_txt.setText(Double.toString(round_user)+"s");
			}		
			
			ImageView play_tn = (ImageView) round_info_cell.findViewById(R.id.round_player_tn);
			new AdvImageLoader(play_tn).execute(b_in.getString("player_tn_url"));
			
			TextView round_player_txt = (TextView) round_info_cell.findViewById(R.id.round_player_txt);
			double round_player = Double.parseDouble(player_rounds_e.getValue("elapsed", i));
			if (round_player == -1){
				round_player_txt.setText("WRONG");
				round_player_txt.setTextColor(0xFFFF0000);
			}else{
				round_player_txt.setText(Double.toString(round_player)+"s");
			}	
			
			round_player_txt.setText(player_rounds_e.getValue("elapsed", i).equals("-1") ? "WRONG" : player_rounds_e.getValue("elapsed", i));
			
			TextView movie_txt = (TextView) round_info_cell.findViewById(R.id.movie_txt);
			movie_txt.setText(media_e.getValue("media_name"));
			
			Button buy_txt = (Button) round_info_cell.findViewById(R.id.buy_txt);
			buy_txt.setTag(media_e.getValue("media_etailer"));
			OnClickListener buy_txt_ltn = new AdvButtonListener(null, this) {
				@Override
				public void onClick(View v) {
					Log.d("debug", "something on clicjk");
					Intent i = new Intent(Intent.ACTION_VIEW);
					String url = (String) v.getTag();
					i.setData(Uri.parse(url));
					startActivity(i);
				}
			};
			buy_txt.setOnClickListener(buy_txt_ltn);
		}
		AdvRDAdjuster.adjust(findViewById(R.id.round_history_wrapper));
	}

}
