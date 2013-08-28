package com.example.movieslam_android_dev.views;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

class xmlRequestHandler extends AsyncTask<Object, Object, Object> {
	
	public ResponseDelegate delegate = null;
	private String _url;
	private String _xmlData;

    protected Void doInBackground(Object... params) {

    	HttpClient hc = new DefaultHttpClient();
    	HttpPost hp = new HttpPost(_url);

    	try {
    		HttpResponse hr = hc.execute(hp);
    		HttpEntity he = hr.getEntity();
    		_xmlData = EntityUtils.toString(he);
    	} catch (ClientProtocolException e) {
    		Log.d("error", "ClientProtocolException");
    	} catch (IOException e) {
    		Log.d("error", "IOException");
    	}    			
    			
    	return null;
    }

    @Override
    protected void onPostExecute(Object obj) {
    	delegate.responseLoaded(_xmlData);
    	
    }
    
    protected void setURL(String url){
    	_url = url;
    }

 }