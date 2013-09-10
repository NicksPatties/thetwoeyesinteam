package com.example.movieslam_android_dev.views;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.R.layout;
import com.example.movieslam_android_dev.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class UidInputPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uid_input_page);
	}
	
	public void gotoPreviousPage(View view){		
		
		this.finish();
	}
	
	public void gotoGenreSelection(View view){		
		
		startActivity(new Intent(getApplicationContext(), GenreSelection.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.uid_input_page, menu);
		return true;
	}

}
