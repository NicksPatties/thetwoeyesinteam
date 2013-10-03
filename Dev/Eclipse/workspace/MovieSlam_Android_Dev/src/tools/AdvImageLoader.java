package tools;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class AdvImageLoader extends AsyncTask<String, Void, Bitmap> {
	  ImageView _img_v;
	  Boolean _user_cache; 
	  AdvImageLoaderListener _listener;
	
	  public AdvImageLoader(ImageView img_v) {
		  this(img_v, true);
	  }
	  
	  public AdvImageLoader(ImageView img_v, Boolean use_cache) {
		  _img_v = img_v;
		  _user_cache =  use_cache;
	  }
	  
	  abstract static public class AdvImageLoaderListener {
		  abstract public void imageLoaderDidFinishLoading();
	  }
	   
	  public AdvImageLoader(ImageView img_v, Boolean use_cache, AdvImageLoaderListener listener) {
		  this(img_v, use_cache);
		  _listener = listener;		  
	  }

	  protected Bitmap doInBackground(String... urls) {
		try {
			String url = urls[0];
			Bitmap bm = _user_cache ? AdvImageManagement.getBitmapByURL(url) : null;
			if (bm == null){
				InputStream in = new java.net.URL(url).openStream();
				// bm = BitmapFactory.decodeStream(in);
				Log.e("URL lookup", url.toString());
				BitmapFactory.Options bpo =  new BitmapFactory.Options();
			//	bpo.inSampleSize = calculateInSampleSize(bpo, _img_v.getWidth(), _img_v.getHeight() );
				bpo.inSampleSize = 1;
				Log.e("sample size", String.valueOf(bpo.inSampleSize) );
				Log.e("Width", String.valueOf(_img_v.getWidth())  );
				Log.e("Height", String.valueOf(_img_v.getHeight()) );
			//	bpo.inJustDecodeBounds = true;
			//	bpo.				
			//	BitmapFactory.decodeStream(in, outPadding, opts)
			//	BitmapFactory.decodeFile(rawImageName, options);

				bm = BitmapFactory.decodeStream(in, null, bpo );
				
				if (_user_cache){
					AdvImageManagement.SetBitmap(url, bm);
				}				
				in.close();
			}
			
			return bm;
			
		} catch (Exception e) {
			Log.e("AdvImageLoader", "BITMAP ERROR");
			return null;
		}
	  }

	  protected void onPostExecute(Bitmap bm) {
		  _img_v.setImageBitmap(bm);

		  if (_listener != null) {
			  _listener.imageLoaderDidFinishLoading();
		  }
	  }
	  
	  public int calculateInSampleSize (BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	    


	    return inSampleSize;
	}
	  
}
