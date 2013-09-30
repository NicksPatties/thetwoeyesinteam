package models;

import java.io.Serializable;

import tools.AdvElement;
import android.util.Log;

public class Round implements Config, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String genre;
	
	public int challenge_id;
	public String challenge_type;
	public String target_type;
	
	public Media[] medias;
	
	public String player_id;
	public String player_fid;
	public String player_fname;
	public String player_lname;
	public String player_thumbnail;
	public String player_score;
	public String player_round_score;
	
	public String user_round_score;

	public int media_idx;
	
	
	public Round(String challenge_type){
		this.challenge_type = challenge_type;
	}
	
	public void init(String response){
					
		AdvElement doc = new AdvElement(response);
		
		// parse media info
		medias = new Media[NUMBER_OF_TURN];
		for (int i = 0; i < NUMBER_OF_TURN; i++){
			medias[i] = new Media();			
			AdvElement media_e = doc.getElement("media", i);
			medias[i].type = media_e.getValue("media_type");
			medias[i].question = media_e.getValue("choice_var");
			medias[i].url = BASE_URL + "/" + media_e.getValue("media_url");
			medias[i].legal = BASE_URL+"/legal/"+media_e.getValue("media_legal");
			medias[i].name = media_e.getValue("media_name");
			medias[i].id = media_e.getValue("media_id");
			medias[i].etailer = media_e.getValue("media_etailer");
			medias[i].thumbnail = BASE_URL+"/"+media_e.getValue("media_thumbnail");
			medias[i].choices = new String[NUMBER_OF_CHOICE];
			for (int j = 0; j < NUMBER_OF_CHOICE; j++)	medias[i].choices[j] = media_e.getValue("choice_value", j);
		}
		
		// parse user and player info
		AdvElement opponent_e = doc.getElement("player");		
		player_id = opponent_e.getValue("player_user_id");
		player_fid = opponent_e.getValue("player_user_fid");
		player_fname = opponent_e.getValue("player_user_fname");
		player_lname = opponent_e.getValue("player_user_lname");
		player_thumbnail = opponent_e.getValue("player_user_thumbnail");
		player_score = opponent_e.getValue("player_user_score");
		player_round_score = challenge_type.equals("accept_game") ? opponent_e.getValue("player_game_score") : "0";		
		user_round_score = "0";		
	}
}
