package tools;

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

public class HttpPoster extends AsyncTask<Object, Object, Object> {
	
	private String _url;
	
    public HttpPoster(Context cxt, String url) {
    	this(cxt, url, false);
	}
    
    public HttpPoster(Context cxt, String url, Boolean show_loader) {
    	//we don't need to show loader...
		_url = url;
	}
    
	protected Void doInBackground(Object... params) {

    	HttpClient hc = new DefaultHttpClient();
    	HttpPost hp = new HttpPost(_url);

    	try {
    		HttpResponse hr = hc.execute(hp);
    		HttpEntity he = hr.getEntity();
    	} catch (ClientProtocolException e) {
    		Log.d("error", "ClientProtocolException");
    		return null;
    	} catch (IOException e) {
    		Log.d("error", "IOException");
    		return null;
    	}    			
    			
    	return null;
    }
 }