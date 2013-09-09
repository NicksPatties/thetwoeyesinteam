package com.example.movieslam_android_dev.tools;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class XmlRequestHandler extends AsyncTask<Object, Object, Object> {
	
	public ResponseDelegate delegate = null;
	private String _url;
	private String _xmlData;
	private ProgressDialog progress;
	
    public XmlRequestHandler(Context cxt, String url) {
    	progress = ProgressDialog.show(cxt, "Loading", "Please wait...", true);
		delegate = (ResponseDelegate) cxt;
		_url = url;
	}


	protected Void doInBackground(Object... params) {

    	HttpClient hc = new DefaultHttpClient();
    	HttpPost hp = new HttpPost(_url);

    	try {
    		HttpResponse hr = hc.execute(hp);
    		HttpEntity he = hr.getEntity();
    		_xmlData = EntityUtils.toString(he);
    	} catch (ClientProtocolException e) {
    		Log.d("error", "ClientProtocolException");
    		return null;
    	} catch (IOException e) {
    		Log.d("error", "IOException");
    		return null;
    	}    			
    			
    	return null;
    }

    @Override
    protected void onPostExecute(Object obj) {
    	delegate.responseLoaded(_xmlData);
    	progress.dismiss();  	
    }

 }