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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.movieslam_android_dev.R;

public class SplashPage extends Activity implements ResponseDelegate{

	private Button myButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);
        
        myButton = (Button)this.findViewById(R.id.btn_splash);
        myButton.setOnClickListener(new MyButtonLisiener());
 
        xmlRequestHandler xrh = new xmlRequestHandler();
        xrh.delegate = this;
        // hardcode api to test
		xrh.setURL("http://postpcmarketing.com/movieslam/intl/it/service/getGameInfo.php?user_id=0&fid=100000855108534");
		xrh.execute();
        
	}
	/*protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main); 
 
        myButton = (Button)this.findViewById(R.id.btn_splash);
        myButton.setOnClickListener(new MyButtonLisiener());
        
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams( 
            ViewGroup.LayoutParams.FILL_PARENT, //width 
            ViewGroup.LayoutParams.WRAP_CONTENT //height 
        ); 
        
        DisplayMetrics dm = new DisplayMetrics(); 
		dm = getResources().getDisplayMetrics(); 
		int screenWidth = dm.widthPixels; 
		int screenHeight = dm.heightPixels; 
		float density = dm.density; 
		float xdpi = dm.xdpi; 
		float ydpi = dm.ydpi; 
 
        //set textView layout_below="@id/button1" 
        lp.addRule(RelativeLayout.BELOW, R.id.btn_splash); 
 
        ((RelativeLayout) findViewById(R.id.mainActivity)).addView(new TextView(this), lp);
	}*/
	
	class MyButtonLisiener implements OnClickListener{
		
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setClass(SplashPage.this, AutoAdjmtPage.class);
			SplashPage.this.startActivity(intent);
		}
	}
	
	// assuming the aspect ratio we chose is the thinnest
	// fill the height to 100% of the screen size, then set the width accordingly
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		
	    super.onWindowFocusChanged(hasFocus);
	    if (hasFocus == true) {
	    	LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.ContainerLayout);
	        int layoutHeight = myLinearLayout.getHeight();
	        
	        android.view.ViewGroup.LayoutParams lp = myLinearLayout.getLayoutParams();
	        // adjust the ratio here if thinner screen is needed
	        lp.width = layoutHeight*9/16;
	        myLinearLayout.setLayoutParams(lp);
	    }
	    
	}

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
			
			TextView t = (TextView)findViewById(R.id.text1); 
		    t.setText("First Name from getGameinfo : "+ ((Node) userFnameList.item(0)).getNodeValue());
		} catch (Exception e) {
			Log.d("debug", "Exception");
		}
		
	}
	
    /**
	I don't know what is this for, it's created by the project wizard
	**/
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
