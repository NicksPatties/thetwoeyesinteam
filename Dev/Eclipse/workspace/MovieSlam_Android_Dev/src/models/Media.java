package models;

import java.io.Serializable;


public class Media implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String question;
	public String url;
	public String legal;
	public String name;
	public String id;
	public String etailer;
	public String thumbnail;
	public String choices[];
	public String type;
	
	public boolean player_correct;
	public float player_elapse;
	public int player_score;
	
	public boolean user_correct;
	public float user_elapse;
	public int user_score;
	
	
	public Media(){
		
	}
}
