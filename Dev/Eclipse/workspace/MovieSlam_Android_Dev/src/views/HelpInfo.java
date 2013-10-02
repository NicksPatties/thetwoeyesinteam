package views;

import tools.AdvRDAdjuster;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.example.movieslam_android_dev.R;

public class HelpInfo extends Activity {
	private boolean screenAdjusted = false;
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (!screenAdjusted){
			screenAdjusted = true;
	        AdvRDAdjuster.adjust(findViewById(R.id.help_info_wrapper));
	        System.out.println("#########print something");
		}
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help_info, menu);
		return true;
	}

}
