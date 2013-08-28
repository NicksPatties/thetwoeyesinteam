package com.example.movieslam_android_dev.views;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.R.id;
import com.example.movieslam_android_dev.R.layout;
import com.example.movieslam_android_dev.models.TempModel;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class GamePlayPage extends Activity {
	private SurfaceView surfaceView;
	private ImageView imageView;
	private TextView textview;
	private Button btn1, btn2, btn3, btn4;
	private SeekBar skbProgress;
	private Player player;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameplay_page);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		surfaceView = (SurfaceView) this.findViewById(R.id.videoplayer);
		imageView = (ImageView) this.findViewById(R.id.stillImage);
		textview = (TextView) this.findViewById(R.id.questionTV);
		btn1 = (Button) this.findViewById(R.id.btn1);
		btn2 = (Button) this.findViewById(R.id.btn2);
		btn3 = (Button) this.findViewById(R.id.btn3);
		btn4 = (Button) this.findViewById(R.id.btn4);

		initAssets();
	}
	
	public void initAssets(){
		initAnswers();
		initQuestions();
		initButtons();
		initLabels();
		initProcessingBar();
		initVideoPlayer();
	}
	
	//init the array of 20 anwsers
	public void initAnswers(){
		
	}
	
	//init the array of 5 questions
	public void initQuestions(){
		textview.setText("What is the Name of the Movie?");
		textview.setGravity(Gravity.CENTER);
	}
	
	//init button with listeners
	public void initButtons(){
		btn1.setOnClickListener(new ClickEvent());
		btn2.setOnClickListener(new ClickEvent());
		btn3.setOnClickListener(new ClickEvent());
		btn4.setOnClickListener(new ClickEvent());
	}
	
	//init text on the buttons
	public void initLabels(){
		btn4.setText("asdfasdf");
	}
	
	//init the processing bar
	public void initProcessingBar(){
		skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
		skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
	}
	
	//init the video player
	public void initVideoPlayer(){
		String s = TempModel.getCurrentMediaURL();
		if (parseAsIPadURL(s) != ""){
			surfaceView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.INVISIBLE);
			player = new Player(surfaceView, skbProgress, this);
		}else{
			surfaceView.setVisibility(View.INVISIBLE);
			imageView.setVisibility(View.VISIBLE);
			try {
				  Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(s).getContent());
				  imageView.setImageBitmap(bitmap);
				  imageView.setVisibility(View.VISIBLE);
				} catch (MalformedURLException e) {
				  e.printStackTrace();
				} catch (IOException e) {
				  e.printStackTrace();
				}
		}
	}
	
	private String parseAsIPadURL(String s){
		if (s.indexOf("image")!= -1){
			return "";
		}else{
			return "ipad";
		}
		
	}
	
	public void showLegal(){
		imageView = (ImageView) this.findViewById(R.id.legalImage);
		try {
			  Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://screenslam.foxfilm.com/legal/legal_default.png").getContent());
			  imageView.setImageBitmap(bitmap);
			  imageView.setVisibility(View.VISIBLE);
			} catch (MalformedURLException e) {
			  e.printStackTrace();
			} catch (IOException e) {
			  e.printStackTrace();
			}
	}
	
	private void nextMedia(){
		TempModel.index = TempModel.index+1;
		Intent intent = new Intent();
		intent.setClass(GamePlayPage.this, GamePlayPage.class);
		GamePlayPage.this.startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {
	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
	     return true;
	     }
	     return super.onKeyDown(keyCode, event);    
	}
	
	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			if(player!=null){
				player.setPause();
			}
			nextMedia();
		}
	}

	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// (progress/seekBar.getMax())*player.mediaPlayer.getDuration()
			this.progress = progress * player.mediaPlayer.getDuration()
					/ seekBar.getMax();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// seekTo()
//			player.mediaPlayer.seekTo(progress);
		}
	}


}
