package tools;

import models.Config;
import models.User;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import tools.ResponseDelegate;
import tools.XmlRequestHandler;

public class ChallengeBoardButtonListener implements OnClickListener, Config {
	private String _gameplay_game_id;
	private String _challenge_id;
	private String _challenge_genre_type;
	private Context _delegate;
	
	public ChallengeBoardButtonListener(String gameplay_game_id, String challenge_id, String challenge_genre_type, Context delegate) {
		set_gameplay_game_id(gameplay_game_id);
		set_challenge_id(challenge_id);
		set_challenge_genre_type(challenge_genre_type);
		set_delegate(delegate);
	}

	@Override
	public void onClick(View v) {
	}

	public String get_gameplay_game_id() {
		return _gameplay_game_id;
	}

	public void set_gameplay_game_id(String _gameplay_game_id) {
		this._gameplay_game_id = _gameplay_game_id;
	}

	public String get_challenge_id() {
		return _challenge_id;
	}

	public void set_challenge_id(String _challenge_id) {
		this._challenge_id = _challenge_id;
	}

	public String get_challenge_genre_type() {
		return _challenge_genre_type;
	}

	public void set_challenge_genre_type(String _challenge_genre_type) {
		this._challenge_genre_type = _challenge_genre_type;
	}

	public Context get_delegate() {
		return _delegate;
	}

	public void set_delegate(Context _delegate) {
		this._delegate = _delegate;
	}

	
}
