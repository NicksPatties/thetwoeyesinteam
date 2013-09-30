package tools;

import java.io.Serializable;

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
	Serializable _round;
	
	
	public AdvActivityStarter(Context delegate, Class page_class, float duration, Serializable round){
		this(delegate, page_class, duration, false, round);
	}
	
	public AdvActivityStarter(Context delegate, Class page_class, float duration, boolean crashed, Serializable round){
		_delegate = delegate;
		_class = page_class;
		_duration = duration;
		_crashed = crashed;
		_round = round;
		
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
            		Intent intent = new Intent(_delegate.getApplicationContext(), _class);
            		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
        			intent.putExtra("round_info", _round);
        			_delegate.startActivity(intent);
	                //_delegate.startActivity(new Intent(_delegate.getApplicationContext(), _class));
            	}else{
            		// clear stack and go to home page
        			Intent intent = new Intent(_delegate.getApplicationContext(), _class);
        			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
        			_delegate.startActivity(intent);
            	}
            	_delegate = null;
        		_class = null;
        		_round = null;
            }
        }
        catch(InterruptedException ex){                    
        }
    }
}
