package views;

import tools.DownloadImageTask;
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

public class InterstitialPage extends Activity {

	private Thread thread;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.readytoplay_page);
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.readytoplayPage);
		ll.setBackgroundResource(R.drawable.bg);
        
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
     	TV1.setText(User.get_score()+" Points");
     	
     	if (Gameplay.getOppoImageURL().equals("")){
     		new DownloadImageTask((ImageView) ready_cell.findViewById(R.id.oppo_image_ready)).execute(Gameplay.getOppoImageURL());
     	}else{
     		new DownloadImageTask((ImageView) ready_cell.findViewById(R.id.oppo_image_ready)).execute(Gameplay.getOppoImageURL());
     	}
//     	new DownloadImageTask((ImageView) ready_cell.findViewById(R.id.oppo_image)).execute("https://graph.facebook.com/100002538660677/picture");
     	
     	TextView TV2 = (TextView) ready_cell.findViewById(R.id.oppo_point_ready);
     	TV2.setTextColor(Color.parseColor("#ffffff"));
     	TV2.setText(Gameplay.getChallOppoScore()+" Points");
        
     	TextView TV3 = (TextView) this.findViewById(R.id.round_number);
     	TV3.setTextColor(Color.parseColor("#ffffff"));
     	String s3 = Gameplay.getUserWon() +"             Round "+Gameplay.getChallRound()+"          "+ Gameplay.getOppoWon();
     	TV3.setText(s3);
     	TV3.setTextColor(Color.parseColor("#1f426e"));;
     	
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
	}
}
