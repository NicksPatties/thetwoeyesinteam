package views;

import tools.AdvActivityStarter;
import tools.AdvImageLoader;
import tools.AdvRDAdjuster;
import tools.DownloadImageTask;
import models.Config;
import models.Gameplay;
import models.User;

import com.example.movieslam_android_dev.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class InterstitialPage extends Activity implements Config {
	// previous version
	/*
	private Thread thread;
	private ImageView gameResult;
	
	protected void onCreate(Bundle savedInstanceState) {
		try {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.readytoplay_page);
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.readytoplayPage);
		ll.setBackgroundResource(R.drawable.bg);
		
		gameResult = (ImageView) findViewById(R.id.game_result_interstitial);
        
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    TableLayout result_table = (TableLayout) findViewById(R.id.ready_table);
	    
	    View ready_cell = layoutInflater.inflate(R.layout.ready_info_cell, result_table, false);
     	result_table.addView(ready_cell);
     	
     	TextView s = (TextView) ready_cell.findViewById(R.id.names);
     	s.setTextColor(Color.parseColor("#1f426e"));
     	String userFName = User.get_fname();
     	String userLName = User.get_lname();
     	String userName = userFName+" "+userLName;
     	String offset1 = "";
     	if (userName.length() <= 52){
     		for (int i = 0; i < 52-userName.length(); i++) {
    			offset1 += " "; 
    		}
     	}
     	userName = "    "+userName+offset1;
     	String oppoFName = "";
     	String oppoLName = "";
     	if (Gameplay.getOppoLName().equals("guest")){
     		oppoFName = "Guest";
	     	oppoLName = Gameplay.getOppoFName();
     	}else{
     		oppoFName = Gameplay.getOppoFName();
	     	oppoLName = Gameplay.getOppoLName();
     	}
     	String oppoName = "    "+oppoFName+" "+oppoLName;
     	s.setText(userName+oppoName);
     	
//     	new DownloadImageTask((ImageView) ready_cell.findViewById(R.id.user_image)).execute("http://screenslam.foxfilm.com/image/title_thumbnail_default.jpg");
     	new DownloadImageTask((ImageView) ready_cell.findViewById(R.id.user_image_ready)).execute(User.get_thumbnail());
     	
     	TextView TV1 = (TextView) ready_cell.findViewById(R.id.user_point_ready);
     	TV1.setTextColor(Color.parseColor("#ffffff"));
     	TV1.setText("This Gmae :\n"+ Gameplay.userScoreThisGame);
     	
     	new DownloadImageTask((ImageView) ready_cell.findViewById(R.id.oppo_image_ready)).execute(Gameplay.getOppoImageURL());
//     	new DownloadImageTask((ImageView) ready_cell.findViewById(R.id.oppo_image)).execute("https://graph.facebook.com/100002538660677/picture");
     	
     	TextView TV2 = (TextView) ready_cell.findViewById(R.id.oppo_point_ready);
     	TV2.setTextColor(Color.parseColor("#ffffff"));
     	if (Gameplay.oppoScoreThisGame == 0){
     		TV2.setText("This Game :\n  ?");
     	}else{
     		TV2.setText("This Game :"+ Gameplay.oppoScoreThisGame);
     	}
     	
        
     	TextView TV3 = (TextView) this.findViewById(R.id.round_number);
     	TV3.setTextColor(Color.parseColor("#ffffff"));
     	String s3 = Gameplay.getUserWon() +"             Round "+Gameplay.getChallRound()+"          "+ Gameplay.getOppoWon();
     	TV3.setText(s3);
     	TV3.setTextColor(Color.parseColor("#1f426e"));;
     	
     	gameResult.setVisibility(View.INVISIBLE);
     	if (Gameplay.getChallType().equals("challenge")){
     		if(Gameplay.userScoreThisGame>Gameplay.oppoScoreThisGame) {
     			gameResult.setImageResource(R.drawable.copy_youwin);
     		}else if(Gameplay.userScoreThisGame<Gameplay.oppoScoreThisGame){
     			gameResult.setImageResource(R.drawable.copy_youlost);
     		}else{
     			gameResult.setImageResource(R.drawable.copy_itsatie);
     		}
     	gameResult.setVisibility(View.VISIBLE);
     	}
     	
        thread=  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(3000);
                        startActivity(new Intent(getApplicationContext(), ResultPage.class));
                        finish();
                    }
                }
                catch(InterruptedException ex){                    
                }

                // TODO              
            }
        };
        
        thread.start();
		}catch(Exception e){
			new AdvActivityStarter(this, SplashPage.class, 0, true).start();		
		}
	}
	*/
	private Thread thread;
	private ImageView gameResult;
	
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState); 
	        setContentView(R.layout.interstitial_page);
	        AdvRDAdjuster.adjust(findViewById(R.id.interstitial_page_wrapper));
	        
			// load user & player info
			new AdvImageLoader((ImageView) findViewById(R.id.interstitial_page_user_tn)).execute(User.get_thumbnail());
			((TextView) findViewById(R.id.interstitial_page_user_partial_score_txt)).setText(Integer.toString(Gameplay.userScoreThisGame));
			((TextView) findViewById(R.id.interstitial_page_user_score_txt)).setText(User.get_score());
			((TextView) findViewById(R.id.interstitial_page_user_name_txt)).setText(User.get_lname().equals("Guest") ? "Guest "+User.get_fname() : User.get_fname()+" "+User.get_lname().charAt(0)+".");
			
			new AdvImageLoader((ImageView) findViewById(R.id.interstitial_page_player_tn)).execute(Gameplay.getOppoImageURL());
			((TextView) findViewById(R.id.interstitial_page_player_partial_score_txt)).setText((String) (Gameplay.oppoScoreThisGame == 0 ? "?" : Integer.toString(Gameplay.oppoScoreThisGame)));
			((TextView) findViewById(R.id.interstitial_page_player_score_txt)).setText(Gameplay.getOppoScore());
			((TextView) findViewById(R.id.interstitial_page_player_name_txt)).setText(Gameplay.getOppoLName().equals("Guest") ? "Guest "+Gameplay.getOppoFName() : Gameplay.getOppoFName()+" "+Gameplay.getOppoLName().charAt(0)+".");
			
			// load round info
			((TextView) findViewById(R.id.interstitial_page_round_txt)).setText(Gameplay.getUserWon() +"     Round "+Gameplay.getChallRound()+"     "+ Gameplay.getOppoWon());
	        
			// load win/lose
			((ImageView) findViewById(R.id.interstitial_page_result_img)).setImageResource(R.drawable.copy_youwin);
			/*
	        
	       
	     	
	     	TextView TV1 = (TextView) ready_cell.findViewById(R.id.user_point_ready);
	     	TV1.setTextColor(Color.parseColor("#ffffff"));
	     	TV1.setText("This Gmae :\n"+ Gameplay.userScoreThisGame);
	     	
	     	TextView TV2 = (TextView) ready_cell.findViewById(R.id.oppo_point_ready);
	     	TV2.setTextColor(Color.parseColor("#ffffff"));
	     	if (Gameplay.oppoScoreThisGame == 0){
	     		TV2.setText("This Game :\n  ?");
	     	}else{
	     		TV2.setText("This Game :"+ Gameplay.oppoScoreThisGame);
	     	}
	     	
	        
	     	TextView TV3 = (TextView) this.findViewById(R.id.round_number);
	     	TV3.setTextColor(Color.parseColor("#ffffff"));
	     	String s3 = Gameplay.getUserWon() +"             Round "+Gameplay.getChallRound()+"          "+ Gameplay.getOppoWon();
	     	TV3.setText(s3);
	     	TV3.setTextColor(Color.parseColor("#1f426e"));;
	     	
	     	gameResult.setVisibility(View.INVISIBLE);
	     	if (Gameplay.getChallType().equals("challenge")){
	     		if(Gameplay.userScoreThisGame>Gameplay.oppoScoreThisGame) {
	     			gameResult.setImageResource(R.drawable.copy_youwin);
	     		}else if(Gameplay.userScoreThisGame<Gameplay.oppoScoreThisGame){
	     			gameResult.setImageResource(R.drawable.copy_youlost);
	     		}else{
	     			gameResult.setImageResource(R.drawable.copy_itsatie);
	     		}
	     	gameResult.setVisibility(View.VISIBLE);
	     	}
     	
	     	
	     	*/
     		// go to result page
     		//new AdvActivityStarter(this, ResultPage.class, INTERSTITIAL_PAGE_DURATION).start();
     		
		}catch(Exception e){
			new AdvActivityStarter(this, SplashPage.class, 0, true).start();
		}
	}
	
}
