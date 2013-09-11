package tools;

import android.view.View;
import android.view.View.OnClickListener;

public class ChallengeBoardButtonListener implements View.OnClickListener  {
	public String _gameplay_game_id;
	public String _challenge_id;
	public String _challenge_genre_type;
	
	public ChallengeBoardButtonListener(String gameplay_game_id, String challenge_id, String challenge_genre_type) {
		_gameplay_game_id = gameplay_game_id;
		_challenge_id = challenge_id;
		_challenge_genre_type = challenge_genre_type;
	}

	@Override
	public void onClick(View v) {		
	}
}
