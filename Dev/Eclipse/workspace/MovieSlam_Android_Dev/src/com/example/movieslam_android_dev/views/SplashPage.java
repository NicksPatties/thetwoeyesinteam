package com.example.movieslam_android_dev.views;


import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.movieslam_android_dev.R;

public class SplashPage extends Activity implements ResponseDelegate, ImgRequestDelegate{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);
        
 
        XmlRequestHandler xrh = new XmlRequestHandler();
        xrh.delegate = this;
        // hardcode api to test
		xrh.setURL("http://postpcmarketing.com/movieslam/intl/it/service/getGameInfo.php?user_id=0&fid=100000855108534");
		xrh.execute();
	}
	
	public void gotoHelp(View view){		
		
		startActivity(new Intent(getApplicationContext(), HelpInfo.class));
	}
	
	public void gotoNewChallenge(View view){		
		
		startActivity(new Intent(getApplicationContext(), GenreSelection.class));
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
			irh.setURL(((Node) userThumbnailList.item(0)).getNodeValue());
			irh.execute();
			
			// generate challenge boxes
			LinearLayout challenges_container = (LinearLayout) findViewById(R.id.challenges_container);
			for(int i = 0; i < 10; i++) {
				
				
				ImageView challenge_box_bg = new ImageView(this);
				challenge_box_bg.setImageResource(R.drawable.panel_challengeboard_inactive);
				challenge_box_bg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				challenge_box_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
				challenge_box_bg.setAdjustViewBounds(true);
				challenge_box_bg.setPadding(15, 15, 15, 0);
				
				
				Button accept_btn = new Button(this);
				accept_btn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				accept_btn.setText("Accept");				
				accept_btn.setTextColor(Color.parseColor("#FFFFFF"));
				accept_btn.setHeight(10);
				accept_btn.setWidth(10);
				accept_btn.setBackgroundResource(R.drawable.button_bg);
				
				LinearLayout challenge_box_content = new LinearLayout(this);
				challenge_box_content.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				
				RelativeLayout challenge_box = new RelativeLayout(this);
				challenge_box.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				
				challenge_box.addView(challenge_box_bg);
				challenge_box.addView(accept_btn);
				
	            challenges_container.addView(challenge_box);
	        }
			
		} catch (Exception e) {
			Log.d("debug", "Exception");
		}
		
	}

	@Override
	public void imgLoaded(Bitmap bitmap) {
		ImageView userThumbnail_iv = (ImageView) findViewById(R.id.userThumbnail_iv);
		userThumbnail_iv.setImageBitmap(bitmap);
	}

}
