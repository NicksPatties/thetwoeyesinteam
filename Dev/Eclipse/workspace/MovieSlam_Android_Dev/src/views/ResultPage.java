package views;

import models.TempModel;
import tools.AdvElement;
import tools.DownloadImageTask;

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
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

public class ResultPage extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.result_page);
        
     	LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     	TableLayout result_table = (TableLayout) findViewById(R.id.result_table);
     	
     	for (int i = 0; i < 5; i++){
	     	View result_cell = layoutInflater.inflate(R.layout.result_cell, result_table, false);
	     	result_table.addView(result_cell);
	     	
	     	new DownloadImageTask((ImageView) result_cell.findViewById(R.id.media_TN)).execute(TempModel.getMediaTN(i));
	     	
	     	TextView mediaName = (TextView) result_cell.findViewById(R.id.media_name);
	     	mediaName.setText(TempModel.getMediaNames(i));
	     	
	     	TextView buynowText =(TextView) findViewById(R.id.buy_now);
	     	buynowText.setClickable(true);
	     	buynowText.setMovementMethod(LinkMovementMethod.getInstance());
	     	String text = "<a href="+TempModel.getMediaEtailers(i)+">BUY NOW</a>";
	     	buynowText.setText(Html.fromHtml(text));
	     	buynowText.setTextColor(Color.parseColor("#FFFFFF"));
     	}
	}
}
