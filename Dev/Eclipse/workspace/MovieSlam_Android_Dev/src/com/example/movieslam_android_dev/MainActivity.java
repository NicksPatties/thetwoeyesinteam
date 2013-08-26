package com.example.movieslam_android_dev;

import com.example.movieslam_android_dev.MainActivity;
import com.example.movieslam_android_dev.AutoAdjmtPage;
import com.example.movieslam_android_dev.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button myButton = null;
	private Button myButton2 = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
	}
	
	class MyButtonLisiener implements OnClickListener{
		
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, AutoAdjmtPage.class);
			MainActivity.this.startActivity(intent);
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
