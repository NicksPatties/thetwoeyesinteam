package tools;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class AdvLoaderThread  extends AsyncTask<Object, Object, Object> {
	public ThreadResponseDelegate _delegate;
	protected ArrayList<String> _data;
	private ProgressDialog _progress;

	public AdvLoaderThread(Context delegate) {
		_delegate = (ThreadResponseDelegate) delegate;
		_progress = ProgressDialog.show((Context) _delegate, "Loading", "Please wait...", true);
	}

	@Override
	protected Object doInBackground(Object... arg0) {
		return null;
	}
	
	@Override
    protected void onPostExecute(Object obj) {		
		_delegate.threadResponseLoaded(_data); 
		_progress.dismiss(); 
    }

}
