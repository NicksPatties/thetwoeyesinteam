package views;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import models.Config;
import models.Gameplay;
import models.User;
import tools.AdvButtonListener;
import tools.AdvElement;
import tools.AdvRequestHandler;
import tools.AdvResponseDelegate;
import tools.DownloadImageTask;
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
import android.widget.Toast;

import com.example.movieslam_android_dev.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class SplashPage extends FragmentActivity implements AdvResponseDelegate, Config {
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
	}
	
	@Override
	protected void onResumeFragments() {
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        Gameplay.set_fbConnected(-1);
        
//        startActivity(new Intent(getApplicationContext(), ResultPage.class));
        
//        /**
        
        // init promo image
//        ImageView bg_preloader = (ImageView) findViewById(R.id.bg_preloader);
//        new DownloadImageTask(bg_preloader).execute(BASE_URL+"/include/images/screenslam_loading_promo.jpg");
//        Timer t = new Timer(false);
//        t.schedule(new TimerTask() {
//        @Override
//        public void run() {
//             runOnUiThread(new Runnable() {
//                  public void run() {
//                	  bg_preloader.setVisibility(View.GONE);
//                  }
//              });
//          }
//      }, (int)(PROMO_IMG_DURATION*1000));
        
     
        
        
        // hardcode to user id 3
        SharedPreferences user_info = this.getSharedPreferences("user_info", MODE_PRIVATE);
		/*
        Editor user_info_edit = user_info.edit();
		user_info_edit.clear();
		//user_info_edit.putString("uid", "2171");
		user_info_edit.commit();*/
        
		// start Facebook Login
    	if (user_info.contains("fbConnected") && user_info.getString("fbConnected", "").equals("1")){
    		Log.d("Debug", "facbeook login detected");
    		Session.openActiveSession(this, true, new Session.StatusCallback() {
    			
    		    // callback when session changes state
    		    @Override
    		    public void call(Session session, SessionState state, Exception exception) {
    		    }
    		});
         }
        
		// add main board content
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout user_panel = (LinearLayout) findViewById(R.id.user_panel);
		View user_main_board = layoutInflater.inflate(R.layout.user_main_board, user_panel, false);
		user_panel.addView(user_main_board);		
		
		
		LoginButton loginButton = (LoginButton) findViewById(R.id.loginButton);
		loginButton.setBackgroundResource(R.drawable.button_bg_small);
		
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
            	try {
            	Session session = Session.getActiveSession();

        	    if (session != null && session.isOpened()) {
        	    	if (Gameplay.get_fbConnected() != 1){
        	    		setfbConnected(1);
        	    		
        	    		// set User data by facebook
            	    	User.set_fid(user.getId());
            	    	User.set_fname(user.getFirstName());
            	    	User.set_lname(user.getLastName());
            	    	User.set_thumbnail("");
            	    	// get user info
            	    	callGameInfoByFID();
        	    	}        	    	
        	    } else {
        	    	if (Gameplay.get_fbConnected() != 0){
        	    		
        	    		// set user to non facebook connect in local decvice
        	    		setfbConnected(0);
        	    		
        	    		// clear session if user log out
        	    		if (session != null){
        	    			Session.getActiveSession().closeAndClearTokenInformation();
        	    		}
        	    		// get user info
        	    		callGameInfoByUID();
        	    	}        	    	
        	    	//session.closeAndClearTokenInformation();
        	    }
        	    
            	} catch (NullPointerException e){
            		showConnectionError();
    			}
            }
            
	        
        });
		
        
        
     
        
	}
	
	public void showConnectionError(){
		Toast toast = Toast.makeText(this, "No internet access.", Toast.LENGTH_LONG);
		toast.show();
	}
	
	public void setfbConnected(int i) {
		Gameplay.set_fbConnected(i);
		
		SharedPreferences user_info = this.getSharedPreferences("user_info", MODE_PRIVATE);
		Editor user_info_edit = user_info.edit();
		user_info_edit.putString("fbConnected", Integer.toString(i));
		user_info_edit.commit();
		Log.d("debug", "facebook login set to "+i);
	}
	
	public void callGameInfoByFID(){
		new AdvRequestHandler(this, BASE_URL+"/service/getGameInfo.php?user_id=0&fid="+User.get_fid()+"&fname="+User.get_fname()+"&lname="+User.get_lname()+"&thumbnail=http://graph.facebook.com/"+User.get_fid()+"/picture?type=large", true).execute();
	}
	
	public void callGameInfoByUID(){
		
		String uid = getUIDFromDevice();
        if (uid != null){
        	Log.d("debug", "call game info 1");
        	new AdvRequestHandler(this, BASE_URL+"/service/getGameInfo.php?user_id="+uid+"&fid=0", true).execute();
        }else{
        	
        	new AdvRequestHandler(this, BASE_URL+"/service/getGameInfo.php?user_id=0&fid=0&fname=guest&lname=Guest&thumbnail="+BASE_URL+"/include/images/avatar.png", true).execute();
        	Log.d("debug", "call game info 2 "+ BASE_URL+"/service/getGameInfo.php?user_id=0&fid=0&fname=guest&lname=Guest&thumbnail="+BASE_URL+"/include/images/avatar.png");
        }
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
		new AdvRequestHandler(this, BASE_URL+"/service/getGameInfo.php?user_id="+uid+"&fid=0").execute();
		
		if (Gameplay.get_fbConnected() == 0){
			callGameInfoByUID();
		}else if (Gameplay.get_fbConnected() == 1){
			callGameInfoByFID();
		}
		
	}
	
	//private ArrayList<View> challenge_cell_array = new ArrayList<View>();
	@Override
	public void responseLoaded(String response) {
		
		if (response == null){
			showConnectionError();
			return;
		}
		
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
		if ((getUIDFromDevice() == null || !this.getUIDFromDevice().equals(uid)) && Gameplay.get_fbConnected() == 0){
			SharedPreferences user_info = this.getSharedPreferences("user_info", MODE_PRIVATE);
			Editor user_info_edit = user_info.edit();
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
			//challenge_cell_array.add(player_challenge_cell);
			
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
			
			if (gameplay_status.equals("wait")){
				
				// waiting for opponent status
				b0.setText("RESULT");
				b0.setEnabled(false);
				b0.setBackgroundResource(R.drawable.button_small_disabled);
				b1.setText("CONTINUE");
				
				b1.setEnabled(false);
				b1.setBackgroundResource(R.drawable.button_small_disabled);
				Gameplay.setChallType("challenge");
				
				player_score_txt.setText("wait");
				player_score_txt.setTextSize(12);
			}else if (gameplay_status.equals("start")){
				
				b0.setText("RESULT");
				Bundle result_bd = new Bundle();
				result_bd.putString("game_id", game_id);
				OnClickListener b0_ltn = new AdvButtonListener(result_bd, this) {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(), RoundHistory.class);						
						intent.putExtras(get_bundle());
						startActivity(intent);
					}
				};
				b0.setOnClickListener(b0_ltn);
				
				b1.setText("CONTINUE");
				Bundle continue_bd = new Bundle();
				continue_bd.putInt("cell_idx", i);
				continue_bd.putString("target_source_type", "cid");
				continue_bd.putString("target_id", challenge_id);
				continue_bd.putString("target_genre", genre_type);
				OnClickListener b1_ltn = new AdvButtonListener(continue_bd, this) {
					@Override
					public void onClick(View v) {
						//int itemIndex = challenge_cell_array.indexOf(player_challenge_cell);
						int cell_idx = this.get_bundle().getInt("cell_idx");
						AdvElement gameplay_e = gameplays_e.getElement("gameplay", cell_idx);
						
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
						Gameplay.setChallType("challenge");
						Gameplay.setChallOppoID(gameplay_e.getValue("player_user_id"));
						Gameplay.setChallOppoFID(gameplay_e.getValue("player_user_fid"));
						Intent intent = new Intent(getApplicationContext(), ReadyToPlayPage.class);						
						intent.putExtras(get_bundle());
						startActivity(intent);
					}
				};
				b1.setOnClickListener(b1_ltn);
			}else if (gameplay_status.equals("accept")){
				// set decline button
				b0.setText("DECLINE");
				Bundle decline_bd = new Bundle();
				decline_bd.putString("game_id", game_id);
				OnClickListener b0_ltn = new AdvButtonListener(decline_bd, this) {
					@Override
					public void onClick(View v) {
						new AdvRequestHandler(get_delegate(), BASE_URL+"/service/getGameInfo.php?user_id="+User.get_uid()+"&remove_game_id="+get_bundle().getString("game_id")).execute();
					}
				};
				b0.setOnClickListener(b0_ltn);
				
				// set accept button
				b1.setText("ACCEPT");
				Bundle accept_bd = new Bundle();
				accept_bd.putInt("cell_idx", i);
				accept_bd.putString("target_source_type", "cid");
				accept_bd.putString("target_id", challenge_id);
				accept_bd.putString("target_genre", genre_type);
				OnClickListener b1_ltn = new AdvButtonListener(accept_bd, this) {
					@Override
					public void onClick(View v) {	
						//int itemIndex = challenge_cell_array.indexOf(player_challenge_cell);
						int cell_idx = this.get_bundle().getInt("cell_idx");
						AdvElement gameplay_e = gameplays_e.getElement("gameplay", cell_idx);
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
						Gameplay.setChallType("self");
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
				result_bd.putString("user_tn_url", user_e.getValue("user_thumbnail"));
				result_bd.putString("player_tn_url", gameplay_e.getValue("player_user_thumbnail"));
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
                String s = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("debug", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
        	Log.d("debug", "NameNotFoundException");
        } catch (NoSuchAlgorithmException e) {
        	Log.d("debug", "NoSuchAlgorithmException");
        }

    }
	
}
