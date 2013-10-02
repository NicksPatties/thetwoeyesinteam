package views;

import java.io.Serializable;
import java.util.Arrays;

import models.Config;
import models.Gameplay;
import models.Media;
import models.Round;
import models.User;
import tools.AdvActivityStarter;
import tools.AdvImageLoader;
import tools.AdvRDAdjuster;
import tools.DownloadImageTask;
import views.component.MoviePlayer;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movieslam_android_dev.R;

public class GamePlayPage extends Activity implements Config{
	private Round round;
	private Media media;
	private MoviePlayer moviePlayer;
	private int correct_idx;
	private boolean isChosen;
	private Button[] buttons;
	
	/*
	private SurfaceView surfaceView;
	private ImageView imageView;
	private ImageView questionImage;
	private ProgressBar progressBar;
	private MoviePlayer moviePlayer;
	private String[] rightAnswers = new String[5];
	private String[] buttonLabels = new String[4];
	private Button[] buttons = new Button[4];
	//private ImageView[] crosses = new ImageView[4];
	//private ImageView[] starses = new ImageView[4];
	private TextView[] score_text = new TextView[4];
	private AnimationDrawable[] starsAnimation = new AnimationDrawable[4];
	private int timer = 0;
	private int rightAnswerPointer = 0;
	
	private Thread thread;
	private Thread movieThread;
	private int score;
	private float elapse;
	private boolean endGame = false;
	private String winnerID = "0";
	*/

	/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameplay_page);
		round = (Round) getIntent().getSerializableExtra("round_info");
		//AdvRDAdjuster.adjust(findViewById(R.id.ready_to_play_wrapper));
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		surfaceView = (SurfaceView) this.findViewById(R.id.videoplayer);
		imageView = (ImageView) this.findViewById(R.id.stillImage);
		questionImage = (ImageView) this.findViewById(R.id.questionIV);
		progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
		buttons[0] = (Button) this.findViewById(R.id.btn1);
		buttons[1] = (Button) this.findViewById(R.id.btn2);
		buttons[2] = (Button) this.findViewById(R.id.btn3);
		buttons[3] = (Button) this.findViewById(R.id.btn4);
		
