package com.example.movieslam_android_dev.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TempModel {
	public static String[] _urls= { 
									"http://screenslam.foxfilm.com/image/fightclub_still4_iphone.jpg",
									"http://screenslam.foxfilm.com/image/xfiles_still3_iphone.jpg",
									"http://screenslam.foxfilm.com/video/big_clip2_iphone.mp4",
									"http://screenslam.foxfilm.com/video/cleopatra_clip6_iphone.mp4", 
								    "http://screenslam.foxfilm.com/video/kingdomofheaven_clip2_iphone.mp4",
								   };
	
	public static String[][] _answers= {
										{"Fight Club","The Descendants","The X-Files: Fight The Future","The Last King of Scotland"},
										{"Gillian Anderson","John Hurt","Tom Wilkinson","Allison Janney"},
										{"Big","Me, Myself &amp; Irene","A Walk In The Clouds","Live Free or Die Hard"},
										{"Cleopatra","Wrong Turn 2: Dead End","Big","From Hell"},
										{"Eva Green","Yul Brynner","Jason Bateman","Shirley Maclaine"}
		};
	
	public static String[] getAnswers(){
		return _answers[index];
	}
	
	public static String[] _questions= {
		"name","actor","name","name","actor" 
	   };
	
	public static String getQuestion(){
		return _questions[index];
	}
	
	public static int index = 0;
	
	public static String getURL()
	{
		System.out.println("-----OUR INDEX------: "+index);
		return _urls[index];
	}

}
