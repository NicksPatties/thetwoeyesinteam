package models;

public interface Config {
	
	static public final String BASE_URL = "http://screenslam.foxfilm.com"; // warning: live server: use only as read
//	static public final String BASE_URL = "http://postpcmarketing.com/movieslam/intl/it";
	
	static public final float PROMO_IMG_DURATION = 5.0F;
	static public final float READY_PAGE_DURATION = 3.0F;
	static public final float GAMEPLAY_DELAY = 2.0F;
	static public final float INTERSTITIAL_PAGE_DURATION = 3.0F;
	static public final int NUMBER_OF_TURN = 5;
	static public final int NUMBER_OF_CHOICE = 4;
	static public final int MAX_TIME_FOR_IMAGE_GAMEPLAY = 10;
}
