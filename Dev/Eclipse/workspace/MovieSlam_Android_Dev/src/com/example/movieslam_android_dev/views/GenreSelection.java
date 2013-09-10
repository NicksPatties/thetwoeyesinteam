package com.example.movieslam_android_dev.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.movieslam_android_dev.R;

public class GenreSelection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.genre_selection);
	}
	
	public void gotoGamePlayPage(View view){
		Bundle b_in = getIntent().getExtras();		
		Intent intent = new Intent(getApplicationContext(), ReadyToPlayPage.class);
		Bundle b_out = new Bundle();
		b_out.putString("target_player_type", b_in.getString("target_player_type"));
		b_out.putString("target_player_id", b_in.getString("target_player_id"));
		b_out.putString("target_genre", (String) view.getTag());
		intent.putExtras(b_out);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.genre_selection, menu);		
		return true;
	}

}
