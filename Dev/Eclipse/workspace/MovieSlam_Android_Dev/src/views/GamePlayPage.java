package views;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import tools.DownloadImageTask;
import views.component.MoviePlayer;
import models.Gameplay;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.R.id;
import com.example.movieslam_android_dev.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
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
	private ProgressBar progressBar;
	private MoviePlayer moviePlayer;
	private String[] rightAnswers = new String[5];
	private String[] buttonLabels = new String[4];
	private Button[] buttons = new Button[4];
	private ImageView[] crosses = new ImageView[4];
	private ImageView[] starses = new ImageView[4];
	private TextView[] score_text = new TextView[4];
	private AnimationDrawable[] starsAnimation = new AnimationDrawable[4];
	private int timer = 0;
	private int rightAnswerPointer = 0;
	
	private Thread thread;
	private Thread movieThread;
	private int score;
	private float elapse;

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
		crosses[0] = (ImageView) this.findViewById(R.id.cross1);
		crosses[1] = (ImageView) this.findViewById(R.id.cross2);
		crosses[2] = (ImageView) this.findViewById(R.id.cross3);
		crosses[3] = (ImageView) this.findViewById(R.id.cross4);
		starses[0] = (ImageView) this.findViewById(R.id.stars1);
		starses[1] = (ImageView) this.findViewById(R.id.stars2);
		starses[2] = (ImageView) this.findViewById(R.id.stars3);
		starses[3] = (ImageView) this.findViewById(R.id.stars4);
		starsAnimation[0] = (AnimationDrawable) starses[0].getDrawable();
		starsAnimation[1] = (AnimationDrawable) starses[1].getDrawable();
		starsAnimation[2] = (AnimationDrawable) starses[2].getDrawable();
		starsAnimation[3] = (AnimationDrawable) starses[3].getDrawable();
		score_text[0] = (TextView) this.findViewById(R.id.score_text1);
		score_text[1] = (TextView) this.findViewById(R.id.score_text2);
		score_text[2] = (TextView) this.findViewById(R.id.score_text3);
		score_text[3] = (TextView) this.findViewById(R.id.score_text4);

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
			rightAnswers[i] = Gameplay._answers[i][0];
		}
		
		String[] wrongAnswers = new String[3];
		wrongAnswers[0] = Gameplay._answers[Gameplay.index][1];
		wrongAnswers[1] = Gameplay._answers[Gameplay.index][2];
		wrongAnswers[2] = Gameplay._answers[Gameplay.index][3];
		
		rightAnswerPointer = (int) (Math.random()*4);
		System.out.println("-----------right answer pointer: "+ rightAnswerPointer);
		buttonLabels[rightAnswerPointer] = rightAnswers[Gameplay.index];
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
		String questionText = Gameplay.getQuestion();
		int i = 0;
		if (questionText.equals("name")){
			i = 1;
		}else if (questionText.equals("actor")){
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
		String s = Gameplay.getMediaURLs();
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
		String s = Gameplay.getMediaURLs();
		if (parseAsIPadURL(s) != ""){
			surfaceView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.INVISIBLE);
			moviePlayer = new MoviePlayer(surfaceView, progressBar, this);
		}else{
			surfaceView.setVisibility(View.INVISIBLE);
			imageView.setVisibility(View.VISIBLE);
			new DownloadImageTask(imageView).execute(s);
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
		new DownloadImageTask(imageView).execute("http://screenslam.foxfilm.com/legal/legal_default.png");
	}
	
	private void nextMedia(){
		Gameplay.index = Gameplay.index+1;
		movieThread=  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(3000);
                        if (Gameplay.index >= 5){
                			startActivity(new Intent(getApplicationContext(), InterstitialPage.class));
                			finish();
                		}else{
                			startActivity(new Intent(getApplicationContext(), GamePlayPage.class));
                			finish();
                		}
                    }
                }
                catch(InterruptedException ex){                    
                }
            }
        };
        
        movieThread.start();
//		if (Gameplay.index >= 5){
//			startActivity(new Intent(getApplicationContext(), InterstitialPage.class));
//		}else{
//			movieThread=  new Thread(){
//	            @Override
//	            public void run(){
//	                try {
//	                    synchronized(this){
//	                        wait(3000);
//	                        startActivity(new Intent(getApplicationContext(), GamePlayPage.class));
//	                    }
//	                }
//	                catch(InterruptedException ex){                    
//	                }
//	            }
//	        };
//	        movieThread.start();
////			startActivity(new Intent(getApplicationContext(), GamePlayPage.class));
//		}
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
				starses[rightAnswerPointer].setVisibility(View.VISIBLE);
				starsAnimation[rightAnswerPointer].setOneShot(true);
				starsAnimation[rightAnswerPointer].start();
				buttons[rightAnswerPointer].setBackgroundResource(R.drawable.button_orange);
				audioPlayer(1);//correct answer sound
				if(moviePlayer!=null){
					int position = moviePlayer.mediaPlayer.getCurrentPosition();
					int duration = moviePlayer.mediaPlayer.getDuration();
					System.out.println("--------------The position you got for this question: "+ position);
					System.out.println("--------------The duration you got for this question: "+ duration);
					System.out.println("--------------The SCORE you got for this question: "+ (100-90*position/duration));
					score = 100-90*position/duration;
					if (score >= 100){
						score = 10;
					}
					elapse = (duration - duration*(score-10)/90)/1000;
					Gameplay.setElapses(elapse);
					score_text[rightAnswerPointer].setText(Integer.toString(score));
				}else{
					score = 100-90*timer/100;
					elapse = 8 - 8*(score-10)/90;
					Gameplay.setElapses(elapse);
					score_text[rightAnswerPointer].setText(Integer.toString(score));
				}
				System.out.println("--------------The SCORE you got for this question: "+ score);
			}else{
				buttons[rightAnswerPointer].setBackgroundResource(R.drawable.button_orange);
				crosses[findIndex(arg0)].setVisibility(View.VISIBLE);
				audioPlayer(0);//incorrect answer sound
				score = 10;
			}
			
			if(moviePlayer!=null){
				moviePlayer.setPause();
			}else{
				thread = null;
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

}
