package com.example.movieslam_android_dev.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.models.Config;
import com.example.movieslam_android_dev.models.User;
import com.example.movieslam_android_dev.tools.AdvElement;
import com.example.movieslam_android_dev.tools.DownloadImageTask;
import com.example.movieslam_android_dev.tools.ResponseDelegate;
import com.example.movieslam_android_dev.tools.XmlRequestHandler;

import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.*;

public class SplashPage extends Activity implements ResponseDelegate, Config {
//  public class SplashPage extends Activity{ // used for testing game play page quickly
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);
        
        // hardcode to user id 3
        SharedPreferences user_info = this.getSharedPreferences("user_info", MODE_PRIVATE);
		Editor user_info_edit = user_info.edit();
		user_info_edit.clear();
		user_info_edit.putString("uid", "3");
		user_info_edit.commit();
		
		
        // check saved user id, create new user if user not existed
        String uid = getUIDFromDevice();
        if (uid != null){
        	// check for fb connect first!!!!!!!!!!!!
        	getGameinfo(uid, "0");
        }else{
        	// check for fb connect first!!!!!!!!!!!!
        	getGameinfo("0", "0", "guest", "Guest", BASE_URL+"/include/images/avatar.png");
        }
        
				
		// add main board content
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout user_panel = (LinearLayout) findViewById(R.id.user_panel);
		user_panel.addView(layoutInflater.inflate(R.layout.user_main_board, user_panel, false));
	}
	
	private String getUIDFromDevice() {
		
		SharedPreferences user_info = this.getSharedPreferences("user_info", MODE_PRIVATE);
		if (user_info.contains("uid") && !user_info.getString("uid", "").equals("")){
			return user_info.getString("uid", "");
		}else{
			return null;
		}
		
	}

	private void getGameinfo(String uid, String fid) {
		
		TableLayout score_table = (TableLayout) findViewById(R.id.score_table);
		score_table.removeAllViews();
		new XmlRequestHandler(this, BASE_URL+"/service/getGameInfo.php?user_id="+uid+"&fid="+fid).execute();
	}
	
	private void getGameinfo(String uid, String fid, String fname, String lname, String thumbnail) {
		
		TableLayout score_table = (TableLayout) findViewById(R.id.score_table);
		score_table.removeAllViews();
		new XmlRequestHandler(this, BASE_URL+"service/getGameInfo.php?user_id="+uid+"&fid="+fid+"&fname="+fname+"&lname="+lname+"&thumbnail="+thumbnail).execute();
		
	}

	public void gotoHelp(View view){
		
		startActivity(new Intent(getApplicationContext(), HelpInfo.class));
	}
	
	public void gotoNewChallenge(View view){
		
		startActivity(new Intent(getApplicationContext(), UserTypeSelection.class));
//		startActivity(new Intent(getApplicationContext(), ReadyToPlayPage.class));
	}
	
	public void gotoRefresh(View view){
		String uid = getUIDFromDevice();
		getGameinfo(uid, "0");
	}
	
	@Override
	public void responseLoaded(String response) {
		// init root element
		AdvElement doc = new AdvElement(response);
		
		
		// set User variables
		AdvElement user_e = doc.getElement("user");
		User.set_uid(user_e.getValue("user_id"));
		User.set_fname(user_e.getValue("user_fname"));
		User.set_lname(user_e.getValue("user_lname"));
				
		// parse user main board	
		TextView userName_txt = (TextView)findViewById(R.id.userName_txt);
		userName_txt.setText(User.get_fname() + " " + User.get_lname());
		
		TextView userID_txt = (TextView)findViewById(R.id.userID_txt);
		String uid = User.get_uid();		
		userID_txt.setText(uid);
		if (!this.getUIDFromDevice().equals(uid)){
			SharedPreferences user_info = this.getSharedPreferences("user_info", MODE_PRIVATE);
			Editor user_info_edit = user_info.edit();
			user_info_edit.clear();
			user_info_edit.putString("uid", uid);
			user_info_edit.commit();
			//Toast.makeText(this, "UID saved.", 3000).show();
		}		
		
		TextView userScore_txt = (TextView)findViewById(R.id.userScore_txt); 
		userScore_txt.setText(user_e.getValue("user_score"));
		
		new DownloadImageTask((ImageView) findViewById(R.id.userThumbnail_iv)).execute(user_e.getValue("user_thumbnail"));
		
		
		
		
		// parse player challenges board
		AdvElement gameplays_e = doc.getElement("gameplays");		
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TableLayout score_table = (TableLayout) findViewById(R.id.score_table);
		
		for (int i = 0; i < gameplays_e.getElementLength("gameplay"); i++){
			AdvElement gameplay_e = gameplays_e.getElement("gameplay", i);
			
			View player_challenge_cell = layoutInflater.inflate(R.layout.player_challenge_cell, score_table, false);
			score_table.addView(player_challenge_cell);
			
			TextView player_name_txt = (TextView) player_challenge_cell.findViewById(R.id.player_name_txt);
			player_name_txt.setText(gameplay_e.getValue("player_user_fname")+"\n"+gameplay_e.getValue("player_user_lname"));
			
			TextView player_score_txt = (TextView) player_challenge_cell.findViewById(R.id.player_score_txt);
			player_score_txt.setText(gameplay_e.getValue("gameplay_user_won")+":"+gameplay_e.getValue("gameplay_player_won"));
			
			new DownloadImageTask((ImageView) player_challenge_cell.findViewById(R.id.challenge_player_tn)).execute(gameplay_e.getValue("player_user_thumbnail"));
			
			// check challenger type
			Button b0 = (Button) player_challenge_cell.findViewById(R.id.player_challenge_b0);			
			Button b1 = (Button) player_challenge_cell.findViewById(R.id.player_challenge_b1);
			String gameplay_status = gameplay_e.getValue("gameplay_status");
			if (gameplay_status.equals("accept")){
				b0.setText("DECLINE");
				b1.setText("ACCEPT");
			}else if (gameplay_status.equals("end")){
				b0.setText("FORFEIT");
				b0.setEnabled(false);
				b0.setBackgroundResource(R.drawable.button_small_disabled);
				b1.setVisibility(View.INVISIBLE);
				player_score_txt.setText("-:-");
			}else{
				b0.setText("RESULT");
				b1.setVisibility(View.INVISIBLE);
			}
			
		}					
	}
	
}
