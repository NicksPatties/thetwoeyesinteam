package models;

public class Gameplay {
	
	public static int _promo;
	public static String _promo_name;
	
	public static int get_promo() {
		return _promo;
	}
	public static void set_promo(int promo) {
		_promo = promo;
	}
	public static String get_promo_name() {
		return _promo_name;
	}
	public static void set_promo_name(String promo_name) {
		_promo_name = promo_name;
	}
	
	public static void setGenre (String s){
		_genre = s;
	}
	
	private static String _genre;
	
	public static String getGenre (){
		return _genre;
	}
	
	public static String getChallOppoID (String s){
		return _challOppoID;
	}
	
	private static String _challOppoID;
	
	public static void setChallOppoID (String s){
		_challOppoID = s;
	}
	
	public static void setOppoImageURL (String s){
		_oppoImageURL = s;
	}
	
	private static String _oppoImageURL;
	
	public static String getOppoImageURL (){
		return _oppoImageURL;
	}
	
	public static void setOppoScore (String s){
		_oppoScore = s;
	}
	
	private static String _oppoScore;
	
	public static String getOppoScore (){
		return _oppoScore;
	}
	
	public static void setOppoFName (String s){
		_oppoFName = s;
	}
	
	private static String _oppoFName;
	
	public static String getOppoFName (){
		return _oppoFName;
	}
	
	public static void setOppoLName (String s){
		_oppoLName = s;
	}
	
	private static String _oppoLName;
	
	public static String getOppoLName (){
		return _oppoLName;
	}
	
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
	
	public static String getMediaNames(int i){
		return _mediaNames[i];
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
	
	public static String getMediaEtailers() {
		return _mediaEtailers[index];
	}
	
	public static String getMediaEtailers(int n) {
		return _mediaEtailers[n];
	}
	
	public static void setMediaTN(String[] s) {
		_mediaTN = s;
	}
	
	public static String[] _mediaTN;
	
	public static String getMediaTN(int i) {
		return "http://screenslam.foxfilm.com/" + _mediaTN[i];
	}
	
	public static int index = 0;
}
