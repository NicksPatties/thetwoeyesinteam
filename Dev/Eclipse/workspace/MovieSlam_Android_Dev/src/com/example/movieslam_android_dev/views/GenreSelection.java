package com.example.movieslam_android_dev.views;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.R.layout;
import com.example.movieslam_android_dev.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GenreSelection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_genre_selection);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.genre_selection, menu);
		return true;
	}

}
