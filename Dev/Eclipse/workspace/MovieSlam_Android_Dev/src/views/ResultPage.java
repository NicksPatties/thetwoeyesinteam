package views;

import models.Gameplay;
import models.User;
import tools.AdvElement;
import tools.DownloadImageTask;
import tools.XmlRequestHandler;

import com.example.movieslam_android_dev.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

public class ResultPage extends Activity {
	
	private Button btn_nextround;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.result_page);
        
        btn_nextround = (Button) findViewById(R.id.btn_next_round);
        
     	LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     	TableLayout result_table = (TableLayout) findViewById(R.id.result_table);
     	
     	for (int i = 0; i < 5; i++){
	     	View result_cell = layoutInflater.inflate(R.layout.result_cell, result_table, false);
	     	result_table.addView(result_cell);
	     	
	     	new DownloadImageTask((ImageView) result_cell.findViewById(R.id.media_TN)).execute(Gameplay.getMediaTN(i));
	     	
	     	TextView mediaName = (TextView) result_cell.findViewById(R.id.media_name);
	     	mediaName.setText(Gameplay.getMediaNames(i));
	     	
	     	TextView buynowText =(TextView) result_cell.findViewById(R.id.buy_now);
	     	buynowText.setClickable(true);
	     	buynowText.setMovementMethod(LinkMovementMethod.getInstance());
	     	String text = "<a href="+Gameplay.getMediaEtailers(i)+">BUY NOW</a>";
	     	buynowText.setText(Html.fromHtml(text));
	     	buynowText.setTextColor(Color.parseColor("#FFFFFF"));
	     	
	     	new DownloadImageTask((ImageView) result_cell.findViewById(R.id.player_image_result)).execute(User.get_thumbnail());
	     	
	     	new DownloadImageTask((ImageView) result_cell.findViewById(R.id.oppo_image_result)).execute(Gameplay.getOppoImageURL());
	     	
	     	TextView userTime = (TextView) result_cell.findViewById(R.id.user_time);
	     	String s1 = Float.toString(Gameplay.getElapses(i));
	     	if (Gameplay.getElapses(i) <= 0){
	     		s1 = "WRONG";
	     	}
	     	userTime.setText(s1);
	     	
	     	TextView oppoTime = (TextView) result_cell.findViewById(R.id.oppo_time);
	     	String s2 = Float.toString(Gameplay.getOppoElapses(i));
	     	if (s2.isEmpty())
	     	{
	     		oppoTime.setText("WRONG");
	     		oppoTime.setTextColor(Color.RED);
	     	}
//	     	oppoTime.setText(Gameplay.getOppoMediaTime(i));
     	}
	}
	
	public void onClick(View v) {
		startActivity(new Intent(getApplicationContext(), GenreSelection.class));
		//go to genre selection page
	}
	
}
