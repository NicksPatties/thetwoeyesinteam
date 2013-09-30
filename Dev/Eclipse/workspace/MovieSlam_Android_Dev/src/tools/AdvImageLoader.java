package tools;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class AdvImageLoader extends AsyncTask<String, Void, Bitmap> {
	ImageView _img_v;
	Boolean _user_cache;

	  public AdvImageLoader(ImageView img_v) {
		  this(img_v, true);
	  }
	  
	  public AdvImageLoader(ImageView img_v, Boolean use_cache) {
		  _img_v = img_v;
		  _user_cache =  use_cache;
	  }

	  protected Bitmap doInBackground(String... urls) {
		try {
			String url = urls[0];
			Bitmap bm = _user_cache ? AdvImageManagement.getBitmapByURL(url) : null;
			if (bm == null){
				InputStream in = new java.net.URL(url).openStream();
				bm = BitmapFactory.decodeStream(in);
				if (_user_cache){
					AdvImageManagement.SetBitmap(url, bm);
				}				
				in.close();
			}
			
			return bm;
			
		} catch (Exception e) {
			Log.e("AdvImageLoader", "bitmap error");
			return null;
		}
	  }

	  protected void onPostExecute(Bitmap bm) {
		  _img_v.setImageBitmap(bm);
	  }
}
