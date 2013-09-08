package com.example.movieslam_android_dev.views.component;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.example.movieslam_android_dev.models.TempModel;
import com.example.movieslam_android_dev.views.GamePlayPage;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

public class MoviePlayer implements OnBufferingUpdateListener,
		OnCompletionListener, MediaPlayer.OnPreparedListener,
		SurfaceHolder.Callback {
	private int videoWidth;
	private int videoHeight;
	public MediaPlayer mediaPlayer;
	private SurfaceHolder surfaceHolder;
	private ProgressBar progressBar;
	private Timer mTimer=new Timer();
	private String _url;
	private GamePlayPage _gp;
	
	public MoviePlayer(SurfaceView surfaceView,ProgressBar progressBar, GamePlayPage gamePlayPage)
	{
		_gp = gamePlayPage;
		this.progressBar=progressBar;
		surfaceHolder=surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mTimer.schedule(mTimerTask, 0, 1000);
	}
	
	/*******************************************************
	 * processing bar
	 ******************************************************/
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if(mediaPlayer==null)
				return;
			if (mediaPlayer.isPlaying() && progressBar.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};
	
	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {

			int position = mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();
			
			if (duration > 0) {
				long pos = progressBar.getMax() * position / duration;
				progressBar.setProgress((int) pos);
			}
		};
	};
	//*****************************************************
	
	public void play()
	{
		mediaPlayer.start();
	}
	
//	public void setURL(String url){
//		_url = TempModel.getMediaURLs();
//	}
	
	public void playUrl(String videoUrl)
	{
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.prepare();//prepare and then play
			//mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPause()
	{
		mediaPlayer.pause();
	}
	
	public void stop()
	{
		if (mediaPlayer != null) { 
			mediaPlayer.stop();
            mediaPlayer.release(); 
            mediaPlayer = null; 
        } 
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		Log.e("mediaPlayer", "surface changed");
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDisplay(surfaceHolder);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setDataSource(TempModel.getMediaURLs());
			mediaPlayer.prepare();
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}
		Log.e("mediaPlayer", "surface created");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.e("mediaPlayer", "surface destroyed");
	}

	
	@Override
	/**
	 * via onPrepared to play
	 */
	public void onPrepared(MediaPlayer arg0) {
		videoWidth = mediaPlayer.getVideoWidth();
		videoHeight = mediaPlayer.getVideoHeight();
		if (videoHeight != 0 && videoWidth != 0) {
			arg0.start();
		}
		Log.e("mediaPlayer", "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		_gp.showLegal();
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		progressBar.setSecondaryProgress(bufferingProgress);
		int currentProgress = progressBar.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
		Log.e(currentProgress+"% play", bufferingProgress + "% buffer");
		
	}

}

