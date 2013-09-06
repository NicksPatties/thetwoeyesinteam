package com.example.movieslam_android_dev.views;


import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.app.ProgressDialog;
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
				
		// add table content
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout user_panel = (LinearLayout) findViewById(R.id.user_panel);
		user_panel.addView(layoutInflater.inflate(R.layout.user_main_board, user_panel, false));
	}
	
	private void getGameinfo() {
		
		TableLayout score_table = (TableLayout) findViewById(R.id.score_table);
		score_table.removeAllViews();
		
		
		
		XmlRequestHandler xrh = new XmlRequestHandler(this);
        xrh.delegate = this;
		xrh.setURL("http://postpcmarketing.com/movieslam/intl/it/service/getGameInfo.php?user_id=0&fid=100000855108534");
		xrh.execute();	
	}

	public void gotoHelp(View view){
		
		startActivity(new Intent(getApplicationContext(), HelpInfo.class));
	}
	
	public void gotoNewChallenge(View view){		
		
		startActivity(new Intent(getApplicationContext(), GenreSelection.class));
//		startActivity(new Intent(getApplicationContext(), UserTypeSelection.class));
//		startActivity(new Intent(getApplicationContext(), ReadyToPlayPage.class));
	}
	
	public void gotoRefresh(View view){		
		getGameinfo();
	}
	
	/*
	// assuming the aspect ratio we chose is the thinnest
	// fill the height to 100% of the screen size, then set the width accordingly
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		
	    super.onWindowFocusChanged(hasFocus);
	    if (hasFocus == true) {
	    	LinearLayout layout = (LinearLayout) findViewById(R.id.ContainerLayout);
	        int layoutHeight = layout.getHeight();
	        
	        android.view.ViewGroup.LayoutParams lp = layout.getLayoutParams();
	        // adjust the ratio here if thinner screen is needed
	        lp.width = layoutHeight*9/16;
	        layout.setLayoutParams(lp);
	    }
	    
	}
	*/
	
	@Override
	public void responseLoaded(String response) {		
		// could use regx for better result
		response = response.replace("\n<?xml version=\"1.0\"?>\n", "");
		
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(response)));
			doc.getDocumentElement().normalize();
			
			// parse user main board
			Element userNode = (Element) doc.getElementsByTagName("user").item(0);
			
			TextView userName_txt = (TextView)findViewById(R.id.userName_txt);
			userName_txt.setText(userNode.getElementsByTagName("user_fname").item(0).getChildNodes().item(0).getNodeValue() + " " + userNode.getElementsByTagName("user_lname").item(0).getChildNodes().item(0).getNodeValue());
			
			TextView userID_txt = (TextView)findViewById(R.id.userID_txt); 
			userID_txt.setText(userNode.getElementsByTagName("user_id").item(0).getChildNodes().item(0).getNodeValue());
			
			TextView userScore_txt = (TextView)findViewById(R.id.userScore_txt); 
			userScore_txt.setText(userNode.getElementsByTagName("user_score").item(0).getChildNodes().item(0).getNodeValue());
			
			new DownloadImageTask((ImageView) findViewById(R.id.userThumbnail_iv)).execute(userNode.getElementsByTagName("user_thumbnail").item(0).getChildNodes().item(0).getNodeValue());
			
			
			// parse player challenges
			Element gameplays_e = (Element) doc.getElementsByTagName("gameplays").item(0);
			
			LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			TableLayout score_table = (TableLayout) findViewById(R.id.score_table);
			for (int i = 0; i < gameplays_e.getElementsByTagName("gameplay").getLength(); i++){
				Element gameplay_e = (Element) gameplays_e.getElementsByTagName("gameplay").item(i);
				
				View player_challenge_cell = layoutInflater.inflate(R.layout.player_challenge_cell, score_table, false);
				score_table.addView(player_challenge_cell);
				
				new DownloadImageTask((ImageView) player_challenge_cell.findViewById(R.id.challenge_player_tn)).execute(gameplay_e.getElementsByTagName("player_user_thumbnail").item(0).getChildNodes().item(0).getNodeValue());
				
				TextView player_name_txt = (TextView) player_challenge_cell.findViewById(R.id.player_name_txt);
				player_name_txt.setText(gameplay_e.getElementsByTagName("player_user_fname").item(0).getChildNodes().item(0).getNodeValue()+"\n"+gameplay_e.getElementsByTagName("player_user_lname").item(0).getChildNodes().item(0).getNodeValue());
				
				TextView player_score_txt = (TextView) player_challenge_cell.findViewById(R.id.player_score_txt);
				player_score_txt.setText(gameplay_e.getElementsByTagName("gameplay_user_won").item(0).getChildNodes().item(0).getNodeValue()+":"+gameplay_e.getElementsByTagName("gameplay_player_won").item(0).getChildNodes().item(0).getNodeValue());
			}
			
		} catch (Exception e) {
			Log.d("debug", "Exception");
		}
	}

}
