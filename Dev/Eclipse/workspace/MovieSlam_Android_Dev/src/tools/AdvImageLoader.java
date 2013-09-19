package tools;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class AdvImageLoader extends AsyncTask<String, Void, Bitmap> {
	ImageView _img_v;

	  public AdvImageLoader(ImageView img_v) {
		  _img_v = img_v;
	  }

	  protected Bitmap doInBackground(String... urls) {
		try {
			String url = urls[0];
			Bitmap bm = AdvImageManagement.getBitmapByURL(url);
			if (bm == null){
				InputStream in = new java.net.URL(url).openStream();
				bm = BitmapFactory.decodeStream(in);
				AdvImageManagement.SetBitmap(url, bm);
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
