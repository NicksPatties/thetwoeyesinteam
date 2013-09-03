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
import com.example.movieslam_android_dev.views.component.Player;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class GamePlayPage extends Activity {
	private SurfaceView surfaceView;
	private ImageView imageView;
	private TextView textview;
//	private Button btn1, btn2, btn3, btn4;
	private ProgressBar progressBar;
	private Player player;
	private String[] rightAnswers = new String[5];
	private String[] buttonLabels = new String[4];
	private Button[] buttons = new Button[4];
	private int timer = 0;
	private int rightAnswerPointer = 0;
	
	private Thread thread;
	private int score;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameplay_page);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		surfaceView = (SurfaceView) this.findViewById(R.id.videoplayer);
		imageView = (ImageView) this.findViewById(R.id.stillImage);
		textview = (TextView) this.findViewById(R.id.questionTV);
		progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
		buttons[0] = (Button) this.findViewById(R.id.btn1);
		buttons[1] = (Button) this.findViewById(R.id.btn2);
		buttons[2] = (Button) this.findViewById(R.id.btn3);
		buttons[3] = (Button) this.findViewById(R.id.btn4);

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
	
	private void audioPlayer(int n){
	    //set up MediaPlayer  
		if (n == 1){
			MediaPlayer mp = MediaPlayer.create(this, R.raw.correct);
		    mp.start();
		}else{
			MediaPlayer mp = MediaPlayer.create(this, R.raw.incorrect);
		    mp.start();
		}
	}
	
	//init the array of 20 anwsers
	public void initAnswers(){
		for (int i=0; i<5; i++){
			rightAnswers[i] = TempModel._answers[i][0];
		}
		
		String[] wrongAnswers = new String[3];
		wrongAnswers[0] = TempModel._answers[TempModel.index][1];
		wrongAnswers[1] = TempModel._answers[TempModel.index][2];
		wrongAnswers[2] = TempModel._answers[TempModel.index][3];
		
		rightAnswerPointer = (int) (Math.random()*4);
		System.out.println("-----------right answer pointer: "+ rightAnswerPointer);
		buttonLabels[rightAnswerPointer] = rightAnswers[TempModel.index];
		int k=0;
		for (int i=0; i<4; i++){
			if (i == rightAnswerPointer){
				
			}else{
				buttonLabels[i] = wrongAnswers[k++];
			}
		}
	}
	
	//init the array of 5 questions
	public void initQuestions(){
		String questionText = TempModel.getQuestion();
		int i = 0;
		if (questionText == "name"){
			i = 1;
		}else if (questionText == "actor"){
			i = 2;
		}else{
			i = 3;
		}
		switch (i) {
			case 1:  
				questionText = "NAME THIS FILM";
				break;
			case 2:
				questionText = "NAME AN ACTOR FROM THIS FILM";
				break;
			default: 
				questionText = "NAME AN ACTOR FROM THIS SCENE";
				break;
		}
		textview.setText(questionText);
		textview.setGravity(Gravity.CENTER);
	}
	
	//init button with listeners
	public void initButtons(){
		
		buttons[0].setOnClickListener(new ClickEvent());
		buttons[1].setOnClickListener(new ClickEvent());
		buttons[2].setOnClickListener(new ClickEvent());
		buttons[3].setOnClickListener(new ClickEvent());
	}
	
	//init the text of buttons
		public void initLabels(){
			buttons[0].setText(buttonLabels[0]);
			buttons[1].setText(buttonLabels[1]);
			buttons[2].setText(buttonLabels[2]);
			buttons[3].setText(buttonLabels[3]);
		}
	
	//init the processing bar
	public void initProcessingBar(){
		String s = TempModel.getURL();
		if (parseAsIPadURL(s) == ""){
			thread = new Thread(timerThread);
			thread.start();
		}
//		progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
//		progressBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
	}
	
	private Runnable timerThread = new Runnable(){

		@Override
		public void run() {
		   // TODO Auto-generated method stub
		   while (timer<100){
			    try{
				    myHandle.sendMessage(myHandle.obtainMessage());
				    Thread.sleep(800);
			    }
			    	catch(Throwable t){
			    }
			}
		 }

		 Handler myHandle = new Handler(){

		@Override
			   public void handleMessage(Message msg) {
				timer = timer + 10;
				progressBar.setProgress(timer);
			   }
			 };
		 };
	
	//init the video player
	public void initVideoPlayer(){
		String s = TempModel.getURL();
		if (parseAsIPadURL(s) != ""){
			surfaceView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.INVISIBLE);
			player = new Player(surfaceView, progressBar, this);
		}else{
			surfaceView.setVisibility(View.INVISIBLE);
			imageView.setVisibility(View.VISIBLE);
			try {
				  Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(s).getContent());
				  imageView.setImageBitmap(bitmap);
				  imageView.setVisibility(View.VISIBLE);
				} catch (MalformedURLException e) 
				{
				  e.printStackTrace();
				} catch (IOException e) 
				{
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
			} catch (MalformedURLException e) 
			{
			  e.printStackTrace();
			} catch (IOException e) 
			{
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
			if (findIndex(arg0) == rightAnswerPointer)
			{
				audioPlayer(1);//correct answer sound
				if(player!=null){
					int position = player.mediaPlayer.getCurrentPosition();
					int duration = player.mediaPlayer.getDuration();
					System.out.println("--------------The position you got for this question: "+ position);
					System.out.println("--------------The duration you got for this question: "+ duration);
					System.out.println("--------------The SCORE you got for this question: "+ (100-90*position/duration));
					score = 100-90*position/duration;
				}else{
					score = 100-90*timer/100;
				}
				System.out.println("--------------The SCORE you got for this question: "+ score);
			}else{
				audioPlayer(0);//incorrect answer sound
				score = 10;
			}
			
			if(player!=null){
				player.setPause();
			}else{
				thread.stop();
			}
			
			
			nextMedia();
		}
		
		private int findIndex(View arg0){
			for (int i = 0; i < buttons.length; i++) {
				  if (arg0 == buttons[i]) {
				    return i;
				  }
				}
			return 0;
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
