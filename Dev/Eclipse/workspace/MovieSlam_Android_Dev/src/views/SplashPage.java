package views;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tools.AdvElement;
import tools.ChallengeBoardButtonListener;
import tools.DownloadImageTask;
import tools.ResponseDelegate;
import tools.XmlRequestHandler;
import models.Config;
import models.Gameplay;
import models.User;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieslam_android_dev.R;
import com.facebook.FacebookException;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.LoginButton;
import com.facebook.widget.PickerFragment;

public class SplashPage extends FragmentActivity implements ResponseDelegate, Config {
//  public class SplashPage extends Activity{ // used for testing game play page quickly

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);
        
        // init promo image
        final ImageView bg_preloader = (ImageView) findViewById(R.id.bg_preloader);
        new DownloadImageTask(bg_preloader).execute(BASE_URL+"/include/images/screenslam_loading_promo.jpg");
        Timer t = new Timer(false);
        t.schedule(new TimerTask() {
        @Override
        public void run() {
             runOnUiThread(new Runnable() {
                  public void run() {
                	  bg_preloader.setVisibility(View.GONE);
                  }
              });
          }
      }, 5000);
        
        
        // hardcode to user id 3
        SharedPreferences user_info = this.getSharedPreferences("user_info", MODE_PRIVATE);
		Editor user_info_edit = user_info.edit();
		user_info_edit.clear();
		user_info_edit.putString("uid", "3");
		user_info_edit.commit();
		
		
        // User info init (check for fb connect first!!!!!!!!!!!!)
        String uid = getUIDFromDevice();
        if (uid != null){
        	new XmlRequestHandler(this, BASE_URL+"/service/getGameInfo.php?user_id="+uid+"&fid=0", false).execute();
        }else{
        	new XmlRequestHandler(this, BASE_URL+"/service/getGameInfo.php?user_id=0&fid=0&fname=guest&lname=Guest&thumbnail="+BASE_URL+"/include/images/avatar.png", false).execute();
        }
        
		// add main board content
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout user_panel = (LinearLayout) findViewById(R.id.user_panel);
		View user_main_board = layoutInflater.inflate(R.layout.user_main_board, user_panel, false);
		user_panel.addView(user_main_board);		
		
		// FB connected
		//LoginButton loginButton = (LoginButton) user_main_board.findViewById(R.id.loginButton);
		
        
	}
	
	private String getUIDFromDevice() {
		
		SharedPreferences user_info = this.getSharedPreferences("user_info", MODE_PRIVATE);
		if (user_info.contains("uid") && !user_info.getString("uid", "").equals("")){
			return user_info.getString("uid", "");
		}else{
			return null;
		}
		
	}
	
	public void connectToFacebook(View view){
	}

	public void gotoHelp(View view){		
		startActivity(new Intent(getApplicationContext(), HelpInfo.class));
	}
	
	public void gotoNewChallenge(View view){		
		startActivity(new Intent(getApplicationContext(), UserTypeSelection.class));
//		startActivity(new Intent(getApplicationContext(), ReadyToPlayPage.class));
	}
	
	public void gotoRefresh(View view){
		String uid = getUIDFromDevice();
		new XmlRequestHandler(this, BASE_URL+"/service/getGameInfo.php?user_id="+uid+"&fid=0").execute();
	}
	
	@Override
	public void responseLoaded(String response) {
		
		// init root element
		AdvElement doc = new AdvElement(response);
		
		// init promo info
		AdvElement promo_e = doc.getElement("promo");
		Gameplay.set_promo(Integer.parseInt(promo_e.getValue("display")));
		Gameplay.set_promo_name(promo_e.getValue("name"));

		// set User variables
		AdvElement user_e = doc.getElement("user");
		User.set_uid(user_e.getValue("user_id"));
		User.set_fname(user_e.getValue("user_fname"));
		User.set_lname(user_e.getValue("user_lname"));
		User.set_thumbnail(user_e.getValue("user_thumbnail"));
		User.set_score(user_e.getValue("user_score"));
				
		// parse user main board	
		TextView userName_txt = (TextView)findViewById(R.id.userName_txt);
		userName_txt.setText(User.get_fname() + " " + User.get_lname());
		
		TextView userID_txt = (TextView)findViewById(R.id.userID_txt);
		String uid = User.get_uid();		
		userID_txt.setText(uid);
		if (!this.getUIDFromDevice().equals(uid)){
			SharedPreferences user_info = this.getSharedPreferences("user_info", MODE_PRIVATE);
			Editor user_info_edit = user_info.edit();
			user_info_edit.clear();
			user_info_edit.putString("uid", uid);
			user_info_edit.commit();
			//Toast.makeText(this, "UID saved.", 3000).show();
		}		
		
		TextView userScore_txt = (TextView)findViewById(R.id.userScore_txt); 
		userScore_txt.setText(user_e.getValue("user_score"));
		
		new DownloadImageTask((ImageView) findViewById(R.id.userThumbnail_iv)).execute(user_e.getValue("user_thumbnail"));
		
		// empty challenge board
		TableLayout score_table = (TableLayout) findViewById(R.id.score_table);
		score_table.removeAllViews();
		
		// parse player challenges board
		AdvElement gameplays_e = doc.getElement("gameplays");		
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		for (int i = 0; i < gameplays_e.getElementLength("gameplay"); i++){
			AdvElement gameplay_e = gameplays_e.getElement("gameplay", i);
			
			View player_challenge_cell = layoutInflater.inflate(R.layout.player_challenge_cell, score_table, false);
			score_table.addView(player_challenge_cell);
			
			TextView player_name_txt = (TextView) player_challenge_cell.findViewById(R.id.player_name_txt);
			player_name_txt.setText(gameplay_e.getValue("player_user_fname")+"\n"+gameplay_e.getValue("player_user_lname"));
			
			TextView player_score_txt = (TextView) player_challenge_cell.findViewById(R.id.player_score_txt);
			player_score_txt.setText(gameplay_e.getValue("gameplay_user_won")+":"+gameplay_e.getValue("gameplay_player_won"));
			
			new DownloadImageTask((ImageView) player_challenge_cell.findViewById(R.id.challenge_player_tn)).execute(gameplay_e.getValue("player_user_thumbnail"));
			
			// check challenger type
			Button b0 = (Button) player_challenge_cell.findViewById(R.id.player_challenge_b0);			
			Button b1 = (Button) player_challenge_cell.findViewById(R.id.player_challenge_b1);
			String game_id = gameplay_e.getValue("gameplay_game_id");
			String challenge_id = gameplay_e.getValue("challenge_id");
			String genre_type = gameplay_e.getValue("challenge_genre_type");
			String gameplay_status = gameplay_e.getValue("gameplay_status");
			String gameplay_round = gameplay_e.getValue("gameplay_round");
			if (gameplay_status.equals("accept")){

				// set decline button
				b0.setText("DECLINE");
				OnClickListener b0_ltn = new ChallengeBoardButtonListener(game_id, challenge_id, genre_type, this) {
					@Override
					public void onClick(View v) {
						new XmlRequestHandler(get_delegate(), BASE_URL+"/service/getGameInfo.php?user_id="+User.get_uid()+"&remove_game_id="+get_gameplay_game_id()).execute();
					}
				};
				b0.setOnClickListener(b0_ltn);
				
				// set accept button
				b1.setText("ACCEPT");
				OnClickListener b1_ltn = new ChallengeBoardButtonListener(game_id, challenge_id, genre_type, this) {
					@Override
					public void onClick(View v) {						
						Intent intent = new Intent(getApplicationContext(), ReadyToPlayPage.class);
						Bundle b_out = new Bundle();
						b_out.putString("target_source_type", "cid");
						b_out.putString("target_id", get_challenge_id());
						b_out.putString("target_genre", get_challenge_genre_type());
						intent.putExtras(b_out);
						startActivity(intent);
					}
				};
				b1.setOnClickListener(b1_ltn);
				
			}else if (gameplay_status.equals("end") && gameplay_round.equals("1")){
				b0.setText("FORFEIT");
				b0.setEnabled(false);
				b0.setBackgroundResource(R.drawable.button_small_disabled);
				b1.setVisibility(View.INVISIBLE);
				player_score_txt.setText("-:-");
			}else{
				// set result button
				b0.setText("RESULT");
				OnClickListener b0_ltn = new ChallengeBoardButtonListener(game_id, challenge_id, genre_type, this) {
					@Override
					public void onClick(View v) {					
						Intent intent = new Intent(getApplicationContext(), RoundHistory.class);
						Bundle b_out = new Bundle();
						b_out.putString("game_id", get_gameplay_game_id());
						intent.putExtras(b_out);
						startActivity(intent);
					}
				};
				b0.setOnClickListener(b0_ltn);
				b1.setVisibility(View.INVISIBLE);
			}
			
		}					
	}
	
	// generate hash for facebook auth
	public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.movieslam_android_dev", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("debug", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
        	Log.d("debug", "NameNotFoundException");
        } catch (NoSuchAlgorithmException e) {
        	Log.d("debug", "NoSuchAlgorithmException");
        }

    }
	
}
