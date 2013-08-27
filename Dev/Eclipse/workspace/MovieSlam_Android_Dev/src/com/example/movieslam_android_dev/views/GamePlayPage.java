package com.example.movieslam_android_dev.views;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.R.id;
import com.example.movieslam_android_dev.R.layout;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
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
		
		textview = (TextView) this.findViewById(R.id.questionTV);
		textview.setText("What is the Name of the Movie?");
		textview.setGravity(Gravity.CENTER);

		btn1 = (Button) this.findViewById(R.id.btn1);
		btn1.setOnClickListener(new ClickEvent());

		btn2 = (Button) this.findViewById(R.id.btn2);
		btn2.setOnClickListener(new ClickEvent());

		btn3 = (Button) this.findViewById(R.id.btn3);
		btn3.setOnClickListener(new ClickEvent());
		
		btn4 = (Button) this.findViewById(R.id.btn4);
		btn4.setOnClickListener(new ClickEvent());

		skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
		skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		
		player = new Player(surfaceView, skbProgress, this);
		player.setURL("http://screenslam.foxfilm.com/video/big_clip2_iphone.mp4");
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
	
	private void nextMovie(){
		
	}
	
	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			player.setPause();
			nextMovie();
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
