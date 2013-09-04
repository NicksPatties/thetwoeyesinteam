package com.example.movieslam_android_dev.views;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

class ImgRequestHandler extends AsyncTask<Object, Object, Object> {
	
	public ImgRequestDelegate delegate = null;
	private int _id;
	private String _url;
	private Bitmap _bitmap;

    protected Void doInBackground(Object... params) {

    	try {
            URL url = new URL(_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            _bitmap = BitmapFactory.decodeStream(connection.getInputStream());

        } catch (IOException e) {
            Log.d("debug","Exception");
            return null;
        }
    			
    	return null;
    }

    @Override
    protected void onPostExecute(Object obj) {
    	delegate.imgLoaded(_bitmap, _id);
    	
    }
    
    protected void init(String url, int id){
    	_url = url;
    	_id = id;
    }

 }