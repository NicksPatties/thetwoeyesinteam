package tools;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class AdvImageManagement {
	public static ArrayList<String> _urls = new ArrayList<String>();
	public static ArrayList<Bitmap> _imgs = new ArrayList<Bitmap>();
	
	public static Bitmap getBitmapByURL(String url) {
		int idx = 0;	
		
		while(idx < _urls.size()){
			if (_urls.get(idx).equals(url)){
				return _imgs.get(idx);
			}
			idx++;
		}		
		return null;
	}
	
	public static void SetBitmap(String url, Bitmap img){
		_urls.add(url);
		_imgs.add(img);
	}
	
	public static void emptyCache() {
		_imgs = new ArrayList<Bitmap>();
	}
	
}
