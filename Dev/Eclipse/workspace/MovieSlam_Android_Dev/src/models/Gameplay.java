package models;

public class Gameplay {
	
	public static int _promo;
	public static String _promo_name;
	public static int _fbConnected;
	
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
	public static int get_fbConnected() {
		return _fbConnected;
	}
	public static void set_fbConnected(int fbConnected) {
		_fbConnected = fbConnected;
	}
	
	public static void setGenre (String s){
		_genre = s;
	}
	
	private static String _genre;
	
	public static String getGenre (){
		return _genre;
	}
	
	public static void setChallType (String s){
		_challType = s;
	}
	
	private static String _challType = "self";
	
	public static String getChallType (){
		return _challType;
	}
	
	public static String getChallOppoID (){
		return _challOppoID;
	}
	
	private static String _challOppoID = "";
	
	public static void setChallOppoID (String s){
		_challOppoID = s;
	}
	
	public static String getChallOppoFID (){
		return _challOppoFID;
	}
	
	private static String _challOppoFID = "";
	
	public static void setChallOppoFID (String s){
		_challOppoFID = s;
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
	
	public static void setElapses(float f) {
		_elapses[index] = f;
	}
	
	public static float[] _elapses = new float[5];
	
//	public static float[] _elapses = {5.5f, 4.5f, 3.7f, 2.8f, 1.5f};
	
	public static float getElapses(int n) {
		return _elapses[n];
	}
	
	public static void setOppoElapses(float f) {
		_oppoElapses[index] = f;
	}
	
	public static float[] _oppoElapses = new float[5];
	
//	public static float[] _oppoElapses = {5.1f, 4.2f, 3.4f, 2.6f, 1.9f};
	
	public static float getOppoElapses(int n) {
		return _oppoElapses[n];
	}
	
	public static int index = 0;

	public static void setChallID(String value) {
		_challID = value;
	}
	
	public static String _challID;
	
	public static String getChallID() {
		return _challID;
	}
	
	public static void setGameID(String value) {
		_gameID = value;
	}
	
	public static String _gameID;
	
	public static String getGameID() {
		return _gameID;
	}
	
	public static void setChallRound(String value) {
		_challRound = value;
	}
	
	public static String _challRound = "0";
	
	public static String getChallRound() {
		return _challRound;
	}
	
	public static void setChallStatus(String value) {
		_challStatus = value;
	}
	
	public static String _challStatus;
	
	public static String getChallStatus() {
		return _challStatus;
	}
	
	public static void setUserWon(String value) {
		_userWon = value;
	}
	
	public static String _userWon = "0";
	
	public static String getUserWon() {
		return _userWon;
	}
	
	public static void setOppoWon(String value) {
		_oppoWon = value;
	}
	
	public static String _oppoWon = "0";
	
	public static String getOppoWon() {
		return _oppoWon;
	}
	
	public static void setChallOppoImageURL(String value) {
		_challOppoImageURL = value;
	}
	
	public static String _challOppoImageURL;
	
	public static String getChallOppoImageURL() {
		return _challOppoImageURL;
	}
	
	public static void setChallOppoScore(String value) {
		_challOppoScore = value;
	}
	
	public static String _challOppoScore;
	
	public static String getChallOppoScore() {
		return _challOppoScore;
	}
	
}
