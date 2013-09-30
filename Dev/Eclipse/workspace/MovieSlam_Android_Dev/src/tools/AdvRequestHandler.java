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


public class AdvRequestHandler extends AsyncTask<Object, Object, Object> {
	
	public AdvResponseDelegate delegate = null;
	private String _url;
	private String _xmlData;
	private ProgressDialog progress;
	
    public AdvRequestHandler(Context cxt, String url) {
		this(cxt, url, true);
	}
    
    public AdvRequestHandler(Context cxt, String url, Boolean show_loader) {
		delegate = (AdvResponseDelegate) cxt;
		_url = url;
		_xmlData = null;
	}


	protected Void doInBackground(Object... params) {
    	HttpClient hc = new DefaultHttpClient();
    	HttpPost hp = new HttpPost(_url);

    	try {
    		HttpResponse hr = hc.execute(hp);
    		HttpEntity he = hr.getEntity();
    		_xmlData = EntityUtils.toString(he);
    	} catch (ClientProtocolException e) {
    		Log.e("xml request handler", "Client Error");
    		return null;
    	} catch (IOException e) {
    		Log.e("xml request handler", "I/O Error.");
    		return null;
    	} catch (NullPointerException e){
    		Log.e("xml request handler", "No Internet Access.");
    		return null;
		} catch (Exception e){
			Log.e("xml request handler", "Error.");
    		return null;
		}
    			
    	return null;
    }
	
	@Override
    protected void onPreExecute() { 
		progress = new ProgressDialog((Context) delegate);
		progress.setMessage("Loading...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show(); 
    }

    @Override
    protected void onPostExecute(Object obj) {
    	Log.d("debug", "finish request: "+_url);
    	delegate.responseLoaded(_xmlData);
    	if (progress != null){
    		progress.dismiss();
    		progress = null;
    	}
    }

 }