//		crosses[0] = (ImageView) this.findViewById(R.id.cross1);
//		crosses[1] = (ImageView) this.findViewById(R.id.cross2);
//		crosses[2] = (ImageView) this.findViewById(R.id.cross3);
//		crosses[3] = (ImageView) this.findViewById(R.id.cross4);
//		starses[0] = (ImageView) this.findViewById(R.id.stars1);
//		starses[1] = (ImageView) this.findViewById(R.id.stars2);
//		starses[2] = (ImageView) this.findViewById(R.id.stars3);
//		starses[3] = (ImageView) this.findViewById(R.id.stars4);
//		
//		starsAnimation[0] = (AnimationDrawable) starses[0].getDrawable();
//		starsAnimation[1] = (AnimationDrawable) starses[1].getDrawable();
//		starsAnimation[2] = (AnimationDrawable) starses[2].getDrawable();
//		starsAnimation[3] = (AnimationDrawable) starses[3].getDrawable();
		
		
		score_text[0] = (TextView) this.findViewById(R.id.score_text1);
		score_text[1] = (TextView) this.findViewById(R.id.score_text2);
		score_text[2] = (TextView) this.findViewById(R.id.score_text3);
		score_text[3] = (TextView) this.findViewById(R.id.score_text4);

		initAssets();
		//startActivity(new Intent(getApplicationContext(), ResultPage.class));
		
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
		//for now, we only have 3 kinds of question, we should add two more when we need
		questionImage.setVisibility(View.INVISIBLE);
     	if(questionText.equals("name")) {
     		questionImage.setImageResource(R.drawable.copy_namethisfilm);
     	}else if(questionText.equals("actor")){
     		questionImage.setImageResource(R.drawable.copy_nametheactorinthisfilm);
     	}else{
     		questionImage.setImageResource(R.drawable.copy_nametheactorinthisscene);
     	}
     	questionImage.setVisibility(View.VISIBLE);
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
			//moviePlayer = new MoviePlayer(surfaceView, progressBar, this);
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
		surfaceView.setVisibility(View.INVISIBLE);
		imageView = (ImageView) this.findViewById(R.id.legalImage);
		new DownloadImageTask(imageView).execute("http://screenslam.foxfilm.com/legal/legal_default.png");
	}
	
	private void nextMedia(){
		Gameplay.index = Gameplay.index+1;
		
		//tried to release memory, but seems failed
//		if(moviePlayer != null){
//			moviePlayer.stop();
//			moviePlayer.surfaceDestroyed(surfaceView.getHolder());
//		}
		
		if (Gameplay.index >= 5){
			finalPost();
		}
		
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
        
	}
	
	private void finalPost(){
		String userWon = Gameplay.getUserWon();
    	String oppoWon = Gameplay.getOppoWon();
    	
        if (Gameplay.getChallType().equals("challenge"))
        {
        	
        	if(Integer.parseInt(userWon) >= 5 && Integer.parseInt(oppoWon) >=5){
        		endGame = true;
        	}
        }
        if(Gameplay.getChallType().equals("self")||endGame){
        	Gameplay.show_next_round = false;
        }else{
        	Gameplay.show_next_round = true;
        }
        
        if (Gameplay.getChallType().equals("challenge")){
        	if(Gameplay.userScoreThisGame>Gameplay.oppoScoreThisGame){
        		Gameplay.setUserWon(Integer.toString((Integer.parseInt(oppoWon)+1)));
        		winnerID = User.get_uid();
        	}else if(Gameplay.userScoreThisGame<Gameplay.oppoScoreThisGame){
        		Gameplay.setOppoWon(Integer.toString((Integer.parseInt(oppoWon)+1)));
        		winnerID = Gameplay.getChallOppoID();
        	}else{
        		winnerID = "-1";
        	}
        }else{
        	winnerID = "0";
        }
        
        int finalUserScore = Integer.parseInt(User.get_score()) + Gameplay.userScoreThisGame;
        String s1 = removeBraces(Arrays.toString(Gameplay.getMediaIDs()));
        String s2 = removeBraces(Arrays.toString(Gameplay.getQuestions()));
        String s3 = removeBraces(Arrays.toString(Gameplay.getElapses()));
        
        String finalPost = "";
        finalPost = Config.BASE_URL + "/service/saveChallenge.php?"
        	+"user_id="+User.get_uid()
        	+"&player_id="+Gameplay.getChallOppoID()
        	+"&user_score="+User.get_score()
        	+"&challenge_type="+Gameplay.getChallType()
        	+"&challenge_id="+Gameplay.getChallID()
        	+"&genre_type="+Gameplay.getGenre()
        	+"&media_id_array="+s1
        	+"&choice_var_array="+s2
        	+"&duel_elapsed_array="+s3
        	+"&score="+Integer.toString(finalUserScore)
        	+"&game_id="+Gameplay.getGameID()
        	+"&winner_id="+winnerID
        	+"&endGame="+Boolean.toString(endGame);
        	
        System.out.println(finalPost);
        
//        new HttpPoster(this, finalPost).execute();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {
	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
	     return true;
	     }
	     return super.onKeyDown(keyCode, event);    
	}
	
	private String removeBraces(String value){
		String s = value.replace("[", "");
		s = s.replace("]", "");
		s = s.replace(" ", "");
		return s;
	}
	
	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			if (findIndex(arg0) == rightAnswerPointer)
			{
//				starses[rightAnswerPointer].setVisibility(View.VISIBLE);
//				starsAnimation[rightAnswerPointer].setOneShot(true);
//				starsAnimation[rightAnswerPointer].start();
				buttons[rightAnswerPointer].setBackgroundResource(R.drawable.button_orange);
				//audioPlayer(1);//correct answer sound
				if(moviePlayer!=null){
					Log.d("debug", "movie");
					Gameplay.setElapses(4);
					
//					int position = moviePlayer.mediaPlayer.getCurrentPosition();
//					int duration = moviePlayer.mediaPlayer.getDuration();
//					
//					System.out.println("--------------The position you got for this question: "+ position);
//					System.out.println("--------------The duration you got for this question: "+ duration);
//					System.out.println("--------------The SCORE you got for this question: "+ (100-90*position/duration));
//					score = 100-90*position/duration;
//					if (score >= 100){
//						score = 10;
//					}
//					elapse = (duration - duration*(score-10)/90)/1000;
//					Gameplay.setElapses(elapse);
//					score_text[rightAnswerPointer].setText(Integer.toString(score));
					
					
					
				}else{
					
					Log.d("debug", "img");
					Gameplay.setElapses(4);
					
//					score = 100-90*timer/100;
//					elapse = 8 - 8*(score-10)/90;
//					Gameplay.setElapses(elapse);
//					score_text[rightAnswerPointer].setText(Integer.toString(score));
					
				}
				Gameplay.userScoreThisGame = Gameplay.userScoreThisGame + score;
				System.out.println("--------------The SCORE you got for this question: "+ score);
			}else{
				buttons[rightAnswerPointer].setBackgroundResource(R.drawable.button_orange);
				//crosses[findIndex(arg0)].setVisibility(View.VISIBLE);
				//audioPlayer(0);//incorrect answer sound
				score = 10;
				Gameplay.userScoreThisGame = Gameplay.userScoreThisGame + score;
				
			}
			
			if(moviePlayer!=null){
				//moviePlayer.setPause();
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
	}*/
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameplay_page);		
		round = (Round) getIntent().getSerializableExtra("round_info");
		AdvRDAdjuster.adjust(findViewById(R.id.gameplay_page_wrapper));	
		
		media = round.medias[round.media_idx];
		initAssets();		
	}
	

	public void initAssets(){
		initMediaPlayer();
		initQA();	
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
	
	public void initQA(){
		
		isChosen =  false;	
		
		buttons = new Button[NUMBER_OF_CHOICE];
		buttons[0] = (Button) this.findViewById(R.id.btn1);
		buttons[1] = (Button) this.findViewById(R.id.btn2);
		buttons[2] = (Button) this.findViewById(R.id.btn3);
		buttons[3] = (Button) this.findViewById(R.id.btn4);			
		
		ImageView question_img = (ImageView) this.findViewById(R.id.questionIV);
		correct_idx = (int) (Math.random()*4);
		
		if(media.question.equals("name")) {
			question_img.setImageResource(R.drawable.copy_namethisfilm);
     	}else if (media.question.equals("actor")){
     		question_img.setImageResource(R.drawable.copy_nametheactorinthisfilm);
     	}else{
     		question_img.setImageResource(R.drawable.copy_nametheactorinthisscene);
     	}
		
		String correct_value = media.choices[0];
		media.choices[0] = media.choices[correct_idx];
		media.choices[correct_idx] = correct_value;
			
		buttons[0].setText(media.choices[0]);
		buttons[1].setText(media.choices[1]);
		buttons[2].setText(media.choices[2]);
		buttons[3].setText(media.choices[3]);		
	}
	
	
	
	public void initMediaPlayer(){
				
		// init legal
		new AdvImageLoader((ImageView) this.findViewById(R.id.legalImage), false).execute(media.legal);
		((ImageView) this.findViewById(R.id.legalImage)).setVisibility(View.INVISIBLE);
		
		//init the media
		String media_url = media.url;
		if (media.type.equals("video")){	
			((SurfaceView) this.findViewById(R.id.videoplayer)).setVisibility(View.VISIBLE);
			((ImageView) this.findViewById(R.id.stillImage)).setVisibility(View.INVISIBLE);
			moviePlayer = new MoviePlayer(((SurfaceView) this.findViewById(R.id.videoplayer)), ((ProgressBar) this.findViewById(R.id.progressBar)), media_url, this);
					
		}else if (media.type.equals("image")){
			//initProcessingBar();
			((SurfaceView) this.findViewById(R.id.videoplayer)).setVisibility(View.INVISIBLE);
			((ImageView) this.findViewById(R.id.stillImage)).setVisibility(View.VISIBLE);
			new AdvImageLoader(((ImageView) this.findViewById(R.id.stillImage)), false).execute(media_url);
		}
	}
	
	public void showLegal(){
		((ImageView) this.findViewById(R.id.legalImage)).setVisibility(View.VISIBLE);					
	}		
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {
	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
	     return true;
	     }
	     return super.onKeyDown(keyCode, event);    
	}
	
	public void onClick(View view) {
		
		// disable all button
		for (int i=0; i<NUMBER_OF_CHOICE; i++){
			buttons[i].setEnabled(false);
		}
				
		// check correctness
		if (!isChosen){
			isChosen = true;
			
			// correct answer response
			if (view.getTag().equals(Integer.toString(correct_idx+1))){
				audioPlayer(1);
				if (media.type.equals("video")){
					
					media.user_correct = true;
					media.user_elapse = moviePlayer.getElapse();
					media.user_score = (int) (100 - 90 * moviePlayer.getElapse() / moviePlayer.getDuration());
					
					moviePlayer.free(); 
					
				}else if (media.type.equals("image")){
					media.user_correct = true;
					media.user_elapse = 3.5F;
					media.user_score = 10;
				}			
				moviePlayer = null;
				buttons = null;
				
			// wrong answer response
			}else{
				
				audioPlayer(0);
				media.user_correct = false;
				media.user_elapse = 0;
				media.user_score = 0;
				
				ImageView[] crosses = new ImageView[NUMBER_OF_CHOICE];
				crosses[0] = (ImageView) this.findViewById(R.id.cross1);
				crosses[1] = (ImageView) this.findViewById(R.id.cross2);
				crosses[2] = (ImageView) this.findViewById(R.id.cross3);
				crosses[3] = (ImageView) this.findViewById(R.id.cross4);
				
				crosses[Integer.parseInt((String) view.getTag())-1].setVisibility(View.VISIBLE);
				buttons[correct_idx].setBackgroundResource(R.drawable.button_orange);
				
				if (media.type.equals("video")){				
					moviePlayer.free();					
				}
				moviePlayer = null;
				buttons = null;
			}
			
			// go to next page
			if (round.media_idx == 4){
				new AdvActivityStarter(this, InterstitialPage.class, GAMEPLAY_DELAY, round).start();
			}else{
				round.media_idx++;
				new AdvActivityStarter(this, GamePlayPage.class, GAMEPLAY_DELAY, round).start();
				/*runOnUiThread(new Runnable() {
					
						@Override
				        public void run() {
				        	round.media_idx++;
				        	
							media = round.medias[round.media_idx];
							
							initQA();
							initMediaPlayer();
														
				        }
				        
				});*/
				
			}
		}	
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//moviePlayer.free();
		Log.e("onDestroy", "onDestroy");
		super.onDestroy();
	}
	
}
