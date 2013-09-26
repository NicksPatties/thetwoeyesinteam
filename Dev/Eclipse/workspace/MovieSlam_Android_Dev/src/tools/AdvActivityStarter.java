package tools;

import views.SplashPage;
import models.Config;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

@SuppressWarnings("rawtypes")
public class AdvActivityStarter extends Thread implements Config{
	
	Context _delegate;
	Class _class;
	float _duration;
	boolean _crashed;
	
	
	public AdvActivityStarter(Context delegate, Class page_class, float duration){
		this(delegate, page_class, duration, false);
	}
	
	public AdvActivityStarter(Context delegate, Class page_class, float duration, boolean crashed){
		_delegate = delegate;
		_class = page_class;
		_duration = duration;
		_crashed = crashed;
		if (crashed){
			// show error prompt
			Toast toast = Toast.makeText(_delegate, "Some error occured.", Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	@Override
    public void run(){
        try {
            synchronized(this){
            	if (!_crashed){
            		if (_duration > 0){
            			wait((int)(1000*_duration));
            		}	                
	                _delegate.startActivity(new Intent(_delegate.getApplicationContext(), _class));
            	}else{
            		// clear stack and go to home page
        			Intent intent = new Intent(_delegate.getApplicationContext(), _class);
        			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        			_delegate.startActivity(intent);
            	}
            }
        }
        catch(InterruptedException ex){                    
        }
    }
}
