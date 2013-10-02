package views.component;


import java.util.Timer;
import java.util.TimerTask;

import views.GamePlayPage;
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

public class MoviePlayer implements OnBufferingUpdateListener, OnCompletionListener, MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {
	private static final int SHOW_PROGRESS = 1;
	private int videoWidth;
	private int videoHeight;
	public MediaPlayer mediaPlayer;
	private SurfaceHolder surfaceHolder;
	private ProgressBar progressBar;
	private Timer mTimer=new Timer();
	private String _url;
	private GamePlayPage _gp;
	private float _elapse;
	
	public MoviePlayer(SurfaceView surfaceView,ProgressBar progressBar, String url, GamePlayPage gamePlayPage)
	{
		_gp = gamePlayPage;
		_url = url;
		_elapse = 0;
		
		this.progressBar=progressBar;
		surfaceHolder=surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		//surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		//mTimer.schedule(mTimerTask, 0, 1000);
	}
	
	/*******************************************************
	 * processing bar
	 ******************************************************/
	
	protected Handler mHandler = new Handler()
	{
		
	    @Override
	    public void handleMessage(Message msg)
	    {
	    	Log.e("error", "set progress bar1");
	        switch (msg.what){

	            case SHOW_PROGRESS:
	            	long pos = progressBar.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
            		progressBar.setProgress((int) pos);
	            	
	            	if (pos > progressBar.getMax()*0.95){
	            		progressBar.setProgress((int) progressBar.getMax());
	            		_gp.showLegal();
						mHandler.removeMessages(SHOW_PROGRESS);
	            	}
	                break;
	        }
	    }
	};
	
		
	
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
			mediaPlayer.setDataSource(_url);
			mediaPlayer.prepareAsync();
			//mediaPlayer.prepare();
			
		} catch (Exception e) {
			//Log.e("mediaPlayer", "error", e);
		}
		Log.e("mediaPlayer", "surface created");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		//Log.e("mediaPlayer", "surface destroyed");
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
	}
	
	@Override
	public void onCompletion(MediaPlayer arg0) {
		
	}
	

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		mHandler.sendEmptyMessage(SHOW_PROGRESS);
		progressBar.setSecondaryProgress(bufferingProgress);
		//int currentProgress = progressBar.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
		//Log.e(currentProgress+"% play", bufferingProgress + "% buffer");
		//Log.e("mediaPlayer", "buffering");
	}
	
	public void free(){
		// free memory
		if (mediaPlayer != null) {
			mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;       	
        }
		surfaceHolder = null;
		progressBar = null;
		mTimer = null;
		_url = null;
		_gp = null;
	}
	
	public float getElapse(){
		if (mediaPlayer.isPlaying()){
			return (float)mediaPlayer.getCurrentPosition()/1000;			
		}
		return _elapse;
	}
	
	public float getDuration(){
		return (float)mediaPlayer.getDuration()/1000;
	}
	
}

