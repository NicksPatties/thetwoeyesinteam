package views;

import models.Gameplay;
import models.User;
import tools.AdvButtonListener;
import tools.AdvElement;
import tools.AdvImageLoader;
import tools.AdvRDAdjuster;
import tools.DownloadImageTask;
import tools.AdvRequestHandler;

import com.example.movieslam_android_dev.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

public class ResultPage extends Activity {
	/*
	private Button btn_nextround;
	private Button btn_home;
	private Button btn_FB;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.result_page);
        
        btn_nextround = (Button) findViewById(R.id.btn_next_round);
        if(Gameplay.show_next_round){
        	btn_nextround.setVisibility(View.VISIBLE);
        }else{
        	btn_nextround.setVisibility(View.INVISIBLE);
        }
        btn_home = (Button) findViewById(R.id.btn_home_result);
        btn_FB = (Button) findViewById(R.id.btn_FB_result);
        
     	LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     	TableLayout result_table = (TableLayout) findViewById(R.id.result_table);
     	
//     	/**
     	for (int i = 0; i < 5; i++){
	     	View result_cell = layoutInflater.inflate(R.layout.result_cell, result_table, false);
	     	result_table.addView(result_cell);
	     	
	     	new DownloadImageTask((ImageView) result_cell.findViewById(R.id.media_TN)).execute(Gameplay.getMediaTN(i));
//	     	new DownloadImageTask((ImageView) result_cell.findViewById(R.id.media_TN)).execute("http://screenslam.foxfilm.com/image/title_thumbnail_default.jpg");
	     	
	     	TextView mediaName = (TextView) result_cell.findViewById(R.id.media_name);
	     	mediaName.setText(Gameplay.getMediaNames(i));
//	     	mediaName.setText("Flight ClubFlight");
	     	
//	     	TextView buynowText =(TextView) result_cell.findViewById(R.id.buy_now);
//	     	buynowText.setClickable(true);
//	     	buynowText.setMovementMethod(LinkMovementMethod.getInstance());
////	     	String text = "BUY NOW";
//	     	String text = "<a href="+Gameplay.getMediaEtailers(i)+">BUY NOW</a>";
//	     	buynowText.setText(Html.fromHtml(text));
//	     	buynowText.setTextColor(Color.parseColor("#FFFFFF"));
	     	
	     	Button buy_txt = (Button) result_cell.findViewById(R.id.buy_now);
			buy_txt.setTag(Gameplay.getMediaEtailers(i));
			OnClickListener buy_txt_ltn = new AdvButtonListener(null, this) {
				@Override
				public void onClick(View v) {
					Log.d("debug", "something on clicjk");
					Intent i = new Intent(Intent.ACTION_VIEW);
					String url = (String) v.getTag();
					i.setData(Uri.parse(url));
					startActivity(i);
				}
			};
			buy_txt.setOnClickListener(buy_txt_ltn);
	     	
	     	new DownloadImageTask((ImageView) result_cell.findViewById(R.id.user_image_result)).execute(User.get_thumbnail());
//	     	new DownloadImageTask((ImageView) result_cell.findViewById(R.id.user_image_result)).execute("https://graph.facebook.com/1447010240/picture");
	     	
	     	new DownloadImageTask((ImageView) result_cell.findViewById(R.id.oppo_image_result)).execute(Gameplay.getOppoImageURL());
//	     	new DownloadImageTask((ImageView) result_cell.findViewById(R.id.oppo_image_result)).execute("https://graph.facebook.com/100002538660677/picture");
//	     	new DownloadImageTask((ImageView) result_cell.findViewById(R.id.oppo_image_result)).execute("http://screenslam.foxfilm.com/include/images/avatar.png");
	     	
	     	TextView userTime = (TextView) result_cell.findViewById(R.id.user_time);
	     	String s1 = Float.toString(Gameplay.getElapse(i));
	     	if (Gameplay.getElapse(i) <= 0){
	     		s1 = "WRONG";
	     	}
	     	userTime.setText(s1);
	     	
	     	TextView oppoTime = (TextView) result_cell.findViewById(R.id.oppo_time);
	     	String s2 = Float.toString(Gameplay.getOppoElapse(i));
	     	if (s2.isEmpty())
	     	{
	     		oppoTime.setText("WRONG");
	     		oppoTime.setTextColor(Color.RED);
	     	}
	     	if(s2.equals("0.0")){
	     		s2 = "";
	     	}
	     	oppoTime.setText(s2);
//	     	oppoTime.setText("WRONG");
 
     	
     	}
     	
	}
	
	public void goHome(View v) {
		startActivity(new Intent(getApplicationContext(), SplashPage.class));
		finish();
	}
	
	public void goFacebook(View v) {
		//go to Facebook
	}
	
	public void onNextRound(View v) {
		startActivity(new Intent(getApplicationContext(), GenreSelection.class));
		finish();
	}
	
	*/
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.result_page);
        
        ((Button) findViewById(R.id.btn_next_round)).setVisibility(Gameplay.show_next_round ? View.VISIBLE : View.INVISIBLE);        
     	LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     	TableLayout result_table = (TableLayout) findViewById(R.id.result_table);
     	
     	for (int i = 0; i < 5; i++){
	     	View result_cell = layoutInflater.inflate(R.layout.result_cell, result_table, false);
	     	result_table.addView(result_cell);
	     	
	     	// load movie thumbnail and name
	     	new AdvImageLoader((ImageView) result_cell.findViewById(R.id.movie_tn)).execute(Gameplay.getMediaTN(i));
	     	TextView movie_txt = (TextView) result_cell.findViewById(R.id.movie_txt);
	     	movie_txt.setText(Gameplay.getMediaNames(i));
	     	
	     	// buy button
	     	Button buy_txt = (Button) result_cell.findViewById(R.id.buy_txt);
			buy_txt.setTag(Gameplay.getMediaEtailers(i));
			OnClickListener buy_txt_ltn = new AdvButtonListener(null, this) {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					String url = (String) v.getTag();
					i.setData(Uri.parse(url));
					startActivity(i);
				}
			};
			buy_txt.setOnClickListener(buy_txt_ltn);
	     	
			// load thumbnails
	     	new AdvImageLoader((ImageView) result_cell.findViewById(R.id.round_user_tn)).execute(User.get_thumbnail());	     	
	     	new AdvImageLoader((ImageView) result_cell.findViewById(R.id.round_player_tn)).execute(Gameplay.getOppoImageURL());
	     	
	     	// user time
	     	TextView round_user_txt = (TextView) result_cell.findViewById(R.id.round_user_txt);	     	
	     	if (Gameplay.getElapse(i) <= 0){
	     		round_user_txt.setText("WRONG");
	     		round_user_txt.setTextColor(Color.RED);
	     	}else{
	     		round_user_txt.setText(Float.toString(Gameplay.getElapse(i)));
	     	}	     	
	     	
	     	// player time
	     	TextView round_player_txt = (TextView) result_cell.findViewById(R.id.round_player_txt);     	
	     	String s2 = Float.toString(Gameplay.getOppoElapse(i));
	     	if (s2.isEmpty())
	     	{
	     		round_player_txt.setText("WRONG");
	     		round_player_txt.setTextColor(Color.RED);
	     	}
	     	if(s2.equals("0.0")){
	     		s2 = "";
	     	}
	     	round_player_txt.setText(s2);
     	
     	}
     	AdvRDAdjuster.adjust(findViewById(R.id.result_page_wrapper));
	}
	
	public void goHome(View v) {
		// clear stack and go to home page
		Intent intent = new Intent(this, SplashPage.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	public void goFacebook(View v) {
		//go to Facebook
	}
	
	public void onNextRound(View v) {
		// go to genre page
		startActivity(new Intent(getApplicationContext(), GenreSelection.class));
		finish();
	}
	
	
}
