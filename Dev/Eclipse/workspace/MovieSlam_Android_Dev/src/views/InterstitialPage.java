package views;

import models.Gameplay;
import models.User;

import com.example.movieslam_android_dev.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class InterstitialPage extends Activity {

	private Thread thread;
	
	private TextView userNameText;
	private TextView oppoNameText;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.interstitial_page);
        
        userNameText = (TextView) findViewById(R.id.user_name_interstitial);
        oppoNameText = (TextView) findViewById(R.id.oppo_name_interstitial);
        
        if (Gameplay.getChallType().equals("self")){
			userNameText.setText(User.get_fname() + "    "+ User.get_lname());
			oppoNameText.setText(Gameplay.getOppoFName() + "    "+ Gameplay.getOppoLName());
        }else{
			userNameText.setText(User.get_fname() + "    "+ User.get_lname());
			oppoNameText.setText(Gameplay.getOppoFName() + "    "+ Gameplay.getOppoLName());
        }
        
        thread=  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(3000);
                        startActivity(new Intent(getApplicationContext(), ResultPage.class));
                    }
                }
                catch(InterruptedException ex){                    
                }

                // TODO              
            }
        };
        
        thread.start();
	}
}
