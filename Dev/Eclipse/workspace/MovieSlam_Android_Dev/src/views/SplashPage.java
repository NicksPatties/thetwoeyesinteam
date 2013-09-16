package views;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import models.Config;
import models.Gameplay;
import models.User;
import tools.AdvButtonListener;
import tools.AdvElement;
import tools.DownloadImageTask;
import tools.ResponseDelegate;
import tools.XmlRequestHandler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

import com.example.movieslam_android_dev.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class SplashPage extends FragmentActivity implements ResponseDelegate, Config {
//  public class SplashPage extends Activity{ // used for testing game play page quickly
	
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = 
	    new Session.StatusCallback() {
	    @Override
	    public void call(Session session, 
	            SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.d("debug","open");
        } else if (state.isClosed()) {
        	Log.d("debug","closed");
        }else{
        	Log.d("debug","error open");
        }
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
//        startActivity(new Intent(getApplicationContext(), ResultPage.class));
        
//        /**
        
        Session session = Session.getActiveSession();
        if (session != null ) {
        	if (session.isOpened() || session.isClosed()) {
        		/*onSessionStateChange(session, session.getState(), null);*/
        		Log.d("debug","session open");
        	}else{
        		Log.d("debug","session closed");
        		session.closeAndClearTokenInformation();
        	}
            
        }
        
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
      }, (int)(PROMO_IMG_DURATION*1000));
        
        
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
		LoginButton loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
            	Log.d("debug", "start");
                //Log.d("debug", user.getFirstName()+" "+user.getLastName());
                //Log.d("debug", user.getId());
                // It's possible that we were waiting for this.user to be populated in order to post a
                // status update.
               // handlePendingAction();
            }
        });
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
	
	private ArrayList<View> challenge_cell_array = new ArrayList<View>();
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
		}		
		
		TextView userScore_txt = (TextView)findViewById(R.id.userScore_txt); 
		userScore_txt.setText(user_e.getValue("user_score"));
		
		new DownloadImageTask((ImageView) findViewById(R.id.userThumbnail_iv)).execute(user_e.getValue("user_thumbnail"));
		
		// empty challenge board
		TableLayout score_table = (TableLayout) findViewById(R.id.score_table);
		score_table.removeAllViews();
		
		// parse player challenges board
		final AdvElement gameplays_e = doc.getElement("gameplays");
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		for (int i = 0; i < gameplays_e.getElementLength("gameplay"); i++){
			
			// add player challenge cell			
			final View player_challenge_cell = layoutInflater.inflate(R.layout.player_challenge_cell, score_table, false);
			score_table.addView(player_challenge_cell);
			challenge_cell_array.add(player_challenge_cell);
			
			AdvElement gameplay_e = gameplays_e.getElement("gameplay", i);
			
			TextView player_name_txt = (TextView) player_challenge_cell.findViewById(R.id.player_name_txt);
			player_name_txt.setText(gameplay_e.getValue("player_user_fname")+"\n"+gameplay_e.getValue("player_user_lname"));
			
			TextView player_score_txt = (TextView) player_challenge_cell.findViewById(R.id.player_score_txt);
			player_score_txt.setText(gameplay_e.getValue("gameplay_user_won")+":"+gameplay_e.getValue("gameplay_player_won"));
			
			new DownloadImageTask((ImageView) player_challenge_cell.findViewById(R.id.challenge_player_tn)).execute(gameplay_e.getValue("player_user_thumbnail"));
			
			// Game history	
			//let's use bundle later
			Bundle game_history_bd = new Bundle();
			game_history_bd.putString("player_id", gameplay_e.getValue("player_user_id"));
			game_history_bd.putString("user_tn_url", user_e.getValue("user_thumbnail"));
			game_history_bd.putString("player_tn_url", gameplay_e.getValue("player_user_thumbnail"));
			OnClickListener challenge_row_ltn = new AdvButtonListener(game_history_bd, this) {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), GameHistory.class);
					intent.putExtras(get_bundle());
					startActivity(intent);
				}
			};
			player_challenge_cell.setOnClickListener(challenge_row_ltn);
			
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
				Bundle decline_bd = new Bundle();
				decline_bd.putString("game_id", game_id);
				OnClickListener b0_ltn = new AdvButtonListener(decline_bd, this) {
					@Override
					public void onClick(View v) {
						new XmlRequestHandler(get_delegate(), BASE_URL+"/service/getGameInfo.php?user_id="+User.get_uid()+"&remove_game_id="+get_bundle().getString("game_id")).execute();
					}
				};
				b0.setOnClickListener(b0_ltn);
				
				// set accept button
				b1.setText("ACCEPT");
				Bundle accept_bd = new Bundle();
				accept_bd.putString("target_source_type", "cid");
				accept_bd.putString("target_id", challenge_id);
				accept_bd.putString("target_genre", genre_type);
				OnClickListener b1_ltn = new AdvButtonListener(accept_bd, this) {
					@Override
					public void onClick(View v) {	
						int itemIndex = challenge_cell_array.indexOf(player_challenge_cell);
						AdvElement gameplay_e = gameplays_e.getElement("gameplay", itemIndex);
						Gameplay.setChallID(gameplay_e.getValue("challenge_id"));
						Gameplay.setGameID(gameplay_e.getValue("gameplay_game_id"));
						Gameplay.setChallOppoScore(gameplay_e.getValue("player_user_score"));
						Gameplay.setChallOppoImageURL(gameplay_e.getValue("player_user_thumbnail"));
						Gameplay.setChallRound(gameplay_e.getValue("gameplay_round"));
						Gameplay.setUserWon(gameplay_e.getValue("gameplay_user_won"));
						Gameplay.setOppoWon(gameplay_e.getValue("gameplay_player_won"));
						Gameplay.setOppoFName(gameplay_e.getValue("player_user_fname"));
						Gameplay.setOppoLName(gameplay_e.getValue("player_user_lname"));
						Gameplay.setGenre(gameplay_e.getValue("challenge_genre_type"));
						Gameplay.setChallStatus(gameplay_e.getValue("gameplay_status"));
						if (Gameplay.getChallStatus().equals("accept")){
							Gameplay.setChallType("challenge");
						}else{
							Gameplay.setChallType("self");
						}
						Gameplay.setChallOppoID(gameplay_e.getValue("player_user_id"));
						Gameplay.setChallOppoFID(gameplay_e.getValue("player_user_fid"));
						Intent intent = new Intent(getApplicationContext(), ReadyToPlayPage.class);						
						intent.putExtras(get_bundle());
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
				Bundle result_bd = new Bundle();
				result_bd.putString("game_id", game_id);
				//result_bd.putString("user_tn_url", user_e.getValue("user_thumbnail"));
				//result_bd.putString("player_tn_url", gameplay_e.getValue("player_user_thumbnail"));
				OnClickListener b0_ltn = new AdvButtonListener(result_bd, this) {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(), RoundHistory.class);						
						intent.putExtras(get_bundle());
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
