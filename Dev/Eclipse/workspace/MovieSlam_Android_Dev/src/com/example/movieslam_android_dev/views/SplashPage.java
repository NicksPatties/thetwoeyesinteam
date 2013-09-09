package com.example.movieslam_android_dev.views;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.tools.DownloadImageTask;

public class SplashPage extends Activity implements ResponseDelegate{
//  public class SplashPage extends Activity{ // used for testing game play page quickly
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);
        
        // call backend
        getGameinfo();
				
		// add main board content
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout user_panel = (LinearLayout) findViewById(R.id.user_panel);
		user_panel.addView(layoutInflater.inflate(R.layout.user_main_board, user_panel, false));
	}
	
	private void getGameinfo() {
		
		TableLayout score_table = (TableLayout) findViewById(R.id.score_table);
		score_table.removeAllViews();
		/*
		XmlRequestHandler xrh = new XmlRequestHandler(this);
        xrh.delegate = this;
		xrh.setURL("http://postpcmarketing.com/movieslam/intl/it/service/getGameInfo.php?user_id=0&fid=100000855108534");
		xrh.execute();
		*/
		new XmlRequestHandler(this,"http://postpcmarketing.com/movieslam/intl/it/service/getGameInfo.php?user_id=0&fid=100000855108534").execute();
	}

	public void gotoHelp(View view){
		
		startActivity(new Intent(getApplicationContext(), HelpInfo.class));
	}
	
	public void gotoNewChallenge(View view){		
		
//		startActivity(new Intent(getApplicationContext(), GenreSelection.class));
		startActivity(new Intent(getApplicationContext(), UserTypeSelection.class));
//		startActivity(new Intent(getApplicationContext(), ReadyToPlayPage.class));
	}
	
	public void gotoRefresh(View view){		
		getGameinfo();
	}
	
	@Override
	public void responseLoaded(String response) {
		// init root element
		AdvElement doc = new AdvElement(response);
		
		// parse user main board
		AdvElement user_e = doc.getElement("user");
		
		TextView userName_txt = (TextView)findViewById(R.id.userName_txt);
		userName_txt.setText(user_e.getValue("user_fname") + " " + user_e.getValue("user_lname"));
		
		TextView userID_txt = (TextView)findViewById(R.id.userID_txt); 
		userID_txt.setText(user_e.getValue("user_id"));
		
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
		}					
	}
	
}
