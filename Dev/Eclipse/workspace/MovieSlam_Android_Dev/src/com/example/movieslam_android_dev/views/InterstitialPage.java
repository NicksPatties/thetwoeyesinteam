package com.example.movieslam_android_dev.views;

import com.example.movieslam_android_dev.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class InterstitialPage extends Activity {

	private Thread thread;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.interstitial_page);
        
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
