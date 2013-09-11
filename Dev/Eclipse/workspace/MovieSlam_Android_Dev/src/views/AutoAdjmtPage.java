package views;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.R.id;
import com.example.movieslam_android_dev.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AutoAdjmtPage extends Activity {

	private TextView myTextView = null;
	private Button myButton = null;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autoadjmt_page);
		
		myTextView = (TextView)findViewById(R.id.textview_autoadj);
		myTextView.setVisibility(View.INVISIBLE);
//		myTextView.setText("screenWidth,,screenHeight: "+screenWidth+","+screenHeight);
//		myTextView.setWidth(screenWidth/2);
//	    myTextView.setBackgroundColor(0xffffffff);
		
		myButton = (Button)this.findViewById(R.id.btn_next);
        myButton.setOnClickListener(new ButtonLisiener());
        
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams( 
                ViewGroup.LayoutParams.FILL_PARENT, //width 
                ViewGroup.LayoutParams.WRAP_CONTENT //height 
        ); 
        
        lp.addRule(RelativeLayout.BELOW, R.id.btn_next); 
        
        ((RelativeLayout) findViewById(R.id.autoadjPage)).addView(new TextView(this), lp);
	}
	
	class ButtonLisiener implements OnClickListener{
		
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setClass(AutoAdjmtPage.this, GamePlayPage.class);
			AutoAdjmtPage.this.startActivity(intent);
		}
	}
	
}
