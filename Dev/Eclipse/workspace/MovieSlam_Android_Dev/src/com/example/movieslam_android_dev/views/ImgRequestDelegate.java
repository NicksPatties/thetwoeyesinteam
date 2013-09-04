package com.example.movieslam_android_dev.views;

import android.graphics.Bitmap;

public interface ImgRequestDelegate {
	void imgLoaded(Bitmap bitmap, int id);
}
