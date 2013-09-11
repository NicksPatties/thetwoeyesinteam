package models;

public class User {
	
	public static String _uid;
	public static String _fid;
	public static String _fname;
	public static String _lname;
	public static String _thumbnail;
	public static String _score;
	
	public static String get_uid() {
		return _uid;
	}
	public static void set_uid(String uid) {
		User._uid = uid;
	}
	public static String get_fid() {
		return _fid;
	}
	public static void set_fid(String fid) {
		User._fid = fid;
	}
	public static String get_fname() {
		return _fname;
	}
	public static void set_fname(String fname) {
		User._fname = fname;
	}
	public static String get_lname() {
		return _lname;
	}
	public static void set_lname(String lname) {
		User._lname = lname;
	}
	public static String get_thumbnail() {
		return _thumbnail;
	}
	public static void set_thumbnail(String thumbnail){
		_thumbnail = thumbnail;
	}
	public static void set_score(String score){
		_score = score;
	}
	public static String get_score() {
		return _score;
	}
}
