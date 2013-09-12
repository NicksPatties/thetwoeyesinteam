package tools;

import models.Config;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AdvButtonListener implements OnClickListener, Config {
	private Bundle _bundle;
	private Context _delegate;
	
	public AdvButtonListener(Bundle bundle, Context delegate) {
		_bundle = bundle;
		set_delegate(delegate);
	}

	@Override
	public void onClick(View v) {
	}

	

	public Context get_delegate() {
		return _delegate;
	}

	public void set_delegate(Context _delegate) {
		this._delegate = _delegate;
	}

	public Bundle get_bundle() {
		return _bundle;
	}

	public void set_bundle(Bundle _bundle) {
		this._bundle = _bundle;
	}

	
}
