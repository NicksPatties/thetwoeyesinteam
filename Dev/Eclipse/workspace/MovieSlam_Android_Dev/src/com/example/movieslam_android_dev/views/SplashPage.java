package com.example.movieslam_android_dev.views;


import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.movieslam_android_dev.R;

public class SplashPage extends Activity implements ResponseDelegate, ImgRequestDelegate{
//  public class SplashPage extends Activity{ // used for testing game play page quickly

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);
        
        // call backend
        XmlRequestHandler xrh = new XmlRequestHandler();
        xrh.delegate = this;
		xrh.setURL("http://postpcmarketing.com/movieslam/intl/it/service/getGameInfo.php?user_id=0&fid=100000855108534");
		xrh.execute();
		
		
		// init layout inflater
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// add table content
		LinearLayout user_panel = (LinearLayout) findViewById(R.id.user_panel);
		user_panel.addView(layoutInflater.inflate(R.layout.user_main_board, user_panel, false));
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
			
			
			NodeList nodeList = doc.getElementsByTagName("user");
			Node userNode = nodeList.item(0);
			
			NodeList userFnameList = ((Element)userNode).getElementsByTagName("user_fname");
			Element userFnameElement = (Element) userFnameList.item(0);
			userFnameList = userFnameElement.getChildNodes();
			
			NodeList userLnameList = ((Element)userNode).getElementsByTagName("user_lname");
			Element userLnameElement = (Element) userLnameList.item(0);
			userLnameList = userLnameElement.getChildNodes();
			
			NodeList userIDList = ((Element)userNode).getElementsByTagName("user_id");
			Element userIDElement = (Element) userIDList.item(0);
			userIDList = userIDElement.getChildNodes();
			
			NodeList userScoreList = ((Element)userNode).getElementsByTagName("user_score");
			Element userScoreElement = (Element) userScoreList.item(0);
			userScoreList = userScoreElement.getChildNodes();
			
			NodeList userThumbnailList = ((Element)userNode).getElementsByTagName("user_thumbnail");
			Element userThumbnailElement = (Element) userThumbnailList.item(0);
			userThumbnailList = userThumbnailElement.getChildNodes();
			
			TextView userName_txt = (TextView)findViewById(R.id.userName_txt); 
			userName_txt.setText(((Node) userFnameList.item(0)).getNodeValue() + " " + ((Node) userLnameList.item(0)).getNodeValue());
			
			TextView userID_txt = (TextView)findViewById(R.id.userID_txt); 
			userID_txt.setText(((Node) userIDList.item(0)).getNodeValue());
			
			TextView userScore_txt = (TextView)findViewById(R.id.userScore_txt); 
			userScore_txt.setText(((Node) userScoreList.item(0)).getNodeValue());
			
			// get img from URL using another thread
			ImgRequestHandler irh = new ImgRequestHandler();
			irh.delegate = this;
			irh.init(((Node) userThumbnailList.item(0)).getNodeValue(), R.id.userThumbnail_iv);
			irh.execute();
			
			
			// parse player challenges
			LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			TableLayout score_table = (TableLayout) findViewById(R.id.score_table);
			for (int i = 0; i < 8; i++){
				View player_challenge_cell = layoutInflater.inflate(R.layout.player_challenge_cell, score_table, false);
				score_table.addView(player_challenge_cell);
				ImgRequestHandler challenge_player_tn = new ImgRequestHandler();
				challenge_player_tn.delegate = this;
				ImageView player_iv = (ImageView) player_challenge_cell.findViewById(R.id.challenge_player_tn);
				challenge_player_tn.init("http://graph.facebook.com/639473268/picture?type=large", player_iv.getId());
				challenge_player_tn.execute();
				
				TextView player_name_txt = (TextView) player_challenge_cell.findViewById(R.id.player_name_txt);
				player_name_txt.setText("Osk Osk"+i);
			}
			
		} catch (Exception e) {
			Log.d("debug", "Exception");
		}
		
	}

	@Override
	public void imgLoaded(Bitmap bitmap, int id) {
		//ImageView userThumbnail_iv = (ImageView) findViewById(R.id.userThumbnail_iv);
		ImageView iv = (ImageView) findViewById(id);
		iv.setImageBitmap(bitmap);
	}

}
