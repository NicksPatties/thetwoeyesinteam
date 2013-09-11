package models;

public class TempModel {
	//this temporary model is used to save all the data related to game play pages, like media URLs and names.
	
	public static void setMediaURLs(String[] s){
		_mediaURLs = s;
	}
	
	public static String[] _mediaURLs;
	
	public static String getMediaURLs(){
		return "http://screenslam.foxfilm.com/" + _mediaURLs[index];
	}
	
	public static void setMediaLegals(String[] s){
		_mediaLegals = s;
	}
	
	public static String[] _mediaLegals;
	
	public static String getMediaLegals(){
		return _mediaLegals[index];
	}
	
	public static void setMediaNames(String[] s){
		_mediaNames = s;
	}
	
	public static String[] _mediaNames;
	
	public static String getMediaNames(){
		return _mediaNames[index];
	}
	
	public static void setMediaIDs(String[] s){
		_mediaIDs = s;
	}
	
	public static String[] _mediaIDs;
	
	public static String getMediaIDs(){
		return _mediaIDs[index];
	}
	
	public static void setAnswers(String[][] s){
		_answers = s;
	}
	
	public static String[][] _answers;
	
	public static String[][] getAnswers(){
		return _answers;
	}
	
	
	public static void setQuestion(String[] s){
		_questions = s;
	}
	
	public static String[] _questions;
	
	public static String getQuestion(){
		return _questions[index];
	}
	
	public static void setMediaEtailers(String[] s) {
		_mediaEtailers = s;
	}
	
	public static String[] _mediaEtailers;
	
	public static String[] getMediaEtailers() {
		return _mediaEtailers;
	}
	
	public static int index = 0;
}
