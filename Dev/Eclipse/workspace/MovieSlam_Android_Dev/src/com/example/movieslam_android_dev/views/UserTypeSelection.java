package com.example.movieslam_android_dev.views;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.R.layout;
import com.example.movieslam_android_dev.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class UserTypeSelection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_type_selection);
	}
	
	public void gotoGenreSelection(View view){		
		
		startActivity(new Intent(getApplicationContext(), GenreSelection.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_type_selection, menu);
		return true;
	}

}
