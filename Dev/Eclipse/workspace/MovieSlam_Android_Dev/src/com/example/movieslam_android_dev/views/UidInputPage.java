package com.example.movieslam_android_dev.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.models.Config;
import com.example.movieslam_android_dev.models.User;
import com.example.movieslam_android_dev.tools.AdvElement;
import com.example.movieslam_android_dev.tools.ResponseDelegate;
import com.example.movieslam_android_dev.tools.XmlRequestHandler;

public class UidInputPage extends Activity implements ResponseDelegate, Config {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uid_input_page);
	}
	
	public void gotoCancel(View view){		
		
		this.finish();
	}
	
	public void gotoGenreSelection(View view){
		
		EditText uid_txt = (EditText) this.findViewById(R.id.uid_txt);
		String uid_s = uid_txt.getText().toString();
		
		if (!uid_s.equals("") && Integer.parseInt(uid_s) != Integer.parseInt(User.get_uid())){
			new XmlRequestHandler(this, BASE_URL+"/service/checkUID.php?user_id="+User.get_uid()+"&player_id="+uid_s+"&fb=0").execute();
		}else{
			Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("ERROR");
            alert.setMessage("Sorry, that User ID is not valid.");
            alert.setPositiveButton("OK", null);
            alert.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.uid_input_page, menu);
		return true;
	}

	@Override
	public void responseLoaded(String response) {
		// init root element
		AdvElement doc = new AdvElement(response);		
		
		// parse response
		String valid = doc.getValue("valid");
		if (valid.equals("1")){
			EditText uid_txt = (EditText) this.findViewById(R.id.uid_txt);
			Intent intent = new Intent(getApplicationContext(), GenreSelection.class);
			Bundle b = new Bundle();
			b.putString("target_player_type", "uid");
			b.putString("target_player_id", uid_txt.getText().toString());
			intent.putExtras(b);
			startActivity(intent);
			finish();			
		}else{			
			Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("ERROR");
            alert.setMessage("Sorry, that User ID doesn't exist.");
            alert.setPositiveButton("OK", null);
            alert.show();
		}
	}

}
